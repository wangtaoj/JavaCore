package com.wangtao.jvm.thread;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangtao
 * created 2018/2/7
 **/
public class VolatileTest {

    private static volatile int race = 0;

    private static final AtomicInteger count = new AtomicInteger(0);

    private static final int THREAD_COUNT = 20;

    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    private static void increace() {
        race++;
        count.incrementAndGet();
    }

    public static void main(String[] args) {
        /*ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        for(Thread thread : threads) {
            System.out.println(thread.getName());
        }*/
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    increace();
                }
            }).start();
        }
        // main线程所在的线程组中最后活动线程会存在2个
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println("Primitive: " + race);
        System.out.println("AtomicInteger: " + count);
    }
}
