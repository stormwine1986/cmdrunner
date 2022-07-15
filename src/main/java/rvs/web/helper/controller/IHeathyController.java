package rvs.web.helper.controller;

import rvs.web.helper.vo.AboutVO;

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
