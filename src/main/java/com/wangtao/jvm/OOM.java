package com.wangtao.jvm;

/**
 * @author wangtao
 * Created on 2018/2/26
 **/
public class OOM {
    private final static int _5M = 1024 * 1024 * 5;

    /**
     * -Xms4m -Xmx4m -XX:+HeapDumpOnOutOfMemoryError
     */
    public static void main(String[] args) {
        byte[] bytes = new byte[_5M];
        System.out.println(bytes.getClass());
    }

}
