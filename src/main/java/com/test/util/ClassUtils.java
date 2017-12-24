package com.test.util;

import java.util.HashMap;
import java.util.Map;

public class ClassUtils {
	
	/**
	 * Map with primitive wrapper type as key and corresponding primitive
	 * type as value, for example: Integer.class -> int.class.
	 */
	private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);
	
	static {
		primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
		primitiveWrapperTypeMap.put(Byte.class, byte.class);
		primitiveWrapperTypeMap.put(Character.class, char.class);
		primitiveWrapperTypeMap.put(Double.class, double.class);
		primitiveWrapperTypeMap.put(Float.class, float.class);
		primitiveWrapperTypeMap.put(Integer.class, int.class);
		primitiveWrapperTypeMap.put(Long.class, long.class);
		primitiveWrapperTypeMap.put(Short.class, short.class);
	}
	
	public static String getClassFileName(Class<?> clazz) {
		Assert.notNull(clazz);
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(".");
		return className.substring(lastDotIndex + 1) + ".class";
	}
	
	/**
	 * 获取类名 比如 Aaa.class -> Aaa
	 * @param className
	 * @return
	 */
	public static String getFileNameWithoutClass(String className) {
		Assert.notNull(className);
		int lastDotIndex = className.lastIndexOf(".");
		return className.substring(0, lastDotIndex);
	}
	
	/**
	 * forName
	 * @param classFullName
	 * @return
	 */
	public static Class<?> forName(String classFullName) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(classFullName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	/**
	 * 判断名称后缀名是不是 class
	 * @param name
	 * @return
	 */
	public static boolean checkIsClass(String name) {
		int lastDotIndex = name.lastIndexOf(".");
		if (lastDotIndex < 0) {
			return false;
		}
		String suffix = name.substring(lastDotIndex + 1);
		return StringUtils.equals(suffix, "class");
	}

	/**
	 * Check if the given class represents a primitive (i.e. boolean, byte,
	 * char, short, int, long, float, or double) or a primitive wrapper
	 * (i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double).
	 * @param clazz the class to check
	 * @return whether the given class is a primitive or primitive wrapper class
	 */
	public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * Check if the given class represents an array of primitive wrappers,
	 * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
	 * @param clazz the class to check
	 * @return whether the given class is a primitive wrapper array class
	 */
	public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
	}

	/**
	 * Check if the given class represents a primitive wrapper,
	 * i.e. Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
	 * @param clazz the class to check
	 * @return whether the given class is a primitive wrapper class
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return primitiveWrapperTypeMap.containsKey(clazz);
	}
	
	public static void main(String[] args) {
	}
	
}
