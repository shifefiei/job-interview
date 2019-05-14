package com.sff.juc.fork.join;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FetureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        SonTask task1 = new SonTask("Thread son1");
        FutureTask<String> f1 = new FutureTask<String>(task1);
        new Thread(f1).start();
        System.out.println(f1.get());

        //拿到特定结果后网下执行
        System.out.println("------------------");

        FutureTask<Integer> f2 = new FutureTask<Integer>(new MyrRun(), 22);
        new Thread(f2).start();
        System.out.println("result_" + f2.get());
    }
}


/**
 * Callable接口是这Runable接口的升级，它可以返回执行结果，可以抛出异常
 */
class SonTask implements Callable<String> {

    private String name;

    public SonTask(String name) {
        this.name = name;
    }

    public String call() throws Exception {
        Thread.sleep(1000L);
        System.out.println(name + "任务计算完成");
        return "result_11";
    }
}

class MyrRun implements Runnable {

    public void run() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("特定线程2完成");
    }


}