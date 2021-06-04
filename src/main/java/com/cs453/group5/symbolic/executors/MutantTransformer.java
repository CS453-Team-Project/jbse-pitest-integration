package com.cs453.group5.symbolic.executors;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javassist.NotFoundException;
import javassist.CannotCompileException;
import java.io.IOException;
import java.util.List;

import com.cs453.group5.symbolic.entities.Assertion;
import com.cs453.group5.symbolic.entities.Assumption;
import com.cs453.group5.symbolic.entities.MethodInfo;

public class MutantTransformer {
  private final String classDirPath;

  public MutantTransformer(String classDirPath) {
    this.classDirPath = classDirPath;
  }

  public void insert(MethodInfo methodInfo, List<Assertion> assertions, List<Assumption> assumptions) {
    // Get target class file
    ClassPool pool = ClassPool.getDefault();
    pool.importPackage("jbse.meta.Analysis.ass3rt");
    pool.importPackage("jbse.meta.Analysis.assume");

    CtClass cc;
    CtMethod m;
    try {
      cc = pool.get(methodInfo.getClassBinName().getDot());
      m = cc.getDeclaredMethod(methodInfo.getName());
    } catch (NotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("NotfoundException (javassist): " + methodInfo.getClassBinName().getDot());
    }

    int debugInt = -1;
    String debugStr = "";
    try {
      for (Assertion assertion : assertions) {
        int line = assertion.getLine();
        String assertStr = assertion.getAssertion();

        debugInt = line;
        debugStr = assertStr;

        if (line == 0) {
          m.insertBefore(assertStr);
        } else {
          m.insertAt(line, assertStr);
        }
      }

      for (Assumption assumption : assumptions) {
        int line = assumption.getLine();
        String assumeStr = assumption.getAssumption();

        debugInt = line;
        debugStr = assumeStr;

        if (line == 0) {
          m.insertBefore(assumeStr);
        } else {
          m.insertAt(line, assumeStr);
        }
      }

      cc.writeFile(classDirPath);
      cc.detach();
    } catch (CannotCompileException e) {
      e.printStackTrace();
      System.err.println(String.format("line: %d\ncondition: %s", debugInt, debugStr));
      throw new RuntimeException("Cannot Insert");
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("IO Exception (javassist)");
    }

    System.out.println("Success");
  }
}