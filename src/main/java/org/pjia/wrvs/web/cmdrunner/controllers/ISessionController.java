package org.pjia.wrvs.web.cmdrunner.controllers;

import javax.servlet.http.HttpServletRequest;

import org.pjia.wrvs.web.cmdrunner.vo.LoginVO;

public interface ISessionController {
	
	/**
	 * 登录
	 * 
	 * @param request request
	 * @return
	 */
	public LoginVO login(HttpServletRequest request);
}
