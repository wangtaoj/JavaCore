package com.wangtao.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtao at 2018/9/30 13:22
 */
public class JSONUtilsTest {

    private User user = new User();
    List<User> list = new ArrayList<>();

    @Before
    public void init() {
        user.setUsername("wangtao");
        user.setAge(21);
        user.setExist(true);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(new Date());
        user.setBirthday(LocalDate.of(1997, 5, 3));
        list.add(user);
        list.add(user);
    }

    @Test
    public void testObjToJson() {
        String json = JSONUtils.objToJson(user);
        System.out.println(json);
        User user = JSONUtils.jsonToObj(json, new TypeReference<User>() {
        });
        System.out.println(user);
    }

    @Test
    public void testJsonToList() {
        String json = JSONUtils.objToJson(list);
        System.out.println(json);
        List<User> users = JSONUtils.jsonToList(json, User.class);
        System.out.println(users);
    }

    @Test
    public void testJsonToObj() {
        String json = JSONUtils.objToJson(list);
        System.out.println(json);
        List<User> users = JSONUtils.jsonToObj(json, new TypeReference<List<User>>() {
        });
        System.out.println(users);
        List<Map<String, Object>> userList = JSONUtils.jsonToObj(json, new TypeReference<List<Map<String, Object>>>() {
        });
        if (userList == null) {
            return;
        }
        userList.forEach(e ->
            e.forEach((key, value) -> {
                System.out.println(key);
                System.out.println(value);
            })
        );
    }

    private static class User {
        private boolean exist;

        private String username;

        private Integer age;

        private LocalDate birthday;

        private LocalDateTime createTime;

        private Date updateTime;

        public boolean isExist() {
            return exist;
        }

        public void setExist(boolean exist) {
            this.exist = exist;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", age=" + age +
                    ", createTime=" + createTime +
                    '}';
        }
    }
}

