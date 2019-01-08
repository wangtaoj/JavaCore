package com.wangtao.proxy.cglib;

import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;

/**
 * @author wangtao
 * Created on 2018/2/23
 **/
public class CglibTest {

    public static final String OUTPUT_DIR = CglibTest.class.getResource("/").getPath();

    //@Before
    public void init(){
        //设置将cglib生成的代理类字节码生成到指定位置
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, OUTPUT_DIR);
    }

    @Test
    public void testProxy() {
        //生成代理类所需的参数
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DBService.class);
        enhancer.setCallback(new ProxyInterceptor(new DBService()));
        //生成代理
        DBService dbService = (DBService)enhancer.create();

        System.out.println(dbService.getClass().getName());
        dbService.add();
    }

    @Test
    public void testProxyFilter() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DBService.class);
        enhancer.setCallbackFilter(new ProxyFilter());
        enhancer.setCallbacks(new Callback[]{new ProxyInterceptor(), NoOp.INSTANCE});
        DBService dbService = (DBService)enhancer.create();
        System.out.println(dbService.getClass().getName());
        dbService.add();
        System.out.println("-----------我是分隔线-------------");
        dbService.remove();
    }
}
