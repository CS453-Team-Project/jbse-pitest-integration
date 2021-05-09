package com.cs453.group5.symbolic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class that manages absolute path of a file or a directory.
 */
public class PathManager {
    private final String projectHome = System.getenv("CS453_PROJECT_HOME");
    private final String pitestBasePath = String.format("%s/target/pit-reports", projectHome);
    private final String targetClassPath = String.format("%s/target/classes", projectHome);

    public Boolean isProjectHome() {
        final String currDir = System.getProperty("user.dir");
        return currDir.equals(projectHome);
    }

    public Boolean checkPIT() {
        Path path = Paths.get(pitestBasePath);
        return Files.exists(path);
    }

    public String getMutantsDirPath(String classBinName) {
        String classPath = classBinNameToPath(classBinName);
        return String.format("%s/export/%s/mutants", pitestBasePath, classPath);
    }

    public String getRecentPitestReportPath() {
        final String command = String.format("cd %s && ls -d */ | grep 20", pitestBasePath);
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        String xmlDir = "";

        try {
            Process process = processBuilder.start();

            BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = outReader.readLine()) != null) {
                xmlDir = line;
            }

            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.format("%s/%s/mutations.xml", pitestBasePath, xmlDir);
    }

    public String getTargetClassDirPath() {
        return this.targetClassPath;
    }

    public String getBackupClassPath(String classBinName) {
        String classPath = classBinNameToPath(classBinName);
        return String.format("%s/target/backup-classes/%s.class", projectHome, classPath);
    }

    public String getClassPath(String classBinName) {
        String classPath = classBinNameToPath(classBinName);
        return String.format("%s/target/classes/%s.class", projectHome, classPath);
    }

    private String classBinNameToPath(String classBinName) {
        return classBinName.replace(".", "/");
    }
}