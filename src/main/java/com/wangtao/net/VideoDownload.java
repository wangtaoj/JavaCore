package com.wangtao.net;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author wangtao
 * Created at 2019/3/25 20:37
 */
public class VideoDownload {

    private static final String DEFAULT_SAVE_DIR = "D:/";

    public static void download(String path) {
        try {
            URL url = new URL(path);
            int delim = path.lastIndexOf("/");
            int end = path.lastIndexOf("?");
            String filename = end > 0 ? path.substring(delim + 1, end) : path.substring(delim + 1);
            try (InputStream in = url.openStream();
                 BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(DEFAULT_SAVE_DIR + filename))) {
                System.out.println(in.getClass().getName());
                byte[] buf = new byte[1024 * 2];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "https://vdn1.vzuu.com/LD/eff1a9e4-4460-11e9-9c3f-0a580a40ea6f.mp4?disable_local_cache=1&bu=com&expiration=1553520991&auth_key=1553520991-0-0-b8916c6d65dab5e84e924aad2aaa7b26&f=mp4&v=hw";
        download(path);
    }
}
