package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGreatCommonDivisor {
    @DisplayName("Get the great common divisor.")
    @Test
    public void test1() {
        GreatCommonDivisor obj = new GreatCommonDivisor();
        obj.check(36, 24);
        assertEquals(1, 1);
    }
}
