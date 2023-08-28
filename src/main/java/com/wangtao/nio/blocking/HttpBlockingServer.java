package com.wangtao.nio.blocking;

import com.wangtao.nio.ChannelUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author wangtao
 * Created at 2019/6/11 22:26
 */
public class HttpBlockingServer {

    /**
     * 浏览器是根据Content-Length响应头来判断需要读取多少字节
     * 响应体和响应头之间必须隔一个空行
     * 收到服务器响应之后，浏览器会关闭写缓冲区，服务端读取时就会返回-1，退出循环
     */
    public static void main(String[] args) {
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 监听端口
            serverSocketChannel.bind(new InetSocketAddress(8080));
            /*
             * ssc.configureBlocking(false)
             * 接受客户端连接, 这些阻塞式方法的行为可以通过configureBlocking来改变是阻塞还是非阻塞
             * 阻塞方法调用之前配置是否阻塞模式即可
             * 服务端和客户端都可以配置阻塞模式
             */
            try(SocketChannel socketChannel = serverSocketChannel.accept()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int i = 0;
                while(socketChannel.read(buffer) != -1) {
                //while(ChannelUtils.tryReadFully(socketChannel, buffer)) {
                    System.out.println(">>>>>>>" + i++);
                    buffer.flip();
                    System.out.println(new String(buffer.array(), buffer.position(), buffer.limit()));
                    buffer.clear();
                    byte[] message = "<h1>Hello World!</h1>".getBytes();
                    ChannelUtils.writeFully(
                            socketChannel,
                            ByteBuffer.wrap("HTTP/1.1 200 OK\r\n".getBytes()),
                            ByteBuffer.wrap("Content-Type: text/html;charset=UTF-8\r\n".getBytes()),
                            ByteBuffer.wrap(("Content-Length: " + message.length + "\r\n").getBytes()),
                            ByteBuffer.wrap("\r\n".getBytes()),
                            ByteBuffer.wrap(message)
                    );
                }
                System.out.println(socketChannel.hashCode() + " will be closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
