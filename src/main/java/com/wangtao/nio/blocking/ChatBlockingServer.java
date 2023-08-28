package com.wangtao.nio.blocking;

import com.wangtao.nio.ChannelUtils;
import com.wangtao.nio.ChatRwUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通信协议
 * 4字节的消息长度 + 消息内容
 * 主要的难题是需要读取多少的数据作为一次请求的参数，因此直接定义一个长度来告诉服务端或者客户端需要读取多少字节
 * @author wangtao
 * Created at 2023/8/28 18:52
 */
@Slf4j
public class ChatBlockingServer {

    public static void main(String[] args) {
        /*
         * 默认情况下为阻塞模式
         * accept、read、write方法都可能被阻塞
         */
        ExecutorService executor = Executors.newFixedThreadPool(10);
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口, 不指定ip时，使用本机任意ip都能访问, 相当于bind 0.0.0.0
            serverSocketChannel.bind(new InetSocketAddress(8080));
        } catch (IOException e) {
            executor.shutdown();
            ChannelUtils.closeQuietly(serverSocketChannel);
            throw new RuntimeException("server start fail!");
        }

        while (!Thread.currentThread().isInterrupted()) {
            SocketChannel socketChannel = null;
            try {
                // 阻塞直到收到客户端的连接
                socketChannel = serverSocketChannel.accept();
                // 提交给线程池处理
                executor.execute(new ProcessRequestTask(socketChannel));
            } catch (RejectedExecutionException e) {
                log.error("server busy, socketChannel is rejected! {}", socketChannel);
                ChannelUtils.closeQuietly(socketChannel);
            } catch (Exception e) {
                log.error("handle client fail", e);
            }
        }
        executor.shutdown();
        try {
            boolean res = executor.awaitTermination(1, TimeUnit.MINUTES);
            log.info("executor shutdown result: {}", res);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ChannelUtils.closeQuietly(serverSocketChannel);
    }

    static class ProcessRequestTask implements Runnable {

        private final SocketChannel socketChannel;

        public ProcessRequestTask(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            WritableByteChannel out = Channels.newChannel(System.out);
            AtomicInteger processCount = new AtomicInteger(0);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 读取数据
                    ByteBuffer reqBuf = ChatRwUtils.readChannel(socketChannel);
                    if (reqBuf.hasRemaining()) {
                        // 打印到控制台
                        ChannelUtils.writeFully(out, reqBuf);
                        // 响应请求
                        String rspBody = "hello client, this is " + processCount.incrementAndGet() + " response!\r\n";
                        ByteBuffer rspBuf = ChatRwUtils.createByteBuffer(rspBody.getBytes(StandardCharsets.UTF_8));
                        ChannelUtils.writeFully(socketChannel, rspBuf);
                    } else {
                        log.info("clinet request over, normal exit, {}", socketChannel);
                        break;
                    }
                } catch (IOException e) {
                    log.error("handle client fail, {}", socketChannel);
                    break;
                }
            }
            ChannelUtils.closeQuietly(socketChannel);
        }
    }
}
