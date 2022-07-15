package rvs.web.helper.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的客户端session管理者
 * 
 * @author Stormwine
 *
 */
@Slf4j
@Component
public class DefaultClientManager implements ApplicationContextAware {
		
	public static String SAMPLER_HEADER_NAME = "SAMPLER";
	
	// 已连接的 client session 的缓存
	private Map<String, WebSocketSessionWapper> sessions = new ConcurrentHashMap<>();

	private ApplicationContext ctx;

	/**
	 * 向缓存添加 session
	 * 
	 * @param session
	 */
	public void apply(WebSocketSession session) {
		WebSocketSessionWapper wrapper = new WebSocketSessionWapper(session, (String) session.getAttributes().get(SAMPLER_HEADER_NAME));
		sessions.put(session.getId(), wrapper);
		log.info("add session [" + session + "] to cache.");
		pushInitData(wrapper);
	}

	private void pushInitData(WebSocketSessionWapper wrapper) {
		try {
			ISampler sampler = (ISampler) ctx.getBean(Class.forName(wrapper.getSimpleName()));
			sampler.getCachedData().stream().forEach((data)->{
				push(data, wrapper.getSimpleName());
			});
		} catch (BeansException | ClassNotFoundException e) {
			log.error("",e);
		}
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
	 * 向客户端推送数据
	 * 
	 */
	public void push(JsonObject data, String simplerName) {
		TextMessage message = new TextMessage(data.toString());
		sessions.values().stream().filter((wapper)->wapper.getSimpleName().equals(simplerName)).forEach((wapper)->{
			try {
				wapper.getSession().sendMessage(message);
			} catch (IOException e) {
				log.error("", e);
			}
		});
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

}
