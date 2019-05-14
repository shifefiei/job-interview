package com.sff.juc.utils;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

/**
 * 在车站、机场等出租车时，当很多空出租车就位时，为防止过度拥挤,车站服务人员会让候车旅客排队，
 * 一次让5个人去坐车，等这5个人坐车出发后，再放下一批旅客坐车
 */
public class SemaphoreDemo {

    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(5);
        for(int i = 0;i<10;i++) {
            FutureTask<Boolean> task = new FutureTask<Boolean>(new SemaphoreWorker(semaphore));
            new Thread(task).start();
        }
    }
}


class SemaphoreWorker implements Callable<Boolean> {

    private String name;

    private Semaphore semaphore;

    public SemaphoreWorker(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public Boolean call() throws Exception {
        try {
            log("is waiting a permit");
            semaphore.acquire();
            log("acquire a permit");
            log("execute");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log("release a permit");
            semaphore.release();
        }
        return true;
    }

    private void log(String message) {
        if (null == name) {
            name = Thread.currentThread().getName();
        }
        System.out.println(name + " " + message);
    }

}