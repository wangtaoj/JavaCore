package com.wangtao.nio.tomcat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
@Setter
@Getter
public class TomcatConfig {

    /**
     * 监听端口
     */
    private int port = 8080;

    /**
     * 最大的socket连接数量
     */
    private int maxConnections = 8192;

    /**
     * 当socket连接数量达到maxConnections时，被挂起连接的最大数量，相当于socket backlog参数
     * 挂起功能由底层socket支持，和tomcat无关
     */
    private int acceptCount = 100;

    /**
     * 线程池中最大的线程数量
     */
    private int maxThreads = 200;

    /**
     * 线程池核心线程数量
     */
    private int minSpareThreads = 10;

    /**
     * socket最大空闲时间, 如果超出该时间还没有发送请求过来，服务端将关闭socket连接
     */
    private long socketIdleTime = 20000;
}
