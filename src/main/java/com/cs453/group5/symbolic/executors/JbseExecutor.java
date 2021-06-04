package com.cs453.group5.symbolic.executors;

import jbse.apps.run.RunParameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cs453.group5.symbolic.entities.MethodInfo;

import jbse.apps.run.Run;

/**
 * Jbse executor executes the jbse.
 */
public class JbseExecutor {
    private String mClassDirPath, mJbseLibPath;

    public JbseExecutor(String classDirPath, String jbseLibPath) {
        mClassDirPath = classDirPath;
        mJbseLibPath = jbseLibPath;
    }

    /**
     * Run jbse for the specific method. We can set a path of the jbse report.
     * MethodInfo should contain the info about the method that you want to apply a
     * symbolic execution.
     * 
     * @param outputPath
     * @param methodInfo
     */
    public void runJbse(String outputPath, MethodInfo methodInfo) {
        RunParameters p = new RunParameters();
        set(p, outputPath, methodInfo);

        Run r = new Run(p);
        r.out("\\dev\\null");
        r.run();
    }

    private void set(RunParameters p, String outputPath, MethodInfo methodInfo) {
        p.addUserClasspath(mClassDirPath);
        p.setJBSELibPath(mJbseLibPath);

        p.setMethodSignature(methodInfo.getClassBinName().getSlash(), methodInfo.getDescriptor(), methodInfo.getName());
        p.setDecisionProcedureType(RunParameters.DecisionProcedureType.Z3);
        p.setExternalDecisionProcedurePath("/opt/local/bin/z3");
        p.setOutputFilePath(outputPath);
        p.setStateFormatMode(RunParameters.StateFormatMode.TEXT);
        p.setStepShowMode(RunParameters.StepShowMode.LEAVES);

        /* Create output file path if it does not exist. */
        Path outputFilePath = Paths.get(outputPath);
        if (!Files.exists(outputFilePath.getParent())) {
            try {
                Files.createDirectories(outputFilePath.getParent());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Creating jbse output file path failed");
            }
        }
    }
}