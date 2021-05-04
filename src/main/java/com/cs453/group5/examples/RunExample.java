package com.cs453.group5.examples;

import jbse.apps.run.RunParameters;
import jbse.apps.run.Run;

import static jbse.apps.run.RunParameters.DecisionProcedureType.Z3;

import static jbse.apps.run.RunParameters.StateFormatMode.TEXT;
import static jbse.apps.run.RunParameters.StepShowMode.LEAVES;

public class RunExample {
  public static void main(String[] args) {
    final RunParameters p = new RunParameters();
    final String defaultClassBinaryName = "com/cs453/group5/examples/Calculator";

    set(p, args.length > 0 ? args[0] : defaultClassBinaryName);

    final Run r = new Run(p);
    r.run();
  }

  private static void set(RunParameters p, String classBinaryName) {
    p.addUserClasspath("./target/classes");
    p.addUserClasspath("./target/pit-reports/export");
    p.setJBSELibPath("./res/jbse-0.10.0-SNAPSHOT-shaded.jar");

    p.setMethodSignature(classBinaryName, "(I)I", "isPositive");
    p.setDecisionProcedureType(Z3);
    p.setExternalDecisionProcedurePath("/opt/local/bin/z3");
    p.setStateFormatMode(TEXT);
    p.setStepShowMode(LEAVES);
  }
}
