package com.cs453.group5.symbolic.entities;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

/**
 * This class wraps Class class.
 * 
 * @see java.lang.Class
 */
public class ClassInfo {
    private ClassBinName binaryName;
    private Class classObj;
    private Method[] methods;

    public ClassInfo(ClassBinName classBinName) {
        try {
            classObj = Class.forName(classBinName.getDot());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Class not found: " + classBinName);
        }

        binaryName = classBinName;
        methods = classObj.getMethods();
    }

    /**
     * @return Binary name of the class.
     */
    public ClassBinName getBinaryName() {
        return binaryName;
    }

    /**
     * Get method information of the class. If the class does not contain the
     * method, then returns null
     * 
     * @param methodName
     * @return Method info for symbolic execution if there is a method. Otherwise,
     *         null.
     */
    public MethodInfo getMethodInfo(String methodName) {
        Function<Parameter, String> paramToDescriptor = (param) -> Type.getDescriptor(param.getType());
        Function<Parameter, String> paramToName = (param) -> param.getName();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                List<Parameter> params = Arrays.asList(method.getParameters());

                List<String> paramDescriptors = params.stream().map(paramToDescriptor).collect(Collectors.toList());
                String returnDescriptor = Type.getDescriptor(method.getReturnType());
                String descriptor = String.format("(%s)%s", String.join("", paramDescriptors), returnDescriptor);

                String[] paramNames = params.stream().map(paramToName).collect(Collectors.toList())
                        .toArray(new String[0]);

                return new MethodInfo(binaryName, methodName, descriptor, paramNames);
            }
        }

        return null;
    }
}
