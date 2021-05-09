package com.cs453.group5.symbolic;

import jbse.apps.run.RunParameters;
import jbse.apps.run.Run;

public class JbseExecutor {
    public void runJbse(int id, String classBinaryName, String methodSignature, String methodName) {
        RunParameters p = new RunParameters();
        set(p, id, classBinaryName, methodSignature, methodName);

        Run r = new Run(p);
        r.run();
    }

    private void set(RunParameters p, int id, String classBinaryName, String methodSignature, String methodName) {
        p.addUserClasspath("./target/classes");
        p.setJBSELibPath("./res/jbse-0.10.0-SNAPSHOT-shaded.jar");

        p.setMethodSignature(classBinaryName, methodSignature, methodName);
        p.setDecisionProcedureType(RunParameters.DecisionProcedureType.Z3);
        p.setExternalDecisionProcedurePath("/opt/local/bin/z3");
        p.setOutputFilePath(String.format("./target/jbse-results/mutant%d.txt", id));
        p.setStateFormatMode(RunParameters.StateFormatMode.TEXT);
        p.setStepShowMode(RunParameters.StepShowMode.LEAVES);
    }
}