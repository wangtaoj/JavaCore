package com.wangtao.jvm;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * @author wangtao
 * Created on 2018/2/26
 **/
public class PermSizeTest {

    /**
     * -XX:PermSize=10m -XX:MaxPermSize=10m
     */
    public static void main(String[] args) {
        for(; ;) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOM.class);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
                    return null;
                }
            });
            enhancer.create();
        }
    }

    static class OOM {

    }

}
