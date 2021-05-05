package com.cs453.group5.examples;

import jbse.apps.run.RunParameters;
import jbse.apps.run.Run;

public class RunExample {
  public static void main(String[] args) {
    final String defaultClassBinaryName = "com/cs453/group5/examples/Calculator";
    final String defaultMethodSignature = "(I)I";
    final String defaultMethodName = "getSign";

    final RunParameters p = new RunParameters();

    set(
      p,
      args.length > 0 ? args[0] : defaultClassBinaryName,
      args.length > 1 ? args[1] : defaultMethodSignature,
      args.length > 2 ? args[2] : defaultMethodName
    );

    final Run r = new Run(p);
    r.run();
  }

  private static void set(
    RunParameters p,
    String classBinaryName,
    String methodSignature,
    String methodName
  ) {
    p.addUserClasspath("./target/classes");
    p.addUserClasspath("./target/pit-reports/export");
    p.setJBSELibPath("./res/jbse-0.10.0-SNAPSHOT-shaded.jar");

    p.setMethodSignature(classBinaryName, methodSignature, methodName);
    p.setDecisionProcedureType(RunParameters.DecisionProcedureType.Z3);
    p.setExternalDecisionProcedurePath("/root/bin/z3-4.8.10-x64-ubuntu-18.04/bin/z3");
    p.setStateFormatMode(RunParameters.StateFormatMode.TEXT);
    p.setStepShowMode(RunParameters.StepShowMode.LEAVES);
  }
}
