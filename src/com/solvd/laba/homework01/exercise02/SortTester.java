package com.solvd.laba.homework01.exercise02;

import java.util.Arrays;
import java.util.Random;

public class SortTester {
    public static void main(String[] args){
        // generate random array
        Random rand = new Random();
        int[] array = new int[100];
        for(int i = 0; i < array.length; ++i) {
            array[i] = rand.nextInt();
        }

        // print unsorted array
        System.out.println("* Unsorted array:");
        for (int element : array) {
            System.out.printf("%d\n", element);
        }

        SortBubble.sort(array);
        assert SortTester.isSorted(array);

        System.out.println("\n* Sorted array:");
        for(int element : array) {
            System.out.printf("%d\n", element);
        }
    }


    protected static boolean isSorted(int[] array) {
        int[] arrayCopy = array.clone();

        Arrays.sort(arrayCopy);

        for (int i = 0; i < array.length; ++i) {
            if (array[i] != arrayCopy[i]) {
                return false;
            }
        }
        return true;
    }
}
