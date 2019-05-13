package com.sff.dp.singleton;

/**
 * @author shifeifei
 * @date 2019-05-13 09:34
 * <p>
 * https://blog.csdn.net/mnb65482/article/details/80458571
 * <p>
 * 单例模式的几种写法：
 * https://blog.csdn.net/nsw911439370/article/details/50456231
 */
public class Singleton {

    /**
     * 静态内部类实现的单利模式
     * <p>
     * 优点：
     * 1.外部类加载时并不需要立即加载内部类，内部类不被加载则不会去初始化实例，保证懒加载
     */
    private Singleton() {
    }

    private static class SingletonHolder {
        private static Singleton singleton = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.singleton;
    }
}