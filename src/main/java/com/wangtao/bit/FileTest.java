package com.wangtao.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author wangtao
 * Created at 2024-08-09
 */
public class FileTest {

    @Test
    public void testPermission() {
        File file = new File(File.R | File.W | File.X);
        Assertions.assertTrue(file.hasPermission(File.R));
        // 判断是否同时拥有R和W权限
        Assertions.assertTrue(file.hasPermission(File.R | File.W));

        // 去掉X权限
        file.removePermission(File.X);
        Assertions.assertTrue(file.hasPermission(File.R | File.W));
        Assertions.assertFalse(file.hasPermission(File.X));

        // 加上X权限
        file.addPermission(File.X);
        Assertions.assertTrue(file.hasPermission(File.X));

        int oldPermission = file.getPermission();
        // 添加已有的权限
        file.addPermission(File.X);
        // 没有任何变化
        Assertions.assertEquals(oldPermission, file.getPermission());
        Assertions.assertThrows(IllegalArgumentException.class, () -> file.setPermission(16));
    }
}
