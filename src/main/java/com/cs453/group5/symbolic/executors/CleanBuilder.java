package com.cs453.group5.symbolic.executors;

import java.lang.ProcessBuilder.Redirect;

/**
 * CleanBuilder builds this project again using a mvn command.
 */
public class CleanBuilder {
    private final String command = "mvn clean test";

    /**
     * Build this project again. (mvn clean test)
     */
    public void cleanBuild() {
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command)
                .redirectError(Redirect.INHERIT);

        try {
            System.out.println("Clean Build");
            processBuilder.start().waitFor();
            System.out.println("Clean Build Complete");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Clean build failed.");
        }
    }
}
