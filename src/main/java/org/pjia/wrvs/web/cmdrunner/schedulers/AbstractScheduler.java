package org.pjia.wrvs.web.cmdrunner.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractScheduler implements InitializingBean {

	private ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 调用这个方法安排任务
	 * 
	 * @param task
	 */
	protected void schedule(IStaticTask task) {
		es.scheduleWithFixedDelay(()->{
					try {
						task.doAction();
					} catch (Exception e) {
						log.error("", e);
					}
				}, 
				task.getRate(), 
				task.getRate(), 
				TimeUnit.SECONDS);
	}
}
