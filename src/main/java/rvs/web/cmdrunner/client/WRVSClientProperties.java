package rvs.web.cmdrunner.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * WRVS Client 配置属性
 * 
 * @author pjia
 *
 */
@Data
@ConfigurationProperties(prefix = "wrvs.client")
public class WRVSClientProperties {
	private String hostname;
	private String port;
	private String connectUser;
	private String password;
	private String secretKey;
}
