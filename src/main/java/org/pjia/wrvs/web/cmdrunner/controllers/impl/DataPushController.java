package org.pjia.wrvs.web.cmdrunner.controllers.impl;

import java.io.IOException;

import javax.annotation.Resource;

import org.pjia.wrvs.web.cmdrunner.controllers.IDataPushController;
import org.pjia.wrvs.web.cmdrunner.websocket.DefaultClientManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/datapush")
public class DataPushController implements IDataPushController {
	
	@Resource
	DefaultClientManager clientMgr;
	
	@Override
	@PostMapping("/data")
	public void data(@RequestBody String data) throws Exception {
		TextMessage message = new TextMessage(data);
		clientMgr.get().forEachRemaining((session) -> {
			try {
				session.sendMessage(message);
				log.info("send message to session [" + session + "]");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
