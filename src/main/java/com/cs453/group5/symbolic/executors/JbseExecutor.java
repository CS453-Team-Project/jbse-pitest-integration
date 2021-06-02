package com.cs453.group5.symbolic.executors;

import jbse.apps.run.RunParameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cs453.group5.symbolic.entities.MethodInfo;

import jbse.apps.run.Run;

public class JbseExecutor {
    private String mClassDirPath, mJbseLibPath;

    public JbseExecutor(String classDirPath, String jbseLibPath) {
        mClassDirPath = classDirPath;
        mJbseLibPath = jbseLibPath;
    }

    public void runJbse(String outputPath, MethodInfo methodInfo) {
        RunParameters p = new RunParameters();

        try {
            set(p, outputPath, methodInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Run r = new Run(p);
        r.out("\\dev\\null");
        r.run();
    }

    private void set(RunParameters p, String outputPath, MethodInfo methodInfo) throws IOException {
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
            Files.createDirectories(outputFilePath.getParent());
        }
    }
}