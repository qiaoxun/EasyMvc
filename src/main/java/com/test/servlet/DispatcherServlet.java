package com.test.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.adapter.HandlerAdapter;
import com.test.beans.BeanFactory;
import com.test.beans.RequestMappingHelper;
import com.test.util.WebRequest;
import com.test.util.bean.Constant;

public class DispatcherServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
		WebRequest webRequest = new WebRequest(req, resp);
		
		System.out.println(this + " == " + Thread.currentThread().getName());

		RequestMappingHelper rmh = BeanFactory.getHandlerByPath(webRequest.getPath());

		if (null == rmh) {
			webRequest.pathNotFound();
			return;
		}

		Object obj = HandlerAdapter.handler(webRequest, rmh);

		// if there is no returning value, do nothing
		if (null != obj && obj instanceof String) {
			try {
				req.getRequestDispatcher(Constant.prefix + String.valueOf(obj) + Constant.suffix).forward(req, resp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
