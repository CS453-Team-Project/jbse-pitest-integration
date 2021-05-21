package com.cs453.group5.symbolic;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

public class PathFinderExecutor {
    public void execFinder(String classBinName, String methodName) throws SecurityException, ClassNotFoundException {
        Method[] declaredMethods = Class.forName(classBinName).getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getName().equals(methodName)) {
                findParameterName(method);
                break;
            }
        }
    }

    private void findParameterName(Method method) {
        Parameter[] parameters = method.getParameters();
        List<String> parameterNames = new ArrayList<String>();
        List<String> parameterDescriptors = new ArrayList<String>();

        for (Parameter parameter : parameters) {
            parameterDescriptors.add(Type.getDescriptor(parameter.getType()));

            if (!parameter.isNamePresent()) {
                throw new IllegalArgumentException("Parameter names are not present!");
            }

            String parameterName = parameter.getName();
            parameterNames.add(parameterName);
        }

        System.out.println(parameterNames.toString());
        System.out.println(parameterDescriptors.toString());
    }
}
