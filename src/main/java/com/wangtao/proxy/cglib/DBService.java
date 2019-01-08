package com.wangtao.proxy.cglib;

/**
 * 被代理类
 * @author wangtao
 * Created on 2018/2/23
 **/
public class DBService {

    public void add() {
        System.out.println("add operator");
    }

    public void remove() {
        System.out.println("remove operator");
    }

}
