package com.wangtao.xml.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * Created by wangtao at 2019/1/4 22:02
 */
public class Resources {

    private Resources() {

    }

    public static Reader getResourceAsReader(String resource) throws IOException {
        return new InputStreamReader(getResourceAsStream(resource));
    }

    public static String getResourceAsPath(String resource) throws IOException {
        URL url = Resources.class.getClassLoader().getResource(resource);
        if(url != null) {
            return url.getPath();
        }
        throw new IOException("不能读取此资源");
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        InputStream is = Resources.class.getClassLoader().getResourceAsStream(resource);
        if(is == null) {
            throw new IOException("不能读取此资源, 文件路径:" + resource);
        }
        return is;
    }
}
