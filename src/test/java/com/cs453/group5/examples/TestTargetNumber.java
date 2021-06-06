package com.cs453.group5.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTargetNumber {
    @Test
    public void test1() {
        TargetNumber obj = new TargetNumber();
        int[] arr = { 1, 1, 1, 1, 2 };
        assertEquals(4, obj.solution(arr, 4));
    }
}
