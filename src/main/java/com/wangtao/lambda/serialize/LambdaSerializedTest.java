package com.wangtao.lambda.serialize;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wangtao
 * Created at 2022/8/9 22:31
 */
public class LambdaSerializedTest {

    @Test
    public void fun1() {
        SFunction<User, String> sFunction = User::getName;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(sFunction);
            oos.flush();
            // 反序列化
            byte[] bytes = baos.toByteArray();
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                Object obj = ois.readObject();
                System.out.println(obj.getClass());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fun2() {
        SFunction<User, String> sFunction = User::getName;
        try {
            System.out.println(sFunction.getClass());
            Method method = sFunction.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(sFunction);
            Assert.assertEquals("getName", serializedLambda.getImplMethodName());
            Assert.assertEquals(User.class.getName().replace(".", "/"), serializedLambda.getImplClass());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fun3() {
        SFunction<User, String> sFunction = User::getName;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(sFunction);
            oos.flush();
            // 反序列化
            byte[] bytes = baos.toByteArray();
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes)) {

                /**
                 * resolveClass可以允许在反序列化时替换类型, 但是需要满足下面条件
                 * 1. 类名必须一致, 包名可以不一样
                 * 2. serialVersionUID必须和原来的类一样(这个是反序列化的必要条件, 不然会报错)
                 */
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    Class<?> clazz = super.resolveClass(desc);
                    return clazz == SerializedLambda .class ? com.wangtao.lambda.serialize.SerializedLambda.class : clazz;
                }
            }) {
                com.wangtao.lambda.serialize.SerializedLambda serializedLambda = (com.wangtao.lambda.serialize.SerializedLambda ) ois.readObject();
                Assert.assertEquals("getName", serializedLambda.getImplMethodName());
                Assert.assertEquals(User.class.getName().replace(".", "/"), serializedLambda.getImplClass());
            }
        } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
        }
    }
}
