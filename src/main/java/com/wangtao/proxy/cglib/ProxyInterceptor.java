package com.wangtao.proxy.cglib;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 使用CGLIB动态代理, 定义拦截器
 * CGLIB原理: 不像JDK动态代理, 需要依赖被代理类实现接口。因为在内存中生成的代理类需要实现同被代理类一样的接口
 * cglib是在内存中生成一个代理类, 这个代理类继承了被代理类。
 * 因此使用cglib, 被代理类一定不能用final修饰, 被代理的方法也不能用final修饰
 * @author wangtao
 * Created on 2018/2/23
 **/
public class ProxyInterceptor implements MethodInterceptor{

    private DBService dbService;

    public ProxyInterceptor(DBService dbService) {
        this.dbService = dbService;
    }

    public ProxyInterceptor() {

    }

    /**
     *
     * @param obj 生成的代理类
     * @param method 被代理的方法
     * @param objects 被代理方法参数
     * @param methodProxy 代理方法
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        try {
            begin();
            //执行代理方法
            result = methodProxy.invokeSuper(obj, objects);
            //result = methodProxy.invoke(dbService, objects);
            //result = method.invoke(dbService, objects);与上面那句作用一样
            commit();
        } catch (Exception e) {
            e.printStackTrace();
            rollback();
        }
        return result;
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

    public DBService getDbService() {
        return dbService;
    }
}
