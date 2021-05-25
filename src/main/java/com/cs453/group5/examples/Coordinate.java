package com.cs453.group5.examples;

public class Coordinate {
    public int check(int x, int y) {
        if (x == 0 || y == 0) {
            return 0;
        }
        if (x > 0 && y > 0) {
            return 1;
        } else if (x < 0 && y > 0) {
            return 2;
        } else if (x < 0 && y < 0) {
            return 3;
        } else {
            return 4;
        }
    }
}
