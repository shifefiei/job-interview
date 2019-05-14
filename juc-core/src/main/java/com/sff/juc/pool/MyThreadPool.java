package com.sff.juc.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author shifeifei
 * @date 2019-04-26 13:46
 * <p>
 * 简化的线程池，仅用来说明工作原理
 */
class MyThreadPool {

    // 利用阻塞队列实现生产者 - 消费者模式
    BlockingQueue<Runnable> workQueue;

    // 保存内部工作线程
    List<WorkerThread> threads = new ArrayList<WorkerThread>();

    // 构造方法
    MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        // 创建工作线程
        for (int idx = 0; idx < poolSize; idx++) {
            WorkerThread work = new WorkerThread();
            work.start();
            threads.add(work);
        }
    }

    // 提交任务
    void execute(Runnable command) throws Exception {
        workQueue.put(command);
    }

    // 工作线程负责消费任务，并执行任务
    class WorkerThread extends Thread {

        @Override
        public void run() {
            try {
                // 循环取任务并执行
                while (true) {
                    Runnable task = workQueue.take();
                    task.run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



