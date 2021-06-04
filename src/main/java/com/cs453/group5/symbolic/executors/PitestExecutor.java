package com.cs453.group5.symbolic.executors;

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

    /**
     * Run putest
     */
    public void runPitest() {
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command)
                .redirectError(Redirect.INHERIT);

        try {
            System.out.println("Running PIT");
            processBuilder.start().waitFor();
            System.out.println("PIT Complete");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PIT failed");
        }
    }

    /**
     * Check whether the exists a pit report. If this returns false, you may want to
     * run pitest.
     * 
     * @see #runPitest()
     * @return true if the project already has a pit report. Otherwise, false.
     */
    public Boolean pitReportExists() {
        Path path = Paths.get(pitReportPath);
        return Files.exists(path);
    }
}
