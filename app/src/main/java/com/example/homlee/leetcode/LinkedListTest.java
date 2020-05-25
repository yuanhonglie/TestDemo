package com.example.homlee.leetcode;

public class LinkedListTest {

    private static class LinkedNode {
        char value;
        LinkedNode next;
        public LinkedNode() { }

        public LinkedNode(char value) {
            this.value = value;
        }
    }

    public boolean isPalindrome(LinkedNode head) {

        LinkedNode prev = null;
        LinkedNode slow = head;
        LinkedNode fast = head;
        LinkedNode next;
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


    private static void print(LinkedNode node) {
        while (node != null) {
            System.out.print(node.value);
            node = node.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        //String text = "asdffdsa";
        String text = "asdfafdsa";
        LinkedNode head = new LinkedNode();
        LinkedNode node = head;
        for (int i = 0; i < text.length(); i++) {
                node.next = new LinkedNode(text.charAt(i));
                node = node.next;
        }
        print(head.next);
        LinkedListTest test = new LinkedListTest();
        boolean isPalindrome = test.isPalindrome(head.next);
        System.out.println(text + " isPalindrome = " + isPalindrome);

    }
}
