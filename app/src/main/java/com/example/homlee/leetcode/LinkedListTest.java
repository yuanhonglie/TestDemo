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

    public static boolean isPalindrome(LinkedNode node) {

        return false;
    }


    private static void print(LinkedNode node) {
        while (node != null) {
            System.out.print(node.value);
            node = node.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String text = "asdffdsa";
        LinkedNode head = new LinkedNode();
        LinkedNode node = head;
        for (int i = 0; i < text.length(); i++) {
                node.next = new LinkedNode(text.charAt(i));
                node = node.next;
        }
        print(head.next);

    }
}
