package com.wangtao.serialize;

import java.io.Serializable;

/**
 * Created by wangtao at 2018/12/10 10:01
 */
public class BeanC implements Serializable {

    private static final long serialVersionUID = 4154320897942022267L;

    public BeanC(String name, A a) {
        this.name = name;
        this.a = a;
    }

    private A a;
    private String name;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static class A implements Serializable {
        private static final long serialVersionUID = -8614183888359035263L;
    }
}
