package com.cs453.group5.examples;

import java.util.*;
import static jbse.meta.Analysis.ass3rt;

public class Calculator {
    public void getSign() {
        Scanner input = new Scanner(System.in);
		int t = input.nextInt();
		for(int test = 0; test < t; test++){
			int n = input.nextInt();
			int l = input.nextInt();
			int r = input.nextInt();
			int[] c = new int[n + 1];
			int count = 0;
			for(int i = 0; i < l; i++){
				c[input.nextInt()]++;
			}
			for(int i = 0; i < r; i++){
				c[input.nextInt()]--;
			}
		}
    }
}
