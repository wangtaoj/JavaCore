package com.wangtao.datastructure;

/**
 * @author wangtao
 * Created on 2018/3/8
 **/
public class Prime {

    static boolean[] visit;
    static int[] primes;
    static int cnt;

    public static void main(String[] args) {
        int n = 101;
        visit = new boolean[n + 1];
        primes = new int[n];
        getPrime(n);
        for(int i = 0; i < cnt; i++) {
            if(i % 10 == 0 && i != 0)
                System.out.println(primes[i]);
            else
                System.out.print(primes[i] + " ");
        }
        System.out.println(isPrime(101));
    }

    private static void getPrime(int n) {
        for(int i = 2; i <= (int)Math.sqrt(n); i++) {
            /*
             * 没有被标记为true, i是一个素数, 用这个素数剔除掉该素数的所有倍数。
             * 2  剔除4, 6, 8, 10, 12 ......
             * 3  剔除9, 12, 15, 18, 21 ......
             * 4  已经被标记
             * 5  剔除25, 30, 35 ......
             */
            if(!visit[i]) {
                for(int j = i * i; j <= n; j = j + i) {
                    visit[j] = true;
                }
            }
        }
        for(int i = 2; i <= n; i++) {
            if(!visit[i])
                primes[cnt++] = i;
        }
    }

    private static boolean isPrime(int n) {
        for(int i = 2; i <= (int)Math.sqrt(n); i++) {
            if(n % i == 0)
                return false;
        }
        return true;
    }

}
