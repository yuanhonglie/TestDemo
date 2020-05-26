package com.example.homlee.leetcode;

public class SortTest {

    public void bubbleSort(int[] array) {
        int size = array.length;
        for (int i = 0; i < size; i++) {
            boolean changed = false;
            for (int j = 0; j < size - i - 1; j++) {
                if (array[j] > array[j+1]) {
                    int tmp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                    changed = true;
                }
            }
            if (!changed) {
                break;
            }
        }
    }

    public void insertionSort(int[] array) {

    }

    public void selectionSort(int[] array) {

    }

    public static void println(int[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(array[i]);
        }
        System.out.println(builder);
    }

    public static int[] createNewArray() {
        int[] array = {9,2,1,3,6,7,23,8,3,5,6};
        return array;
    }

    public static void main(String[] args) {
        int[] array = createNewArray();
        println(array);
        SortTest sort = new SortTest();
        System.out.println("bubbleSort:");
        sort.bubbleSort(array);
        println(array);
        System.out.println("insertionSort:");
        array = createNewArray();
        sort.insertionSort(array);
        println(array);
        System.out.println("selectionSort:");
        array = createNewArray();
        sort.selectionSort(array);
        println(array);
    }
}
