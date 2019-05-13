package com.sff.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shifeifei
 * @date 2019-05-13 14:20
 * <p>
 * 数组相关算法
 */
public class ArrayAlgorithm {


    /**
     * 1.整数反转： 123 --> 321
     *
     * @param x
     * @return
     */
    public static int reverseInt(int x) {

        int t = 0;
        while (x != 0) {
            t = t * 10 + x % 10;
            x = x / 10;
        }

        if (t > Integer.MAX_VALUE / 10) {
            return 0;
        }
        if (t < Integer.MIN_VALUE / 10) {
            return 0;
        }

        int flag = x < 0 ? -1 : 1;
        return t * flag;
    }


    /**
     * 2. 两数之和
     * <p>
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回他们的数组下标。
     * <p>
     * eg:
     * <p>
     * 给定 nums = [2, 7, 11, 15], target = 9
     * 因为 nums[0] + nums[1] = 2 + 7 = 9
     * 所以返回 [0, 1]
     */
    public static int[] getTwoSum(int[] nums, int target) {

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {

            int data = target - nums[i];
            if (map.containsKey(data)) {
                return new int[]{map.get(data), i};
            } else {
                map.put(nums[i], i);
            }
        }
        return null;
    }

    /**
     * 3. 二分查找算法实现
     *
     * @param a
     * @param low
     * @param high
     * @param key
     * @return
     */
    public static int search(int[] a, int low, int high, int key) {

        if (low > high) {
            return -1;
        }

        int mid = (low + high) / 2;
        if (key == a[mid]) {
            return mid;
        }

        if (key > a[mid]) {
            return search(a, mid + 1, high, key);
        } else {
            return search(a, low, mid - 1, key);
        }
    }


    /**
     * 4.利用冒泡排序的思想实现查找奇数和偶数
     * <p>
     * int[] array = {1, 26, 4, 6, 7, 7, 5, 10, 7};
     * <p>
     * 输出 ：[1, 7, 7, 5, 7, 26, 4, 6, 10]
     *
     * @param array
     */
    public static void sort(int[] array) {

        for (int i = 0; i < array.length - 1; i++) {

            for (int j = 0; j < array.length - 1 - i; j++) {

                if (array[j] % 2 == 0 && array[j + 1] % 2 != 0) {
                    int t = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = t;
                }
            }
        }
        System.out.println(Arrays.toString(array));
    }


    public static void main(String[] args) {

        System.out.println("整数反转：" + reverseInt(-123));

        int[] a = {2, 3, 4, 18, 9};
        System.out.println("两数之和: " + Arrays.toString(getTwoSum(a, 5)));

    }

}
