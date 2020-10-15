package com.example.homlee.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * leetcode 146 LRU缓存机制
 */
public class LRUCache {
    private static class DLinkedNode {
        int key;
        int value;
        DLinkedNode next;
        DLinkedNode prev;

        public DLinkedNode() { }

        public DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Map<Integer, DLinkedNode> map = new HashMap<Integer, DLinkedNode>(16);
    private int capacity;
    private int size;
    private DLinkedNode mDummyHead = new DLinkedNode();
    private DLinkedNode mDummyTail = new DLinkedNode();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        mDummyHead.next = mDummyTail;
        mDummyTail.prev = mDummyHead;
    }

    public int get(int key) {
        DLinkedNode node = map.get(key);
        if (node != null) {
            move2Head(node);
            return node.value;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        DLinkedNode node = map.get(key);
        if (node != null) {
            node.value = value;
            move2Head(node);
        } else {
            DLinkedNode newNode = new DLinkedNode(key, value);
            add2Head(newNode);
            map.put(newNode.key, newNode);
            if (++size > capacity) {
                DLinkedNode tail = removeTail();
                map.remove(tail.key);
                size--;
            }
        }
    }

    private void add2Head(DLinkedNode node) {
        mDummyHead.next.prev = node;
        node.next = mDummyHead.next;
        node.prev = mDummyHead;
        mDummyHead.next = node;
    }


    private void move2Head(DLinkedNode node) {
        removeNode(node);
        add2Head(node);
    }

    private DLinkedNode removeTail() {
        DLinkedNode tail = mDummyTail.prev;
        return removeNode(tail);
    }

    private DLinkedNode removeNode(DLinkedNode node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
        return node;
    }


    public static void main(String[] args) {
        LRUCache cache = new LRUCache( 2 /* 缓存容量 */ );

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));       // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        System.out.println(cache.get(2));       // 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        System.out.println(cache.get(1));       // 返回 -1 (未找到)
        System.out.println(cache.get(3));       // 返回  3
        System.out.println(cache.get(4));       // 返回  4
    }
}
