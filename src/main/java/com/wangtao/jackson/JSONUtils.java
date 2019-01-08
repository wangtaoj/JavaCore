package com.wangtao.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON工具类.
 * Created at 2018/9/30 11:45
 *
 * @author wangtao
 */
public class JSONUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";

    private static final Logger LOG = LoggerFactory.getLogger(JSONUtils.class);

    static {

        //设置java.util.Date时间类的序列化以及反序列化的格式
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_PATTERN));

        // 初始化JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        //处理LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        //处理LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        //处理LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        //注册时间模块, 支持支持jsr310, 即新的时间类(java.time包下的时间类)
        objectMapper.registerModule(javaTimeModule);

        // 包含所有字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 在序列化一个空对象时时不抛出异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // 忽略反序列化时在json字符串中存在, 但在java对象中不存在的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private JSONUtils() {

    }

    /**
     * 将Java对象序列化成一个JSON对象或者JSON数组.
     *
     * @param object Java对象
     * @return 返回一个JSON格式的字符串
     */
    public static String objToJson(Object object) {
        try {
            if (object != null) {
                return objectMapper.writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            LOG.error("parse {} to json error!", object, e);
        }
        return null;
    }

    /**
     * 将JSON对象反序列化成一个Java原生对象, 不支持泛型.
     *
     * @param json JSON对象
     * @param cls  Java对象原始类型的class对象
     * @param <T>  Java对象的原始类型
     * @return 返回一个T类型的对象
     */
    public static <T> T jsonToObj(String json, Class<T> cls) {
        try {
            if (json != null && json.length() > 0) {
                return objectMapper.readValue(json, cls);
            }
        } catch (IOException e) {
            LOG.error("parse {} to object error!", json, e);
        }
        return null;
    }

    /**
     * 将JSON反序列化成一个Java对象, 支持泛型.
     * TypeReference是一个抽象类, 用来构造类型
     * 调用方式: 传入一个TypeReference的匿名实现类即可
     * User user = jsonToObj(json, new TypeReference<User>(){})
     * List<User> users = jsonToObj(json, new TypeReference<List<User>>(){})
     *
     * @param json          JSON对象
     * @param typeReference 类型引用
     * @param <T>           返回值类型
     * @return 返回一个Java对象
     */
    public static <T> T jsonToObj(String json, TypeReference<?> typeReference) {
        try {
            if (json != null && json.length() > 0) {
                return objectMapper.readValue(json, typeReference);
            }
        } catch (Exception e) {
            LOG.error("parse {} to object error!", json, e);
        }
        return null;
    }

    /**
     * 将一个JSON数组反序列化成一个List对象.
     *
     * @param json JSON数组
     * @param cls  Java对象原始类型的class对象
     * @param <T>  Java对象的原始类型
     * @return 返回一个List列表
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            if (json != null && json.length() > 0) {
                JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, cls);
                list = objectMapper.readValue(json, javaType);
            }
        } catch (IOException e) {
            LOG.error("parse {} to object error!", json, e);
        }
        return list;
    }
}
