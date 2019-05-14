package com.sff.leetcode.other;


/**
 * 求100000以内的素素
 * <p>
 * 什么是素素？
 * 质素：又称素数，只能被 1 和它本身整除的且大于1的自然数
 * <p>
 * 思路：
 * (1) 偶数肯定不是素素，排除偶数(除了2),所以从3开始遍历所有的奇数
 * (2) 从奇数里面挑质素
 */
public class PrimeNumber {

    public static void main(String[] args) {
        System.out.println(2);

        boolean flag = true;
        for (int i = 3; i <= 100000; i = i + 2) {
            for (int j = 3; j <= Math.sqrt(i); j = j + 2) {

                if (i % j == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println(i);
            }
            flag = true;
        }
    }
}
