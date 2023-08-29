package com.wangtao.nio.nonblocking;

import com.wangtao.nio.ChannelUtils;
import com.wangtao.nio.ChatRwUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023/8/27 20:54
 */
@Slf4j
public class ChatNonBlockingServer {

    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            // 监听端口
            serverSocketChannel.bind(new InetSocketAddress(8080));
            // 非阻塞, 之后所有可能的阻塞方法都不再阻塞(此方法可随时调用，会响应阻塞方法的行为)
            serverSocketChannel.configureBlocking(false);
            // 创建选择器
            selector = Selector.open();
            /*
             * 注册感兴趣的事件
             * 只有非阻塞模式才能使用Selector相关API
             * 同一个Channel和Selector只会有一个SelectionKey
             * 再次注册会覆盖掉, 内部实际会调用SelectionKey.interestOps方法
             * OP_ACCEPT: 如果不调用accept方法消费掉，就会一直处于就绪状态
             * OP_READ: 缓冲区有数据或者达到流末尾、通道关闭，都会处于就绪状态
             * 多个事件可以使用OP_READ | OP_WRITE
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error("server start fail!", e);
            ChannelUtils.closeQuietly(serverSocketChannel);
            ChannelUtils.closeQuietly(selector);
            return;
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 选择就绪事件, 阻塞直到至少存在一个就绪事件，避免CPU空转
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                log.info("selected keys: {}", selectionKeys.size());
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    // 移除它, 否则下次selector.selectedKeys()还会返回
                    it.remove();
                    try {
                        if (selectionKey.isAcceptable()) {
                            // 接收事件就绪
                            ServerSocketChannel currentChannel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel socketChannel = currentChannel.accept();
                            socketChannel.configureBlocking(false);
                            // 添加数据上下文信息, selectionKey.attachment()可以获取
                            ExchangeContext exchangeContext = new ExchangeContext();
                            // 注册读事件
                            socketChannel.register(selector, SelectionKey.OP_READ, exchangeContext);
                        } else if (selectionKey.isReadable()) {
                            processReadEvent(selectionKey);
                        } else if (selectionKey.isWritable()) {
                            processWriteEvent(selectionKey);
                        }
                    } catch (Exception e) {
                        log.error("handle selectionKey fail, channel: {}", selectionKey.channel(), e);
                        // 关闭通道，会取消掉通道注册的事件
                        ChannelUtils.closeQuietly(selectionKey.channel());
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        ChannelUtils.closeQuietly(serverSocketChannel);
        ChannelUtils.closeQuietly(selector);
    }

    private static void processReadEvent(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer reqBuf = ChatRwUtils.readChannel(socketChannel);
        if (reqBuf.hasRemaining()) {
            // 打印请求信息
            log.info("reqBody: {}", new String(reqBuf.array(), StandardCharsets.UTF_8));
            ExchangeContext context = (ExchangeContext)selectionKey.attachment();
            String rspBody = "hello clinet, this is " + context.getProcessCount().incrementAndGet() + " response\r\n";
            context.setDatas(rspBody.getBytes(StandardCharsets.UTF_8));
            // 切换为为可写事件
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        } else {
            log.info("client request over, normat exit");
            // 关闭通道，会取消掉通道注册的事件
            ChannelUtils.closeQuietly(socketChannel);
        }
    }

    private static void processWriteEvent(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ExchangeContext context = (ExchangeContext)selectionKey.attachment();
        ByteBuffer rspBuf = ChatRwUtils.createByteBuffer(context.getDatas());
        ChannelUtils.writeFully(socketChannel, rspBuf);
        // 切换为为可读事件
        selectionKey.interestOps(SelectionKey.OP_READ);
    }
}
