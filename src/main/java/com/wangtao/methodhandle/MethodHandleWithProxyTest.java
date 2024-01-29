package com.wangtao.methodhandle;

import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangtao
 * Created at 2024-01-29
 */
public class MethodHandleWithProxyTest {

    /**
     * 动态代理中借助MethodHandle执行接口中的默认方法
     * 如果直接调用method.invoke(proxy, args)则会无限递归
     * proxy.method() -> InvocationHandler.invoke() -> proxy.method()
     */
    @Test
    public void testExecuteDefaultAtProxy() {
        Mapper proxyInstance = (Mapper) Proxy.newProxyInstance(
                Mapper.class.getClassLoader(),
                new Class<?>[]{Mapper.class},
                new DefaultHandler()
        );
        String sql = "select * from user";
        proxyInstance.executeWithPage(sql, 0 , 10);
    }

    public interface Mapper {
        void execute(String sql);

        default void executeWithPage(String sql, int offset, int size) {
            execute(sql + " limit " + offset + ", " + size);
        }
    }

    public static class DefaultHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
            if (method.isDefault()) {
                MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
                /*
                 * 1. 指定lookupClass为方法声明的类, 即接口Mapper.class
                 * 2. 查找接口中对应的默认方法, 限制findSpecial第一个参数和第四个参数都是Mapper.class
                 * 3. 这样lookupClass=findSpecial第四个参数, 可以通过权限检查
                 * 4. 绑定方法到代理对象中, 这样子默认方法如果有调用接口其他方法, 可以让其他方法拥有代理增强
                 *    多态体现
                 */
                Class<?> lookupClass = method.getDeclaringClass();
                MethodHandle methodHandle = MethodHandles.privateLookupIn(lookupClass, MethodHandles.lookup())
                        .findSpecial(lookupClass, method.getName(), methodType, lookupClass)
                        .bindTo(proxy);
                return methodHandle.invokeWithArguments(args);
            } else if("execute".equals(method.getName())) {
                System.out.println(args[0]);
                return null;
            } else {
                throw new UnsupportedOperationException(method.getName());
            }
        }
    }
}
