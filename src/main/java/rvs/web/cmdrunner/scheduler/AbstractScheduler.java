package rvs.web.cmdrunner.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import rvs.web.cmdrunner.feature.Feature;

@Slf4j
public abstract class AbstractScheduler implements IStaticTask {

	private ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 调用这个方法安排任务
	 * 
	 * @param task
	 */
	public void schedule(IStaticTask task) {
		if(Feature.isEnable(Feature.SCHEDULER)) {
			es.scheduleAtFixedRate(()->{
						try {
							task.run();
						} catch (Exception e) {
							log.error("", e);
						}
					}, 
					task.getInitDelay(), 
					task.getRate(), 
					TimeUnit.SECONDS);
		}
	}
	
	
}
