package com.wangtao.nio.blocking;

import com.wangtao.nio.ChannelUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author wangtao
 * Created at 2023/8/27 20:10
 */
public class SimpleBlockingClient {

    public static void main(String[] args) {
        // 默认阻塞模式
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // 断言是阻塞模式
            assert socketChannel.isBlocking();
            // 阻塞直到连接建立成功
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
            // 写数据
            ChannelUtils.writeFully(socketChannel, ByteBuffer.wrap("hello server!".getBytes()));
            // 关闭写通道, 这样服务端循环读取时返回值才会返回-1结束循环
            socketChannel.shutdownOutput();
            // 读数据
            ByteBuffer buf = ByteBuffer.allocate(1024);
            try (WritableByteChannel out = Channels.newChannel(System.out)) {
                while (socketChannel.read(buf) != -1) {
                    // 将读取的数据写到控制台
                    buf.flip();
                    ChannelUtils.writeFully(out, buf);
                    // 清空缓冲区
                    buf.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
