package com.wangtao.methodhandle;

import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;

/**
 * @author wangtao
 * Created at 2024-01-29
 */
public class MethodHandleInvokeTest {

    public static class Caculator {

        public Integer sum(Integer num1, Integer num2) {
            return num1 + num2;
        }
    }

    /**
     * invokeExact: 参数和返回值需要精确匹配, 不会自动类型转换, 使用参数声明的静态类型
     * invoke: 参数和返回值会自动类型转换, 比如转型、数字类型提升、拆箱装箱(包装类型,原始类型)
     * invokeWithArguments: invoke方法的升级版本, 支持使用数组作为可变参数传递
     */
    @Test
    public void testInvoke() throws Throwable {
        MethodType methodType = MethodType.methodType(Integer.class, Integer.class, Integer.class);
        MethodHandle tmp = MethodHandles.lookup()
                .findVirtual(Caculator.class, "sum", methodType);
        /*
         * 绑定对象, 相当于该对象调用这个方法
         * 如果不绑定, 则调用invoke系列方法时, 第一个参数是调用对象, 其余参数是方法参数
         * 注: 会返回一个新的MethodHandle
         */
        MethodHandle methodHandle = tmp.bindTo(new Caculator());
        Integer one = 1;
        // ok
        methodHandle.invoke(1, 1);

        // error, 因为参数类型是int, 不是Integer
        assertException(WrongMethodTypeException.class, () -> methodHandle.invokeExact(1, 1));
        // error, 因为返回类型是void, 不是Integer
        assertException(WrongMethodTypeException.class, () -> methodHandle.invokeExact(one, one));
        // ok, 参数类型是Integer, 返回值也是Integer
        Integer result = (Integer)methodHandle.invokeExact(one, one);
        Assert.assertEquals(2, result.intValue());

        Object[] args = new Object[] {1, 1};
        // error, invoke和invokeExact不接受数组作为可变参数, 会认为args是一个参数
        assertException(WrongMethodTypeException.class, () -> methodHandle.invoke(args));
        // ok, invokeWithArguments支持数组作为可变参数, 会当做两个参数看待
        methodHandle.invokeWithArguments(args);
    }

    private <T extends Throwable> void assertException(Class<T> expectedType, Executable executable) {
        try {
            executable.execute();
        } catch (Throwable e) {
            if (expectedType.isInstance(e)) {
                return;
            }
        }
        Assert.fail();
    }

    @FunctionalInterface
    public interface Executable {
        void execute() throws Throwable;
    }
}
