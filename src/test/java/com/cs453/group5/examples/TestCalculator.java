package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCalculator {
    @DisplayName("Test Positive")
    @Test
    public void test1() {
        Calculator obj = new Calculator();
        assertEquals(1, obj.isPositive(10));
    }

    @DisplayName("Test Positive")
    @Test
    public void test2() {
        Calculator obj = new Calculator();
        assertEquals(1, obj.isPositive(0));
    }
}
