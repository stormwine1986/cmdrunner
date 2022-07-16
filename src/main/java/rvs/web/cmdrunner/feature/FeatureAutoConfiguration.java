package rvs.web.cmdrunner.feature;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({FeatureProperties.class})
public class FeatureAutoConfiguration implements InitializingBean
{
	private final FeatureProperties properties;
	
	public FeatureAutoConfiguration(ApplicationContext context, FeatureProperties properties) {
		this.properties = properties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	@Bean()
    public Feature getFeature() throws Exception {
		return new Feature(properties);
    }
}
