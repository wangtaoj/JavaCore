package com.wangtao.nio.tomcat;

/**
 * @author wangtao
 * Created at 2024-08-10
 */
public record PollerEvent(SocketChannelWrap socketChannelWrap, int interestOPs) {

    public static final int OP_REGISTER = 100;

}
