package rvs.web.helper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;

import lombok.extern.slf4j.Slf4j;
import rvs.web.helper.client.IWRVSClient;
import rvs.web.helper.context.RequestContext;

@Slf4j
@WebServlet(urlPatterns="/cmdrunner")
public class CmdRunnerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private IWRVSClient client;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String contentType = req.getContentType();
		String encoding = req.getCharacterEncoding();
		if(!"application/json".equals(contentType) || !"UTF-8".equals(encoding)) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		
		String json = IOUtils.toString(req.getInputStream(),"UTF-8");
		Gson gson = new Gson();
		JsonArray array = gson.fromJson(json, JsonArray.class);
		Command command = new Command(array.get(0).getAsString(), array.get(1).getAsString());
		JsonArray selections = array.get(3).getAsJsonArray();
		for(int i = 0; i < selections.size(); i++) {
			command.addSelection(selections.get(i).getAsString());
		}
		JsonArray options = array.get(2).getAsJsonArray();
		for(int i = 0; i < options.size(); i++) {
			if(options.get(i).isJsonObject()) {
				JsonObject option = options.get(i).getAsJsonObject();
				Iterator<Entry<String, JsonElement>> iterator = option.entrySet().iterator();
				if(iterator.hasNext()) {
					Entry<String, JsonElement> entry = iterator.next();
					command.addOption(new Option(entry.getKey(), entry.getValue().getAsString()));
				}
			} else {
				String option = options.get(i).getAsString();
				command.addOption(new Option(option));
			}
		}
		log.info("Command: " + Arrays.toString(command.toStringArray()));
		JsonObject result = new JsonObject();
		
		try {
			Response response = client.execute(command, RequestContext.getCurrent().getUser());
			WorkItemIterator workItems = response.getWorkItems();
			result.add("workItems", new JsonArray());
			while(workItems.hasNext()) {
				WorkItem workItem = workItems.next();
				JsonObject jsonObject = workitemToJsonObject(workItem);
				result.get("workItems").getAsJsonArray().add(jsonObject);
			}
			result.add("result", resultToJsonObject(response.getResult()));
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (APIException e) {
			log.error("", e);
			result.add("class", new JsonPrimitive(e.getClass().getName()));
			if(e.getMessage()!=null) {
				result.add("message", new JsonPrimitive(e.getMessage()));				
			}
			result.add("fields", new JsonObject());
			Iterator<?> fields = e.getFields();
			while(fields.hasNext()) {
				Field field = (Field) fields.next();
				result.get("fields").getAsJsonObject().add(field.getName(), valueToJsonElement(field.getValue()));
			}
			result.add("workItems", new JsonArray());
			WorkItemIterator workItems = e.getResponse().getWorkItems();
			while(workItems.hasNext()) {
				try {
					workItems.next();
				} catch (APIException e1) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("class", e1.getClass().getName());
					jsonObject.addProperty("message", e1.getMessage());
					result.get("workItems").getAsJsonArray().add(jsonObject);
				}
			}
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		resp.getOutputStream().write(result.toString().getBytes());
		resp.getOutputStream().flush();
	}

	private JsonElement resultToJsonObject(Result result) {
		if(result == null) {
			return JsonNull.INSTANCE;
		}
		JsonObject jsonObject = new JsonObject();
		Iterator<?> fields = result.getFields();
		while(fields.hasNext()) {
			Field field = (Field) fields.next();
			jsonObject.add(field.getName(), valueToJsonElement(field.getValue()));
		}
		jsonObject.add("message", new JsonPrimitive(result.getMessage()));
		return jsonObject;
	}

	private JsonObject workitemToJsonObject(WorkItem workItem) {
		JsonObject jsonObject = new JsonObject();
		Enumeration<?> keys = workItem.getContextKeys();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String context = workItem.getContext(key);
			jsonObject.add("_" + key, new JsonPrimitive(context));
		}
		Iterator<?> fields = workItem.getFields();
		while(fields.hasNext()) {
			Field field = (Field) fields.next();
			String name = field.getName();
			Object value = field.getValue();
			jsonObject.add(name, valueToJsonElement(value));
		}
		return jsonObject;
	}

	private JsonElement valueToJsonElement(Object value) {
		if(value == null) {
			return JsonNull.INSTANCE;
		}
		if(value instanceof Integer) {
			return new JsonPrimitive((Integer) value);
		}
		if(value instanceof Double) {
			return new JsonPrimitive((Double) value);
		}
		if(value instanceof Float) {
			return new JsonPrimitive((Float) value);
		}
		if(value instanceof Long) {
			return new JsonPrimitive((Long) value);
		}
		if(value instanceof String) {
			return new JsonPrimitive((String) value);
		}
		if(value instanceof Date) {
			return new JsonPrimitive(((Date) value).getTime());
		}
		if(value instanceof ItemList) {
			return itemListToJsonArray((ItemList) value);
		}
		if(value instanceof Item) {
			return itemToJsonObject((Item) value);
		}
		
		// 兜底
		return new JsonPrimitive(value.toString());
	}

	private JsonArray itemListToJsonArray(List<?> itemList) {
		JsonArray array = new JsonArray();
		for(int i = 0; i < itemList.size(); i++) {
			Item item = (Item) itemList.get(i);
			array.add(itemToJsonObject(item));
		}
		return array;
	}
	
	private JsonObject itemToJsonObject(Item item) {
		JsonObject jsonObject = new JsonObject();
		Enumeration<?> keys = item.getContextKeys();
		while(keys.hasMoreElements()) {
			String element = (String) keys.nextElement();
			String context = item.getContext(element);
			jsonObject.add("_" + element, new JsonPrimitive(context));
		}
		Iterator<?> fields = item.getFields();
		while(fields.hasNext()) {
			Field field = (Field) fields.next();
			JsonElement element = valueToJsonElement(field.getValue());
			jsonObject.add(field.getName(), element);
		}
		return jsonObject;
	}
}
