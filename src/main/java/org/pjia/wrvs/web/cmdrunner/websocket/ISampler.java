package org.pjia.wrvs.web.cmdrunner.websocket;

import java.util.List;

import com.google.gson.JsonObject;

/**
 * 数据采集者
 * 
 * @author Stormwine
 *
 */
public interface ISampler {
	/**
	 * 执行采集
	 * 
	 */
	public void sample() throws Exception;
	/**
	 * 获取已缓存的数据
	 * 
	 * @return
	 */
	public List<JsonObject> getCachedData();
}
