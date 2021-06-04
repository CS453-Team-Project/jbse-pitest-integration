package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPassingTruck {
    @DisplayName("first test")
    @Test
    void test1() {
        PassingTruck passingTruck = new PassingTruck();
        int[] truckWeights = { 7, 4, 5, 6 };
        assertEquals(8, passingTruck.solution(2, 10, truckWeights));
    }
}
