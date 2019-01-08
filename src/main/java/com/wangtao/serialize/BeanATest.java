package com.wangtao.serialize;

import org.junit.Test;

import java.io.*;

/**
 * Created by wangtao at 2018/12/7 14:35
 */
public class BeanATest {

    @Test
    public void serialize() {
        BeanA a = new BeanA();
        //a.setAge(21);
        a.setName("汪涛");

        try (OutputStream os = new FileOutputStream("in.txt");
             ObjectOutputStream out = new ObjectOutputStream(os) ) {
            out.writeObject(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deserialize() {
        /*
         * 1. 反序列化时, 如果曾经被序列化的类不在了, JVM找不到这个类,
         *    readObject会抛出ClassNotFoundException异常
         * 2. 反序列化时, 会检查类的serialVersionUID是否一致, 不一致会报InvalidClassException异常.
         *    意味者如果删掉了曾经序列化时的类, 重新新建一个一样的类(serialVersionUID不一致), 反序列化也会报错.
         *    关于serialVersionUID: 如果没有显示指定, JVM会自己生成.
         * 3. 如果序列化时的类在进行反序列化时, 该类增加的或减少的字段都会被忽略.
         * 4. 反序列化时, 并不会调用类的任何构造方法, 直接从流中获取数据恢复.
         * 5. 反序列化时的结果对象是一个新的对象, 与原对象不同.
         */
        try (InputStream is = new FileInputStream("in.txt");
            ObjectInputStream in = new ObjectInputStream(is)) {
            Object obj = in.readObject();
            BeanA a = (BeanA) obj;
            System.out.println(a);
            System.out.println(obj.getClass());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
