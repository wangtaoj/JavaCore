package com.wangtao.nio.tomcat;

import com.wangtao.nio.ChannelUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author wangtao
 * Created at 2024-08-11
 */
public class TomcatClient {

    public static void main(String[] args) {
        Socket socket;
        try {
            socket = new Socket("127.0.0.1", 8080);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            if ("exit".equals(msg)) {
                break;
            }
            if ("ok".equals(msg)) {
                sendData(socket, "\n");
            } else {
                sendData(socket, msg);
            }
        }
        ChannelUtils.closeQuietly(socket);
    }

    private static void sendData(Socket socket, String msg) {
        try {
            socket.getOutputStream().write((msg).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
