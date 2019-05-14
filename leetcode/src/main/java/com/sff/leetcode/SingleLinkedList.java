package com.sff.leetcode;

import lombok.Data;

import java.util.Stack;

/**
 * @author shifeifei
 * @date 2019-05-13 14:22
 * <p>
 * 链表相关算法
 */
public class SingleLinkedList<E> {


    int size; //表示链表元素个数
    Node header; //表示链表头结点

    /**
     * 如何逐对反转链表
     * 举例：
     * 1->2->3->4
     * 反转后：2->1->4->3
     * <p>
     * 可以一对一对的反转 2->1  ->4->3
     *
     * @return
     */
    public Node reversePair(Node header) {
        if (header == null || header.next == null) {
            return header;
        } else {

            Node temp = header.next;
            header.next = temp.next; // 连接上后面的节点
            temp.next = header;

            header = temp; //header节点向后移动

            header.next.next = reversePair(header.next.next);
            return header;
        }
    }

    /**
     * 12.将一个单链表排序
     * <p>
     * 参考答案：https://www.cnblogs.com/qiaozhoulin/p/4585401.html
     */
    public Node sortLinkedList(Node header) {
        //TODO
        return null;
    }

    /**
     * 11.假设两个单向链表相交于某一点后，变成了一个单向链表
     * <p>
     * 思路：(1)分别计算出两个链表的长度
     * (2)比较两个链表的长度大小，并计算出长度差
     * (3)从较长的链表开始遍历
     */
    public Node findIntersectNode(Node head1, Node head2) {

        int len1 = 0;
        Node temp1 = head1;

        int len2 = 0;
        Node temp2 = head2;

        while (null != temp1) {
            len1++;
            temp1 = temp1.next;
        }
        while (null != temp2) {
            len2++;
            temp2 = temp2.next;
        }

        int diff = 0;
        //让 temp1 始终指向较长的链表
        if (len1 < len2) {
            temp1 = head2;
            temp2 = head1;
            diff = len2 - len1;
        } else {
            diff = len1 - len2;
        }
        for (int i = 0; i < diff; i++) {
            temp1 = temp1.next;
        }

        while (temp1 != null && temp2 != null) {
            if (temp1 == temp2) {
                return temp1;
            }
            temp1 = temp1.next;
            temp2 = temp2.next;
        }

        return null;
    }

    /**
     * 10.向有序链表中插入一个元素
     *
     * @return
     */
    public Node addNodeToSortList(Node head, Integer data) {
        if (null == head) {
            return new Node(data);
        }

        Node newNode = new Node(data);
        if (head.data > data) {
            newNode.next = head;
            return newNode;
        }

        Node temp = null;
        Node current = head;

        //遍历链表找到比插入的新节点更大的元素节点
        while (current != null && current.data < data) {
            temp = current;
            //比当前节点大的节点
            current = current.next;
        }
        //新节点后插入
        temp.next = current;
        current.next = newNode;
        return current;
    }

    /**
     * 9.合并两个有序链表
     * <p>
     * 思路：
     * 1.判断链表是否为空
     * 2.递归遍历合并 ：
     * (1) 确定链表表头
     * (2) 遍历合并，并判断大小;
     * <p>
     * 考虑这种情况：
     * 1->2->4
     * 1->1->1->2->2->2->3
     *
     * @param list1
     * @param list2
     */
    public Node mergeTwoSortList(Node list1, Node list2) {

        if (null == list1 && null == list2) {
            return null;
        } else if (null == list1) {
            return list2;
        } else if (null == list2) {
            return list1;
        } else {
            Node temp = null;
            if (list1.data < list2.data) {
                temp = list1;
                temp.next = mergeTwoSortList(list1.next, list2);
            } else {
                temp = list2;
                temp.next = mergeTwoSortList(list1, list2.next);
            }
            return temp;
        }
    }


