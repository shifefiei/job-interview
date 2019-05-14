package com.sff.leetcode.other;

import java.util.concurrent.CountDownLatch;

/**
 * 一个或者一组线程等待另外的线程执行完成后开始执行
 */
public class CountDownLatchDemo {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        for (int i = 0; i < 2; i++) {
            new Thread(new SubThread(countDownLatch)).start();
        }

        try {


            new Thread(new MyThread()).start();

            System.out.println("等待2个子线程执行...");
            countDownLatch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class SubThread implements Runnable {
    private CountDownLatch downLatch;

    public SubThread(CountDownLatch downLatch) {
        this.downLatch = downLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println("子线程：" + Thread.currentThread().getName() + "正在执行");
            Thread.sleep(100);
            System.out.println("子线程：" + Thread.currentThread().getName() + "执行完成");
            downLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread implements Runnable {
    @Override
    public void run() {
        System.out.println("my thread");
    }
}