package org.pjia.wrvs.web.cmdrunner.demo;

import java.io.IOException;

import javax.annotation.Resource;

import org.pjia.wrvs.web.cmdrunner.schedulers.AbstractScheduler;
import org.pjia.wrvs.web.cmdrunner.schedulers.IStaticTask;
import org.pjia.wrvs.web.cmdrunner.websocket.DefaultClientManager;
import org.pjia.wrvs.web.cmdrunner.wrvsclient.IWRVSClient;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.google.gson.JsonObject;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;

import lombok.extern.slf4j.Slf4j;

/**
 * 静态任务的示例:
 * 	需求文档状态采样任务
 * 
 * @author Stormwine
 *
 */
@Slf4j
@Component
public class RequirementDocumentStateSample extends AbstractScheduler implements IStaticTask {
	
	@Resource
	private IWRVSClient client;
	
	@Resource
	private DefaultClientManager clientMgr;
	
	long rate = 15; // 单位  s

	@Override
	public void afterPropertiesSet() throws Exception {
		schedule(this);
	}

	@Override
	public long getRate() {
		return rate;
	}

	@Override
	public void doAction() throws Exception {
		// 业务逻辑写在这里
		log.info("PullRequirementDocumentStateTask.doAction");
		Command cmd = new Command("im", "issues");
		cmd.addOption(new Option("fields", "ID,State"));
		cmd.addOption(new Option("queryDefinition", "((field[Type]=Requirement Document) and (field[Project]=/Demo))"));
		Response response = client.execute(cmd, null);
		log.info("result size = " + response.getWorkItemListSize());
		WorkItemIterator workItems = response.getWorkItems();
		while(workItems.hasNext()) {
			WorkItem workItem = workItems.next();
			JsonObject data = new JsonObject();
			data.addProperty("id", workItem.getId());
			data.addProperty("completed", workItem.getField("State").getValueAsString().equals("Accepted"));
			log.info("push data " + data.toString() + " to all");
			pushData(data);
		}
	}

	private void pushData(JsonObject data) {
		clientMgr.get().forEachRemaining((session)->{
				try {
					session.sendMessage(new TextMessage(data.toString()));
				} catch (IOException e) {
					log.error("", e);
				}
		});
	}

}
