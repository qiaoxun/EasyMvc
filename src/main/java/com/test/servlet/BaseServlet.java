package com.test.servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.beans.BeanFactory;
import com.test.util.StringUtils;

public abstract class BaseServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		String xml = this.getInitParameter("xml");
		if (StringUtils.isBlank(xml)) {
			xml = "EasyMvc.xml";
		}
		URL url = BaseServlet.class.getClassLoader().getResource(xml);
		BeanFactory factory = new BeanFactory();
		factory.init(url.getPath());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDispatch(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	protected abstract void doDispatch(HttpServletRequest req, HttpServletResponse resp);
	
}
