package com.wangtao.proxy.cglib;

import org.springframework.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 过滤方法, 有些方法不需要代理
 * @author wangtao
 * Created on 2018/2/23
 **/
public class ProxyFilter implements CallbackFilter {

    /**
     * 过滤方法, 返回的是enhancer.setCallbacks()中参数数组的索引
     * 从0开始, NoOp.INSTANCE是一个Callback实例, 不会拦截被代理的方法
     * @param method 被代理的方法
     */
    @Override
    public int accept(Method method) {
        String name = method.getName();
        if(!Objects.equals(name, "remove"))
            return 0;
        return 1;
    }
}
