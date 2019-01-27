package com.wangtao.generic.capture;

import java.util.List;

/**
 * @author wangtao
 * Created at 2019/1/27 14:10
 */
public class CaptureConversion {

    /**
     * 测试赋值.
     * 1. 原生类型与<?>可以相互赋值
     */
    List temp1;
    List<?> temp2;
    void assign1(List<?> list) {
        temp1 = list;
        temp2 = temp1;
    }

    /**
     * 2. 原生类型可以赋值给确切类型.
     * 3. <?>不可以赋值给确切类型.
     */
    @SuppressWarnings("unchecked")
    void assign2() {
        // Right, Unchecked警告
        List<String> list1 = temp1;
        System.out.println(list1);
        // Compile Error. 不兼容的类型
        // List<String> list2 = temp2;
    }

    /*
     * 类型捕获
     * 如果向一个使用无限定通配符的方法, 传递原生类型, 那么对编译器, 可能会推断出实际类型.
     * 可以使得这个方法可以回调另一个使用这个确切类型的方法.
     * 注:
     * 1. 必须带参数化泛型. 参数不能写死, 如Holder<String> holder, 否则编译不通过.
     * 2. Holder<T> 与 Holder<?>本身不能直接赋值, 必须通过方法参数传递, 由编译器推断.
     */
    static <T> void f1(Holder<T> holder) {
        System.out.println(holder.get());
    }

    static void f2(Holder<?> holder) {
        f1(holder);
    }

    public static void main(String[] args) {
        Holder holder = new Holder<>("capture conversion");
        // f1(holder); // 产生警告
        f2(holder); // 推断实际类型为String, 传给f1
    }
}
