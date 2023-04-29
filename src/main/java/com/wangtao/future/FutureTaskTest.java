package com.wangtao.future;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author wangtao
 * Created at 2023/4/29 16:07
 */
public class FutureTaskTest {

    @Test
    public void testRun() throws ExecutionException, InterruptedException {
        final String res = "Hello, Future!";
        FutureTask<String> task = new FutureTask<>(() -> {
            Assert.assertEquals("main", Thread.currentThread().getName());
            return res;
        });
        task.run();
        Assert.assertTrue(task.isDone());
        Assert.assertFalse(task.isCancelled());
        Assert.assertEquals(res, task.get());
    }

    @Test
    public void testCancelThenRun() {
        final String res = "Hello, Future!";
        FutureTask<String> task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName());
            return res;
        });
        boolean cancelled = task.cancel(true);
        Assert.assertTrue(cancelled);
        Assert.assertTrue(task.isCancelled());
        // isDone包含正常结束、异常结束、被取消
        Assert.assertTrue(task.isDone());
        // get方法会抛CancellationException
        try {
            task.get();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof CancellationException);
        }
        // 取消后运行, 直接结束, 不会执行后续逻辑
        task.run();
    }
}
