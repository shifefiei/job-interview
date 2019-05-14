package com.sff.leetcode.other.consumer.blocking.queue;


import com.sff.leetcode.other.consumer.TaskPool;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 任务池
 */
public class QueueTaskPool extends TaskPool {

    private BlockingQueue taskQueue = new LinkedBlockingQueue(10);

    @Override
    public void add() {
        try {
            Integer obj = new Random(10).nextInt();
            taskQueue.put(obj);

            System.out.println("生产者 " + Thread.currentThread().getName()
                    + " 生产一个资源 : " + obj + ",当前资源池有 " + taskQueue.size() +
                    " 个资源");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void remove() {
        try {
            Object obj = taskQueue.take();

            System.out.println("消费者 " + Thread.currentThread().getName() +
                    " 消耗一件资源 : " + obj + " ,当前资源池有" + taskQueue.size()
                    + " 个资源");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
