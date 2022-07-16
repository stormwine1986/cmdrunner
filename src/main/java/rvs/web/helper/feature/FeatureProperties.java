package rvs.web.helper.feature;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 用于接受来自命令行的feature参数
 * 
 * @author Stormwine
 *
 */
@Data
@ConfigurationProperties()
public class FeatureProperties {
	/**
	 * 启用的特性列表
	 * 
	 */
	private List<String> feature = new ArrayList<>();
}
