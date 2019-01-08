package com.wangtao.nowcoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * @author wangtao
 * 2018-2018/1/5-21:07
 **/

public class Main {

    public static void main(String[] args) {
        int[] arr = {5, 4, 1, 3, 7, 9, 2, 11, 10, 8};
        findKminByPriority(arr, 4);
    }

    public int calMaxSum(int array[]) {
        int max = array[0], sum = 0;
        for(int number : array) {
            sum += number;
            if(sum > max) {
                max = sum;
            }
            if(sum < 0) {
                sum = 0;
            }
        }
        return max;
    }

    public static ArrayList<Integer> findKminByPriority(int[] arr, int k) {
        PriorityQueue<Integer> q = new PriorityQueue<>(Collections.reverseOrder());
        ArrayList<Integer> list = new ArrayList<>();
        if(k <= 0 || k > arr.length)
            return list;
        for(int i = 0; i < k; i++) {
            if(q.size() <= k)
                q.offer(arr[i]);
        }
        for(int i = k; i < arr.length; i++) {
            int max = q.peek();
            if (arr[i] < max) {
                q.poll();
                q.offer(arr[i]);
            }
        }
        list.addAll(q);
        return list;
    }

    /**
     * 找出数组中最小的k个数
     *
     * @param arr
     * @param k
     */
    public static void findKMin(int[] arr, int k) {
        int low = 0, high = arr.length - 1;
        while (low < high) {
            int mid = partition(arr, low, high);
            if (mid == k) {
                break;
            } else if (mid > k) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        for (int i = 0; i < k; i++)
            System.out.println(arr[i]);
    }

    private static int partition(int[] arr, int low, int high) {
        int temp = arr[low];
        while (low < high) {
            while (low < high && arr[high] >= temp)
                high--;
            arr[low] = arr[high];
            while (low < high && arr[low] <= temp)
                low++;
            arr[high] = arr[low];
        }
        arr[low] = temp;
        return low;
    }
}
