package com.wangtao.nio.tomcat;

import com.wangtao.nio.ChannelUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
@Slf4j
public class SocketChannelWrap {

    private final SocketChannel socketChannel;

    private final Poller poller;

    private int interestOps;

    private HttpInputBuffer httpInputBuffer;

    private volatile long lastReadTimestamp = System.currentTimeMillis();

    public SocketChannelWrap(SocketChannel socketChannel, Poller poller) {
        this.socketChannel = socketChannel;
        this.poller = poller;
    }

    public void setHttpInputBuffer(HttpInputBuffer httpInputBuffer) {
        this.httpInputBuffer = httpInputBuffer;
    }

    public HttpInputBuffer getHttpInputBuffer() {
        return httpInputBuffer;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setInterestOps(int interestOps) {
        this.interestOps = interestOps;
    }

    public int getInterestOps() {
        return interestOps;
    }

    public boolean interestOpsHas(int targetOp) {
        return (this.interestOps & targetOp) == targetOp;
    }

    public void registerReadEvent() {
        poller.registerEvent(this, SelectionKey.OP_READ);
    }

    public void updatelastReadTimestamp() {
        this.lastReadTimestamp = System.currentTimeMillis();
    }

    public long getLastReadTimestamp() {
        return lastReadTimestamp;
    }

    /**
     * 释放socket以及许可证
     */
    public void close() {
        log.info("close socket channel");
        poller.countDownConnection();
        if (socketChannel != null) {
            ChannelUtils.closeQuietly(socketChannel);
        }
    }
}
