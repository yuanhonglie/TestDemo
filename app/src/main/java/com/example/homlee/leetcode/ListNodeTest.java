package com.example.homlee.leetcode;

import java.util.List;

public class ListNodeTest {

    private static class ListNode<T> {
        T value;
        ListNode next;
        public ListNode() { }

        public ListNode(T value) {
            this.value = value;
        }
    }

    //判断回文
    public boolean isPalindrome(ListNode head) {
        ListNode prev = null;
        ListNode slow = head;
        ListNode fast = head;
        ListNode next;
        while(fast != null && fast.next != null) {
            fast = fast.next.next;
            next = slow.next;
            slow.next = prev;
            prev = slow;
            slow = next;
        }

        if (fast != null) {
            slow = slow.next;
        }

        while (prev != null) {
            if (prev.value != slow.value) {
                return false;
            }
            prev = prev.next;
            slow = slow.next;
        }

        return true;
    }




    //leetcode 206反转链表 迭代
    private ListNode reverseList(ListNode node) {
        if (node == null) {
            return null;
        }

        ListNode prev = null;
        ListNode next = null;
        while (node != null) {
            //暂存当前结点的下一结点；
            next = node.next;
            //将当前结点指向前一个结点；
            node.next = prev;
            //将当前结点赋值给prev暂存起来；
            prev = node;
            //将下一结点指定给node；
            node = next;
        }

        //最后一个不为null的结点就是反转链表的头
        return prev;
    }

    //leetcode 206反转链表 递归
    private ListNode reverseList1(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = reverseList1(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }

    //leetcode 141链表中环的检测
    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }

        ListNode fast = head;
        ListNode slow = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }

        return false;
    }
    //leetcode 21两个有序的链表合并
    ListNode<Integer> mergeList(ListNode<Integer> node1, ListNode<Integer> node2) {
        if (node1 == null) {
            return node2;
        }

        if (node2 == null) {
            return node1;
        }

        ListNode<Integer> head = new ListNode<>(0);
        ListNode<Integer> node = head;
        while (node1 != null && node2 != null) {
            if (node1.value < node2.value) {
                node.next = node1;
                node1 = node1.next;
            } else {
                node.next = node2;
                node2 = node2.next;
            }
            node = node.next;
        }

        //最后最多只有一个还未被完全合并，直接将链表指定到未合并的链表即可
        node.next = node1 == null ? node2 : node1;

        return head.next;
    }
    //leetcode 19删除链表倒数第n个结点
    public ListNode<Integer> removeNthFromEnd(ListNode<Integer> head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }
    //leetcode 876求链表的中间结点 快慢指针
    public ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    //leetcode 876求链表的中间结点 数组法
    public ListNode middleNode1(ListNode head) {

        return null;
    }

    //leetcode 876求链表的中间结点 两次遍历
    public ListNode middleNode2(ListNode head) {

        return null;
    }

    private static void print(ListNode node) {
        while (node != null) {
            System.out.print(node.value);
            node = node.next;
        }
        System.out.println();
    }

    private static ListNode text2ListNode(String text) {
        ListNode head = new ListNode();
        ListNode node = head;
        for (int i = 0; i < text.length(); i++) {
            node.next = new ListNode(text.charAt(i));
            node = node.next;
        }

        return head.next;
    }

    private static ListNode createCycleListNode() {
        ListNode head = new ListNode('a');
        head.next = new ListNode('b');
        head.next.next = head;
        return head;
    }

    private static ListNode createListNode(int[] list) {
        ListNode head = new ListNode();
        ListNode node = head;
        for (int i = 0; i < list.length; i++) {
            node.next = new ListNode(list[i]);
            node = node.next;
        }

        return head.next;
    }

    public static void main(String[] args) {
        ListNodeTest test = new ListNodeTest();

        //String text = "asdffdsa";
        String text = "asdfafdsa";

        ListNode node1 = text2ListNode(text);
        print(node1);

        boolean isPalindrome = test.isPalindrome(node1);
        System.out.println(text + " isPalindrome = " + isPalindrome);

        String text1 = "abcdefghi";
        ListNode node2 = text2ListNode(text1);
        print(node2);
        ListNode reverseNode2 = test.reverseList1(node2);
        print(reverseNode2);
        System.out.println("isCycleList " + test.hasCycle(reverseNode2));
        System.out.println("isCycleList " + test.hasCycle(createCycleListNode()));
        int[] array1 = {1,2,4,5,8,9};
        int[] array2 = {1,3,4,6,7};
        ListNode<Integer> list1 = createListNode(array1);
        ListNode<Integer> list2 = createListNode(array2);
        print(list1);
        print(list2);
        ListNode<Integer> mergedList = test.mergeList(list1, list2);
        print(mergedList);

        int[] array = {1,2,3,4};
        ListNode<Integer> removedList = test.removeNthFromEnd(createListNode(array), 1);
        print(removedList);

        ListNode<Integer> middleNode = test.middleNode(createListNode(array));
        print(middleNode);

    }
}
