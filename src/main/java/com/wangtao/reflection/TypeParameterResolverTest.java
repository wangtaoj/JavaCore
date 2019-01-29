package com.wangtao.reflection;

import com.wangtao.reflection.bean.First;
import com.wangtao.reflection.bean.Second;
import com.wangtao.reflection.bean.Son;
import com.wangtao.reflection.bean.Third;
import com.wangtao.reflection.type.TypeParameterResolver;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.List;

/**
 * @author wangtao
 * Created at 2019/1/29 13:44
 */
public class TypeParameterResolverTest {

    @Test
    public void resolveFieldType() {
        try {
            Field field = Son.class.getField("item");
            Type type = TypeParameterResolver.resolveFieldType(field, Son.class);
            Assert.assertTrue(type instanceof Class);
            Assert.assertEquals(String.class, type);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resolveFieldTypeDeeply() {
        try {
            Field field = Third.class.getField("value");
            Type type = TypeParameterResolver.resolveFieldType(field, Third.class);
            Assert.assertTrue(type instanceof Class);
            Assert.assertEquals(String.class, type);

            field = Second.class.getField("value");
            type = TypeParameterResolver.resolveFieldType(field, Second.class);
            Assert.assertTrue(type instanceof Class);
            Assert.assertEquals(Object.class, type);

            field = First.class.getField("value");
            type = TypeParameterResolver.resolveFieldType(field, First.class);
            Assert.assertTrue(type instanceof Class);
            Assert.assertEquals(Object.class, type);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resolveReturnTypeDeeply() {
        try {
            Method method = Third.class.getMethod("getValue");
            Type type = TypeParameterResolver.resolveReturnType(method, Third.class);
            Assert.assertTrue(type instanceof Class);
            Assert.assertEquals(String.class, type);

            method = Second.class.getMethod("getValue");
            type = TypeParameterResolver.resolveReturnType(method, Second.class);
            Assert.assertTrue(type instanceof Class);
            Assert.assertEquals(Object.class, type);

            method = First.class.getMethod("getValue");
            type = TypeParameterResolver.resolveReturnType(method, First.class);
            Assert.assertTrue(type instanceof Class);
            Assert.assertEquals(Object.class, type);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resolveWildcardTypeV1() {
        try {
            Method method = First.class.getMethod("wildcardMethod");
            Type returnType = TypeParameterResolver.resolveReturnType(method, First.class);
            Assert.assertTrue(returnType instanceof ParameterizedType);
            ParameterizedType type = (ParameterizedType) returnType;
            Assert.assertEquals(List.class, type.getRawType());
            Assert.assertEquals(1, type.getActualTypeArguments().length);
            Assert.assertTrue("?", type.getActualTypeArguments()[0] instanceof WildcardType);
            Assert.assertEquals("?", type.getActualTypeArguments()[0].toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resolveWildcardTypeV2() {
        try {
            Class<?> clazz = Second.class;
            Method method = clazz.getMethod("wildcardMethod");
            Type returnType = TypeParameterResolver.resolveReturnType(method, clazz);
            Assert.assertTrue(returnType instanceof ParameterizedType);
            ParameterizedType type = (ParameterizedType) returnType;
            Assert.assertEquals(List.class, type.getRawType());
            Assert.assertEquals(1, type.getActualTypeArguments().length);
            Assert.assertTrue(type.getActualTypeArguments()[0] instanceof WildcardType);
            Assert.assertEquals("?", type.getActualTypeArguments()[0].toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
