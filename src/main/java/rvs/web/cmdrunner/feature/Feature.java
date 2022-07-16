package rvs.web.cmdrunner.feature;

/**
 * 特性属性
 * 
 * @author Stormwine
 *
 */
public class Feature {
	
	public final static String API = "API";
	public final static String GATEWAY = "Gateway";
	public final static String ATTACHMENT = "Attachment";
	public final static String SCHEDULER = "Scheduler";
	
	private static FeatureProperties props;
	
	/**
	 * 检查是否启用了某项特性
	 * 
	 * @param feature
	 * @return
	 */
	public static boolean isEnable(String feature) {
		return props.getFeature().contains(feature);
	}
	
	
	protected static void set(FeatureProperties properties) {
		props = properties;
	}
}
