package com.cs453.group5.examples;

import static jbse.meta.Analysis.assume;

public class Parenthese {
    public int check(char[] arr) {
      assume(arr.length <= 2);
      int cnt = 0;
      for(int i = 0; i < arr.length; i++){
          if(arr[i] == '(')   
            cnt = cnt + 1;
        else 
            cnt = cnt - 1;
        if(cnt < 0){              
            return 0;
        }
      }   

      if(cnt == 0) 
          return 1;
      else
          return 0; 
    }
}
