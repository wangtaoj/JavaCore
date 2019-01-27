package com.wangtao.generic.erase;

import java.util.Arrays;

/**
 * @author wangtao
 * Created at 2019/1/25 15:45
 */
public class GenericArray2<T> {

    /**
     * 在内部将array作为Object[]数组类型而不是T[]数组类型,
     * 不会遗忘array的运行时类型
     */
    private Object[] array;

    public GenericArray2(int size) {
        array = new Object[size];
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T) array[index];
    }

    public void set(T t, int index) {
        array[index] = t;
    }

    public Object[] getArray() {
        return array;
    }

    public static void main(String[] args) {
        GenericArray2<String> genericArray = new GenericArray2<>(10);
        genericArray.set("aa", 0);
        String s = genericArray.get(0);
        System.out.println(s);
        // safe
        Object[] array = genericArray.getArray();
        System.out.println(Arrays.toString(array));
    }


}
