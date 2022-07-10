package org.pjia.wrvs.web.cmdrunner.schedulers;

public interface IStaticTask {
	
	/**
	 * 获取执行间隔
	 * 
	 * @return s
	 */
	public long getRate();
	
	/**
	 * 任务的业务逻辑
	 * 
	 */
	public void doAction() throws Exception;
}
