package com.sff.leetcode;

import java.util.Arrays;

/**
 * @author shifeifei
 * @date 2019-05-13 14:20
 * <p>
 * 字符串相关算法
 */
public class StringAlgorithm {


    public static void main(String[] args) {




        System.out.println("字符串反转：" + reverse("-a----bcde----fgh----"));

        string2Data("+123.008");

        System.out.println("字符串ip处理：" + getLongIp("127.23.12.1"));
        System.out.println("字符串ip处理：" + getStringIp(getLongIp("127.23.12.1")));

        String a = "12345666666666666666555";
        String b = "9998388838288238";
        System.out.println("大字符串相加：" + bigIntegerAdd(a, b));


        System.out.println("----------------------------------");


    }

    /**
     * @param str
     * @return
     */
    public String deleteString(String str) {
        char[] a = str.toCharArray();

        return null;
    }

    /**
     * 1.字符串反转
     *
     * @param originStr
     * @return
     */
    public static String reverse(String originStr) {

        if (null == originStr || originStr.length() < 1) {
            return originStr;
        }
        return reverse(originStr.substring(1)) + originStr.charAt(0);
    }


    /**
     * 2.字符串转数字
     *
     * @param str
     */
    public static void string2Data(String str) {

        if (!str.matches("^((\\+?\\d+)|(-?\\d+))(\\.\\d+)?$")) {
            System.out.println("输入的数字字符串有误");
            return;
        }

        int count = 0; // 记录小数点个数
        int index = 0; // 记录小数点下标位置
        boolean flagData = false; //默认输入的是整数
        char[] c = str.toCharArray();

        //判断输入的字符串是否是小数
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '.') {
                count++;
                index = i;
                flagData = true;
            }
        }

        // 小数点数大于1时 不合法
        if (flagData && count > 1) {
            System.out.println("输入数" + str + "为非法数字！");
            return;
        }

        // 不含小数点的整数
        if (count == 0 && index == 0) {
            index = c.length;
            flagData = false;
        }

        int intPart = 0;  // 小数点左边数
        boolean flag = true; // 记录输入的数字正负
        /** 处理整数部分 **/
        for (int i = 0; i < index; i++) {
            char temp = c[i];
            if (temp == '-') {
                flag = false;
                continue;
            } else if (temp == '+') {
                continue;
            }

            int tempInt = temp - '0';
            intPart = (intPart * 10 + tempInt);

        }
        if (!flag) {
            intPart = (-1) * intPart;
            System.out.println("字符串转化结果：" + intPart);
        }


        if (flagData) {
            double doublePart = 0; // 小数点右边数

            /** 处理小数部分 **/
            for (int j = c.length - 1; j > index; j--) {
                double tempDecimal = c[j] - '0';
                doublePart = (doublePart * 0.1 + tempDecimal * 0.1);
            }

            //结果等于 整数+小数
            double result = intPart + doublePart;
            if (!flag) {
                result = (-1) * result;
            }
            System.out.println("字符串转化结果：" + result);
        }
    }

    /**
     * 字符串ip转化为long类型ip
     *
     * @param ip
     */
    public static long getLongIp(String ip) {
        String[] ipArrays = ip.split("\\.");
        Long ipLong = 256 * 256 * 256 * Long.parseLong(ipArrays[0]) +
                256 * 256 * Long.parseLong(ipArrays[1]) +
                256 * Long.parseLong(ipArrays[2]) +
                Long.parseLong(ipArrays[3]);
        return ipLong;
    }

    /**
     * long类型的ip转化为字符串ip
     *
     * @param ip
     * @return
     */
    public static String getStringIp(long ip) {
        long y = ip % 256;
        long m = (ip - y) / (256 * 256 * 256);
        long n = (ip - 256 * 256 * 256 * m - y) / (256 * 256);
        long x = (ip - 256 * 256 * 256 * m - 256 * 256 * n - y) / 256;
        return m + "." + n + "." + x + "." + y;
    }

    /**
     * -a----bcde----fgh----
     * ----fgh----bcde----a-
     *
     * @param str
     */
    public static String reverse_01(String str) {

        String[] a = str.split("^[a-zA-Z]*$");
        String[] b = new String[a.length];

        int j = 0;
        for (int i = a.length - 1; i > 0; i--) {
            b[j] = a[i];
            j++;
        }
        return Arrays.toString(b);
    }

    /**
     * 大整数字符串相加
     * <p>
     * a = "12345666666666666666555";
     * b = "9998388838288238";
     */
    public static String bigIntegerAdd(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int len1 = num1.length();
        int len2 = num2.length();

        while (len1 > 0 || len2 > 0 || carry > 0) {
            int temp = carry;
            if (len1 > 0) {
                len1--;
                temp = temp + num1.charAt(len1) - '0';
            }
            if (len2 > 0) {
                len2--;
                temp = temp + num2.charAt(len2) - '0';
            }
            //保留整数进的那位数
            carry = temp / 10;
            result.append(temp % 10);
        }
        return result.reverse().toString();
    }

}
