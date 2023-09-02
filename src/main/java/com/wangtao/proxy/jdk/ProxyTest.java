package com.wangtao.proxy.jdk;

import org.junit.Test;

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
     * 保存位置: ${project.dir}/com/sun/proxy
     * 注: 不是src下, 而是项目根目录/com/sun/proxy
     */
    @Test
    public void testProxy() {
        UserService userService = new UserServiceImpl();
        UserService proxyUserService = (UserService) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(),
                userService.getClass().getInterfaces(), new DefaultHandle(userService));
        System.out.println(proxyUserService.getClass().getName());
        // 普通方法
        proxyUserService.service();
        // 默认方法
        proxyUserService.close();
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
        // byte[] bytes = ProxyGenerator.generateProxyClass(userService.getClass().getSimpleName(),
        // userService.getClass().getInterfaces());
        byte[] bytes = new byte[0];
        String path = ProxyTest.class.getResource("/").getPath() + userService.getClass().getSimpleName() + ".class";
        System.out.println(path);
        try (OutputStream ops = new FileOutputStream(path)) {
            ops.write(bytes);
            ops.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        /*
        byte[] bytes = ProxyGenerator.generateProxyClass("aa", new Class<?>[] {UserService.class});
        String path = ProxyTest.class.getResource("/").getPath() + "$Proxy.class";
        System.out.println(path);
        try(OutputStream ops = new FileOutputStream(path)) {
            ops.write(bytes);
            ops.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

}
