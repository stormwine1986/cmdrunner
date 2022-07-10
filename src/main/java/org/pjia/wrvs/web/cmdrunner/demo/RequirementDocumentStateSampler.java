package org.pjia.wrvs.web.cmdrunner.demo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.pjia.wrvs.web.cmdrunner.schedulers.AbstractScheduler;
import org.pjia.wrvs.web.cmdrunner.schedulers.IStaticTask;
import org.pjia.wrvs.web.cmdrunner.websocket.DefaultClientManager;
import org.pjia.wrvs.web.cmdrunner.websocket.ISampler;
import org.pjia.wrvs.web.cmdrunner.wrvsclient.IWRVSClient;
import org.springframework.stereotype.Component;

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
public class RequirementDocumentStateSampler extends AbstractScheduler implements IStaticTask, ISampler {
	
	private List<JsonObject> cache = new ArrayList<>();
	
	@Resource
	private IWRVSClient client;
	
	@Resource
	private DefaultClientManager clientMgr;
	
	long rate = 15; // 单位  s

	@Override
	public void afterPropertiesSet() throws Exception {
		// 必须要写这句，不然任务不会被安排执行
		schedule(this);
	}

	@Override
	public long getRate() {
		return rate;
	}

	@Override
	public void doAction() throws Exception {
		// StaticTask的业务逻辑写在这里
		log.info("PullRequirementDocumentStateTask.doAction");
		// 直接使用数据采集逻辑
		sample();
	}

	@Override
	public void sample() throws Exception {
		// 数据采集逻辑写这里
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
			log.info("push data " + data.toString() + " to all, simpler = " + this.getClass().getName());
			cache.add(data);
			pushData(data);
		}
	}
	
	private void pushData(JsonObject data) {
		clientMgr.push(data, this.getClass().getName());
	}

	@Override
	public List<JsonObject> getCachedData() {
		return cache;
	}

}
