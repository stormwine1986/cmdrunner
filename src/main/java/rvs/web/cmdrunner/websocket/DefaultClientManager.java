package rvs.web.cmdrunner.websocket;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的客户端session管理者
 * 
 * @author Stormwine
 *
 */
@Slf4j
@Component
public class DefaultClientManager {
	
	// 已连接的 client session 的缓存
	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	/**
	 * 向缓存添加 session
	 * 
	 * @param session
	 */
	public void apply(WebSocketSession session) {
		sessions.put(session.getId(), session);
		log.info("add session [" + session + "] to cache.");
	}

	/**
	 * 从缓存中移除 session
	 * 
	 * @param session
	 */
	public void remove(WebSocketSession session) {
		sessions.remove(session.getId());
		log.info("remove session [" + session + "] from cache.");
	}
	
	/**
	 * 获取所有已连接的session缓存
	 * 
	 * @return
	 */
	public Iterator<WebSocketSession> getAll() {
		return sessions.values().iterator();
	}
}
