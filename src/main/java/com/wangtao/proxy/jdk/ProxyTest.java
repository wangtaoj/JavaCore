package com.wangtao.proxy.jdk;

import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Proxy;

/**
 * @author wangtao
 * Created on 2018/2/22
 **/
public class ProxyTest {

    /**
     * -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true
     * 开启这个JVM参数会将动态生成的代理对象保存到硬盘
     */
    @Test
    public void testProxy()  {
        UserService userService = new UserServiceImpl();
        UserService proxyUserService = (UserService) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(),
                userService.getClass().getInterfaces(), new DefaultHandle(userService));

        //生成对象的字节码
        //saveToDisk(proxyUserService);
        System.out.println(proxyUserService.getClass().getName());
        proxyUserService.service();


        /*try {
            //被代理对象
            UserService userService = new UserServiceImpl();
            //拿到代理类的字节码对象
            Class<?> cls = Proxy.getProxyClass(ProxyTest.class.getClassLoader(),
                userService.getClass().getInterfaces());
            //获取构造方法
            Constructor<?> con = cls.getConstructor(InvocationHandler.class);
            //获取代理对象
            UserService proxyUserService = (UserService)con.newInstance(
                new DefaultHandle(userService));
            proxyUserService.service();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    /**
     * 生成代理对象的字节码 或者下面这样也可以, 会在项目的根目录下生成class
     * -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true
     * 开启这个JVM参数会将动态生成的代理对象保存到硬盘
     */
    private void saveToDisk(UserService userService) {
        /*
         * ProxyGenerator.generateProxyClass(String name, Class<?>[] interfaces)
         * name: 生成的类的类名(反编译后看到的类名)
         * interfaces: 接口列表
         * 此方法生成的字节码(类名 = name), 会自动继承Proxy, 实现指定的接口列表
         * 里面包含接口的所有方法, 以及hashCode, equals, toString方法
         */
        byte[] bytes = ProxyGenerator.generateProxyClass(userService.getClass().getSimpleName(),
                userService.getClass().getInterfaces());
        String path = ProxyTest.class.getResource("/").getPath() + userService.getClass().getSimpleName() + ".class";
        System.out.println(path);
        try(OutputStream ops = new FileOutputStream(path)) {
            ops.write(bytes);
            ops.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test() {
        byte[] bytes = ProxyGenerator.generateProxyClass("aa", new Class<?>[] {UserService.class});
        String path = ProxyTest.class.getResource("/").getPath() + "$Proxy.class";
        System.out.println(path);
        try(OutputStream ops = new FileOutputStream(path)) {
            ops.write(bytes);
            ops.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
