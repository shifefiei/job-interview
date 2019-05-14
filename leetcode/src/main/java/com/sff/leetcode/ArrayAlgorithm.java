package com.sff.leetcode;

import java.util.*;

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

    /**
     * 7.给定一个整数数组，判断是否存在重复元素。
     * <p>
     * 输入: [1,2,3,1]
     * 输出: true
     * <p>
     * 输入: [1,2,3,4]
     * 输出: false
     *
     * @param nums
     * @return
     */
    public static boolean containsDuplicate(int[] nums) {
        if (nums.length == 0 || nums.length == 1) {
            return true;
        }
        HashSet<Integer> set = new HashSet<Integer>();
        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }

    /**
     * 6. 删除排序数组中的重复项
     * 给定 nums = [0,0,1,1,1,2,2,3,3,4],
     * <p>
     * 函数应该返回新的长度 5, 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4。
     *
     * @param nums
     * @return
     */
    public static void removeDuplicates(int[] nums) {
        int i = 0;

        StringBuffer a = new StringBuffer();
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
                a = a.append(nums[i]).append(",");
            }
        }
        System.out.println(a);
    }

    /**
     * 5.移除数组中指定元素
     * <p>
     * 给定 nums = [0,1,2,2,3,0,4,2], val = 2,
     * <p>
     * 函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
     *
     * @param nums
     * @param val
     * @return
     */
    public static int removeElement(int[] nums, int val) {

        List<Integer> list = new ArrayList<Integer>();
        for (int num : nums) {
            if (val != num) {
                list.add(num);
            }
        }
        System.out.println(list.toString());
        return list.size();
    }

    /**
     * 4. 给定两个数组，求它们的交集
     * <p>
     * 输入: nums1 = [1,2,2,1], nums2 = [2,2]
     * 输出: [2]
     * <p>
     * 示例 2:
     * 输入: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
     * 输出: [9,4]
     * <p>
     * (1) 先排序
     * (2) 定义两个指针同时遍历 两个数组，找出相同的数
     */
    public static void unionTwoArray(int[] a, int[] b) {

        Arrays.sort(a);
        Arrays.sort(b);

        int aLen = 0;
        int bLen = 0;

        Set<Integer> sets = new HashSet<Integer>();

        while (aLen < a.length && bLen < b.length) {
            if (a[aLen] < b[bLen]) {
                aLen++;
            } else if (a[aLen] > b[bLen]) {
                bLen++;
            } else {
                if (!sets.contains(a[aLen])) {
                    sets.add(a[aLen]);
                }
                aLen++;
                bLen++;
            }
        }
        System.out.println(Arrays.toString(sets.toArray()));
    }


    public static void main(String[] args) {

        System.out.println("整数反转：" + reverseInt(-123));

        int[] a = {2, 3, 4, 18, 9};
        System.out.println("两数之和: " + Arrays.toString(getTwoSum(a, 5)));

    }

}
