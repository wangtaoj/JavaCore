package com.wangtao.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao
 * Created at 2023/4/16 14:44
 */
@Slf4j
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
     * <p>
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

    /**
     * then方法只有当future正常完成时才会执行回调函数，并产生一个新的future
     * 如果future是异常完成，那么then方法不会执行回调函数，而是直接返回一个新的异常完成的Future
     * 并且这个future的结果是前面那个future的结果(异常)
     * thenAccept: 参数是future的返回结果, 返回一个新的Future, 泛型为Void，无返回结果
     * thenRun: 不需要future的返回结果，返回一个新的Future, 泛型为Void，，无返回结果
     * thenApply: 参数是future的返回结果，返回一个新的Future，结果是回调函数返回值
     */
    @Test
    public void thenXxx() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().isDaemon());
            log.info("executed");
            sleep(2);
            return 2;
        });
        CompletableFuture<Void> thenAcceptFuture = future.thenAccept(val ->
            log.info("thenAccept result: {}", val)
        );
        CompletableFuture<Void> thenRunFuture = future.thenRun(() ->
            log.info("thenRun: i just execute, not need the last result")
        );

        // 产生一个新的结果
        CompletableFuture<Integer> thenApplyFuture = future.thenApply(val -> {
            log.info("thenApply result: {}", val);
            return val * val;
        });

        // 等待执行完成
        future.join();

        Assertions.assertNull(thenAcceptFuture.join());
        Assertions.assertNull(thenRunFuture.join());
        Assertions.assertEquals(thenApplyFuture.join(), 4);
    }

    /**
     * thenXXX: then系列方法，future正常完成时回调
     * exceptionally: future异常完成时回调
     */
    @Test
    public void exp() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("task executed");
            throw new RuntimeException("异常测试");
        });
        CompletableFuture<Void> exceptionally = future
                .thenAccept((val) -> log.info("result: {}", val))
                .exceptionally(e -> {
                    log.info("task exceptionally: {}", e.getCause().getMessage());
                    return null;
                });
        exceptionally.join();
    }

    /**
     * 链式调用，与js的promise类似
     */
    @Test
    public void chain() {
        CompletableFuture.supplyAsync(() -> 1)
                .thenApply((val) -> {
                    log.info("result: {}, resultType: {}", val, val.getClass().getSimpleName());
                    // 产生一个新的结果
                    return String.valueOf(val);
                })
                .thenAccept((val) -> log.info("result: {}, resultType: {}", val, val.getClass().getSimpleName()))
                .exceptionally(e -> {
                    log.info("task exceptionally: {}", e.getCause().getMessage());
                    return null;
                })
                .join();
    }

    private void sleep(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
