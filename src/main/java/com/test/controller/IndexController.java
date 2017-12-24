package com.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.annotation.Controller;
import com.test.annotation.RequestMapping;

@Controller
public class IndexController {
	@RequestMapping(value = "/index2.do")
	public String index(String name, HttpServletRequest request, HttpServletResponse response) {
		
		return "index";
	}
}
