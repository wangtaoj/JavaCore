package com.wangtao.nio.blocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author wangtao
 * Created at 2019/6/11 22:26
 */
public class BlockingServer {


    public static void main(String[] args) {
        try(ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.bind(new InetSocketAddress(8080));
            try(SocketChannel sc = ssc.accept()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int len;
                while((len = sc.read(buffer)) > 0) {
                    System.out.println(new String(buffer.array(), 0, len));
                    sc.write(ByteBuffer.wrap("HTTP/1.1 200 OK\r\n".getBytes()));
                    sc.write(ByteBuffer.wrap("Content-Type: text/html;charset=UTF-8\r\n".getBytes()));
                    byte[] message = "<h1>Hello World!</h1>".getBytes();
                    sc.write(ByteBuffer.wrap(("Content-Length: " + message.length + "\r\n").getBytes()));
                    sc.write(ByteBuffer.wrap("\r\n".getBytes()));
                    sc.write(ByteBuffer.wrap(message));
                }
                System.out.println(sc.hashCode() + " will be closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
