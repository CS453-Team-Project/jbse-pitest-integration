package com.cs453.group5.examples;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ClassFile;

//Exceptions
import javassist.NotFoundException;
import javassist.CannotCompileException;
import java.io.IOException;

public class JavassistTutorial {
  public static void main(String[] args) {
    System.out.println("Hello World!");
    try {
      //Get target class file
      ClassPool pool =  ClassPool.getDefault();
      pool.importPackage("jbse.meta.Analysis.ass3rt");
      pool.insertClassPath("/root/jbse-pitest-integration/target/pit-reports/export/com/cs453/group5/examples/Calculator/mutants/2");
      CtClass cc = pool.get("com.cs453.group5.examples.Calculator"); // mutation line number 10
      
      //Insert Byte code, ass3rt(false) in target file
      CtMethod m = cc.getDeclaredMethod​("getSign");
      m.insertAt(10, true, "jbse.meta.Analysis.ass3rt(false);");

      //Write class file
      cc.writeFile();
    } catch (NotFoundException e){
      e.printStackTrace();
    } catch (CannotCompileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}