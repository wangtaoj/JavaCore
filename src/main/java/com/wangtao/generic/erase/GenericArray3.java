package com.wangtao.generic.erase;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author wangtao
 * Created at 2019/1/25 16:00
 */
public class GenericArray3<T> {

    private T[] array;

    /**
     * 通过Array.newInstance()实列化真正T[]数组
     */
    @SuppressWarnings("unchecked")
    public GenericArray3(Class<T> type, int size) {
        this.array = (T[]) Array.newInstance(type, size);
    }

    public T get(int index) {
        return array[index];
    }

    public void set(T t, int index) {
        array[index] = t;
    }

    public T[] getArray() {
        return array;
    }

    public static void main(String[] args) {
        GenericArray3<String> genericArray = new GenericArray3<>(String.class, 10);
        genericArray.set("aa", 0);
        String s = genericArray.get(0);
        System.out.println(s);
        String[] array = genericArray.getArray();
        System.out.println(Arrays.toString(array));
    }
}
