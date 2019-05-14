package com.sff.leetcode.other;

/**
 * 自己实现一个hashMap
 * https://www.jianshu.com/p/65bc58887626
 */
public class MyHashMap {


    private static Integer DEFAULT_SIZE = 16;


    private final class MyEntry<K, V> {

        private K key;
        private V value;
        private MyEntry next;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
