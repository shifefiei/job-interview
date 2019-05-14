package com.sff.leetcode;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class StackAndQueue {


    /**
     * 1.如何用两个栈来实现一个队列
     */
    class QueueWithTwoStack {

        private Stack<Integer> stackOne;
        private Stack<Integer> stackTwo;

        public QueueWithTwoStack() {
            this.stackOne = new Stack<Integer>();
            this.stackTwo = new Stack<Integer>();
        }

        /**
         * 元素入队列
         *
         * @param data
         */
        public void add(Integer data) {
            stackOne.push(data);
        }

        /**
         * 取出队列头部元素
         */
        public Integer poll() {
            if (!stackTwo.isEmpty()) {
                return stackTwo.pop();
            } else {
                while (!stackOne.isEmpty()) {
                    stackTwo.push(stackOne.pop());
                }
                return stackTwo.pop();
            }
        }
    }

    /**
     * 2. 用两个队列实现一个栈的功能
     */
    class StackWithTwoQueue {

        private Queue<Integer> queueOne;
        private Queue<Integer> queueTwo;

        public StackWithTwoQueue() {
            this.queueOne = new ArrayBlockingQueue<Integer>(10);
            this.queueTwo = new ArrayBlockingQueue<Integer>(10);
        }

        /**
         * 元素入栈
         *
         * @param data
         */
        public void push(Integer data) {
            if (queueOne.isEmpty()) {
                queueTwo.add(data);
            } else {
                queueOne.add(data);
            }
        }

        /**
         * 取出栈顶元素
         * 思路：将一个队列的n-1个元素移动到另外一个队列，删除当前队列的最后一个元素
         *
         * @return
         */
        public Integer pop() {
            int i = 0;
            if (queueTwo.isEmpty()) {
                while (i < queueOne.size() - 1) {
                    queueTwo.add(queueOne.poll());
                    i++;
                }
                return queueOne.poll();
            } else {
                while (i < queueTwo.size() - 1) {
                    queueOne.add(queueTwo.poll());
                    i++;
                }
                return queueTwo.poll();
            }
        }
    }


}
