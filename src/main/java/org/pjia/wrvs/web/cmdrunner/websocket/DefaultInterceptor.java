package org.pjia.wrvs.web.cmdrunner.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 默认通信握手拦截器
 * 
 * @author Stormwine
 *
 */
@Component
public class DefaultInterceptor implements HandshakeInterceptor {

	@Override
	public void afterHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler handler, Exception e) {
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler handler, Map<String, Object> attributes) throws Exception {
		// 返回 false 拒绝连接，true 则通过
		// 用户信息等有用信息可存储在 Map<String, Object> attributes 中，在  handler 中可使用 WebSocketSession.getAttributes() 方法取出相应的数据。
		System.out.println("beforeHandshake");
		return true;
	}

}
