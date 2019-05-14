package com.sff.leetcode.other;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SnowFlake 的结构如下(每部分用-分开):
 * <p>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * (1) 1  位标识，由于 long 基本类型在 Java 中是带符号的，最高位是符号位，正数是 0，负数是 1，所以 id 一般是正数，最高位是 0;
 * (2) 41 位时间截(毫秒级)，注意，41 位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值，这里的的开始时间截，一般是我们的 id 生成器开始使用的时间，一般都是使用系统当前时间,
 * 41 位的时间截，可以使用 69 年，年 T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69;
 * (3) 10 位的数据机器位，可以部署在 1024 个节点，包括 5 位数据中id 和 5 位机器id;
 * (4) 12 位序列，毫秒内的计数，12 位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生 4096 个 ID 序号;
 * (5) 1+41+10+12 加起来刚好 64 位，为一个 Long 型
 * <p>
 * <p>
 * SnowFlake 的优点是：
 * 整体上按照时间自增排序，并且整个分布式系统内不会产生 ID 碰撞(由数据中心 ID 和机器 ID 作区分)，并且效率较高，经测试，SnowFlake 每秒能够产生 26 万 ID 左右。
 */
public class SnowflakeDemo {

    /**
     * 10 位数据机器位最大移位
     */
    public static final int NODE_SHIFT = 10;

    /**
     * 12 位序列号的最大移位
     */
    public static final int SEQ_SHIFT = 12;

    /**
     * 10 位数据机器位的最大值
     */
    public static final short MAX_NODE = 1024;

    /**
     * 12 位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生 4096 个 id 序号
     */
    public static final short MAX_SEQUENCE = 4096;

    /**
     * 表示12位序列好
     */
    private short sequence;

    /**
     * 表示记录上次生成id的时间
     */
    private long referenceTime;

    /**
     * 表示机器数据数
     */
    private long node;


    public SnowflakeDemo(int node) {
        if (node < 0 || node > MAX_NODE) {
            throw new IllegalArgumentException(String.format("node must be between %s and %s", 0, MAX_NODE));
        }
        this.node = getIpLastValue();
    }

    /**
     * 生成一个64位的整数
     */
    public long next() {

        long counter;
        long currentTime = System.currentTimeMillis();

        synchronized (this) {
            if (currentTime < referenceTime) {
                throw new RuntimeException(String.format("上次生成id时间： %s ,当前时间： %s", referenceTime, currentTime));
            } else if (currentTime > referenceTime) {
                this.sequence = 0;
            } else {
                //表示 currentTime 和 referenceTime 相同，即上次生成时间和当前时间相同,在同一毫秒内
                if (this.sequence < SnowflakeDemo.MAX_SEQUENCE) {
                    this.sequence++;
                } else {
                    throw new RuntimeException("已达到最大序列数: " + this.sequence);
                }
            }
            counter = this.sequence;
            referenceTime = currentTime;
        }

        /**
         *
         * 移位操作：
         * 1. a = currentTime << NODE_SHIFT << SEQ_SHIFT 即 (currentTime * 2^NODE_SHIFT) * 2^SEQ_SHIFT
         *
         * 2. b = node << SEQ_SHIFT 即 node * 2^SEQ_SHIFT
         *
         * 3. c = counter
         *
         * 或操作：a | b | c
         *
         */
        return currentTime << NODE_SHIFT << SEQ_SHIFT | node << SEQ_SHIFT | counter;
    }


    private static long getIpLastValue() {
        byte ipLastValue = 0;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] ipByte = ip.getAddress();
            ipLastValue = ipByte[ipByte.length - 1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //0x000000FF 就是10进制 255,作用是如果 getLastIP() 复数，则会清除0
        long workerId = 0x000000FF & ipLastValue;
        return workerId;
    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(20);

        final SnowflakeDemo s = new SnowflakeDemo(2);

        for (int i = 0; i < 40; i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    long id = s.next();
                    System.out.println(Thread.currentThread().getName() + ",id=" + id);
                }
            };
            executor.execute(runnable);
        }
        executor.shutdown();
    }
}
