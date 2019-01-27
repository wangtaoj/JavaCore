package com.wangtao.generic.capture;

/**
 * @author wangtao
 * Created at 2019/1/27 14:11
 */
public class Holder<T> {

    private T t;

    public Holder(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }
}
