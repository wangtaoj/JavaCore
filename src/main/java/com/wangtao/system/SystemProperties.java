package com.wangtao.system;

import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author wangtao
 * Created at 2019/1/18 14:11
 */
public class SystemProperties {

    public static void main(String[] args) {
        // 系统默认编码
        System.out.println(System.getProperty("file.encoding"));
        // 默认字符集
        System.out.println(Charset.defaultCharset());
        // 默认语言
        System.out.println(System.getProperty("user.language"));
        System.out.println(System.getProperty("line.separator"));
        System.out.println(System.getProperty("path.separator"));
        Properties properties = System.getProperties();
        properties.forEach((key, value) ->
            System.out.println(key + " = " + value)
        );
    }
}