    /**
     * 9.求链表的中间节点
     * <p>
     * 题目描述：求链表的中间节点，如果链表的长度为偶数，返回中间两个节点的任意一个，若为奇数，则返回中间节点。
     * <p>
     * 分析：此题的解决思路和第3题「求链表的倒数第 k 个节点」很相似。可以先求链表的长度，然后计算出中间节点所在链表顺序的位置。
     * <p>
     * 但是如果要求只能扫描一遍链表，如何解决呢？
     * <p>
     * 通过两个指针来完成。用两个指针从链表头节点开始，一个指针每次向后移动两步，一个每次移动一步，
     * 直到快指针移到到尾节点，那么慢指针即是所求。
     */
    public Node findMiddleNode(Node header) {
        if (null == header) {
            return null;
        }

        Node fast = header;
        Node slow = header;

        while (null != fast && null != fast.next) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * 8.求链表倒数第k个节点
     * <p>
     * 题目描述：输入一个单向链表，输出该链表中倒数第k个节点，链表的倒数第0个节点为链表的尾指针。
     * <p>
     * 分析：设置两个指针 p1、p2，首先 p1 和 p2 都指向 head，然后 p2 向前走 k 步，这样 p1 和 p2 之间就间隔 k 个节点，最后 p1 和 p2 同时向前移动，直至 p2 走到链表末尾。
     */

    public Node countBackwards(Node header, int k) {

        if (null == header || k < 0) {
            return null;
        }

        Node fast = header;
        Node slow = header;

        while (null != fast && k > 0) {
            fast = fast.next;
            k--;
        }

        //防止k比链表的长度还长
        if (k > 0) {
            return null;
        }

        while (null != fast) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }


    /**
     * 7.判断两个链表是否相交
     * <p>
     * 如果两个链表相交于某一节点，那么在这个相交节点之后的所有节点都是两个链表所共有的。
     * 也就是说，如果两个链表相交，那么最后一个节点肯定是共有的。
     * (1) 先遍历第一个链表，记住最后一个节点，
     * (2) 然后遍历第二个链表，到最后一个节点时和第一个链表的最后一个节点做比较，如果相同，则相交，否则不相交。
     * <p>
     * 时间复杂度为O(n1+n2)，因为只需要一个额外指针保存最后一个节点地址，空间复杂度为O(1)
     *
     * @param list
     * @param list2
     * @return
     */
    public boolean intersect(SingleLinkedList<E> list, SingleLinkedList<E> list2) {

        Node header1 = list.header;
        Node header2 = list2.header;
        if (null == header1 || null == header2) {
            return false;
        }

        Node tailNode = header1;
        while (null != tailNode.next) {
            tailNode = tailNode.next;
        }

        Node tailNode2 = header2;
        while (null != tailNode2.next) {
            tailNode2 = tailNode2.next;
        }
        return tailNode == tailNode2;
    }

    /**
     * 6.判断一个单链表是否有环
     * <p>
     * 如果一个链表中有环，也就是说用一个指针去遍历，是永远走不到头的。
     * 因此，我们可以用两个指针去遍历，一个指针一次走两步，一个指针一次走一步，如果有环，两个指针肯定会在环中相遇。时间复杂度为O（n）
     *
     * @param list
     * @return
     */
    public boolean hasCircle(SingleLinkedList list) {

        Node header = list.header;

        Node fastHeader = header; //快指针一次走两步
        Node slowHeader = header; //慢指针一次走一步

        while (null != fastHeader && null != fastHeader.next) {
            fastHeader = fastHeader.next.next;
            slowHeader = slowHeader.next;
            if (fastHeader == slowHeader) {
                return true;
            }
        }
        return false;
    }

    /**
     * 5.倒序打印单链表元素
     * <p>
     * 利用栈先进后出的特性来打印
     *
     * @param list
     */
    public void reversPrint(SingleLinkedList list) {

        Node header = list.header;

        Stack<Integer> stack = new Stack<Integer>();
        while (null != header) {
            stack.push(header.data);
            header = header.next;
        }
        while (!stack.empty()) {
            System.out.print(stack.peek() + "\t");
            stack.pop();
        }
    }

    /**
     * 4.单向链表反转，递归方法
     * <p>
     * 1,2,4,5,3 --> 3,5,4,2,1
     * <p>
     * 第一次：2,1,4,5,3
     * 第二次：2,4,1,5,3
     *
     * @return
     */
    public Node reverseByRecursion(Node head) {
        //第一个条件是判断异常，第二个条件是结束判断
        if (head == null || head.next == null) {
            return head;
        }

        Node newHead = reverseByRecursion(head.next);

        head.next.next = head;
        head.next = null;

        return newHead;    //返回新链表的头指针
    }

    /**
     * 3.迭代方式实现反转
     * 举例子：
     * <p>
     * 4->3->5->1->8
     * <p>
     * 断链过程：
     * null<-4       3->5->1->8
     * null<-4<-3    5->1->8
     *
     * @param head
     * @return
     */
    public Node reverse(Node head) {

        if (null == head) {
            return null;
        }

        Node temp = null;
        while (null != head) {
            Node nextNode = head.next;
            head.next = temp;
            temp = head;
            head = nextNode;
        }
        return temp;
    }

    /**
     * 2.获取单向链表长度
     *
     * @param list
     * @return
     */
    public int size(SingleLinkedList list) {
        int length = 0;
        Node header = list.header;
        if (null == header) {
            return length;
        }

        while (null != header) {
            header = header.next;
            length++;
        }
        return length;
    }


    /**
     * 1.单向链表中添加元素
     *
     * @param data
     * @return
     */
    public void add(Integer data) {
        Node node = new Node(data);

        if (null == header) {
            header = node;
            size++;
            return;
        }

        Node temp = header;
        while (null != temp.next) {
            temp = temp.next;
        }
        temp.next = node;
        size++;
    }


    public void print(SingleLinkedList<E> list) {
        Node temp = list.header;
        if (null == temp) {
            System.out.println("null");
            return;
        }
        while (null != temp) {
            System.out.print(temp.data + "\t");
            temp = temp.next;
        }
    }

    /**
     * 初始化链表
     */
    public SingleLinkedList() {
        header = null;
        size = 0;
    }


    /**
     * 单链表节点
     */
    @Data
    class Node {
        private Integer data;         //节点元素
        private Node next;   //指向下一个节点元素指针

        public Node() {
        }

        public Node(Integer data) {
            this.data = data;
        }
    }
}
