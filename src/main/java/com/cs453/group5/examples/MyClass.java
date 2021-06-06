package com.cs453.group5.examples;

public class MyClass {
    public int intCalculate(MyClass myClass, int number) {
        int result = 0;
        if (number > 0) {
            result = 1;
        } else if (number < 0) {
            result = -1;
        }

        return result;
    }
}
