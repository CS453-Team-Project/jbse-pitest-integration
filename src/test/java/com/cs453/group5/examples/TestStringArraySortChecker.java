package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStringArraySortChecker {
    @DisplayName("Sorted Example 1")
    @Test
    public void test1() {
        StringArraySortChecker obj = new StringArraySortChecker();
        String[] arr = { "1234", "1235", "1236", "1237", "1238" };
        assertEquals(true, obj.checkSorted(arr));
    }

    @DisplayName("Unorted Example 1")
    @Test
    public void test2() {
        StringArraySortChecker obj = new StringArraySortChecker();
        String[] arr = { "1234", "1235", "1236", "1238", "1237" };
        assertEquals(false, obj.checkSorted(arr));
    }
}
