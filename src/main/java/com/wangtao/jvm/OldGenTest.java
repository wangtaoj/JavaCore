package com.wangtao.jvm;

/**
 * @author wangtao
 **/
public class OldGenTest {
    private static final int _1MB = 1024 * 1024;

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:PretenureSizeThreshold=3145728
     */
    public static void main(String[] args) {
        testAllocation();
    }

    public static void testAllocation() {
        byte[]  allocation4= new byte[6 * _1MB];
    }
}
