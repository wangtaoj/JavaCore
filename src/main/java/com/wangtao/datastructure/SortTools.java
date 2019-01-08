package com.wangtao.datastructure;

/**
 * @author wangtao
 * Created on 2018/2/28
 **/
public class SortTools {

    public static int ans = 0;

    public static void quickSort(int[] arr) {
        if(arr.length == 0 || arr.length == 1)
            return;
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int low, int high) {
        if(low < high) {
            int mid = partition(arr, low, high);
            quickSort(arr, low, mid - 1);
            quickSort(arr, mid + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int i = low, j = high;
        int temp = arr[i];
        while(i < j) {
            while(i < j && arr[j] >= temp)
                j--;
            arr[i] = arr[j];
            while(i < j && arr[i] <= temp)
                i++;
            arr[j] = arr[i];
        }
        arr[i] = temp;
        return i;
    }

    public static void mergeSort(int arr[]) {
        mergeSort(arr, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int low, int high) {
        if(low < high) {
            int mid = (low + high) / 2;
            mergeSort(arr, low, mid);
            mergeSort(arr, mid + 1, high);
//            merge(arr, low, mid, high);
            merge1(arr, low, mid, high);
        }
    }

    /**
     * 顺带求逆序数
     */
    private static void merge1(int[] arr, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int i = low, j = mid + 1, k = 0;
        while(i <= mid && j <= high) {
            if(arr[i] <= arr[j])
                temp[k++] = arr[i++];
            else {
                temp[k++] = arr[j++];
                ans += mid - i + 1;
            }
        }
        while(i <= mid) {
            temp[k++] = arr[i++];
        }
        while(j <= high) {
            temp[k++] = arr[j++];
        }
        for(i = 0, j = low; i < k; i++, j++)
            arr[j] = temp[i];
    }
    //arr[low]-arr[mid]  arr[mid + 1] - arr[high]
    private static void merge(int[] arr, int low, int mid, int high) {
        int[] temp = new int[high - low + 1];
        int i = low, j = mid + 1, k = 0;
        while(i <= mid && j <= high) {
            if(arr[i] <= arr[j])
                temp[k++] = arr[i++];
            else {
                temp[k++] = arr[j++];
                ans++;
            }
        }
        while(i <= mid) {
            temp[k++] = arr[i++];
        }
        while(j <= high) {
            temp[k++] = arr[j++];
        }
        for(i = 0, j = low; i < k; i++, j++)
            arr[j] = temp[i];
    }
}
