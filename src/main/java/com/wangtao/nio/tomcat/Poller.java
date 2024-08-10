package com.wangtao.nio.tomcat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
@Slf4j
public class Poller implements Runnable {

    private final Selector selector;

    private final ServerSocketChannel serverSocketChannel;

    private final TomcatConfig tomcatConfig;

    /**
     * 线程池，用于处理请求
     */
    private final Executor executor;

    /**
     * 用于控制socket连接数量
     */
    private final Semaphore semaphore;

    private volatile boolean stop;

    private final Queue<PollerEvent> eventQueue = new ConcurrentLinkedQueue<>();

    /**
     * 处于超时socket的时间
     */
    private long nextTimeoutMillis;

    public Poller(ServerSocketChannel serverSocketChannel, TomcatConfig tomcatConfig) {
        this.serverSocketChannel = serverSocketChannel;
        this.tomcatConfig = tomcatConfig;
        // 最多允许maxConnections个连接
        this.semaphore = new Semaphore(tomcatConfig.getMaxConnections());
        /*
         * 线程池和tomcat中使用线程池的有些差别, 主要是阻塞队列区别
         * tomcat对阻塞队列的offer方法做了改造，使得逻辑变化如下
         * 当线程数量大于核心线程数量时，不再将任务先放入到阻塞队列中，而是新建一个线程
         * 来处理任务，当达到最大线程数量时才会将任务放入到阻塞队列中，阻塞队列满了，执行拒绝策略
         */
        this.executor = new ThreadPoolExecutor(
                tomcatConfig.getMinSpareThreads(), tomcatConfig.getMaxThreads(),
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>()
        );
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException("selector create failed!", e);
        }
    }

    /**
     * 关于selector的操作都在一个线程执行, 否则会有死锁可能
     */
    @Override
    public void run() {
        while (!stop) {
            try {
                boolean hasEvent;
                int selectCount;
                try {
                    hasEvent = handleEvents();
                    selectCount = selector.select(1000);
                    if (selectCount == 0) {
                        // 被唤醒了或者超时了, 先处理事件
                        hasEvent = handleEvents() || hasEvent;
                    }
                } catch (Throwable e) {
                    log.error("event loop failed", e);
                    continue;
                }
                Iterator<SelectionKey> it = selectCount == 0 ? null : selector.selectedKeys().iterator();
                if (it != null && it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    SocketChannelWrap socketChannelWrap = (SocketChannelWrap) key.attachment();
                    if (socketChannelWrap != null) {
                        processKey(key);
                    }
                }
                // 处理超时的socket连接
                timeout(selectCount, hasEvent);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private boolean handleEvents() {
        boolean hasEvent = false;
        // size复用，防止eventQueue元素一直在增长，退不出循环
        for (int i = 0, size = eventQueue.size(); i < size; i++) {
            PollerEvent event = eventQueue.poll();
            if (event == null) {
                continue;
            }
            hasEvent = true;
            SocketChannelWrap socketChannelWrap = event.socketChannelWrap();
            SocketChannel socketChannel = socketChannelWrap.getSocketChannel();
            if (event.interestOPs() == PollerEvent.OP_REGISTER) {
                // 新的socketChannel, 注册读事件
                try {
                    socketChannel.register(selector, SelectionKey.OP_READ, socketChannelWrap);
                    // 需要复用, 因为处理请求前会先移除selectionKey的就绪事件, 用这个保存
                    socketChannelWrap.setInterestOps(SelectionKey.OP_READ);
                } catch (Exception e) {
                    log.error("socket register fialed!", e);
                    socketChannelWrap.close();
                }
            } else {
                // 添加新事件
                SelectionKey key = socketChannel.keyFor(selector);
                if (key != null) {
                    int interestOps = key.interestOps() | event.interestOPs();
                    //noinspection MagicConstant
                    key.interestOps(interestOps);
                    socketChannelWrap.setInterestOps(interestOps);
                    if (key.attachment() == null) {
                        key.attach(socketChannelWrap);
                    }
                } else {
                    socketChannelWrap.close();
                }
            }
        }
        return hasEvent;
    }

    private void timeout(int selectCount, boolean hasEvent) {
        long now = System.currentTimeMillis();
        boolean shouldHandleTimeoutSocket = false;
        if (now > nextTimeoutMillis) {
            shouldHandleTimeoutSocket = true;
        } else {
            // 还未到处理时间, 判断现在是否空闲
            if (selectCount == 0 && !hasEvent) {
                shouldHandleTimeoutSocket = true;
            }
        }

        if (!shouldHandleTimeoutSocket) {
            return;
        }

        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
             SocketChannelWrap socketChannelWrap = (SocketChannelWrap) key.attachment();
             if (socketChannelWrap != null) {
                 if (socketChannelWrap.interestOpsHas(SelectionKey.OP_READ)) {
                     long diffMillis = System.currentTimeMillis() - socketChannelWrap.getLastReadTimestamp();
                     long timeout = tomcatConfig.getSocketIdleTime();
                     // 超时了，关闭socket
                     if (timeout > 0 && diffMillis > timeout) {
                         socketChannelWrap.close();
                     }
                 }
             } else {
                 key.cancel();
             }
        }
        // 下一次处理时间
        nextTimeoutMillis = System.currentTimeMillis() + 1000;
    }

    private void processKey(SelectionKey key) {
        /*
         * 取消对应就绪事件, 因为是在另外的线程池执行, 如何线程还没有处理完，poller线程开始新的循环时
         * 这个key又会出现, 导致混乱
         */
        SocketChannelWrap socketChannelWrap = (SocketChannelWrap) key.attachment();
        if (key.isValid()) {
            unRegister(key);

            // 将处理逻辑委托给线程池
            SocketProcessor socketProcessor = new SocketProcessor(socketChannelWrap);
            executor.execute(socketProcessor);
        } else {
            socketChannelWrap.close();
        }

    }

    private void unRegister(SelectionKey key) {
        key.interestOps(key.interestOps() & ~key.readyOps());
    }

    public void configSocketChannel(SocketChannel socketChannel) {
        SocketChannelWrap socketChannelWrap = new SocketChannelWrap(socketChannel, this);
        HttpInputBuffer httpInputBuffer = new HttpInputBuffer(socketChannelWrap);
        socketChannelWrap.setHttpInputBuffer(httpInputBuffer);
        try {
            socketChannel.configureBlocking(false);
            registerEvent(socketChannelWrap, PollerEvent.OP_REGISTER);
        } catch (IOException e) {
            log.error("configSocketChannel failed!", e);
            socketChannelWrap.close();
        }

    }

    public void registerEvent(SocketChannelWrap socketChannelWrap, int interestOps) {
        PollerEvent pollerEvent = new PollerEvent(socketChannelWrap, interestOps);
        eventQueue.offer(pollerEvent);
        // 唤醒selector, 以至于poll线程可以来处理事件
        selector.wakeup();
    }

    public SocketChannel acceptSocketChannel() throws IOException {
        return serverSocketChannel.accept();
    }

    public void countUpOrAwaitConnection() {
        semaphore.acquireUninterruptibly();
    }

    public void countDownConnection() {
        semaphore.release();
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
