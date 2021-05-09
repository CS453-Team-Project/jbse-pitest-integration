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

  public void inertBytecode(int lineno, String command) {
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
      cc.defrost();

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