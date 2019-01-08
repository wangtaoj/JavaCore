package com.wangtao.datastructure;

import java.util.NoSuchElementException;

/**
 * @author wangtao
 * Created on 2018/2/28
 **/
public class Queue<T> {

    private class Node {
        T data;
        Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node front;

    private Node tail;

    public boolean isEmpty() {
        return front == null && tail == null;
    }

    public void push(T element) {
        if(isEmpty()) {
            front = tail = new Node(element, null);
        } else {
            Node temp = new Node(element, null);
            tail.next = temp;
            tail = temp;
        }
    }

    public T pop() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        T temp = front.data;
        if(front == tail)
            front = tail = null;
        else
            front = front.next;
        return temp;
    }

    public static void main(String[] args) {
        Queue<Integer> queue = new Queue<>();
        queue.push(1);
        queue.push(2);
        queue.push(3);
        while(!queue.isEmpty())
            System.out.println(queue.pop());
    }
}
