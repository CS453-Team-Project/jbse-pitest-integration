package com.cs453.group5.examples;

public class CharArraySortChecker {
    public Boolean checkSorted(char[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }

        return true;
    }
}
