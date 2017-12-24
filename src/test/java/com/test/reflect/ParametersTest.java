package com.test.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.test.util.MethodParameterUtil;

public class ParametersTest {
	
	public static void main(String[] args) {
		Class<?> clazz = ParametersTest.class;
		
		try {
			
			Method[] mArr = clazz.getMethods();
			
			Method m = null;
			
			for (Method one : mArr) {
				if (one.getName().equals("handler")) {
					m = one;
					break;
				}
			}
			
			String[] methodParam = MethodParameterUtil.getMethodParameters(m, clazz);
			
			System.out.println(Arrays.toString(methodParam));
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void handler(String str1, String str2, List<String> list, int i) {
		System.out.println("handler");
	}
}
