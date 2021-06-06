package com.cs453.group5.symbolic.executors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.cs453.group5.symbolic.entities.MethodInfo;
import com.cs453.group5.symbolic.exceptions.IllegalPathFinderOutputException;

public class PathFinderExecutor {
    private String killReportPath;

    public PathFinderExecutor(String killReportPath) {
        this.killReportPath = killReportPath;
    }

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
        if (pathList.length == 0) {
            throw new IllegalPathFinderOutputException();
        }

        final String command = String.format("python3 parse-jbse-output/src/main.py -a parse -p %s -m %d",
                jbseMethodPath, mutantId);
        String[] output = runPathFinder(command);
        String result = parsePathCondDeprecated1(output);

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

        final String command = String.format("python3 parse-jbse-output/src/main.py -a kill -p %s -m %d",
                jbseMethodPath, mutantId);
        String[] output = runPathFinder(command);
        String outputStr = String.join("\n", output);

        System.out.println(outputStr);
        try {
            BufferedWriter pathWriter = new BufferedWriter(new FileWriter(killReportPath, true));

            pathWriter.write(methodInfo.dumpString() + "\n" + outputStr + "\n\n");
            pathWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException");
        }
    }

    public void clearKillReport() {
        try {
            BufferedWriter clear = new BufferedWriter(new FileWriter(killReportPath));
            clear.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException");
        }
    }

    private String parsePathCondDeprecated1(String[] output) {
        String result = null;

        if (output.length > 1) {
            List<String> conditions = IntStream.range(0, output.length).filter((i) -> i % 2 == 1)
                    .mapToObj((i) -> output[i]).map((str) -> str.equals("") || str.equals("True") ? "true" : str)
                    .map((str) -> "(" + str + ")").collect(Collectors.toList());
            result = String.join("||", conditions);
        }

        if (result == null) {
            throw new IllegalPathFinderOutputException();
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
