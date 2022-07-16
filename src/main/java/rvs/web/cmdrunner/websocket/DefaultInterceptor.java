package rvs.web.cmdrunner.websocket;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.extern.slf4j.Slf4j;
import rvs.web.cmdrunner.client.IWRVSClient;

/**
 * 默认通信握手拦截器
 * 
 * @author Stormwine
 *
 */
@Slf4j
@Component
public class DefaultInterceptor implements HandshakeInterceptor {
	
	private static final String AuthorizationToken = "AuthorizationToken";
	
	@Resource
	private IWRVSClient client;

	@Override
	public void afterHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler handler, Exception e) {
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler handler, Map<String, Object> attributes) throws Exception {
		// 返回 false 拒绝连接，true 则通过
		// 用户信息等有用信息可存储在 Map<String, Object> attributes 中，在  handler 中可使用 WebSocketSession.getAttributes() 方法取出相应的数据。
		if(!req.getHeaders().containsKey(AuthorizationToken)) return false;
		
		String token = req.getHeaders().get(AuthorizationToken).get(0);
		
		if(StringUtils.isEmpty(token)) return false;
		
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(client.getProps().getSecretKey())).build();
			verifier.verify(token);
			return true;
		}catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

}
