package com.cs453.group5.symbolic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that manages absolute path of a file or a directory.
 * 
 * 1. Naming convention
 * 
 * @DirPath - Path of a directory
 * @Path - Path of a file
 */
public class PathManager {
    private static final String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    private final String projectHome = System.getenv("CS453_PROJECT_HOME");
    private final String classDirPath = String.format("%s/target/classes", projectHome);
    private final String backupClassDirPath = String.format("%s/target/backup-classes", projectHome);
    private final String pitestBaseDirPath = String.format("%s/target/pit-reports", projectHome);
    private final String jbseResultsDirPath = String.format("%s/target/jbse-results/%s", projectHome, timestamp);
    private final String jbseLibPath = String.format("%s/res/jbse-0.10.0-SNAPSHOT-shaded.jar", projectHome);

    public Boolean isProjectHome() {
        final String currDir = System.getProperty("user.dir");
        return currDir.equals(projectHome) || currDir.equals(projectHome + "/") || (currDir + "/").equals(projectHome);
    }

    public Boolean checkPIT() {
        Path path = Paths.get(pitestBaseDirPath);
        return Files.exists(path);
    }

    public String getClassDirPath() {
        return this.classDirPath;
    }

    public String getClassPath(String classBinName) {
        String classPath = classBinNameToPath(classBinName);
        return String.format("%s/%s.class", classDirPath, classPath);
    }

    public String getBackupClassPath(String classBinName) {
        String classPath = classBinNameToPath(classBinName);
        return String.format("%s/%s.class", backupClassDirPath, classPath);
    }

    public String getRecentPitestReportPath() {
        final String command = String.format("cd %s && ls -d */ | grep 20", pitestBaseDirPath);
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

        return String.format("%s/%s/mutations.xml", pitestBaseDirPath, xmlDir);
    }

    public String getMutantsDirPath(String classBinName) {
        String classPath = classBinNameToPath(classBinName);
        return String.format("%s/export/%s/mutants", pitestBaseDirPath, classPath);
    }

    public String getMutantClassPath(String classBinName, int mutantNumber) {
        return String.format("%s/%d/%s.class", getMutantsDirPath(classBinName), mutantNumber, classBinName);
    }

    public String getJbseResultsDirPath() {
        return jbseResultsDirPath;
    }

    public String getJbseLibPath() {
        return jbseLibPath;
    }

    public String classBinNameToPath(String classBinName) {
        return classBinName.replace(".", "/");
    }
}