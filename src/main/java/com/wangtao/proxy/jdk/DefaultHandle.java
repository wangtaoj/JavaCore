package com.wangtao.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类, 实现事务的提交或者回滚
 * @author wangtao
 * Created on 2018/2/22
 **/
public class DefaultHandle implements InvocationHandler {

    public UserService userService;

    public DefaultHandle(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            begin();
            /*
             * 此处参数切记不能为proxy(生成的代理对象), 一定要真实的被代理对象, 否则
             * 会无限递归, proxy.method->invoke()->proxy.method
             * 即代理类的业务方法执行InvocationHandler接口的invoke方法
             * invoke方法调用代理类的业务方法。
             */
            Object res = method.invoke(userService, args);
            commit();
            return res;
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    private void begin() {
        System.out.println("开启事务!");
    }

    private void commit() {
        System.out.println("提交事务");
    }

    private void rollback() {
        System.out.println("出现异常, 回滚事务");
    }


}
