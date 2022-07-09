package org.pjia.wrvs.web.cmdrunner;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Web Socket 配置类
 * 
 * @author Stormwine
 *
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Resource
	WebSocketHandler defaultHandler;
	@Resource
	HandshakeInterceptor defaultInterceptor;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(defaultHandler,"ws")  // 添加处理器
				.addInterceptors(defaultInterceptor) // 添加拦截器
				.setAllowedOrigins("*"); // 解决跨域
	}

}
