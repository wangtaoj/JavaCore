package com.wangtao.nio.tomcat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
@Slf4j
public class TomcatServer {

    private TomcatConfig tomcatConfig = new TomcatConfig();

    private ServerSocketChannel serverSocketChannel;

    public TomcatServer() {

    }

    public TomcatServer(TomcatConfig tomcatConfig) {
        this.tomcatConfig = tomcatConfig;
    }

    public static void main(String[] args) {
        TomcatServer tomcatServer = new TomcatServer();
        tomcatServer.start();
    }

    /**
     * 开启tomcat server
     */
    public void start() {
        bind();
        startInternalThread();
        log.info("TomcatServer start success");
    }

    /**
     * 监听端口，准备接收socket连接
     */
    private void bind() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            // 监听端口
            serverSocketChannel.bind(new InetSocketAddress(tomcatConfig.getPort()), tomcatConfig.getAcceptCount());
            // 接收连接使用阻塞模式
            serverSocketChannel.configureBlocking(true);
        } catch (IOException e) {
            throw new RuntimeException("tomcat server bind failed", e);
        }
    }

    private void startInternalThread() {
        Poller poller = new Poller(serverSocketChannel, tomcatConfig);
        Acceptor acceptor = new Acceptor(poller);

        new Thread(acceptor, "acceptor").start();
        new Thread(poller, "poller").start();
    }
}
