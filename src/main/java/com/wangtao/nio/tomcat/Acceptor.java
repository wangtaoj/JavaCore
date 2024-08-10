package com.wangtao.nio.tomcat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
@Slf4j
public class Acceptor implements Runnable {

    private final Poller poller;

    private volatile boolean stop;

    public Acceptor(Poller poller) {
        this.poller = poller;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                // 获取一个许可证, 如果达到了maxConnections，线程阻塞
                poller.countUpOrAwaitConnection();
                SocketChannel socketChannel = null;
                try {
                    // 接收连接(阻塞模式)
                    socketChannel = poller.acceptSocketChannel();
                } catch (IOException e) {
                    // 释放许可证
                    poller.countDownConnection();
                    log.error("接收连接出现异常", e);
                }
                if (socketChannel != null) {
                    // socketChannel配置为非阻塞, 并注册事件
                    poller.configSocketChannel(socketChannel);
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
