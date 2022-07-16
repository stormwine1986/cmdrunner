package rvs.web.cmdrunner.scheduler;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import rvs.web.cmdrunner.feature.Feature;
import rvs.web.cmdrunner.feature.FeatureProperties;

@Slf4j
@Configuration
@EnableConfigurationProperties({FeatureProperties.class})
public class SchedulerAutoConfiguration {
	
	public SchedulerAutoConfiguration(ApplicationContext context, FeatureProperties properties) {
		if(properties.getFeature().contains(Feature.SCHEDULER)) {
			Map<String, AbstractScheduler> beansOfType = context.getBeansOfType(AbstractScheduler.class);
			beansOfType.values().stream().forEach(task -> {
				task.schedule(task);
				log.info("Scheduler Actived ========> " + task.getClass().getName());
			});			
		}
	}
}
