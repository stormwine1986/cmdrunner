package rvs.web.cmdrunner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mks.api.Command;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;

import rvs.web.cmdrunner.client.IWRVSClient;

@RestController
@RequestMapping("/heathy")
public class HeathyController implements IHeathyController {
	
	@Autowired
	private IWRVSClient client;

	@Override
	@GetMapping("/ping")
	public AboutVO ping() throws Exception {
		AboutVO aboutVO = new AboutVO();
		Command command = new Command("im", "about");
		Response response = client.execute(command, null);
		WorkItem workItem = response.getWorkItems().next();
		String title = workItem.getField("title").getValueAsString();
		String apiversion = workItem.getField("apiversion").getValueAsString();
		String version = workItem.getField("version").getValueAsString();
		String build = workItem.getField("build").getValueAsString();
		String patchLevel = workItem.getField("patch-level").getValueAsString();
		aboutVO.setTitle(title);
		aboutVO.setApiversion(apiversion);
		aboutVO.setVersion(version);
		aboutVO.setBuild(build);
		aboutVO.setPatchLevel(patchLevel);
		return aboutVO;
	}

}
