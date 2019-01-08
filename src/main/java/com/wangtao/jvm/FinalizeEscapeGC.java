package com.wangtao.jvm;

import java.util.concurrent.TimeUnit;

/**
 * Java对象的finalize方法测试自救行为
 * @author wangtao
 **/
public class FinalizeEscapeGC {

    public static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive() {
        System.out.println("yes, I am still alive");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        SAVE_HOOK = this; //使得垃圾对象重新与引用链连接, 自救
    }

    public static void main(String[] args) {
        SAVE_HOOK = new FinalizeEscapeGC();

        SAVE_HOOK = null; //标记为null, 使得垃圾回收
        System.gc(); //触发回收, 并且JVM会调用垃圾对象的finalize方法

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //对象第一次通过finalize方法拯救成功
        if(SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, I am dead!");
        }

        System.out.println("--------------我是华丽的分界线---------------");

        SAVE_HOOK = null; //标记为null, 使得垃圾回收
        System.gc(); //触发回收, 并且JVM会调用垃圾对象的finalize方法

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //对象第二次通过finalize方法拯救失败, 因为任意一个对象的finalize方法只会被系统调用一次
        if(SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, I am dead!");
        }
    }
}
