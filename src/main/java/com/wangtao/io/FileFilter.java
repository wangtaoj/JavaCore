package com.wangtao.io;

import java.io.File;

/**
 * Created by wangtao at 2018/12/10 13:38
 */
public class FileFilter {

    static void listByExt(String dir) {
        File file = new File(dir);
        if(file.exists() && file.isDirectory()) {
            String[] names = file.list((p, name) -> name.endsWith(".txt"));
            if(names == null) {
                return;
            }
            for(String name : names) {
                System.out.println(name);
            }
        }
    }

    public static void main(String[] args) {
        listByExt("./");
    }
}
