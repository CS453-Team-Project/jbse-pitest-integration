package com.cs453.group5.examples;

public class StringArraySortChecker {
    public Boolean checkSorted(String[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }

        return true;
    }
}
