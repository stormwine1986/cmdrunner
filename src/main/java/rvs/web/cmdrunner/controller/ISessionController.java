package rvs.web.cmdrunner.controller;

import javax.servlet.http.HttpServletRequest;

public interface ISessionController {
	
	/**
	 * 登录
	 * 
	 * @param request request
	 * @return
	 */
	public LoginVO login(HttpServletRequest request);
}
