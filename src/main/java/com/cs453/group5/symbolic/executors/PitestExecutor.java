package com.cs453.group5.symbolic.executors;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PitestExecutor {
    private final String command = "mvn org.pitest:pitest-maven:mutationCoverage -Dfeatures=+EXPORT";
    private String pitReportPath;

    public PitestExecutor(String pitReportPath) {
        this.pitReportPath = pitReportPath;
    }

    public int runPitest() {
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command)
                .redirectError(Redirect.INHERIT);

        try {
            System.out.println("Running PIT");

            Process process = processBuilder.start();
            int exitcode = process.waitFor();

            System.out.println("PIT Complete");

            return exitcode;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Boolean pitReportExists() {
        Path path = Paths.get(pitReportPath);
        return Files.exists(path);
    }
}
