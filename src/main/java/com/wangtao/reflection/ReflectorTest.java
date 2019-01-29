package com.wangtao.reflection;

import com.wangtao.reflection.bean.Son;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.List;

/**
 * @author wangtao
 * Created at 2019/1/28 16:25
 */
public class ReflectorTest {

    @Test
    public void typeVariable() {
        try {
            Method method = Son.class.getMethod("get");
            Assert.assertEquals(Object.class, method.getReturnType());
            Type type = method.getGenericReturnType();
            Assert.assertTrue(type instanceof TypeVariable);
            TypeVariable typeVariable = (TypeVariable) type;
            Assert.assertEquals("T", typeVariable.getName());
            Assert.assertEquals(1, typeVariable.getBounds().length);
            Assert.assertEquals(Object.class, typeVariable.getBounds()[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parameterizedType() {
        try {
            Field field = Son.class.getField("list");
            Type type = field.getGenericType();
            Assert.assertTrue(type instanceof ParameterizedType);
            // List<?>
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Assert.assertEquals(List.class, parameterizedType.getRawType());
            // ?
            Type typeArg = parameterizedType.getActualTypeArguments()[0];
            Assert.assertTrue(typeArg instanceof WildcardType);
            WildcardType wildcardType = (WildcardType) typeArg;
            // ?占位符(无限定通配符)没有下边界, 上边界如果没有指定, 那么是Object.
            Assert.assertEquals(0, wildcardType.getLowerBounds().length);
            Assert.assertEquals(1, wildcardType.getUpperBounds().length);
            Assert.assertEquals(Object.class, wildcardType.getUpperBounds()[0]);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
