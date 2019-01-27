package com.wangtao.generic.erase;


/**
 * @author wangtao
 * Created at 2019/1/25 16:14
 */
public class GenericErase<T> {

    /**
     * 泛型擦除的影响
     * 由于泛型擦除的原因, 泛型信息只存在编译期间, 运行时将丢失.
     * 对于有关类型的操作都将编译失败.
     */
    public static void f(Object arg) {
        /*
        if(arg instanceof T) {

        }
        T t = new T();
        T[] array = new T[5];
        */
    }

}
