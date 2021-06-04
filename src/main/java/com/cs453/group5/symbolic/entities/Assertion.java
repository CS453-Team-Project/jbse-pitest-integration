package com.cs453.group5.symbolic.entities;

public class Assertion {
    private int line;

    private String assertion;

    public Assertion(int line, String assertion) {
        this.line = line;
        this.assertion = assertion;
    }

    public int getLine() {
        return line;
    }

    public String getAssertion() {
        return String.format("jbse.meta.Analysis.ass3rt(%s);", assertion);
    }
}
