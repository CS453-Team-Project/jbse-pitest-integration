package com.cs453.group5.symbolic;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

//Exceptions
import javassist.NotFoundException;
import javassist.CannotCompileException;
import java.io.IOException;

public class MutantTransformer {
  private final String targetClass;
  private final String targetMethod;
  private final String saveDirPath;
  private final UserAssume userAssume;

  public MutantTransformer(String targetCalss, String targetMethod, String saveDirPath, UserAssume userAssume) {
    this.targetClass = targetCalss;
    this.targetMethod = targetMethod;
    this.saveDirPath = saveDirPath;
    this.userAssume = userAssume;
  }

  public void insertBytecode(int lineno, String command) {
    String userCommand = "true";

    // Get target class file
    ClassPool pool = ClassPool.getDefault();
    pool.importPackage("jbse.meta.Analysis.ass3rt");

    CtClass cc;
    CtMethod m;
    try {
      cc = pool.get(targetClass);
      m = cc.getDeclaredMethod(targetMethod);
    } catch (NotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("NotfoundException (javassist): " + targetClass);
    }

    // Insert Byte code, ass3rt(false) in target file
    try {
      m.insertAt(lineno, true, command);
      // Insert user custom assume option
      if (userAssume.getLine() != -1) {
        userCommand = String.format("jbse.meta.Analysis.assume(%s);", userAssume.getCommand());
        m.insertBefore(userCommand);
      }

      // Write class file
      cc.writeFile(this.saveDirPath);
      cc.detach();
    } catch (CannotCompileException e) {
      e.printStackTrace();
      System.err.println(String.format("comamnd: %s\nusercommand: %s", command, userCommand));
      throw new RuntimeException("Cannot insert: " + command + " in line number: " + Integer.toString(lineno));
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("IO Exception (javassist)");
    }

    System.out.println("Success");
  }

  public void insertBoth(int lineno, String condition, String command) {
    String userCommand = "true";

    // Get target class file
    ClassPool pool = ClassPool.getDefault();
    pool.importPackage("jbse.meta.Analysis.ass3rt");
    pool.importPackage("jbse.meta.Analysis.assume");

    CtClass cc;
    CtMethod m;
    try {
      cc = pool.get(targetClass);
      m = cc.getDeclaredMethod(targetMethod);
    } catch (NotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("NotfoundException (javassist): " + targetClass);
    }

    try {
      // Insert Byte code, ass3rt(false) in target file
      m.insertBefore(String.format("jbse.meta.Analysis.assume(%s);", condition));
      if (!command.equals("")) {
        m.insertAt(lineno + 1, true, command);
      }
      // Insert user custom assume option
      if (userAssume.getLine() != -1) {
        userCommand = String.format("jbse.meta.Analysis.assume(%s);", userAssume.getCommand());
        System.out.println(userCommand);
        m.insertBefore(userCommand);
      }

      // Write class file
      cc.writeFile(this.saveDirPath);
      cc.detach();
    } catch (CannotCompileException e) {
      e.printStackTrace();
      System.err.println(String.format("comamnd: %s\nusercommand: %s", command, userCommand));
      throw new RuntimeException("Cannot insert: " + command + " in line number: " + Integer.toString(lineno));
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("IO Exception (javassist)");
    }

    System.out.println("Success");
  }
}