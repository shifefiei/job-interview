package com.sff.juc.pool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shifeifei
 * @date 2019-05-15 09:41
 * <p>
 * 如何使线程池的名称变动有意义？而不是默认的名字
 */
public class ThreadPoolRename {

    /**
     * cpu密集型任务，核心线程数设置为 cpu数+1; io密集型任务，核心线程数设置为 2*cpu数
     * <p>
     * 需要注意的是：Runtime.getRuntime().availableProcessors() 获取的是物理机的cpu数；
     * 但是好多系统都是基于虚拟容器部署，此时该方法要慎重使用，最好手动设固定的线程数
     */
    private static int coreSize = Runtime.getRuntime().availableProcessors() + 1;

    private static int maxSize = 4;

    private static int timeOut = 1000;

    public static void main(String[] args) {
        /**
         * 线程池的拒绝策略
         *
         * RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
         *
         * 1、DiscardPolicy: 默默丢弃无法处理的任务，不予任何处理
         * 2、DiscardOldestPolicy: 丢弃队列中最老的任务, 尝试再次提交当前任务
         * 3、AbortPolicy: 丢弃任务并抛出RejectedExecutionException异常
         * 4、CallerRunsPolicy: 将任务分给调用线程来执行,运行当前被丢弃的任务，这样做不会真的丢弃任务，但是提交的线程性能有可能急剧下降
         *
         */
        RejectedExecutionHandler handler = new MyRejectedExecutionHandler();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                maxSize,
                timeOut,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10),
                new RenameThreadFactory("sff"),
                handler);

        try {
            AtomicInteger atomic = new AtomicInteger(0);
            while (true) {
                executor.execute(new Task(atomic.getAndIncrement()));
                //Thread.sleep(1);
            }
//        } catch (InterruptedException ex) {
//            //线程池的中断异常如何处理
//            ex.printStackTrace();
        } catch (Exception e) {
            //其他异常处理
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

    }
}

/**
 * 自定义线程的名称规则
 */
class RenameThreadFactory implements ThreadFactory {

    /**
     * 线程池编号（static修饰）(容器里面所有线程池的数量)
     */
    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    /**
     * 线程编号(当前线程池线程的数量)
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 线程组
     */
    private final ThreadGroup group;

    /**
     * 业务名称前缀
     */
    private final String namePrefix;

    /**
     * 重写线程名称（获取线程池编号，线程编号，线程组）
     *
     * @param prefix 你需要指定的业务名称
     */
    public RenameThreadFactory(String prefix) {
        SecurityManager securityManager = System.getSecurityManager();
        group = (null != securityManager) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        //组装线程前缀
        namePrefix = prefix + "-pool:" + poolNumber.getAndIncrement() + "-thread:";
    }

    public Thread newThread(Runnable r) {
        //方便dump的时候排查（重写线程名称）
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);

        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

}

/**
 * 自定义拒接策略
 */
class MyRejectedExecutionHandler implements RejectedExecutionHandler {

    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {

    }
}


/**
 * 线程任务
 */
class Task implements Runnable {
    private Integer atomic;

    public Task(Integer atomic) {
        this.atomic = atomic;
    }

    public void run() {
        System.out.println("task : " + atomic);
    }
}