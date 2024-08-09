package com.wangtao.bit;

/**
 * @author wangtao
 * Created at 2024-08-09
 */
public class File {

    public static final int X = 1;

    public static final int R = 2;

    public static final int W = 4;

    /**
     * 权限位, 0无权限
     * 将其看做二进制
     * 第一位为1，则具有X
     * 第二位为1，则具有R
     * 第三位为1，则具有W
     */
    private int permission;

    public File(int permission) {
        checkPermission(permission);
        this.permission = permission;
    }

    /**
     * 判断是否拥有权限
     * 二进制相同位上都是1
     */
    public boolean hasPermission(int permission) {
        return (this.permission & permission) == permission;
    }

    public void addPermission(int permission) {
        // 2的整数幂或运算相当于加法, 如果已经有了此权限或运算不会发生任何变化
        // 添加权限也就是把0变成1
        setPermission(this.permission | permission);
    }

    public void removePermission(int permission) {
        // 做减法, 本质上需要把代表该权限所在的1变为0，如果本来没有此权限则不会有任何变化
       setPermission(this.permission & ~permission);
    }

    private void checkPermission(int permission) {
        if ((permission & ~allPermission()) != 0) {
            throw new IllegalArgumentException(String.valueOf(permission));
        }
    }

    private int allPermission() {
        return (X | R | W);
    }

    public void setPermission(int permission) {
        checkPermission(permission);
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }
}
