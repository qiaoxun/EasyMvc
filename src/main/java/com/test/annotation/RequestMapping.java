package com.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
	/**
	 * path
	 * @return
	 */
	String[] value() default {};
	
	/**
	 * GET POST 
	 * @return
	 */
	RequestMethod[] method() default {};
	
}
