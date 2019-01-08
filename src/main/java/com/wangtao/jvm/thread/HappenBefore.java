package com.wangtao.jvm.thread;

/**
 * 测试先行发生原则的volatile变量规则
 * 在不同线程环境下对一个volatile变量的写操作总是先行发生于(后面)对这个变量的读操作
 *
 * @author wangtao
 * Created at 2019/1/6 17:30
 */
public class HappenBefore {

    public static void main(String[] args) {
        A a = new A();

        for (int i = 0; i < 1000; i++) {
            new Thread(()-> a.setValue(5)).start();
            new Thread(()->System.out.println(a.getValue())).start();
        }
    }

}

class A {
    private volatile int value;

    void setValue(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
