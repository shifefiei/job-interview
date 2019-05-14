package com.sff.leetcode.other.consumer.condition;


import com.sff.leetcode.other.consumer.Consumer;
import com.sff.leetcode.other.consumer.Producer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestDemo {

    public static void main(String[] args) {

        Lock lock = new ReentrantLock();

        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        ConditionTaskPool taskPool = new ConditionTaskPool(lock, condition1, condition2);

        //启动生产者线程
        Producer producer = new Producer(taskPool);
        new Thread(producer).start();


        for (int i = 0; i < 3; i++) {
            Consumer consumer = new Consumer(taskPool);
            new Thread(consumer).start();
        }


    }

}
