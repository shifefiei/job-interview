package com.sff.leetcode;

import java.util.Arrays;

/**
 * @author shifeifei
 * @date 2019-05-13 14:41
 * <p>
 * 排序算法
 */
public class SortedAlgorithm {

    /**
     * 1.快速排序
     * <p>
     * 快速排序执行过程：https://blog.csdn.net/wthfeng/article/details/78037228
     *
     * @param arr
     * @param low
     * @param high
     * @return
     */
    public static int[] quickSort(int[] arr, int low, int high) {

        //当low==high是表示该序列只有一个元素，不必排序了
        if (low >= high) {
            return null;
        }

        // 选出哨兵元素和基准元素。这里左边的哨兵元素也是基准元素
        int i = low;
        int j = high;
        int key = arr[low];

        while (i < j) {
            //右边哨兵从后向前找
            while (arr[j] >= key && i < j) {
                j--;
            }
            //左边哨兵从前向后找
            while (arr[i] <= key && i < j) {
                i++;
            }
            //交换元素
            swap(arr, i, j);
        }

        //基准元素与右哨兵交换
        swap(arr, low, j);

        //递归调用，排序左子集合和右子集合
        quickSort(arr, low, j - 1);
        quickSort(arr, j + 1, high);

        return arr;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    /**
     * 2.冒泡排序
     * <p>
     * 比较相邻两个元素的大小。如果前一个元素比后一个元素大，则两元素位置交换
     * <p>
     * 排序算法过程：https://blog.csdn.net/guoweimelon/article/details/50902597
     */
    public static void bubbleSort(int[] array) {

        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array.length - i - 1; j++) {

                //交换相邻的两个元素
                if (array[j] > array[j + 1]) {
                    int t = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = t;
                }
            }
        }
        System.out.println(Arrays.toString(array));
    }


    /**
     * 选择排序
     * <p>
     * 每一趟从待排序的记录中选出最小(最大)的元素，顺序放在已排好序的序列最后，直到全部记录排序完毕
     * <p>
     * int[] arr={里面n个数据}；
     * (1) 第1趟排序，在待排序数据arr[1]~arr[n]中选出最小的数据，将它与arr[1]交换
     * (2) 第2趟，在待排序数据arr[2]~arr[n]中选出最小的数据，将它与r[2]交换
     * (3) 以此类推，第i趟在待排序数据arr[i]~arr[n]中选出最小的数据，将它与r[i]交换，直到全部排序完成
     */
    public static void selectSort(int[] array) {

        for (int i = 0; i < array.length - 1; i++) {
            int k = i;
            //选择最小的记录
            for (int j = k; j < array.length; j++) {
                if (array[j] < array[k]) {
                    // 记录最小的数的下标
                    k = j;
                }
            }

            //说明在待排序序列中找到了最小的一个元素
            if (i != k) {
                int temp = array[i];
                array[i] = array[k];
                array[k] = temp;
            }
        }
        System.out.println(Arrays.toString(array));

    }

    public static void main(String[] args) {
        int[] a = {9, 9, 10, 2, 1, 19, 20, 5};
        System.out.println("快速排序：" + Arrays.toString(quickSort(a, 0, a.length - 1)));

        bubbleSort(a);

        selectSort(a);
    }

}
