package com.wangtao.datastructure;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * 二分查找树实现
 *
 * @param <T>
 * @author wangtao
 */
public class BinarySearchTree<T extends Comparable<T>> {

    //树根
    private Node root;

    /**
     * 二叉树结点类
     */
    private class Node {
        Node left;
        Node right;
        Node parent;
        T data;

        public Node(T data) {
            this.data = data;
        }
    }

    /**
     * 插入结点
     *
     * @param t 元素
     */
    public void add(T t) {
        if (t == null)
            throw new NullPointerException();
        //处理树根
        if (root == null) {
            root = new Node(t);
            root.parent = null;
            return;
        }

        Node current = root;
        while (true) {
            //小
            if (t.compareTo(current.data) < 0) {
                if (current.left == null) {
                    Node temp = new Node(t);
                    temp.parent = current;
                    current.left = temp;
                    return;
                }
                current = current.left;
            }
            //大或者等于
            else {
                if (current.right == null) {
                    Node temp = new Node(t);
                    temp.parent = current;
                    current.right = temp;
                    return;
                }
                current = current.right;
            }
        }
    }

    /**
     * 找到此节点的中序后继结点
     * 中序后继结点肯定是在该节点的右子树上的最左边上的结点
     *
     * @param node 已经保证了node有右子树
     */
    private Node getSuccessor(Node node) {
        Node successor = node.right;
        while (successor.left != null) {
            successor = successor.left;
        }
        return successor;
    }

    /**
     * 删除结点
     * 思路：
     * 1.删除结点是叶子结点,删除即可
     * 2.删除结点有一个子节点(左或右),将该节点的子节点挂到该节点的父节点即可
     * 3.删除结点有两个子节点,那么找到该节点的中序后继结点,将中序后继结点的值赋制到要删除的结点上
     * 将中序后继结点删除即可
     * 注意删除结点是否是根结点
     *
     */
    private void freeNode(Node node) {
        Node parent = node.parent;
        //第一种
        if (node.left == null && node.right == null) {
            if (node == root) {
                root = null;
                return;
            }
            if (parent.right == node)
                parent.right = null;
            else
                parent.left = null;
        } else if (node.left == null || node.right == null) {
            Node child;
            if (node.right != null)
                child = node.right;
            else
                child = node.left;
            //根结点
            if (parent == null) {
                root = child;
                return;
            }
            if (parent.right == node) {
                parent.right = child;
                child.parent = parent;
            } else {
                parent.left = child;
                child.parent = parent;
            }
        } else {
            Node successor = getSuccessor(node);
            //中序后继结点只有右孩子或者是叶子节点,因此此处也可用递归来删除该中序后继结点
            Node child = successor.right;
            if (successor != node.right) {
                Node successorParent = successor.parent;
                successorParent.left = child;
                if (child != null)
                    child.parent = successorParent;
            } else {
                node.right = child;
            }
            node.data = successor.data;
            successor = null;//删除掉
        }
    }

    /**
     * 根据key值删除结点
     */
    public void remove(T key) {
        Node delNode = findNode(key);
        if (delNode == null)
            throw new NoSuchElementException();
        freeNode(delNode);
    }

    /**
     * 根据key值查找结点
     */
    private Node findNode(T key) {
        if (key == null || root == null)
            return null;
        Node current = root;
        while (current != null) {
            //小
            if (key.compareTo(current.data) < 0)
                current = current.left;
                //大
            else if (key.compareTo(current.data) > 0)
                current = current.right;
                //找到了
            else
                return current;
        }
        return null;
    }

    /**
     * 判断是否包含元素t
     */
    public boolean contains(T t) {
        return findNode(t) != null;
    }

    /**
     * 先序递归遍历
     */
    public void preOrder(Node node) {
        System.out.print(node.data + " ");
        if (node.left != null)
            preOrder(node.left);
        if (node.right != null)
            preOrder(node.right);
    }

    public void preOrder() {
        if (root == null)
            return;
        preOrder(root);
        System.out.println();
    }

    /**
     * 中序递归遍历
     */
    private void inOrder(Node node) {
        if (node.left != null)
            inOrder(node.left);
        System.out.print(node.data + " ");
        if (node.right != null)
            inOrder(node.right);
    }

    public void inOrder() {
        if (root == null)
            return;
        inOrder(root);
        System.out.println();
    }

    /**
     * 后序递归遍历
     */
    private void postOrder(Node node) {
        if (node.left != null)
            postOrder(node.left);
        if (node.right != null)
            postOrder(node.right);
        System.out.print(node.data + " ");
    }

    public void postOrder() {
        if (root == null)
            return;
        postOrder(root);
        System.out.println();
    }

    /**
     * 先序遍历非递归方式,借助栈结构
     */
    public void preOrderByStack() {
        LinkedList<Node> stack = new LinkedList<>();
        if (root != null) {
            stack.push(root);
            while (!stack.isEmpty()) {
                Node cur = stack.pop();
                System.out.println(cur.data);
                if (cur.right != null)
                    stack.push(cur.right);
                if (cur.left != null)
                    stack.push(cur.left);
            }
        }
    }

    /**
     * 中序遍历非递归方式
     */
    public void inOrderByStack() {
        LinkedList<Node> stack = new LinkedList<>();
        Node current = root;
        //循环结束条件,栈不为空或者当前节点不为空值
        while (current != null || stack.size() > 0) {
            if (current != null) {
                stack.push(current);
                current = current.left;//将左孩子一直压入栈中
            } else {
                current = stack.pop();
                System.out.println(current.data);
                current = current.right;//将右孩子入栈
            }

        }
    }

    /**
     * 非递归后序遍历
     */
    public void postOrderByStack() {
        LinkedList<Node> stack = new LinkedList<>();
        if (root != null) {
            stack.push(root);
            Node cur = root.left;
            while (!stack.isEmpty()) {
                //将左孩子入栈
                while (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                }
                Node flag = null;
                while (!stack.isEmpty()) {
                    //栈顶元素
                    cur = stack.peekFirst();
                    //说明右子树已经访问过了, 可以访问自己
                    if (cur.right == flag) {
                        stack.pop(); //出栈
                        System.out.print(cur.data + " ");
                        flag = cur;
                    } else {
                        cur = cur.right;
                        break;
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * 层次遍历,bfs遍历
     */
    public void layer() {
        if (root == null)
            return;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        Node current;
        while (!queue.isEmpty()) {
            current = queue.remove();
            System.out.print(current.data + " ");
            if (current.left != null)
                queue.add(current.left);
            if (current.right != null)
                queue.add(current.right);
        }
        System.out.println();
    }

    public int calHight(Node node) {
        if (node.right == null && node.left == null)
            return 1;
        int rh = 0, lh = 0;
        if (node.right != null)
            rh = calHight(node.right);
        if (node.left != null)
            lh = calHight(node.left);
        return Math.max(rh, lh) + 1;
    }


    public static void main(String[] args) {
        int[] arrs = {3, 2, 1, 5, 4, 6, 7};
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int arr : arrs) {
            bst.add(arr);
        }
        System.out.println(bst.calHight(bst.root));
        bst.postOrder();
        System.out.println("---------------------------");
        bst.postOrderByStack();
        bst.inOrderByStack();
    }

}
