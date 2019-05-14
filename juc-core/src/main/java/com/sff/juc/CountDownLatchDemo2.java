package com.sff.juc;


import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch的构造函数接收一个int类型的参数作为计数器，如果你想等待N个点完成，这里就传入N。
 * 当我们调用一次CountDownLatch 的 countDown 方法时，N就会减1.
 * CountDownLatch 的 await 会阻塞当前线程，直到N变成零。
 * 由于countDown方法可以用在任何地方，所以这里说的N个点，可以是N个线程，也可以是1个线程里的N个执行步骤
 * <p>
 * <p>
 * 创建n个线程，让每个线程完成一部分内容，当所有部分完成后，执行其他流程
 */
public class CountDownLatchDemo2 {

    public static void main(String[] args) throws Exception {
        DriverDemo demo = new DriverDemo();
        demo.test();
    }

    static class DriverDemo {

        public void test() throws InterruptedException {

            int n = 10;
            CountDownLatch doneSignal = new CountDownLatch(n);

            // create and start threads
            for (int i = 0; i < n; ++i) {
                new Thread(new WorkerRunnable(doneSignal, i)).start();
            }

            // wait for all to finish
            doneSignal.await();

            for (int  i = 0; i < n; i++) {
                new Thread() {
                    @Override
                    public void run() {
                        System.out.println("=======workRunnable完成,开始执行========");
                    }
                }.start();
            }
        }
    }

    static class WorkerRunnable implements Runnable {
        private CountDownLatch doneSignal;
        private int i;

        WorkerRunnable(CountDownLatch doneSignal, int i) {
            this.doneSignal = doneSignal;
            this.i = i;
        }

        public void run() {
            try {
                doWork(i);
                doneSignal.countDown();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void doWork(int i) {
            System.out.println("执行 i ：" + i);
        }
    }

}
