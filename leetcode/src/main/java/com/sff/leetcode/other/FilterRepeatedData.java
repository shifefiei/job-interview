package com.sff.leetcode.other;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

/**
 * 数据去重方案实现算法
 */
public class FilterRepeatedData {


    public static void main(String[] args) {
        HashMap<Integer, Integer> map = hashMapFilterData();
        for (Integer key : map.keySet()) {
            System.out.println(key + "," + map.get(key));
        }

        System.out.println("-------------------------------------");

        System.out.println(bitSetFilterData());
    }

    /**
     * hashMap去重方案
     */
    public static HashMap hashMapFilterData() {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5, 2, 3, 4);
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Integer list : lists) {
            if (map.containsKey(list)) {
                continue;
            }
            map.put(list, list);
        }
        return map;
    }

    /**
     * java api中的 bitSet使用
     * <p>
     * https://blog.csdn.net/kongmin_123/article/details/82225172
     * <p>
     * bit-map算法
     * https://blog.csdn.net/u013063153/article/details/70800381
     *
     * @return
     */
    public static BitSet bitSetFilterData() {
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5, 2, 3, 4);
        BitSet set = new BitSet();
        for (Integer list : lists) {
            if (set.get(list)) {
                continue;
            }
            set.set(list);
        }
        return set;
    }

}
