package com.wangtao.regexp;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangtao
 * Created at 2019/1/6 17:38
 */
public class PatternExample {

    /**
     * public static String quote(String s)
     * 返回指定字符串的字面值模式, 也就是说字符串序列中的元字符和转义序列将不具有特殊含义.
     * 会使用\Q \E包裹, \Q \E中间的字符是字面意思, 不具有特殊含义.
     * <p>
     * public static Pattern compile(String regex, int flag)
     * 编译给定正则表达式
     * flag: 匹配标志, 常用的如下解释
     * CASE_INSENSITIVE: 匹配时大小写不敏感.
     * MULTILINE: 启用多行模式, ^ $匹配行的开头和结尾, 而不是整个输入序列的的开头和结尾.
     * UNIX_LINES: 启用UNIX换行符, 在多行模式中使用^ $时只有\n被识别成终止符.
     * DOTALL: 在此模式中元字符.可以匹配任意字符, 包括换行符.
     * LITERAL: 启用模式的字面值意思, 模式中的元字符、转义字符不再具有特殊含义.
     * <p>
     * public static boolean matches(String regex, String s)
     * 判断整个输入序列是否与给定的模式匹配.
     * 底层调用Matcher实例的matchers方法
     */
    @Test
    public void quote() {
        String regex1 = Pattern.quote(".java");
        Assert.assertEquals("\\Q.java\\E", regex1);
        Matcher matcher = Pattern.compile(regex1).matcher("Effect.java");
        Assert.assertTrue(matcher.find());

        String regex2 = Pattern.quote("\\.java");
        Assert.assertEquals("\\Q\\.java\\E", regex2);
        Assert.assertTrue(Pattern.compile(regex2).matcher("Effect\\.java").find());
        Assert.assertFalse(Pattern.compile(regex2).matcher("Effect.java").find());
    }

    @Test
    public void matches() {
        // 匹配一个非零整数
        String regex = "[+-]?[1-9][0-9]+";
        String case1 = "1234";
        String case2 = "-1234";
        String case3 = "+1234";
        String case4 = "0";
        String case5 = "0000";
        String case6 = "1234a";
        /*
         * matches()与find()方法的区别:
         * matches()判断整个输入序列是否与模式匹配.
         * find()只要输入序列有一处符合模式就返回true.
         */
        Assert.assertTrue(Pattern.matches(regex, case1));
        Assert.assertTrue(Pattern.matches(regex, case2));
        Assert.assertTrue(Pattern.matches(regex, case3));
        Assert.assertFalse(Pattern.matches(regex, case4));
        Assert.assertFalse(Pattern.matches(regex, case5));
        Assert.assertFalse(Pattern.matches(regex, case6));
    }

    @Test
    public void matchesVsFind() {
        String s = "satat";
        String regex = "at";
        Assert.assertFalse(Pattern.matches(regex, s));
        Assert.assertTrue(Pattern.compile(regex).matcher(s).find());
        Assert.assertFalse(Pattern.compile("^at").matcher(s).find());
    }
}
