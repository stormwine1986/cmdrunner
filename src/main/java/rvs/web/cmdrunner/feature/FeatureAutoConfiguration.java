package rvs.web.cmdrunner.feature;

import java.util.Arrays;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties({FeatureProperties.class})
public class FeatureAutoConfiguration
{	
	public FeatureAutoConfiguration(ApplicationContext context, FeatureProperties properties) {
		log.info("Enabled Feature: " + Arrays.toString(properties.getFeature().toArray()));
		Feature.set(properties);
	}
}
