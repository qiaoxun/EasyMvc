package com.test.adapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.beans.RequestMappingHelper;
import com.test.util.MethodParameterUtil;
import com.test.util.WebRequest;
import com.test.util.bean.BeanUtils;

public class HandlerAdapter {
	
	public static Object handler(WebRequest webRequest, RequestMappingHelper rmh) {
		String[] parametersNameArr = null;
		Method method = rmh.getMethod();
		Class<?> hanlderClazz = rmh.getBeanHelper().getClazz();
		Class<?>[] clazzArr = method.getParameterTypes();
		Object[] parametersArr = new Object[clazzArr.length];
		for (int i = 0, len = clazzArr.length; i < len; i++) {
			Class<?> paramClazz = clazzArr[i];
			if (HttpServletResponse.class.isAssignableFrom(paramClazz)) {
				parametersArr[i] = webRequest.getResponse();
			} else if (HttpServletRequest.class.isAssignableFrom(paramClazz)) {
				parametersArr[i] = webRequest.getRequest();
			} else if (Map.class.isAssignableFrom(paramClazz)) {
				parametersArr[i] = webRequest.getRequest().getParameterMap();
			} else if (BeanUtils.isSimpleProperty(paramClazz)) {
				if (null == parametersNameArr) {
					parametersNameArr = MethodParameterUtil.getMethodParameters(method, hanlderClazz);
				}
				// 参数的名称
				String paramName = parametersNameArr[i];
				// 从 request 的请求参数中获取值
				String paramValue = webRequest.getParameterValue(paramName);
				parametersArr[i] = convertIfNecessary(paramClazz, paramValue);
			} else {
//				TODO
			}
		}
		
		Object obj = null;
		
		// do the real invoke
		try {
			obj = method.invoke(rmh.getBeanHelper().getObj(), parametersArr);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	private static Object convertIfNecessary(Class<?> paramClazz, String paramValue) {
		if (Integer.class.isAssignableFrom(paramClazz) || int.class.isAssignableFrom(paramClazz)) {
			return Integer.valueOf(paramValue);
		} else if (Double.class.isAssignableFrom(paramClazz) || double.class.isAssignableFrom(paramClazz)) {
			return Double.valueOf(paramValue);
		} else if (Float.class.isAssignableFrom(paramClazz) || float.class.isAssignableFrom(paramClazz)) {
			return Float.valueOf(paramValue);
		} else if (Long.class.isAssignableFrom(paramClazz) || long.class.isAssignableFrom(paramClazz)) {
			return Long.valueOf(paramValue);
		} else if (Short.class.isAssignableFrom(paramClazz) || short.class.isAssignableFrom(paramClazz)) {
			return Short.valueOf(paramValue);
		} else if (Boolean.class.isAssignableFrom(paramClazz) || boolean.class.isAssignableFrom(paramClazz)) {
			return Boolean.valueOf(paramValue);
		} else if (Character.class.isAssignableFrom(paramClazz) || char.class.isAssignableFrom(paramClazz)) {
//			TODO
//			return Character.
		} else if (Byte.class.isAssignableFrom(paramClazz) || byte.class.isAssignableFrom(paramClazz)) {
			return Byte.valueOf(paramValue);
		}
		
		return paramValue;
	}
	
}
