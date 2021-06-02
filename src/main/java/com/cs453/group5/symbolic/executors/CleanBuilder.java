package com.cs453.group5.symbolic.executors;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class CleanBuilder {
    private final String command = "mvn clean test";

    public int cleanBuild() {
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command)
                .redirectError(Redirect.INHERIT);

        try {
            System.out.println("Clean Build");

            Process process = processBuilder.start();
            int exitcode = process.waitFor();

            System.out.println("Clean Build Complete");

            return exitcode;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
