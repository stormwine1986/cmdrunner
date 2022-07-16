package rvs.web.helper.scheduler;

public interface IStaticTask {
	
	/**
	 * 获取执行间隔
	 * 
	 * @return s
	 */
	public long getRate();
	
	/**
	 * 获取首次执行延时
	 * 
	 * @return s
	 */
	public long getInitDelay();
	
	/**
	 * 任务的业务逻辑
	 * 
	 */
	public void run() throws Exception;

	
}
