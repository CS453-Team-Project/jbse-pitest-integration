package com.cs453.group5.symbolic.entities;

/**
 * This class contains information about the method.
 * 
 * @see ClassInfo
 */
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

    /**
     * @return Binary name of the class that contains this method.
     */
    public ClassBinName getClassBinName() {
        return classBinName;
    }

    /**
     * @return method name
     */
    public String getName() {
        return methodName;
    }

    /**
     * 
     * @return descriptor of the method
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * 
     * @return An array of parameter names
     */
    public String[] getParamNames() {
        return paramNames;
    }

    /**
     * 
     * @return Method dump string
     */
    public String dumpString() {
        return String.format("%s:%s:%s:%s", classBinName.getSlash(), methodName, descriptor,
                String.join(":", paramNames));
    }
}
