package com.example.homlee.leetcode;

import java.util.HashMap;
import java.util.Map;

public class Solution202005 {
    //===0521===
    /**
     * 求最长回文子串
     * @date 0521
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        int size = s.length();
        int [][] p = new int [size][size];
        String output = "";
        for (int k = 0; k < size; k++) {
            for (int i = 0; i + k < size; i++) {
                int j = i + k;
                if (k == 0) {
                    p[i][j] = 1;
                } else if (k == 1) {
                    p[i][j] = s.charAt(i) == s.charAt(j) ? 1 : 0;
                } else {
                    p[i][j] = s.charAt(i) == s.charAt(j) && p[i+1][j-1] == 1 ? 1 : 0;
                }
                int curSize = output != null ? output.length() : 0;
                if (p[i][j] == 1 && curSize < j - i + 1) {
                    output = s.substring(i, j + 1);
                }
            }
        }

        return output;
    }

    //====0522=====
    private static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.value = val;
        }
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {

        if (preorder == null || inorder == null) {
            return null;
        }

        if (preorder.length != inorder.length) {
            return null;
        }

        int n = inorder.length;

        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            indexMap.put(inorder[i], i);
        }

        return buildTreeInner(indexMap, preorder, 0, n - 1, 0, n - 1);
    }

    private TreeNode buildTreeInner(Map<Integer, Integer> map, int[] preorder, int preorderLeft, int preorderRight, int inorderLeft, int inorderRight) {
        if (preorderLeft > preorderRight) {
            return null;
        }

        int rootValue = preorder[preorderLeft];
        int inorderIndex = map.get(rootValue);

        int leftTreeNodeSize = inorderIndex - inorderLeft;

        TreeNode node = new TreeNode(rootValue);
        node.left = buildTreeInner(map, preorder, preorderLeft + 1, preorderLeft + leftTreeNodeSize, inorderLeft, inorderIndex - 1);
        node.right = buildTreeInner(map, preorder, preorderLeft + leftTreeNodeSize + 1, preorderRight, inorderIndex + 1, inorderRight);

        return node;
    }

    public static void preorderPrint(TreeNode root) {
        if (root == null) {
            return;
        }

        print(root.value);
        preorderPrint(root.left);
        preorderPrint(root.right);
    }

    public static void inorderPrint(TreeNode root) {
        if (root == null) {
            return;
        }
        inorderPrint(root.left);
        print(root.value);
        inorderPrint(root.right);
    }

    public static void postorderPrint(TreeNode root) {
        if (root == null) {
            return;
        }
        postorderPrint(root.left);
        postorderPrint(root.right);
        print(root.value);
    }

    private static void print(int i) {
        System.out.print(i);
        System.out.print(',');
    }

    //====0522=====




    public static void main(String[] args) {
        Solution202005 solution = new Solution202005();
        //0522
        /*
        String input = "addeeefssfsfweesdefed";
        String output = solution.longestPalindrome(input);
        System.out.println(output);
        */
        /*
        int [] preorder = new int[] {1, 2, 4, 5, 7, 8, 3, 6};
        int[] inorder = new int[] {4, 2, 7, 5, 8, 1, 3, 6};
        TreeNode treeNode = solution.buildTree(preorder, inorder);
        preorderPrint(treeNode);
        System.out.println();
        inorderPrint(treeNode);
        System.out.println();
        postorderPrint(treeNode);
         */
    }
}
