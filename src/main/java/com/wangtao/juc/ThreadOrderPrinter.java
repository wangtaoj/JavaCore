package com.wangtao.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangtao
 * Created at 2023/6/26 19:52
 */
public class ThreadOrderPrinter {

    private static boolean flag = true;

    private static final Lock lock = new ReentrantLock();

    private static final Condition conditionA = lock.newCondition();

    private static final Condition conditionB = lock.newCondition();

    private static int countA;

    private static int countB;

    /**
     * 依次交替答应AB, 每个线程打印50次
     */
    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            try {
                while (countA != 50) {
                    while (!flag) {
                        try {
                            conditionA.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("A");
                    countA++;
                    flag = !flag;
                    conditionB.signalAll();
                }
            } finally {
                lock.unlock();
            }

        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                while (countB != 50) {
                    while (flag) {
                        try {
                            conditionB.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("B");
                    countB++;
                    flag = !flag;
                    conditionA.signalAll();
                }
            } finally {
                lock.unlock();
            }

        }).start();
    }
}
