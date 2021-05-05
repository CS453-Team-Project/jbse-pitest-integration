package com.cs453.group5.examples;

import static jbse.meta.Analysis.ass3rt;
import java.util.Scanner;
import static jbse.meta.Analysis.assume;

public class Calculator {
    public int nested_if(int[] arr, boolean[] dummy) {
        int result = 0;

        assume(dummy.length==1);
        // int[] arr = {2, 4, 6, 8};
        System.out.println(dummy[0]);


        int iter = arr.length;
        assume(iter<3);
        for(int q=0;q<iter;q++) {
			long n=arr[q];
			int flag=0;
			for(int i=1;i<Math.sqrt(100);i++) {
				if(n==2*i*i||n==4*i*i) {
					flag=1;
					break;
				}
			}
			if(flag==1) {
				System.out.println("YES");
			}
			else System.out.println("NO");
		}
        // ass3rt(result == 1);
        return result;
    }
}
