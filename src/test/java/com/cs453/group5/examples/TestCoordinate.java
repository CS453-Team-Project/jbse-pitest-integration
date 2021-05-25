package com.cs453.group5.examples;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCoordinate {
    @DisplayName("Distinguish the coordinates")
    @Test
    public void test1() {
        Coordinate obj = new Coordinate();
        assertEquals(2, obj.check(-1, 20));
    }
}
