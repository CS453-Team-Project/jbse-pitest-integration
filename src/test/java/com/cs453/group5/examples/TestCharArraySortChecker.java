package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCharArraySortChecker {
    @DisplayName("Sorted Example 1")
    @Test
    public void test1() {
        CharArraySortChecker obj = new CharArraySortChecker();
        char[] arr = { 'a', 'b', 'c', 'd' };
        assertEquals(true, obj.checkSorted(arr));
    }

    @DisplayName("Unsorted Example 1")
    @Test
    public void test2() {
        CharArraySortChecker obj = new CharArraySortChecker();
        char[] arr = { 'a', 'b', 'd', 'c' };
        assertEquals(false, obj.checkSorted(arr));
    }
}
