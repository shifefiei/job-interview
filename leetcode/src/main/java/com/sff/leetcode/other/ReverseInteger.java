package com.sff.leetcode.other;

public class ReverseInteger {


    public static void main(String[] args) {

        int a = 1234;
        System.out.println(Integer.MAX_VALUE);
        System.out.println(reverse(a));

    }

    public static int reverse(int x) {
        int rev = 0;

        while (0 != x) {
            //取余，作为反转后的整数的最高位数
            int pop = x % 10;

            //取模，剩余需要反转的数
            x /= 10;

            /**
             * rev = rev * 10 + pop : 反转后的整数，存在溢出的风险(即：可能大于 2^31 , Integer.MAX_VALUE = 2147483647)
             *
             * (1) rev * 10 + pop > Integer.MAX_VALUE ,溢出；
             *
             * (2) 当 rev * 10 + pop = Integer.MAX_VALUE 时，pop > Integer.MAX_VALUE % 10 = 7 溢出；
             */

            //被反转的数是正数
            if (rev > Integer.MAX_VALUE / 10
                    || (rev == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }

            //被反转的数是负数
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }

            rev = rev * 10 + pop;
        }
        return rev;
    }

}
