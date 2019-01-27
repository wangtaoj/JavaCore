package com.wangtao.generic.erase;

import java.util.Arrays;

/**
 * @author wangtao
 * Created at 2019/1/25 15:31
 */
public class GenericArray1<T> {

    private T[] array;

    /**
     * T[] array = new T[size] ERROR
     * 不能通过new关键字创建泛型数组, 只能强转
     */
    @SuppressWarnings("unchecked")
    public GenericArray1(int size) {
        array = (T[]) new Object[size];
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
        GenericArray1<String> genericArray = new GenericArray1<>(10);
        genericArray.set("aa", 0);
        String s = genericArray.get(0);
        System.out.println(s);
        // ClassCastException, 实际运行时是Object[]
        // String[] array = genericArray.getArray();
        Object[] array = genericArray.getArray();
        System.out.println(Arrays.toString(array));
    }
}
