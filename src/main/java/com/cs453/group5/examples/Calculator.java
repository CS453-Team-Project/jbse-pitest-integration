package com.cs453.group5.examples;

import static jbse.meta.Analysis.ass3rt;

public class Calculator {
    public int isPositive(int number) {
        int result = 0;
        if (number >= 0) {
            result = 1;
        } else if (number < -1) {
            result = -1;
        }
        // ass3rt(result == 1);
        return result;
    }
}
