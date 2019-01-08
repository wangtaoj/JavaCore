package com.wangtao.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GenericExample {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        testQuestion(list);
        System.out.println("-----------------------------------");

        testExtends(Arrays.asList(1, 2, 3, 4));
        System.out.println("-----------------------------------");

        testSuper(new ArrayList<>());
    }

    /**
     * 无限定通配符(?), 不能往容器里添加元素, 只能获取Object的引用
     */
    public static void testQuestion(List<?> list) {
        // list.add(1) ERROR
        //遍历
        for (Object o : list) {
            System.out.println(o);
        }
    }

    /**
     * ? extends T 限定符 T为具体的Java类型
     * 只能读取T的引用, 因为T是上限 T是容器里所有元素的基类 ,不能往容器添加元素.
     * 比如List<? extends Number> list 可以接收List<Integer>, 也可以接收List<Double>, List<Number>
     * 所以list容器不能判断是该加Integer类型, 还是Double类型, 所以不能添加元素
     * 但是知道容器有一个上限基类Number. 所以读取是可行的
     */
    public static void testExtends(List<? extends Number> list) {
        //list.add(1); ERROR
        for(Number i : list) {
            System.out.println(i);
        }
    }

    /**
     * ? super T 限定符
     * 可以添加T类型元素, 可以读取但是需要用Object来接收, 不能用T接收, 也就是说不能读取T类型元素
     * 参数可以穿List<Number>, List<Integer>, List<Object> 只要是T的基类即可
     * 比方说List<? super Integer> list 这个容器里的元素肯定是Integer的基类型, 因此
     * 可以添加Integer类型.
     */
    public static void testSuper(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        //Integer e = list.get(0); ERROR
        Object object = list.get(0); //Right
        System.out.println(object);
    }
}