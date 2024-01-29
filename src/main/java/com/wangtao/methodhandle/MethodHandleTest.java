package com.wangtao.methodhandle;

import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author wangtao
 * Created at 2024-01-29
 */
public class MethodHandleTest {

    public static class GrandFather {

        public void say(String message) {
            System.out.println("GrandFather: " + message);
        }
    }

    public static class Father extends GrandFather {

        public void say(String message) {
            System.out.println("Father: " + message);
        }
    }

    public static class Son extends Father {

        public final void say(String message) {
            System.out.println("Son: " + message);
        }
    }

    /**
     * 支持方法多态调用, 因此像私有方法这种不能重写的方法是不支持的
     * 私有方法需要使用findSpecial方法
     */
    @Test
    public void testFindVirtual() throws Throwable{
        // 第一个参数为参数返回值类型, 其余参数为方法参数类型
        MethodType methodType = MethodType.methodType(void.class, String.class);
        MethodHandle tmp = MethodHandles.lookup()
                .findVirtual(GrandFather.class, "say", methodType);
        /*
         * 绑定对象, 相当于该对象调用这个方法
         * 如果不绑定, 则调用invoke系列方法时, 第一个参数是调用对象, 其余参数是方法参数
         * 注: 会返回一个新的MethodHandle
         */
        MethodHandle methodHandle = tmp.bindTo(new Son());
        // 打印Son: Hello
        methodHandle.invokeWithArguments("Hello");

        methodHandle = tmp.bindTo(new Father());
        // 打印Father: Hello
        methodHandle.invokeWithArguments("Hello");

        methodHandle = tmp.bindTo(new GrandFather());
        // 打印GrandFather: Hello
        methodHandle.invokeWithArguments("Hello");

        // 只支持绑定到Son这个类型的实例(包括子类), 因为使用的Son.class查找
        methodHandle = MethodHandles.lookup()
                .findVirtual(Son.class, "say", methodType)
                .bindTo(new Son());
        methodHandle.invokeWithArguments("Hello");
    }

    /**
     * 1. 这个方法不支持多态
     * 2. 关于方法查找逻辑:
     *    如果第一个参数与第四个参数一样, 则只从指定的这个类中查找方法, 查找不到抛异常
     *    如果不一样, 第四个参数必须是第一个参数的子类型, 则第一个参数是查找的上界,
     *    查找的起点类是第四个参数的父类, 查到方法则停止, 否则继续往父类找。
     * 3. 权限检查:
     *    MethodHandles.LookUp类中存在一个lookupClass属性
     *    在执行findSpecial方法时会进行一个检查, lookupClass必须与第四个参数是同一个类型
     *    从JDK9新增了一个另外, 如果第一个参数是接口, 则可以不一致。
     *    这样子即便直接使用MethodHandles.lookup()也能查找接口中的默认方法。
     *    具体逻辑参见findSpecial方法中的checkSpecialCaller方法
     *
     *    注: MethodHandles.lookup()返回的实例lookupClass属性为调用lookup()方法所在的类
     */
    @Test
    public void testFindSpecial() throws Throwable {
        MethodType methodType = MethodType.methodType(void.class, String.class);
        /*
         * JDK9才有privateLookupIn这个方法, JDK8无
         * 如果直接使用MethodHandles.lookup()获取Lookup实例, 则lookupClass=MethodHandleTest.class
         * 因为lookupClass属性值为执行MethodHandles.lookup()所在的类。
         * 那么lookupClass与第四个参数Son.class不一致, 执行findSpecial会报错
         * 因此使用MethodHandles.privateLookupIn修改lookupClass属性为Son.class
         *
         * 在JDK8中该如何呢, 可通过反射获取LookUp构造方法, 传入lookupClass即可
         * 或者不通过反射, 则只能在Son这个类中使用MethodHandles.lookup()了, 因为此时
         * lookupClass=Son.class, 与第四个参数Son.class一致, 则可以满足检查
         */
        MethodHandle methodHandle = MethodHandles.privateLookupIn(Son.class, MethodHandles.lookup())
                .findSpecial(GrandFather.class, "say", methodType, Son.class)
                .bindTo(new Son());
        /*
         * 尽管绑定的对象是Son的实例
         * 打印Father: Hello, 如果Father中没有此方法, 则打印GrandFather: Hello
         */
        methodHandle.invokeWithArguments("Hello");

        // 查到的方法为Son中的say方法
        methodHandle = MethodHandles.privateLookupIn(Son.class, MethodHandles.lookup())
                .findSpecial(Son.class, "say", methodType, Son.class)
                .bindTo(new Son());
        // 打印Son: Hello
        methodHandle.invokeWithArguments("Hello");
    }
}
