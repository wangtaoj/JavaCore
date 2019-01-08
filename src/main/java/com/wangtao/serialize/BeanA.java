package com.wangtao.serialize;

import java.io.Serializable;

/**
 * Created by wangtao at 2018/12/7 14:33
 */
public class BeanA implements Serializable {

    private static final long serialVersionUID = 3675271667513495921L;

    private int age;

    private String name;

    private String password;

    public BeanA() {
        System.out.println("default constructor is called!");
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "BeanA{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
