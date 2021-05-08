package com.cs453.group5.symbolic;

import jbse.apps.run.RunParameters;
import jbse.apps.run.Run;

public class JbseExecutor {
    public void runJbse(String classBinaryName, String methodSignature, String methodName) {
        RunParameters p = new RunParameters();
        set(p, classBinaryName, methodSignature, methodName);

        Run r = new Run(p);
        r.run();
    }

    private void set(
        RunParameters p, 
        String classBinaryName, 
        String methodSignature, 
        String methodName
    ) {
        p.addUserClasspath("./");
        p.setJBSELibPath("./res/jbse-0.10.0-SNAPSHOT-shaded.jar");

        p.setMethodSignature(classBinaryName, methodSignature, methodName);
        p.setDecisionProcedureType(RunParameters.DecisionProcedureType.Z3);
        p.setExternalDecisionProcedurePath("/opt/local/bin/z3");
        p.setStateFormatMode(RunParameters.StateFormatMode.TEXT);
        p.setStepShowMode(RunParameters.StepShowMode.LEAVES);
    }
}