package com.wangtao.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author wangtao
 * Created at 2019/3/25 13:29
 */
public class VFS {

    public static void list(String path) {
        try {
            URL url = new URL("file:///" + path);
            try (InputStream in = url.openStream()) {
                byte[] buf = new byte[1024];
                int len;
                while((len = in.read(buf)) != -1) {
                    String content = new String(buf, 0, len);
                    System.out.print(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        list("D:/");
    }
}
