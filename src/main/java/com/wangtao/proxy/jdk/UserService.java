package com.wangtao.proxy.jdk;

/**
 * 接口类
 * @author wangtao
 * Created on 2018/2/22
 **/
public interface UserService {
    /**
     * 业务方法
     */
    void service();

    void add();

    default void close() {
        System.out.println("execute default method 'close'.");
    }
}
