package rvs.web.cmdrunner.client;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * WRVS Client 自动配置
 * 
 * @author pjia
 *
 */
@Configuration
@EnableConfigurationProperties({WRVSClientProperties.class})
public class WRVSClientAutoConfiguration implements InitializingBean {
	
	private final WRVSClientProperties properties;

	public WRVSClientAutoConfiguration(ApplicationContext context, WRVSClientProperties properties) {
        this.properties = properties;
    }
	
	@Bean(name = "wrvsClient", destroyMethod = "close")
    public IWRVSClient getWRVSClient() throws Exception {
		return new WRVSClient(properties);
    }
	
	@Bean  
    public FilterRegistrationBean<JWTAuthFilter> registerSecurityFilter() {  
        FilterRegistrationBean<JWTAuthFilter> registration = new FilterRegistrationBean<>();  
        registration.setFilter(new JWTAuthFilter(properties));  
        registration.addUrlPatterns("/api","/services/*","/attachment/*","/gateway/*");
        registration.setName("SecurityFilter");  
        registration.setOrder(2); 
        return registration;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
	}
}
