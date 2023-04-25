package com.wangtao.future;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/4/16 14:44
 */
public class CompletableFutureTest {

    /**
     * main: 1
     * >>>>>>>>> main
     */
    @Test
    public void fun0() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(() -> future.complete(1)).start();
        /*
         * 执行逻辑, 如果当前future已经完成则在当前线程执行消费逻辑
         * 如果当前future未完成, 则将消费逻辑放到栈上, 等到future完成后再执行消费逻辑
         * 执行消费逻辑的线程为执行future的线程
         *
         * 具体参见fun0  fun1  fun2结果
         */
        future.thenAccept((val) -> System.out.println(Thread.currentThread().getName() + ": " + val));
        System.out.println(">>>>>>>>> " + Thread.currentThread().getName());
        future.join();
    }

    /**
     * >>>>>>>>> main
     * future-thread: 1
     */
    @Test
    public void fun1() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(() -> {
            sleep(2);
            future.complete(1);
        }, "future-thread").start();
        future.thenAccept((val) -> System.out.println(Thread.currentThread().getName() + ": " + val));
        System.out.println(">>>>>>>>> " + Thread.currentThread().getName());
        future.join();
    }

    /**
     * 注: 该结果线程编号可能不一样
     * ForkJoinPool.commonPool-worker-9: future execute
     * >>>>>>>>> main
     * ForkJoinPool.commonPool-worker-9: 1
     *
     * 与fun1相比, 执行future的线程变成了内置的ForkJoinPool.commonPool-worker-9
     * 因此thenAccept消费逻辑也就变成了线程ForkJoinPool.commonPool-worker-9来执行
     */
    @Test
    public void fun2() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": future execute");
            sleep(2);
            return 1;
        });
        future.thenAccept((val) -> System.out.println(Thread.currentThread().getName() + ": " + val));
        System.out.println(">>>>>>>>> " + Thread.currentThread().getName());
        future.join();
    }

    @Test
    public void fun3() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        future.complete(1);
        /*
         * 与thenAccept相比, 消费逻辑交给内置的ForkJoinPool.commonPool执行
         * 即便future已经完成也是由ForkJoinPool.commonPool执行
         * 并且执行消费逻辑的线程不一定是执行future的线程
         */
        future.thenAcceptAsync((val) -> System.out.println(Thread.currentThread().getName() + ": " + val));
        future.join();
    }

    @Test
    public void fun4() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": future execute");
            sleep(2);
            return 1;
        });
        future.thenAcceptAsync((val) -> System.out.println(Thread.currentThread().getName() + ": " + val));
        future.join();
    }

    private void sleep(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
