package com.cs453.group5.examples;

public class IntArraySortChecker {
    public Boolean checkSorted(int[] arr) {
        if (arr[0] == 1) {
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    return false;
                }
            }
        }

        return true;
    }
}
