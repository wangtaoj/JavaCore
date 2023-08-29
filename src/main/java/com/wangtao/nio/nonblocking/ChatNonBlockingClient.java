package com.wangtao.nio.nonblocking;

import com.wangtao.nio.ChannelUtils;
import com.wangtao.nio.ChatRwUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangtao
 * Created at 2023/8/29 21:26
 */
@Slf4j
public class ChatNonBlockingClient {

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             Selector selector = Selector.open();
             WritableByteChannel out = Channels.newChannel(System.out)) {
            // 配置非阻塞
            socketChannel.configureBlocking(false);
            // 建立一次连接操作, 因为非阻塞模式, 如果没有建立连接该方法会立即返回false
            boolean connected = socketChannel.connect(new InetSocketAddress(8080));
            if (!connected) {
                // 注册连接事件
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
            boolean loop = true;
            while (loop) {
                /*
                 * 这里有点疑惑, 建立连接后，没有注册读写事件, 方法也不会阻塞
                 * 且selectionKeys为空, 就会导致CPU空转
                 */
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    it.remove();
                    if (selectionKey.isConnectable()) {
                        // 可以连接了
                        if (socketChannel.finishConnect()) {
                            log.info("{} connect successfully!", socketChannel);
                            // 切换为写事件, 准备向服务端发起请求
                            selectionKey.interestOps(SelectionKey.OP_WRITE);
                        } else {
                            log.info("{} connect fail, wait next try!", socketChannel);
                        }
                    } else if (selectionKey.isWritable()) {
                        ByteBuffer reqBuf = ChatRwUtils.createByteBuffer("hello server!\r\n".getBytes(StandardCharsets.UTF_8));
                        ChannelUtils.writeFully(socketChannel, reqBuf);
                        // 切换为读事件, 等待服务端响应
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        ByteBuffer rspBuf = ChatRwUtils.readChannel(socketChannel);
                        ChannelUtils.writeFully(out, rspBuf);
                        // 切换为写事件, 准备再次向服务端发起请求
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                        // 结束本次会话
                        loop = false;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
