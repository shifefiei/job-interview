package com.sff.juc.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
     */
    private static int coreSize = Runtime.getRuntime().availableProcessors() + 1;


    private static int maxSize = 4;
    private static int timeOut = 1000;

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                timeOut,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10), new RenameThreadFactory("sff"));

        try {
            AtomicInteger atomic = new AtomicInteger(0);

            System.out.println(Runtime.getRuntime().availableProcessors());

            while (true) {
                executor.execute(new Task(atomic.getAndIncrement()));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.getCause();
        }
    }
}


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
        namePrefix = prefix + "-poolNumber:" + poolNumber.getAndIncrement() + "-threadNumber:";
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


class Task implements Runnable {
    private Integer atomic;

    public Task(Integer atomic) {
        this.atomic = atomic;
    }

    public void run() {
        System.out.println("task : " + atomic);
    }
}