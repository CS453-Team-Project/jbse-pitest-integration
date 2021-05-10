package com.cs453.group5.symbolic;

import jbse.apps.run.RunParameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jbse.apps.run.Run;

public class JbseExecutor {
    private String mClassDirPath, mJbseLibPath, mOutputDirPath;

    public JbseExecutor(String classDirPath, String jbseLibPath, String outputDirPath) {
        mClassDirPath = classDirPath;
        mJbseLibPath = jbseLibPath;
        mOutputDirPath = outputDirPath;
    }

    public void runJbse(int id, String classBinaryName, String methodSignature, String methodName) {
        RunParameters p = new RunParameters();

        try {
            set(p, id, classBinaryName, methodSignature, methodName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Run r = new Run(p);
        r.run();
    }

    private void set(RunParameters p, int id, String classBinaryName, String methodSignature, String methodName)
            throws IOException {
        final String outputFilePathStr = String.format("%s/%s/mutant%d.txt", mOutputDirPath, classBinaryName, id);

        p.addUserClasspath(mClassDirPath);
        p.setJBSELibPath(mJbseLibPath);

        p.setMethodSignature(classBinaryName, methodSignature, methodName);
        p.setDecisionProcedureType(RunParameters.DecisionProcedureType.Z3);
        p.setExternalDecisionProcedurePath("/opt/local/bin/z3");
        p.setOutputFilePath(outputFilePathStr);
        p.setStateFormatMode(RunParameters.StateFormatMode.TEXT);
        p.setStepShowMode(RunParameters.StepShowMode.LEAVES);

        /* Create output file path if it does not exist. */
        Path outputFilePath = Paths.get(outputFilePathStr);
        if (!Files.exists(outputFilePath.getParent())) {
            Files.createDirectories(outputFilePath.getParent());
        }
    }
}