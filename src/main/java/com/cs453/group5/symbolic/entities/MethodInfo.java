package com.cs453.group5.symbolic.entities;

public class MethodInfo {
    private ClassBinName classBinName;
    private String methodName;
    private String descriptor;
    private String[] paramNames;

    public MethodInfo(ClassBinName classBinName, String methodName, String descriptor, String[] paramNames) {
        this.classBinName = classBinName;
        this.methodName = methodName;
        this.descriptor = descriptor;
        this.paramNames = paramNames;
    }

    public ClassBinName getClassBinName() {
        return classBinName;
    }

    public String getName() {
        return methodName;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String[] getParamNames() {
        return paramNames;
    }
}
