package com.wangtao.datastructure;

import java.util.Arrays;

/**
 * @author wangtao
 * Created on 2018/3/4
 **/
public class Pailie {

    static boolean[] visit;
    static int n;
    static int m;
    static int number;
    public static void main(String[] args) {
       //pailie();
        zuhe1();
    }

    public static void pailie() {
        n = 4; m = 3;
        int[] arr = new int[m];
        visit = new boolean[n + 1];
        dfs(arr, 0, m);
        System.out.println(number);
    }

    /**
     * 排列
     * @param arr
     * @param cnt
     * @param count
     */
    private static void dfs(int[] arr, int cnt, int count) {
        if(cnt == count) {
            System.out.println(Arrays.toString(arr));
            number++;
            return;
        }
        for(int i = 1; i <= n; i++) {
            if(!visit[i]) {
                arr[cnt] = i;
                visit[i] = true;
                dfs(arr, cnt + 1, count);
                visit[i] = false;
            }
        }
    }

    public static void zuhe() {
        n = 4; m = 2;
        int[] arr = new int[m];
        for(int i = 1; i <= n; i++) {
            fun(i, arr, 0, n, m);
        }
        System.out.println(number);
    }

    /**
     * 组合
     * @param cur
     * @param arr
     * @param cnt
     * @param n
     * @param m
     */
    private static void fun(int cur, int arr[], int cnt, int n, int m) {
        if(m == 0)
            return;
        arr[cnt] = cur;
        if(cnt == m - 1) {
            System.out.println(Arrays.toString(arr));
            number++;
            return;
        }
        for(int i = cur + 1; i <= n; i++) {
            fun(i, arr, cnt + 1, n, m);
        }
    }

    public static void zuhe1() {
        n = 4; m = 4;
        int[] arr = new int[m];
        fun1(1, arr, 0, n, m);
        System.out.println(number);
    }

    private static void fun1(int cur, int arr[], int cnt, int n, int m) {
        if(cnt == m) {
            System.out.println(Arrays.toString(arr));
            number++;
            return;
        }
        for(int i = cur; i <= n; i++) {
            arr[cnt] = i;
            fun1(i + 1, arr, cnt + 1, n, m);
        }
    }
}
