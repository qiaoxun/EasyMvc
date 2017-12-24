package com.test.util;

public class StringUtils {
	
	public static boolean isBlank(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * equals 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 != null) {
			return str1.equals(str2);
		}
		if (str2 != null) {
			return str2.equals(str1);
		}
		return false;
	}
	
	public static String cleanPath(String path) {
		Assert.notNull(path);
		path = path.trim();
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return path;
	}
	
	public static void main(String[] args) {
		System.out.println(cleanPath("ddd"));
	}
	
}
