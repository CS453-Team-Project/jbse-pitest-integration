package com.cs453.group5.symbolic;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ClassFile;

//Exceptions
import javassist.NotFoundException;
import javassist.CannotCompileException;
import java.io.IOException;

public class MutantTransformer {
  private static String targetClass;
  private static String classPath;
  private static String targetMethod;

  public MutantTransformer(
    String targetCalss, 
    String classPath, 
    String targetMethod
  ) {
    this.targetClass = targetCalss;
    this.classPath = classPath;
    this.targetMethod = targetMethod;
  }
  
  public static void inertBytecode(int lineno, String command) {
    try {
      //Get target class file
      ClassPool pool =  ClassPool.getDefault();
      pool.importPackage("jbse.meta.Analysis.ass3rt");
      pool.insertClassPath(targetMethod);
      CtClass cc = pool.get(targetClass);
      
      //Insert Byte code, ass3rt(false) in target file
      CtMethod m = cc.getDeclaredMethod​(targetMethod);
      m.insertAt(lineno, true, command);

      //Write class file
      cc.writeFile();

      System.out.println("Success");
    } catch (NotFoundException e){
      e.printStackTrace();
    } catch (CannotCompileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}