package rvs.web.helper.websocket;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

/**
 * WebSocketSession 包装类
 * 
 * @author Stormwine
 *
 */
@Data
public class WebSocketSessionWapper {
	
	public WebSocketSessionWapper(WebSocketSession session, String sampler) {
		this.session = session;
		this.simpleName = sampler;
	}

	private WebSocketSession session;
	
	private String simpleName;
}
