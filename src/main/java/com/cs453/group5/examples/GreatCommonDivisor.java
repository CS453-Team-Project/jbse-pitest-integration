package com.cs453.group5.examples;

public class GreatCommonDivisor {
    public static int gcd(int num1, int num2) {
        if (num2 == 0)
            return num1;
        else
            return gcd(num2, num1 % num2);
    }

    public int check(int num1, int num2) {
        int gcd = gcd(num1, num2);
        return gcd;
    }
}
