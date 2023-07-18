package com.wangtao.threadlocal;

/**
 * @author wangtao
 * Created at 2023/7/18 19:55
 */
public class ThreadLocalTest {

    /**
     * 当前线程启动的线程在创建时会把父线程的threadLocalMap里存的变量复制出来
     * 这样子启动的异步线程可以获取到父线程保存的值了
     */
    private static final InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set("parent");

        new Thread(() -> System.out.println(threadLocal.get())).start();
    }
}
