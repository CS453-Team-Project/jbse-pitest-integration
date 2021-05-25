package com.cs453.group5.symbolic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JbseResultParser {
  private String jbseResultsDirPath;

  public JbseResultParser(String jbseResultsDirPath) {
    this.jbseResultsDirPath = jbseResultsDirPath;
  }

  public static Boolean isStartingPoint(String st) {
    if (st.isEmpty()) {
      return false;
    }
    return st.toCharArray()[0] == '.';
  }

  public static Boolean isViolated(String st, boolean violation) {
    if (violation) {

      return st.contains("path violates an assertion.");
    } else {
      return st.contains("path is safe.");
    }
  }

  public void extract(int id, String classBinaryName, boolean violation) {
    String targetPath = String.format("%s/%s/mutant%d.txt", jbseResultsDirPath, classBinaryName, id);

    try {
      File file = new File(targetPath);
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

      List<String> readData = new ArrayList<String>();

      StringBuffer stringBuffer = new StringBuffer();

      String line;
      Boolean start = false;

      while ((line = bufferedReader.readLine()) != null) {
        if (isStartingPoint(line)) {
          if (start) {
            if (isViolated(line, violation)) {
              start = false;
              stringBuffer.append(line + "\n\n");
              System.out.println(stringBuffer.toString());
              readData.add(stringBuffer.toString());
            }
            stringBuffer.delete(0, stringBuffer.length());
          } else {
            start = true;
          }
        }

        if (start) {
          stringBuffer.append(line + '\n');
        }
      }

      // clearing the file
      BufferedWriter clear = new BufferedWriter(new FileWriter(file));
      clear.close();

      // write the violation data
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
      for (String data : readData) {
        bufferedWriter.write(data.toString());
      }

      // close the buffer
      bufferedWriter.close();
      bufferedReader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}