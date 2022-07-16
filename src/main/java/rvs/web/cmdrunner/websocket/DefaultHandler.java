package rvs.web.cmdrunner.websocket;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的 Web Socket 处理器
 * 
 * @author Stormwine
 *
 */
@Slf4j
@Component
public class DefaultHandler implements WebSocketHandler {
	
	@Resource
	DefaultClientManager clientMgr;

	/**
	 * 关闭连接
	 * 
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 在这里清除缓存中的用户信息
		clientMgr.remove(session);
		log.info("session [" + session + "] disconnected");
	}

	/**
	 * 建立连接
	 * 
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 在这里缓存用户信息
		clientMgr.apply(session);
		log.info("session [" + session + "] connected");
	}

	/**
	 * 接受消息
	 * 
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
	}

	/**
	 * 发生错误
	 * 
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
		// 在这里清除缓存中的用户信息
		clientMgr.remove(session);
		log.info("session [" + session + "] disconnected");
	}

	/**
	 * 是否支持发送部分消息
	 * 
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
