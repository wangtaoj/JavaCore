package com.wangtao.reflection.bean;

import java.util.List;

/**
 * @author wangtao
 * Created at 2019/1/28 16:22
 */
public class Father<T> {

    public T item;

    public List<?> list;

    public List<? extends List<String>> test;

    public T get() {
        return item;
    }

    public void set(T item) {
        this.item = item;
    }
}
