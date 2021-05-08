package com.cs453.group5.symbolic.entities;

import java.util.Objects;

public class MutantId {
    private String mutatedClass;
    private String mutatedMethod;
    private String mutator;
    private String methodDescription;
    private int index;
    private int block;
    private int line;

    public MutantId(String mutatedClass, String mutatedMethod, String methodDescription, String mutator, int index,
            int block, int line) {
        this.mutatedClass = mutatedClass;
        this.mutatedMethod = mutatedMethod;
        this.methodDescription = methodDescription;
        this.mutator = mutator;
        this.index = index;
        this.block = block;
        this.line = line;
    }

    public String getMutatedClass() {
        return mutatedClass;
    }

    public String getMutatedMethod() {
        return mutatedMethod;
    }

    public String methodDescription() {
        return methodDescription;
    }

    public String getMutator() {
        return mutator;
    }

    public int getIndex() {
        return index;
    }

    public int getBlock() {
        return block;
    }

    public int getLine() {
        return line;
    }

    public void setMutatedClass(String mutatedClass) {
        this.mutatedClass = mutatedClass;
    }

    public void setMutatedMethod(String mutatedMethod) {
        this.mutatedMethod = mutatedMethod;
    }

    public void setmethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    public void setMutator(String mutator) {
        this.mutator = mutator;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj.getClass() != this.getClass()) {
            return false;
        } else if (obj == this) {
            return true;
        }

        final MutantId other = (MutantId) obj;

        return this.index == other.index && this.block == other.block && this.line == other.line
                && this.mutator.equals(other.mutator) && this.mutatedClass.equals(other.mutatedClass)
                && this.mutatedMethod.equals(other.mutatedMethod)
                && this.methodDescription.equals(other.methodDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mutatedClass, mutatedMethod, mutator, methodDescription, index, block, line);
    }

    public String toString() {
        return String.format("Class: %s\nMethod: %s%s\nMutator: %s\n, Index: %d, Block: %d, Line: %d", mutatedClass,
                mutatedMethod, methodDescription, mutator, index, block, line);
    }
}