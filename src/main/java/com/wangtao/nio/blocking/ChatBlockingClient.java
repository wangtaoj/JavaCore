package com.wangtao.nio.blocking;

import com.wangtao.nio.ChannelUtils;
import com.wangtao.nio.ChatRwUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author wangtao
 * Created at 2023/8/27 20:10
 */
@Slf4j
public class ChatBlockingClient {

    public static void main(String[] args) {
        // 默认阻塞模式
        try (SocketChannel socketChannel = SocketChannel.open();
             WritableByteChannel out = Channels.newChannel(System.out)) {
            // 断言是阻塞模式
            assert socketChannel.isBlocking();
            // 阻塞直到连接建立成功
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
            for (int i = 1; i <= 3; i++) {
                // 写数据
                String reqBody = "hello server, this is " + i + " request!\r\n";
                ByteBuffer reqBuf = ChatRwUtils.createByteBuffer(reqBody.getBytes(StandardCharsets.UTF_8));
                ChannelUtils.writeFully(socketChannel, reqBuf);

                // 读取响应
                ByteBuffer rspBuffer = ChatRwUtils.readChannel(socketChannel);
                if (rspBuffer.hasRemaining()) {
                    ChannelUtils.writeFully(out, rspBuffer);
                } else {
                    log.info("the server response is empty!");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
