package com.sff.juc;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch的构造函数接收一个int类型的参数作为计数器，如果你想等待N个点完成，这里就传入N。
 * 当我们调用一次CountDownLatch 的 countDown 方法时，N就会减1.
 * CountDownLatch 的 await 会阻塞当前线程，直到N变成零。
 * 由于countDown方法可以用在任何地方，所以这里说的N个点，可以是N个线程，也可以是1个线程里的N个执行步骤
 * <p>
 * <p>
 * 一组工作线程使用两个倒计时信号
 */
public class CountDownLatchDemo {


    public static void main(String[] args) throws Exception {
        Driver driver = new Driver();
        driver.test();
    }

    static class Driver {
        public void test() throws InterruptedException {

            int n = 10;

            /**
             * start signal 是为了阻止 worker 执行，直到 driver 准备好后才能执行
             */
            CountDownLatch startSignal = new CountDownLatch(1);

            /**
             *  让 driver 处于阻塞状态直到 workers 全部完成
             */
            CountDownLatch doneSignal = new CountDownLatch(n);

            for (int i = 0; i < n; ++i) {
                // 创建线程并启动
                new Thread(new Worker(startSignal, doneSignal, i)).start();
                System.out.println("线程 " + i + " 创建完成");
            }

            //让所有 worker 线程继续执行
            startSignal.countDown();

            doSomethingElse();

            //等待所有 worker 线程执行完成
            doneSignal.await();
        }

        private void doSomethingElse() {
            System.out.println("===========>>>so something else<<<<===========");
        }


    }

    static class Worker implements Runnable {

        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        private int i;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal, int i) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.i = i;
        }

        public void run() {
            try {

                System.out.println("startSignal 开始信号使" + i + "个线程即将阻塞");
                startSignal.await();
                doWork();
                doneSignal.countDown();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void doWork() {
            System.out.println("worker工作开始...");
        }
    }


}
