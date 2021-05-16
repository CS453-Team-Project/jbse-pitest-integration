package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPalindrome {
    @DisplayName("Check isPalindrome for a true case")
    @Test
    public void test1() {
        Palindrome palindrome = new Palindrome();
        int result = palindrome.isPalindrome("AABBAA");
        assertEquals(1, result);
    }

    @DisplayName("Check isPalindrome for a false case")
    @Test
    public void test2() {
        Palindrome palindrome = new Palindrome();
        int result = palindrome.isPalindrome("ABBC");
        assertEquals(0, result);
    }
}
