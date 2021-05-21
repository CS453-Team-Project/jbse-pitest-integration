package com.cs453.group5.symbolic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

public class PathFinderExecutor {
    private PathManager pathManager = new PathManager();

    public PathFinderExecutor(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public String execFinder(String classBinName, String methodName, int mutantNumber)
            throws SecurityException, ClassNotFoundException {
        Method[] declaredMethods = Class.forName(classBinName).getDeclaredMethods();
        MethodInfo methodInfo = null;

        for (Method method : declaredMethods) {
            if (method.getName().equals(methodName)) {
                methodInfo = findParameterName(method);
            }
        }

        if (methodInfo == null) {
            throw new IllegalArgumentException("Method info null!");
        }

        String classPath = pathManager.classBinNameToPath(classBinName);
        String textPath = String.format("%s/%s/mutant%d.txt", pathManager.getJbseResultsDirPath(), classPath,
                mutantNumber);

        // Run python code
        final String command = String.format("python3 parse-jbse-output/src/main.py -t \"%s\" -m \"%s:%s:%s:%s\"",
                textPath, classPath, methodName, methodInfo.descriptor, String.join(":", methodInfo.parameterNames));
        String result = runPythonFinder(command).trim();
        if (result.equals("True")) {
            return "true";
        } else {
            return result;
        }
    }

    private String runPythonFinder(String command) {
        String returnLine = null;

        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        try {
            Process process = processBuilder.start();

            BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = outReader.readLine()) != null) {
                returnLine = line;
            }

            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (returnLine == null) {
            throw new IllegalArgumentException("Python Path Finder returns nothing!");
        }

        return returnLine;
    }

    private MethodInfo findParameterName(Method method) {
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

        String inputDescriptor = String.join("", parameterDescriptors);
        String outputDescriptor = Type.getDescriptor(method.getReturnType());
        String descriptor = String.format("(%s)%s", inputDescriptor, outputDescriptor);

        String[] parameterNamesArr = parameterNames.toArray(new String[parameterNames.size()]);

        return new MethodInfo(descriptor, parameterNamesArr);
    }

    private class MethodInfo {
        String descriptor;
        String[] parameterNames;

        MethodInfo(String descriptor, String[] parameterNames) {
            this.descriptor = descriptor;
            this.parameterNames = parameterNames;
        }
    }
}
