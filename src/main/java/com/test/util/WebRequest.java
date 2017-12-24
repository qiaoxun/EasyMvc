package com.test.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WebRequest {
	private final HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private HttpSession session;
	
	private String url = "";
	private String contentPath = "";
	private String uri = "";
	private String path = "";
	
	
	public WebRequest(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		// 初始化
		init();
	}
	
	private void init() {
		contentPath = request.getContextPath();// 项目名 "/SpringTest" 
		url = request.getRequestURL().toString(); // "http://localhost:8083/SpringTest/hello/param.do"
		uri = request.getRequestURI(); // "/SpringTest/hello/param.do"
		path = uri.replace(contentPath, ""); // "hello/param.do"
		
		session = request.getSession();
	}
	
	/**
	 * 路径没有找到
	 */
	public void pathNotFound() {
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write((path + " not found").getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取请求的参数值
	 * @param key
	 * @return
	 */
	public String getParameterValue(String key) {
		return this.request.getParameter(key);
	}
	
	
	/**
	 * 访问路径 "/" 开头
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public String getUrl() {
		return url;
	}

	public String getContentPath() {
		return contentPath;
	}

	public String getUri() {
		return uri;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return session;
	}

	
}
