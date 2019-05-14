package com.sff.juc;


import java.util.concurrent.CyclicBarrier;

/**
 * 如何同时启动5个线程？
 */
public class CyclicBarrierDemo {


    public static void main(String[] args) {

        CyclicBarrier cyc = new CyclicBarrier(5);

        for (int i = 1; i < 6; i++) {
            new Thread(new ExecuteThread(cyc, i)).start();
        }
    }

    static class ExecuteThread implements Runnable {

        private CyclicBarrier cyclicBarrier;
        private Integer count;

        public void run() {
            try {
                System.out.println("线程 ：" + count + "已启动将被阻塞");
                cyclicBarrier.await();
                System.out.println("线程 ：" + count + "被释放，执行下一步逻辑");
                System.out.println("i = " + count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ExecuteThread(CyclicBarrier cyclicBarrier, Integer count) {
            this.cyclicBarrier = cyclicBarrier;
            this.count = count;
        }
    }

}



