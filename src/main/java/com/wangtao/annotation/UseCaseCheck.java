package com.wangtao.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangtao at 2018/12/10 11:31
 */
public class UseCaseCheck {

    @UseCase(ids = {1, 2, 3})
    public static boolean checkId(List<Integer> idList) {
        try {
            Method method = UseCaseCheck.class.getMethod("checkId", List.class);
            UseCase useCase = method.getAnnotation(UseCase.class);
            int[] ids = useCase.ids();
            for(int id : ids) {
                if(!idList.contains(id)) {
                    System.out.println("缺失ID: " + id);
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        List<Integer> idList = new ArrayList<>();
        Collections.addAll(idList, 1, 2, 4, 5);
        System.out.println(checkId(idList));
    }
}
