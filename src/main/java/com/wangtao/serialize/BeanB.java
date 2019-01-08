package com.wangtao.serialize;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Externalizable可以控制对象在序列化时和反序列化时的行为.
 * 所有想要序列化的字段必须在writeExternal方法中手动调用writeXXX方法
 * 反序列化时会调用默认的构造方法进行初始化
 *
 * Externalizable实例在writeExternal序列化指定字段,
 * 反序列化时先调用默认的构造方法实例化对象, 然后在readExternal中恢复对象的指定字段
 * Created by wangtao at 2018/12/7 15:07
 */
public class BeanB implements Externalizable {

    private static final long serialVersionUID = -6056464369031411595L;

    private String name;

    private int age;

    public BeanB() {
        System.out.println("constructor is called");
    }

    public BeanB(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("writeExternal is called!");
        out.writeInt(age);
        out.writeUTF(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        System.out.println("readExternal is called!");
        age = in.readInt();
        name = in.readUTF();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "BeanB{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
