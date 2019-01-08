package com.wangtao.serialize;

import org.junit.Test;

import java.io.*;

/**
 * Created by wangtao at 2018/12/7 15:11
 */
public class BeanBTest {

    @Test
    public void serialize() {
        BeanB b = new BeanB("汪涛", 21);


        try (OutputStream os = new FileOutputStream("beanB.txt");
             ObjectOutputStream out = new ObjectOutputStream(os) ) {
            out.writeObject(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deserialize() {
        try (InputStream is = new FileInputStream("beanB.txt");
             ObjectInputStream in = new ObjectInputStream(is)) {
            Object obj = in.readObject();
            BeanB b = (BeanB) obj;
            System.out.println(b);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
