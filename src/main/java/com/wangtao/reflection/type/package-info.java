/**
 * @author wangtao
 * Created at 2019/1/29 10:01
 */
package com.wangtao.reflection.type;

/*
 * Java语言中的Type体系
 *
 * 一. 简介
 * java.lang.reflect.Type是Java语言所有类型的公共超级接口.
 * 类型包括:
 * 1. 原始类型(Class),  如String, List, String[], int[](普通数组类型也属于原始类型).
 * 2. 基本类型(Class),  如int, long, float等八种基本类型.
 * 3. 参数化类型(ParameterizedType),  如List<String>, List<?>, List<? extends Number>等.
 * 4. 类型变量(TypeVariable), 如Map中声明的变量K, V.
 * 5. 泛型数组(GenericArrayType), 如K[], List<String>[]等.
 *    泛型数组的componentType([]前面那部分)是类型变量或者参数化泛型.
 *
 * 二. 继承体系
 * Type是超级接口, 具有5个直接子类
 * 1. Class
 * 2. TypeVariable
 * 3. ParameterizedType
 * 4. WildcardType: 代表通配符. ?(无限定通配符), ? extends Number(上界通配符), ? super Integer(下界通配符)
 *    虽然WildcardType接口实现了Type接口, 但是它Java语言中的类型.
 * 5. GenericArrayType
 * 除了Class外, 其它子类都是接口.
 */