package com.cs453.group5.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestPriQueue {
    @DisplayName("first test")
    @Test
    void test1() {
        PriQueue priQueue = new PriQueue();
        int[] scoville = { 1, 2, 3, 9, 10, 12 };
        assertEquals(2, priQueue.solution(scoville, 7));
    }
}
