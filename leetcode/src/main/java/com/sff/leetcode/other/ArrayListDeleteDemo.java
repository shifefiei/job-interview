package com.sff.leetcode.other;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrayListDeleteDemo {


    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(4);

        /**
         * 正确删除链表元素的方式
         */
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer integer = iterator.next();
            if (integer.equals(new Integer(1))) {
                iterator.remove();
            }
        }
        System.out.println(list.toString());
    }
}
