package com.wangtao.jvm.thread;

/**
 * 测试先行发生原则的volatile变量规则
 * 在不同线程环境下对一个volatile变量的写操作总是先行发生于(后面)对这个变量的读操作
 * @author wangtao
 * Created at 2019/1/6 17:30
 */
public class HappenBefore {

    private volatile int value = 1;

    void setValue(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            HappenBefore happenBefore = new HappenBefore();
            new Thread(() -> happenBefore.setValue(5)).start();
            new Thread(() -> assertBoolean(happenBefore.getValue() == 5)).start();
            // 直到上面两个线程运行完毕再运行
            while (Thread.activeCount() > 2) {
                Thread.yield();
            }
        }
    }

    private static void assertBoolean(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}
