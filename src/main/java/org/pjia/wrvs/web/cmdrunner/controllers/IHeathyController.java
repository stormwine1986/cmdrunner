package org.pjia.wrvs.web.cmdrunner.controllers;

import org.pjia.wrvs.web.cmdrunner.vo.AboutVO;

/**
 * 服务健康
 * 
 * @author pjia
 *
 */
public interface IHeathyController {
	
	/**
	 * 检测接口
	 * @return 
	 * 
	 * @throws Exception
	 */
	public AboutVO ping() throws Exception;
}
