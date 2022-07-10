package org.pjia.wrvs.web.cmdrunner.websocket;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认通信握手拦截器
 * 
 * @author Stormwine
 *
 */
@Slf4j
@Component
public class DefaultInterceptor implements HandshakeInterceptor,ApplicationContextAware {

	private ApplicationContext ctx;

	@Override
	public void afterHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler handler, Exception e) {
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler handler, Map<String, Object> attributes) throws Exception {
		// 返回 false 拒绝连接，true 则通过
		// 用户信息等有用信息可存储在 Map<String, Object> attributes 中，在  handler 中可使用 WebSocketSession.getAttributes() 方法取出相应的数据。
		String samplerHeader = getSamplerHeader(req);
		if(samplerHeader==null) return false;
		attributes.put(DefaultClientManager.SAMPLER_HEADER_NAME, samplerHeader);
		return true;
	}
	
	private String getSamplerHeader(ServerHttpRequest req) {
		try {
			if(req.getHeaders().containsKey(DefaultClientManager.SAMPLER_HEADER_NAME)) {
				String simpleName = req.getHeaders().get(DefaultClientManager.SAMPLER_HEADER_NAME).get(0);
				ISampler bean = (ISampler) ctx.getBean(Class.forName(simpleName));
				if(bean!=null) {
					return simpleName;
				}
			}
		} catch (BeansException | ClassNotFoundException e) {
			log.error("", e);
		}
		return null;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

}
