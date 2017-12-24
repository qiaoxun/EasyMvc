package com.test.beans;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.test.annotation.Controller;
import com.test.annotation.RequestMapping;
import com.test.util.ClassUtils;
import com.test.util.StringUtils;
import com.test.util.bean.Constant;
import com.test.util.bean.EasyMvcXmlUtils;

public class BeanFactory {
	private final static Map<String, BeanHelper> beanMap = new HashMap<>();
	
	private final static Map<String, RequestMappingHelper> handlerMap = new LinkedHashMap<String, RequestMappingHelper>();
	
	public void init(String xmlFilePath) {
		Map<String, String> dataMap = EasyMvcXmlUtils.init(xmlFilePath);
		// 获取扫描包路径
		String scanBasePackage = dataMap.get(Constant.baseScanPackage);
		// 获取扫描包下所有类的 全类名
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		List<String> classFullNameList = getPackageClasses(scanBasePackage, loader, true);
		// 处理所有类
		resolveAllClasses(classFullNameList);
	}
	
	/**
	 * 获取扫描包下面的所有类
	 * @param packageName - com.test.controller
	 * @param loader
	 * @param scanChildrePackage
	 * @return
	 */
	public List<String> getPackageClasses(String packageName, ClassLoader loader, boolean scanChildrePackage) {
		List<String> classFullNameList = new ArrayList<>();
		String packagePath = packageName.replace(".", "/");
		URL url = loader.getResource(packagePath);
		if (url != null) {
			String type = url.getProtocol();
			
			if (!"file".equals(type)) {
				return classFullNameList;
			}
			
			String path = "";
			try {
				path = URLDecoder.decode(url.getPath(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			File file = new File(path);
			
			if (file.isDirectory()) {
				File[] childrenFiles = file.listFiles();
				for (File f : childrenFiles) {
					// 如果有子包，处理子包
					if (f.isDirectory() && scanChildrePackage) {
						String name = f.getName();
						packageName = packageName + "." + name;
						classFullNameList.addAll(getPackageClasses(packageName, loader, scanChildrePackage));
					} else {
						// Aaa.class
						String fileName = f.getName();
						// 判断是不是 .class 结尾的文件，只处理 .class 结尾的文件
						if (ClassUtils.checkIsClass(fileName)) {
							// Aaa
							String name = ClassUtils.getFileNameWithoutClass(fileName);
							// com.test.Aaa
							String classFullName = packageName + "." + name;
							classFullNameList.add(classFullName);
						}
					}
				}
			}
		}
		return classFullNameList;
	}
	
	/**
	 * 解析所有类
	 * @param classFullNameList
	 */
	public void resolveAllClasses(List<String> classFullNameList) {
		for (int i = 0, len = classFullNameList.size(); i < len ; i++) {
			String classFullName = classFullNameList.get(i);
			resolveOneClass(classFullName);
		}
	}
	
	/**
	 * 解析类
	 * @param classFullName
	 * @return
	 */
	public void resolveOneClass(String classFullName) {
		Class<?> clazz = ClassUtils.forName(classFullName);
		if (null == clazz) {
			return;
		}
		
		Annotation[] anArr = clazz.getAnnotations();
		for (Annotation an : anArr) {
			// 获取 Controller 注解
			if (an instanceof Controller) {
				// 解析 controller
				BeanHelper bh = resolveControllerAnnotation(an, clazz);
				// 如果是 controller 则需要处理路径和方法的对应
				resolveRequestMappingAnnotation(bh);
			}
		}
	}
	
	/**
	 * 解析 Controller 注解
	 */
	private BeanHelper resolveControllerAnnotation(Annotation an, Class<?> clazz) {
		Controller cont = (Controller)an;
		String name = cont.name();
		
		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		// class obj isSingleton isController
		BeanHelper bh = new BeanHelper(clazz, obj, true, true);
		
		if (StringUtils.isNotBlank(name)) {
			bh.setBeanName(name);
			beanMap.put(name, bh);
		} else {
			String nameWithClass = ClassUtils.getClassFileName(clazz);
			name = ClassUtils.getFileNameWithoutClass(nameWithClass);
			String beanName = name.substring(0, 1).toLowerCase() + name.substring(1);
			bh.setBeanName(beanName);
			beanMap.put(beanName, bh);
		}
		
		return bh;
	}
	
	/**
	 * 如果是 controller ，则需要 url 映射
	 * @param an
	 * @param clazz
	 */
	private void resolveRequestMappingAnnotation(BeanHelper bh) {
		Class<?> clazz = bh.getClazz();
		if (clazz.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
			String[] classPathArr = mapping.value();
			resolveMethodRequestMappingAnnotation(bh, classPathArr);
		} else {
			resolveMethodRequestMappingAnnotation(bh, null);
		}
	}
	
	/**
	 * 解析方法上的 RequestMapping 注解
	 * @param clazz
	 * @param classPathArr
	 */
	private void resolveMethodRequestMappingAnnotation(BeanHelper bh, String[] classPathArr) {
		Method[] methodArr = bh.getClazz().getMethods();
		for (Method m : methodArr) {
			if (m.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping mapping = m.getAnnotation(RequestMapping.class);
				String[] methodPathArr = mapping.value();
				dealPaths(bh, m, classPathArr, methodPathArr);
			}
		}
	}
	
	/**
	 * 处理路径
	 * @param bh
	 * @param method
	 * @param classPathArr
	 * @param methodPathArr
	 */
	private void dealPaths(BeanHelper bh, Method method, String[] classPathArr, String[] methodPathArr) {
		RequestMappingHelper rmHelper = new RequestMappingHelper(method, bh);
		if (null != classPathArr) {
			for (String classPath : classPathArr) {
				classPath = StringUtils.cleanPath(classPath);
				for (String methodPath : methodPathArr) {
					methodPath = StringUtils.cleanPath(methodPath);
					String fullPath = classPath + methodPath;
					handlerMap.put(fullPath, rmHelper);
				}
			}
		} else {
			for (String methodPath : methodPathArr) {
				methodPath = StringUtils.cleanPath(methodPath);
				handlerMap.put(methodPath, rmHelper);
			}
		}
	}
	
	/**
	 * 通过 beanName 获取 BeanHelper
	 * @param beanName
	 * @return
	 */
	public static BeanHelper getBeanByName(String beanName) {
		return beanMap.get(beanName);
	}
	
	/**
	 * 获取路径对应的 RequestMappingHelper
	 * @param path
	 * @return
	 */
	public static RequestMappingHelper getHandlerByPath(String path) {
		return handlerMap.get(path);
	}
	
}
