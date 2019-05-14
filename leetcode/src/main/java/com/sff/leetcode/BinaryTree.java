package com.sff.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author shifeifei
 * @date 2019-05-13 14:21
 * <p>
 * 树相关算法
 */
public class BinaryTree {


    private BinaryTreeNode root; //二叉树根节点

    /**
     * 二叉树节点
     */
    private class BinaryTreeNode {
        private int data;
        private BinaryTreeNode left;
        private BinaryTreeNode right;

        public BinaryTreeNode(int data) {
            this(data, null, null);
        }

        public BinaryTreeNode(int data, BinaryTreeNode left, BinaryTreeNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {


        /**
         * 构造的二叉树形态如下
         *       3
         *      - -
         *     -   -
         *    2     5
         *   - -     -
         *  -   -     -
         * 1     2     8
         *              -
         *               -
         *               23
         *              -
         *             -
         *            9
         */
        int[] arrays = {3, 2, 5, 8, 1, 23, 9, 2};

        BinaryTree binaryTree = new BinaryTree();
        BinaryTreeNode root = null;
        for (int a : arrays) {
            root = binaryTree.createdTree(a, root);
        }

        System.out.println(binaryTree.listOrder(root));
        System.out.println(binaryTree.order(root));
        System.out.println(binaryTree.postList(root));
        System.out.println("-----------------------");

        System.out.println("二叉树深度：" + binaryTree.getTreeDepth(root));
        System.out.println("叶子节点个数：" + binaryTree.getLeafNodeNums(root));
        System.out.println("第k层节点个数：" + binaryTree.countNodeLevel(root, 3));
    }

    /**
     * 先根非递归遍历
     *
     * @param root
     * @return
     */
    private List<Integer> dfsPreOrder(BinaryTreeNode root) {

        ArrayList<Integer> results = new ArrayList<Integer>();

        Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();

        BinaryTreeNode cur = root;
        while (cur != null || !stack.empty()) {

            while (cur != null) {
                results.add(cur.data);
                stack.push(cur);
                cur = cur.left;
            }

            cur = stack.pop();
            // 转向
            cur = cur.right;
        }

        return results;
    }

    /**
     * 中根遍历
     *
     * @param root
     * @return
     */
    private List<Integer> dfsInOrder(BinaryTreeNode root) {
        List<Integer> results = new ArrayList<Integer>();

        Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();
        BinaryTreeNode cur = root;

        while (cur != null || !stack.empty()) {

            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            results.add(cur.data);
            cur = cur.right;
        }
        return results;
    }


    /**
     * 求二叉树第k层节点数
     * 求以root为根的k层节点数目 = 求以root左孩子为根的k-1层节点数目 + 以root右孩子为根的k-1层节点数目
     * 1. 如果二叉树为空或者 k<0，则k层节点是0
     * 2. 如果k=1,则节点数是1
     *
     * @param root
     * @param k
     * @return
     */
    public int countNodeLevel(BinaryTreeNode root, int k) {
        if (null == root || k < 1) {
            return 0;
        }
        if (1 == k) {
            return 1;
        }
        return countNodeLevel(root.left, k - 1) + countNodeLevel(root.right, k - 1);
    }

    /**
     * 求叶子节点的个数
     *
     * @param root
     * @return
     */
    public int getLeafNodeNums(BinaryTreeNode root) {
        if (null == root) {
            return 0;
        }
        int leafNum = 0;//叶子节点个数

        //队列先进先出
        Stack<BinaryTreeNode> stack = new Stack<BinaryTreeNode>();
        stack.push(root);

        while (!stack.isEmpty()) {

            BinaryTreeNode temp = stack.pop(); //移除栈顶元素
            //叶子节点无左右孩子
            if (temp.left == null && temp.right == null) {
                leafNum++;
            }

            if (temp.right != null) {
                stack.add(temp.right);
            }
            if (temp.left != null) {
                stack.add(temp.left);
            }
        }
        return leafNum;
    }

    /**
     * 二叉树深度
     * <p>
     * 1.如果二叉树为空，二叉树的深度为0
     * 2.如果二叉树不为空，二叉树的深度 = max(左子树深度， 右子树深度) + 1
     *
     * @param root
     * @return
     */
    public int getTreeDepth(BinaryTreeNode root) {
        if (null == root) {
            return 0;
        }
        return Math.max(getTreeDepth(root.left), getTreeDepth(root.right)) + 1;
    }

    /**
     * 二叉树节点个数
     *
     * @param root
     * @return
     */
    public int nodeSize(BinaryTreeNode root) {
        if (null == root) {
            return 0;
        }
        return nodeSize(root.right) + nodeSize(root.left) + 1;
    }

    /**
     * 递归先根遍历
     *
     * @param root
     * @return
     */
    public String preOrder(BinaryTreeNode root) {
        StringBuffer buffer = new StringBuffer();
        if (null != root) {
            buffer.append(root.data).append(",");
            //左子树
            buffer.append(preOrder(root.left));
            //右子树
            buffer.append(preOrder(root.right));
        }
        return buffer.toString();
    }

    /**
     * 迭代先根遍历
     *
     * @param root
     * @return
     */
    public String listOrder(BinaryTreeNode root) {
        StringBuffer buffer = new StringBuffer();
        if (null != root) {
            Stack<BinaryTreeNode> s = new Stack<BinaryTreeNode>();

            while (true) {
                while (null != root) {
                    buffer.append(root.data).append(",");
                    s.push(root);
                    root = root.left;
                }

                if (s.isEmpty()) {
                    break;
                }
                root = s.pop();
                root = root.right;
            }
        }
        return buffer.toString();
    }


    /***
     * 中根迭代遍历
     */
    public String order(BinaryTreeNode root) {

        StringBuffer sb = new StringBuffer();
        Stack<BinaryTreeNode> s = new Stack<BinaryTreeNode>();

        if (null != root) {

            while (true) {

                while (null != root) {
                    s.push(root);
                    root = root.left;
                }
                if (s.isEmpty()) {
                    break;
                }

                root = s.pop();
                sb.append(root.data).append(",");

                root = root.right;
            }
        }
        return sb.toString();
    }

    /**
     * 中根遍历
     *
     * @param root
     * @return
     */
    public String inOrder(BinaryTreeNode root) {
        StringBuffer buffer = new StringBuffer();
        if (null != root) {
            //左子树
            buffer.append(preOrder(root.left));
            buffer.append(root.data).append(",");
            //右子树
            buffer.append(preOrder(root.right));
        }
        return buffer.toString();
    }

    /**
     * 后根遍历
     *
     * @param root
     * @return
     */
    public String postOrder(BinaryTreeNode root) {
        StringBuffer buffer = new StringBuffer();
        if (null != root) {
            //左子树
            buffer.append(preOrder(root.left));
            //右子树
            buffer.append(preOrder(root.right));
            buffer.append(root.data).append(",");
        }
        return buffer.toString();
    }

    /**
     * 非递归后续遍历
     */
    public String postList(BinaryTreeNode root) {

        StringBuffer sb = new StringBuffer();
        Stack<BinaryTreeNode> s = new Stack<BinaryTreeNode>();
        if (null != root) {

            while (true) {

                if (null != root) {
                    s.push(root);
                    root = root.left;
                } else {
                    if (s.isEmpty()) {
                        return null;
                    } else if (s.pop().right == null) {
                        root = s.pop();
                        System.out.println(root.data);

                    } else {
                        root = null;
                    }

                }


            }

        }

        return null;
    }

    /**
     * 二叉查找树的构造
     * （1）若它的左子树不空，则左子树上所有结点的值均小于它的根结点的值；
     * （2）若它的右子树不空，则右子树上所有结点的值均大于它的根结点的值；
     * （3）它的左、右子树也分别为二叉排序树；
     *
     * @param element
     * @return
     */
    public BinaryTreeNode createdTree(int element, BinaryTreeNode root) {

        if (root == null) {
            root = new BinaryTreeNode(element, null, null);
            return root;
        }

        if (element < root.data) {
            root.left = createdTree(element, root.left);
        } else {
            root.right = createdTree(element, root.right);
        }
        return root;
    }

}
