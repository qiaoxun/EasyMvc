package com.test.beans;

public class BeanHelper {
	private Class<?> clazz;
	private Object obj;
	private boolean isSingleton = true;
	private boolean isController = false;
	private String beanName;
	
	public BeanHelper(Class<?> clazz, Object obj, boolean isSingleton, boolean isController) {
		this.clazz = clazz;
		this.obj = obj;
		this.isSingleton = isSingleton;
		this.isController = isController;
	}

	public BeanHelper(Class<?> clazz, Object obj) {
		this.clazz = clazz;
		this.obj = obj;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public boolean isSingleton() {
		return isSingleton;
	}
	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}
	public boolean isController() {
		return isController;
	}
	public void setController(boolean isController) {
		this.isController = isController;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
}
