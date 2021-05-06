package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestParenthese {
    @DisplayName("Valid parenthese")
    @Test
    public void test1() {
        Parenthese obj = new Parenthese();
        assertEquals(1, obj.check("()".toCharArray()));
    }

    @DisplayName("Invalid Parenthese")
    @Test
    public void test2() {
        Parenthese obj = new Parenthese();
        assertEquals(0, obj.check("((".toCharArray()));
    }
}
