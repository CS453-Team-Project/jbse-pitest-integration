package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestIntArraySortChecker {
    @DisplayName("Sorted Example 1")
    @Test
    public void test1() {
        IntArraySortChecker obj = new IntArraySortChecker();
        int[] arr = { 1, 2, 3, 4, 5 };
        assertEquals(true, obj.checkSorted(arr));
    }

    @DisplayName("Unsorted Example 1")
    @Test
    public void test2() {
        IntArraySortChecker obj = new IntArraySortChecker();
        int[] arr = { 1, 2, 3, 5, 4 };
        assertEquals(false, obj.checkSorted(arr));
    }
}
