package org.pjia.wrvs.web.cmdrunner.controllers.impl;

import javax.websocket.server.PathParam;

import org.pjia.wrvs.web.cmdrunner.RequestContext;
import org.pjia.wrvs.web.cmdrunner.controllers.IInputDocumentController;
import org.pjia.wrvs.web.cmdrunner.vo.InputDocumentVO;
import org.pjia.wrvs.web.cmdrunner.wrvsclient.IWRVSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;

@RestController
@RequestMapping("/services/inputdocument")
public class InputDocumentController implements IInputDocumentController {
	
	@Autowired
	private IWRVSClient client;

	@Override
	@GetMapping("/{id}")
	public InputDocumentVO getById(@PathVariable String id) throws Exception {
		Command command = new Command("im", "viewissue");
		command.addSelection(id);
		command.addOption(new Option("showRichContent"));
		command.addOption(new Option("showLabels"));
		command.addOption(new Option("showAttachments"));
		Response response = client.execute(command, RequestContext.getCurrent().getUser());
		WorkItem workItem = response.getWorkItem(id);
		InputDocumentVO inputDocumentVO = new InputDocumentVO();
		inputDocumentVO.setId(workItem.getField("ID").getValueAsString());
		inputDocumentVO.setProject(workItem.getField("Project").getValueAsString());
		inputDocumentVO.setState(workItem.getField("State").getValueAsString());
		inputDocumentVO.setDocumentType(workItem.getField("Document Type").getValueAsString());
		inputDocumentVO.setDocumentShortTitle(workItem.getField("Document Short Title").getValueAsString());
		inputDocumentVO.setSharedText(workItem.getField("Shared Text").getValueAsString());
		return inputDocumentVO;
	}
}
