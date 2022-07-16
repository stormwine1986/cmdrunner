package rvs.web.cmdrunner.controller;

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
