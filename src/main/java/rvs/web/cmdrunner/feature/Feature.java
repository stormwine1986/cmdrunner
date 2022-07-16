package rvs.web.cmdrunner.feature;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

/**
 * 特性属性
 * 
 * @author Stormwine
 *
 */
@Slf4j
public class Feature {
	
	public final static String API = "API";
	public final static String GATEWAY = "Gateway";
	public final static String ATTACHMENT = "Attachment";
	public final static String SCHEDULER = "Scheduler";
	
	private static FeatureProperties props;

	public Feature(FeatureProperties properties) {
		props = properties;
		log.info("Enabled Feature: " + Arrays.toString(props.getFeature().toArray()));
	}
	
	/**
	 * 检查是否启用了某项特性
	 * 
	 * @param feature
	 * @return
	 */
	public static boolean isEnable(String feature) {
		return props.getFeature().contains(feature);
	}

}
