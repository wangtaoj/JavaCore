package com.wangtao.reflection.bean;

import java.util.List;

/**
 * @author wangtao
 * Created at 2019/1/28 16:22
 */
public class First<K, V> {

    public V value;

    public K key;

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }

    public <T extends List<?>> T wildcardMethod() {
        return null;
    }
}




