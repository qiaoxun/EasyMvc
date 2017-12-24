package com.test.beans;

import java.lang.reflect.Method;

public class RequestMappingHelper {
	private Method method;
	
	private BeanHelper beanHelper;
	
	public RequestMappingHelper() {
	}
	
	public RequestMappingHelper(Method method, BeanHelper beanHelper) {
		super();
		this.method = method;
		this.beanHelper = beanHelper;
	}

	public BeanHelper getBeanHelper() {
		return beanHelper;
	}

	public void setBeanHelper(BeanHelper beanHelper) {
		this.beanHelper = beanHelper;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
}
