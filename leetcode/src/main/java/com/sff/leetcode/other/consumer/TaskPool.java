package com.sff.leetcode.other.consumer;

public abstract class TaskPool {

    /**
     * 向池中添加资源
     */
    public abstract void add();

    /**
     * 从资源池中取走资源
     */
    public abstract void remove();
}
