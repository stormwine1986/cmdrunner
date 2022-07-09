package org.pjia.wrvs.web.cmdrunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@ServletComponentScan
public class App extends SpringBootServletInitializer implements WebApplicationInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }
	
    public static void main(String[] args) {
    	SpringApplication.run(App.class, args);
    }
    
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // 解决跨域
        config.setMaxAge(3600l);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
    
    @Bean  
    public FilterRegistrationBean<RequestContextFilter> registerRequestContextFilter() {  
        FilterRegistrationBean<RequestContextFilter> registration = new FilterRegistrationBean<>();  
        registration.setFilter(new RequestContextFilter());  
        registration.addUrlPatterns("/*");
        registration.setName("RequestContextFilter");
        registration.setOrder(1);  
        return registration;
    }
}
