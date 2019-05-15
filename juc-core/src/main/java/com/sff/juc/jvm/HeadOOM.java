package com.sff.juc.jvm;


import java.util.ArrayList;
import java.util.List;

/**
 * 堆内存溢出程序
 * <p>
 * 堆内存参数设置：
 * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeadOOM {

    public static void main(String[] args) throws Exception {

        Thread t = new Thread(new MyThread(111));
       // t.setName("序号：" + 111);
        t.start();

    }


}


class MyThread implements Runnable {

    private final int i;

    public MyThread(int i) {
        this.i = i;
    }

    public void run() {

        List<String> list = new ArrayList<String>();
        while (true) {
            list.add(new String("qqq"));

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("-----");

        }
    }


}
