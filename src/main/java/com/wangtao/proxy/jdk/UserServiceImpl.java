package com.wangtao.proxy.jdk;

/**
 * 实现类, 处理业务, 被代理对象
 * @author wangtao
 * Created on 2018/2/22
 **/
public class UserServiceImpl implements UserService {
    @Override
    public void service() {
        System.out.println("execute user service");
        add();
    }

    @Override
    public void add() {
        System.out.println("execute user add");
    }
}
