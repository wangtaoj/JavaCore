package com.wangtao.datastructure;

import java.util.NoSuchElementException;

/**
 * @author wangtao
 **/
public class Stack<T> {

    private static final int DEFAULT_CAPICITY = 10;

    private Object[] elements;

    private int top;

    public Stack() {
        elements = new Object[DEFAULT_CAPICITY];
        top = -1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    private void resize() {

    }

    public void push(T element) {
        if (top + 1 > elements.length) {
            resize();
        }
        elements[++top] = element;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty())
            throw new NoSuchElementException();
        T temp = (T) elements[top];
        elements[top] = null;
        top--;
        return temp;
    }

    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();
        for (int i = 0; i < 5; i++) {
            s.push(i + 1);
        }
        while (!s.isEmpty()) {
            System.out.println(s.pop());
        }
        System.out.println("-----------------------------");
        for (int i = 0; i < 5; i++) {
            s.push(i + 1);
        }
        while (!s.isEmpty()) {
            System.out.println(s.pop());
        }

    }

    /*private class Node {
        T data;
        Node next;

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node top;

    public Stack() {

    }

    public void push(T t) {
        top = new Node(t, top);
    }

    public T pop() {
        if(!isEmpty())
            return null;
        T t = top.data;
        top = top.next;
        return t;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void print() {
        Node cur = top;
        while(cur != null) {
            System.out.println(cur.data);
            cur = cur.next;
        }
    }*/
}
