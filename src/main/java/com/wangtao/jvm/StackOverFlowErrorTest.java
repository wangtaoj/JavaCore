package com.wangtao.jvm;

/**
 * @author wangtao
 * Created on 2018/2/26
 **/
public class StackOverFlowErrorTest {

    private int length;

    public static void main(String[] args) {

        StackOverFlowErrorTest test = new StackOverFlowErrorTest();
        try {
            test.dfs();
        } catch (Throwable e) {
            System.out.println("------------------stack length: " + test.length + "----------------");
            e.printStackTrace();
        }

    }

    void dfs() {
        length++;
        dfs();
    }

}
