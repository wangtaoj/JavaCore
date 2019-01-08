package com.wangtao.jvm.thread;


/**
 * @author wangtao
 * created 2018/2/7
 **/
public class VolatileTest {

    private static volatile int race = 0;

    private static final int THREAD_COUNT = 20;

    private static void increace() {
        race++;
    }

    public static void main(String[] args) {
        for(int i = 0; i < THREAD_COUNT; i++) {
            new Thread(()->{
                for(int j = 0; j < 10000; j++)
                    increace();
            }).start();
        }

        System.out.println(race);
    }
}
