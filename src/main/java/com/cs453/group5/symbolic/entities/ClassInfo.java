package com.cs453.group5.symbolic.entities;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

public class ClassInfo {
    private ClassBinName binaryName;
    private Class classObj;
    private Method[] methods;

    public ClassInfo(ClassBinName classBinName) throws ClassNotFoundException {
        classObj = Class.forName(classBinName.getDot());

        binaryName = classBinName;
        methods = classObj.getMethods();
    }

    public ClassBinName getBinaryName() {
        return binaryName;
    }

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
