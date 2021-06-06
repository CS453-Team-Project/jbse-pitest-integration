package com.cs453.group5.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestMyClass {
    @DisplayName("Distinguish the coordinates")
    @Test
    public void test1() {
        MyClass obj = new MyClass();
        assertEquals(1, obj.intCalculate(obj, 1));
    }
}
