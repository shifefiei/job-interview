package com.sff.leetcode.other.consumer.condition;


import com.sff.leetcode.other.consumer.TaskPool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 利用 condition 实现 生产者消费者模式
 */
public class ConditionTaskPool extends TaskPool {

    /**
     * 允许生产最大个数
     */
    private int size = 10;

    /**
     * 当前元素个数
     */
    private int count;

    private Lock lock;

    private Condition producerCondition;

    private Condition consumerCondition;

    public ConditionTaskPool(Lock lock, Condition producerCondition, Condition consumerCondition) {
        this.lock = lock;
        this.producerCondition = producerCondition;
        this.consumerCondition = consumerCondition;
    }

    @Override
    public void add() {
        lock.lock();
        try {
            if (count < size) {
                count++;

                System.out.println("生产者 " + Thread.currentThread().getName() + "生产资源,当前任务池中有" + count + "个任务");

                //唤醒等待的消费者
                consumerCondition.signalAll();

            } else {
                //让生产者等待
                producerCondition.await();
                System.out.println("生产者 " + Thread.currentThread().getName() + "线程进入等待");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 从资源池中取走资源
     */
    @Override
    public void remove() {
        lock.lock();
        try {

            if (count > 0) {
                count--;
                System.out.println("消费者" + Thread.currentThread().getName()
                        + "消耗一件资源," + "当前资源池有" + count + "个");

                //唤醒等待的生产者
                producerCondition.signalAll();
            } else {

                //让消费者等待
                consumerCondition.await();
                System.out.println("消费者 " + Thread.currentThread().getName() + "线程进入等待");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
