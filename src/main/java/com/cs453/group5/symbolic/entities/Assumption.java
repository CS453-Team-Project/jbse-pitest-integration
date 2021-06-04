package com.cs453.group5.symbolic.entities;

public class Assumption {
    private int line;
    private String assumption;

    public Assumption(int line, String assumption) {
        this.line = line;
        this.assumption = assumption;
    }

    public int getLine() {
        return line;
    }

    public String getAssumption() {
        return String.format("jbse.meta.Analysis.assume(%s);", assumption);
    }
}
