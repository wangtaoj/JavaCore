package com.wangtao.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author wangtao
 * Created at 2019/3/25 14:46
 */
public class HttpClient {

    /**
     * GET请求方式
     * @param requestUrl 请求地址
     * @return 返回服务的相应内容
     */
    public static String get(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                StringBuilder content = new StringBuilder();
                try (InputStream in = connection.getInputStream()) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        content.append(new String(buf, 0, len));
                    }
                }
                return content.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * POST请求方式
     * @param requestUrl 请求URL
     * @return 返回服务端相应内容
     */
    public static String post(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                // 设置可以向服务端发送数据
                if(!connection.getDoOutput()) {
                    connection.setDoOutput(true);
                }
                // 向服务端发送参数
                try (Writer writer = new OutputStreamWriter(connection.getOutputStream())) {
                    writer.write("type=1");
                }
                StringBuilder response = new StringBuilder();
                // 读取响应内容
                try (InputStream in = connection.getInputStream()) {
                    byte[] buf = new byte[512];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        response.append(new String(buf, 0, len));
                    }
                }
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // String url = "http://localhost:8080/user/getType?type=2";
        // System.out.println(get(url));
        String url = "http://localhost:8080/user/getType";
        System.out.println(post(url));
    }
}
