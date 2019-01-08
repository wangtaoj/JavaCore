package com.wangtao.nowcoder;


/**
 * @author wangtao
 * 2018-2018/1/7-12:43
 **/
public class ReverseLinkList {

    public static LNode reverseLink(LNode p) {
        if (p == null)
            return null;
        LNode pre = null;
        LNode cur = p;
        while (cur.next != null) {
            LNode q = cur.next;
            cur.next = pre;
            pre = cur;
            cur = q;
        }
        cur.next = pre;
        return cur;
    }

    public static LNode merge(LNode p, LNode q) {
        if (p == null)
            return q;
        if (q == null)
            return p;
        LNode ans;
        if (p.val <= q.val) {
            ans = p;
            ans.next = merge(p.next, q);
        } else {
            ans = q;
            ans.next = merge(p, q.next);
        }
        return ans;
    }

    public static LNode mergeWithLoop(LNode p, LNode q) {
        if (p == null)
            return q;
        if (q == null)
            return p;
        LNode ans = null, r = null;
        while (p != null && q != null) {
            if (p.val <= q.val) {
                if (ans == null) {
                    ans = p;
                    r = ans;
                } else {
                    r.next = p;
                    r = p;
                }
                p = p.next;
            } else {
                if (ans == null) {
                    ans = q;
                    r = ans;
                } else {
                    r.next = q;
                    r = q;
                }
                q = q.next;
            }
        }
        if (p != null)
            r.next = p;
        if (q != null)
            r.next = q;
        return ans;
    }

    public static void main(String[] args) {
        int[] elements = {1, 3, 5, 7};
        LNode p = new LNode(elements);
        System.out.println("p: ");
        p.print();
        reverseLink(p).print();
        p.print();

    }

}

class LNode {
    int val;
    LNode next;

    public LNode(int val, LNode next) {
        this.val = val;
        this.next = next;
    }

    public LNode(int[] elements) {
        if (elements == null || elements.length == 0)
            throw new IllegalArgumentException("参数异常!");
        buildLink(elements);
    }

    public void buildLink(int[] elements) {
        this.val = elements[0];
        this.next = null;
        LNode r = this;
        for (int i = 1; i < elements.length; i++) {
            LNode temp = new LNode(elements[i], null);
            r.next = temp;
            r = temp;
        }
    }

    public void print() {
        System.out.print("[" + this.val);
        LNode p = this.next;
        while (p != null) {
            System.out.print(", " + p.val);
            p = p.next;
        }
        System.out.println("]");
    }
}
