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
    public String findPathCond(String jbseMethodPath, MethodInfo methodInfo, int mutantId) {
        makeMethodsTxt(methodInfo, jbseMethodPath);
        File dir = new File(jbseMethodPath + "/mutants/" + mutantId);
        File[] pathList = dir.listFiles();
        ArrayList<String> condition = new ArrayList<String>();

        if (pathList.length == 0) {
            throw new IllegalPathFinderOutputException();
        }

        for (int i = 0; i < pathList.length; i++) {
            final String command = String.format(
                    "python3 parse-jbse-output/src/main.py -a parse -p %s -m %d -f \"path%d.txt\"", jbseMethodPath,
                    mutantId, i);
            System.out.println(command);
            String[] output = runPathFinder(command);
            String result = parsePathCondDeprecated1(output);

            condition.add(result);
        }
        String result = String.join(" || ", condition);
        System.out.println(result);
        return result;
    }

    public void findKillCond(String jbseMethodPath, MethodInfo methodInfo, int mutantId) {
        makeMethodsTxt(methodInfo, jbseMethodPath);
        File dir = new File(jbseMethodPath + "/mutants/" + mutantId);
        File[] pathList = dir.listFiles();

        if (pathList.length == 0) {
            throw new IllegalPathFinderOutputException();
        }

        for (int i = 0; i < pathList.length; i++) {
            final String command = String.format(
                    "python3 parse-jbse-output/src/main.py -a kill -p %s -m %d -f \"path%d.txt\"", jbseMethodPath,
                    mutantId, i);
            System.out.println(command);
            String[] output = runPathFinder(command);
            System.out.println(String.join("\n", output));

            try {
                BufferedWriter pathWriter = new BufferedWriter(
                        new FileWriter("/root/jbse-pitest-integration/kill_cond.txt", true));

                pathWriter.write(String.join("\n", output) + "\n\n");
                pathWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        if (result.equals("True")) {
            return "true";
        }

        return String.format("(%s)", result);
    }

    private String[] runPathFinder(String command) {
        List<String> result = new ArrayList<>();

        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        try {
            Process process = processBuilder.start();

            BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = outReader.readLine()) != null) {
                result.add(line);
            }

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
