package com.example.homlee.leetcode;

public class Solution0521 {
    public static String longestPalindrome(String s) {
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

    public static void main() {
        String input = "addeeefssfsfweesdefed";
        String output = longestPalindrome(input);
        System.out.println(output);
    }
}
