package com.sff.leetcode.other.consumer;

public class Producer implements Runnable {

    private TaskPool taskPool;

    public Producer(TaskPool taskPool) {
        this.taskPool = taskPool;
    }

    public void run() {

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            taskPool.add();
        }

    }
}
