package rvs.web.helper.controller;

import javax.servlet.http.HttpServletRequest;

import rvs.web.helper.vo.LoginVO;

public interface ISessionController {
	
	/**
	 * 登录
	 * 
	 * @param request request
	 * @return
	 */
	public LoginVO login(HttpServletRequest request);
}
