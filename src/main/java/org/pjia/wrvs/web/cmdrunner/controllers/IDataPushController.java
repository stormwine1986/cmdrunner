package org.pjia.wrvs.web.cmdrunner.controllers;

/**
 * 提供给 RVS 使用的数据推送接口
 * 
 * @author Stormwine
 *
 */
public interface IDataPushController {

	/**
	 * 推送数据
	 * 
	 * @param data JSON格式的数据
	 * @throws Exception
	 */
	public void data(String data) throws Exception;
}
