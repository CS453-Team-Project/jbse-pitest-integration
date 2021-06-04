package com.cs453.group5.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTargetNumber {
    @Test
    public void test1() {
        TargetNumber obj = new TargetNumber();
        int[] arr = { 1, 1, 1, 1, 1 };
        assertEquals(5, obj.solution(arr, 3));
    }
}
