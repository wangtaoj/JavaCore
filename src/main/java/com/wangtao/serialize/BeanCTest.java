package com.wangtao.serialize;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * Created by wangtao at 2018/12/10 10:04
 */
public class BeanCTest {

    /**
     * 因为每次反序列化回来的对象是一个新的对象, 根据这个特性
     * 可以对实现了Serializable的对象进行深拷贝.
     */
    @Test
    public void deepCopy() {
        ByteArrayOutputStream os = null;
        ByteArrayInputStream is = null;
        try {
            os = new ByteArrayOutputStream(1024);
            ObjectOutputStream out = new ObjectOutputStream(os);
            BeanC bean = new BeanC("beanC", new BeanC.A());
            out.writeObject(bean);
            is = new ByteArrayInputStream(os.toByteArray());
            ObjectInputStream in = new ObjectInputStream(is);
            BeanC copyBean = (BeanC) in.readObject();
            Assert.assertNotSame(bean, copyBean);
            Assert.assertNotSame(bean.getA(), copyBean.getA());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
