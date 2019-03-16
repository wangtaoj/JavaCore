package com.wangtao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 4个元注解
 * {@code @Target}: 表示该注解可以用于什么地方
 * {@code @Retention}: 表示注解信息可以保留到哪个阶段
 * {@code @Documented}: 将注解信息包含在Javadoc中
 * {@code @Inherited}: 允许子类可以继承父类中的注解, 也就是说如果自定义一个注解@UseCase, 而这个自定义注解使用
 * 了元注解{@code @Inherited}, 那么类A使用了@UseCase, 类A的子类B也能拿到注解@UseCase
 * <p/>
 * 注解属性:
 * 属性类型只能为基本类型, String, Class, Enum, Annotation以及这个几个类型的数组形式.
 * @author wangtao
 * Created at 2019/3/16 14:24
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {

    int[] ids() default {};
    String desp() default "";
}
