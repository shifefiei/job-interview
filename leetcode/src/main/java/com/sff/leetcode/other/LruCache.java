package com.sff.leetcode.other;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> extends LinkedHashMap<K, V> {

    private int cacheSize;

    /**
     * 传递进来最多能缓存多少数据
     *
     * @param cacheSize 缓存大小
     */
    public LruCache(int cacheSize) {
        // true 表示让 linkedHashMap 按照访问顺序来进行排序，最近访问的放在头部，最老访问的放在尾部。
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
        this.cacheSize = cacheSize;
    }


    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 当 map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据。
        return size() > cacheSize;
    }

    public static void main(String[] args) {
        LruCache<String,String> cache = new LruCache<>(2);
        cache.put("a","a");
        cache.put("b","b");
        cache.put("c","c");

        System.out.println(cache.get("c"));
    }

}
