package com.cs453.group5.examples;

import jbse.apps.run.RunParameters;
import jbse.apps.run.Run;

import static jbse.apps.run.RunParameters.DecisionProcedureType.Z3;

import static jbse.apps.run.RunParameters.StateFormatMode.TEXT;
import static jbse.apps.run.RunParameters.StepShowMode.LEAVES;

public class RunExample {
  public static void main(String[] args) {
    final RunParameters p = new RunParameters();
    set(p);
    final Run r = new Run(p);
    r.run();
  }

  private static void set(RunParameters p) {
    // p.addUserClasspath("./target/classes");
    p.addUserClasspath("./target/pit-reports/export");
    p.setJBSELibPath("./src/main/java/com/cs453/group5/examples/jbse-0.10.0-SNAPSHOT-shaded.jar");
    // p.setMethodSignature("com/cs453/group5/examples/Calculator", "(I)I",
    // "isPositive");
    p.setMethodSignature("com/cs453/group5/examples/Calculator/mutants/2/Calculator", "(I)I", "isPositive");
    p.setDecisionProcedureType(Z3);
    p.setExternalDecisionProcedurePath("/opt/local/bin/z3");
    // p.setOutputFileName("./out/runIf_z3.txt");
    p.setStateFormatMode(TEXT);
    p.setStepShowMode(LEAVES);
  }
}
