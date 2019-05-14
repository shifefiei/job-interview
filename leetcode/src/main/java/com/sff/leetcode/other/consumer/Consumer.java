package com.sff.leetcode.other.consumer;


public class Consumer implements Runnable {

    private TaskPool taskPool;

    public Consumer(TaskPool taskPool) {
        this.taskPool = taskPool;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            taskPool.remove();
        }

    }
}
