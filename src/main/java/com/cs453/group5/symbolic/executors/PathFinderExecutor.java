package com.cs453.group5.symbolic.executors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cs453.group5.symbolic.entities.MethodInfo;

public class PathFinderExecutor {
    public String findPathCond(String jbseResultPath, MethodInfo methodInfo) {
        String classPath = methodInfo.getClassBinName().getSlash();

        final String command = String.format("python3 parse-jbse-output/src/main.py -t \"%s\" -m \"%s:%s:%s:%s\"",
                jbseResultPath, classPath, methodInfo.getName(), methodInfo.getDescriptor(),
                String.join(":", methodInfo.getParamNames()));
        System.out.println(command);

        String result = runPathFinder(command).trim();

        if (result.equals("True")) {
            return "true";
        } else {
            return result;
        }
    }

    private String runPathFinder(String command) {
        String returnLine = null;

        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        try {
            Process process = processBuilder.start();

            BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            returnLine = outReader.readLine();

            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (returnLine == null || returnLine.isEmpty()) {
            throw new IllegalArgumentException("Python Path Finder returns nothing!");
        }

        return returnLine;
    }
}
