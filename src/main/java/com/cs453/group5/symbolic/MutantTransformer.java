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

  public MutantTransformer(String targetCalss, String targetMethod, String saveDirPath) {
    this.targetClass = targetCalss;
    this.targetMethod = targetMethod;
    this.saveDirPath = saveDirPath;
  }

  public void insertBytecode(int lineno, String command) {
    try {
      // Get target class file
      ClassPool pool = ClassPool.getDefault();
      pool.importPackage("jbse.meta.Analysis.ass3rt");
      CtClass cc = pool.get(targetClass);

      // Insert Byte code, ass3rt(false) in target file
      CtMethod m = cc.getDeclaredMethod(targetMethod);
      m.insertAt(lineno, true, command);

      // Write class file
      cc.writeFile(this.saveDirPath);
      cc.detach();

      System.out.println("Success");
    } catch (NotFoundException e) {
      e.printStackTrace();
    } catch (CannotCompileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void insertBoth(int lineno, String condition, String command) {
    try {
      // Get target class file
      ClassPool pool = ClassPool.getDefault();
      pool.importPackage("jbse.meta.Analysis.ass3rt");
      pool.importPackage("jbse.meta.Analysis.assume");
      CtClass cc = pool.get(targetClass);

      // Insert Byte code, ass3rt(false) in target file
      CtMethod m = cc.getDeclaredMethod(targetMethod);
      m.insertBefore(String.format("jbse.meta.Analysis.assume(%s);", condition));
      if (!command.equals("")) {
        m.insertAt(lineno + 1, true, command);
      }
      // Write class file
      cc.writeFile(this.saveDirPath);
      cc.detach();

      System.out.println("Success");
    } catch (NotFoundException e) {
      e.printStackTrace();
    } catch (CannotCompileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}