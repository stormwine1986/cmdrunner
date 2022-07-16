package rvs.web.helper.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;

import lombok.extern.slf4j.Slf4j;
import rvs.web.helper.feature.Feature;

@Slf4j
public abstract class AbstractScheduler implements InitializingBean {

	private ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 调用这个方法安排任务
	 * 
	 * @param task
	 */
	protected void schedule(IStaticTask task) {
		if(Feature.isEnable(Feature.SCHEDULER)) {
			es.scheduleWithFixedDelay(()->{
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
