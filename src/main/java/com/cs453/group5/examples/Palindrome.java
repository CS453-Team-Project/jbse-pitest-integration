package com.cs453.group5.examples;

public class Palindrome {
    public int isPalindrome(String string) {
        int j = string.length() - 1;
        for (int i = 0; i < string.length() / 2; i++, j--) {
            if (string.charAt(i) != string.charAt(j)) {
                return 0;
            }
        }
        return 1;
    }
}