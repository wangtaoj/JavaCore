package com.wangtao.datetime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author wangtao
 * Created at 2024-09-02
 */
public class DateFormatWithZoneTest {

    @Test
    public void testFormatDate() {
        // 该毫秒数对应UTC时间为2024-09-02 13:45:49
        long millis = 1725284749641L;
        Date date = new Date(millis);

        String pattern = "yyyy-MM-dd HH:mm:ss";
        /*
         * SimpleDateFormat线程不安全, 方法会修改自身
         * timeZone默认为TimeZone.getDefault()，国内即Asia/Shanghai
         */
        DateFormat dateFormat = new SimpleDateFormat(pattern);

        DateFormat dateFormatGmt9 = new SimpleDateFormat(pattern);
        dateFormatGmt9.setTimeZone(TimeZone.getTimeZone("GMT+09:00"));

        // 东八区: 2024-09-02 21:45:49
        System.out.println(dateFormat.format(date));
        // 东九区: 2024-09-02 22:45:49
        System.out.println(dateFormatGmt9.format(date));
    }

    @Test
    public void testFormatJsr310Date() {
        /*
         * 下面4个都是代表东八区的2024-09-02 12:45:49
         * LocalDateTime没有时区概念
         * Instant + 东八区便能得到时间024-09-02 12:45:49
         */
        LocalDateTime localDateTime = LocalDateTime.of(2024,9, 2, 21, 45, 49);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Shanghai"));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(8));
        Instant instant = Instant.ofEpochMilli(1725284749641L);

        // 2024-09-02T21:45:49
        System.out.println(localDateTime);
        // 2024-09-02T21:45:49+08:00[Asia/Shanghai]
        System.out.println(zonedDateTime);
        // 2024-09-02T21:45:49+08:00
        System.out.println(offsetDateTime);
        /*
         * 2024-09-02T13:45:49.641Z
         * Instant toString方法展示的是UTC时区的时间, 末尾的Z代表UTC
         * 那么对应于东八区的时间就是2024-09-02T21:45:49.64
         */
        System.out.println(instant);

        /*
         * DateTimeFormatter线程安全，和String一样，为不可变对象
         * 与SimpleDateFormat不同，时区字段默认是null，没有默认值
         */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 2024-09-02 21:45:49
        System.out.println(formatter.format(localDateTime));
        // 2024-09-02 21:45:49
        System.out.println(formatter.format(zonedDateTime));
        // 2024-09-02 21:45:49
        System.out.println(formatter.format(offsetDateTime));
        // 会报错, 格式化成年月日这样形式, Formatter必须要指定时区
        Assertions.assertThrowsExactly(UnsupportedTemporalTypeException.class, () -> formatter.format(instant));

        // 指定东九区时区
        DateTimeFormatter utc9Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneOffset.ofHours(9));

        // 2024-09-02 21:45:49
        System.out.println(utc9Formatter.format(localDateTime));
        // 2024-09-02 22:45:49
        System.out.println(utc9Formatter.format(zonedDateTime));
        // 2024-09-02 22:45:49
        System.out.println(utc9Formatter.format(offsetDateTime));
        // 2024-09-02 22:45:49
        System.out.println(utc9Formatter.format(instant));
    }
}
