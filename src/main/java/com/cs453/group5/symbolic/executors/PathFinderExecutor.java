package com.cs453.group5.symbolic.executors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cs453.group5.symbolic.entities.MethodInfo;
import com.cs453.group5.symbolic.exceptions.IllegalPathFinderOutputException;

public class PathFinderExecutor {
    /**
     * Find path condition of a method. A jbse report path and a method ibfo should
     * be provided.
     * 
     * @param jbseResultPath
     * @param methodInfo
     * @return JAVA syntax condition of certain path
     */
    public String findPathCond(String jbseMethodPath, MethodInfo methodInfo) {
        // TODO: Change command building
        makeMethodsTxt(methodInfo, jbseMethodPath);

        final String command = String.format("python3 parse-jbse-output/src/main.py -t \"%s\" -m \"%s\"",
                jbseMethodPath, methodInfo.dumpString());
        System.out.println(command);

        String[] output = runPathFinder(command);
        String result = parsePathCondDeprecated1(output);

        if (result.equals("True")) {
            return "true";
        } else {
            return result;
        }
    }

    // TODO: new method: Kill Mutant

    private String parsePathCondDeprecated1(String[] output) {
        String result = null;

        if (output.length > 0) {
            result = output[0];
        }

        if (result == null || result.isEmpty()) {
            throw new IllegalPathFinderOutputException();
        }

        return result;
    }

    private String[] runPathFinder(String command) {
        List<String> result = new ArrayList<>();

        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        try {
            Process process = processBuilder.start();

            BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            result.add(outReader.readLine());

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(command);
            throw new RuntimeException("Python path finder execution failed");
        }

        return result.toArray(new String[0]);
    }

    private void makeMethodsTxt(MethodInfo methodInfo, String path) {
        String methodsPath = path + "/methods.txt";

        File txtFile = new File(methodsPath);

        if (!txtFile.exists()) {
            try {
                BufferedWriter pathWriter = new BufferedWriter(new FileWriter(txtFile));
                pathWriter.write(methodInfo.dumpString());
                pathWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("IOException while writing " + methodsPath);
            }
        }
    }
}
