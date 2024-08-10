package com.wangtao.nio.tomcat;

import lombok.extern.slf4j.Slf4j;

import java.io.EOFException;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
@Slf4j
public class SocketProcessor implements Runnable {

    private final SocketChannelWrap socketChannelWrap;

    public SocketProcessor(SocketChannelWrap socketChannelWrap) {
        this.socketChannelWrap = socketChannelWrap;
    }

    @Override
    public void run() {
        // 读取数据
        try {
            String requestData = socketChannelWrap.getHttpInputBuffer().parseRequestData();
            if (requestData != null) {
                simpleResponseData(requestData);
                // 重置buf，便于再次处理请求数据
                socketChannelWrap.getHttpInputBuffer().reset();
                socketChannelWrap.registerReadEvent();
            }
        } catch (Exception e) {
            socketChannelWrap.close();
            if (!(e instanceof EOFException)) {
                log.error("处理请求失败", e);
            }
        }
    }

    private void simpleResponseData(String requestData) {
        log.info("receive data: {}", requestData);
    }
}
