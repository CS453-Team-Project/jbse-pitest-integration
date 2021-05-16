package com.cs453.group5.examples;

public class StringArraySortChecker {
    public Boolean checkSorted(String[] arr) {
        if (arr.length > 0 && arr[0].equals("1234")) {
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i].compareTo(arr[i + 1]) > 0) {
                    return false;
                }
            }
        }

        return true;
    }
}
