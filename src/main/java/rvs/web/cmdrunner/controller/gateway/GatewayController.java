package rvs.web.cmdrunner.controller.gateway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;

import lombok.extern.slf4j.Slf4j;
import rvs.web.cmdrunner.client.IWRVSClient;
import rvs.web.cmdrunner.feature.Feature;

@Slf4j
@RestController
@RequestMapping("/gateway")
public class GatewayController implements IGatewayController {
	
	@Autowired
	private IWRVSClient client;
	
	private static String IMPORT_CMD = "gateway import --hostname=${hostname} --port=${port} --user=${user} --password=${pwd} "
			+ "--config=\"${config}\" "
			+ "--file=\"${file}\" "
			+ "--Fields=${fields} --silent";
	
	private static String Doc_Studio_URL = "http://${hostname}:${port}/extended-web-ui/doc-workspace/doc/${docId}";

	@Override
	@PostMapping("/import")
	public Map<String, String> doImport(MultipartFile file, @RequestParam("Title") String title, @RequestParam("Project") String project, @RequestParam(name="Windchill OID",required=false) String oid) throws Exception {
		
		if(!Feature.isEnable(Feature.GATEWAY)) throw new IllegalStateException("节点未启用 Gateway特性");
		
		if(!file.getOriginalFilename().endsWith(".docx")) throw new IllegalArgumentException("only support .docx");
		if(title==null||title.trim().equals("")) throw new IllegalArgumentException("'title' must NOT be empty");
		if(project==null||project.trim().equals("")) throw new IllegalArgumentException("'prorject' must NOT be empty");
		
		Map<String, String> result = new HashMap<>();
		
		File tempFile = File.createTempFile((UUID.randomUUID().toString().replace("-", "")), ".docx");
		FileOutputStream fos = new FileOutputStream(tempFile);
		IOUtils.write(file.getBytes(), fos);
		fos.flush();
		fos.close();
		
		Map<String, String> fields = new HashMap<>();
		fields.put("Title", title);
        fields.put("Project", project);
        fields.put("Category", "Comment");
        if(oid!=null&&!oid.trim().equals("")) {
        	String[] tokens = oid.split(":");
        	fields.put("Windchill Type", tokens[1]);   
        	fields.put("Windchill ID", tokens[2]); 
        }
		
		// 组装命令
		String cmd = IMPORT_CMD
			.replace("${hostname}", client.getProps().getHostname())
			.replace("${port}", client.getProps().getPort())
			.replace("${user}", client.getProps().getConnectUser())
			.replace("${pwd}", client.getProps().getPassword())
			.replace("${config}", "Requirements Document Import and Re-import with Outline Levels")
			.replace("${file}", tempFile.getAbsolutePath())
			.replace("${fields}", formatFields(fields));
		System.out.println(cmd);
		
		// 执行导入命令
		Process p = Runtime.getRuntime().exec(cmd);
		String line = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = br.readLine()) != null  || (line = brError.readLine()) != null) {
        	//输出exe输出的信息以及错误信息
        	log.info(line);    
        }
        br.close();
        brError.close();
		
        tempFile.delete();
        
        if(oid!=null&&!oid.trim().equals("")) {
        	Command imissues = new Command("im", "issues");
        	imissues.addOption(new Option("fields","ID"));
        	imissues.addOption(new Option("queryDefinition", "((field[Windchill Type]=\"" + fields.get("Windchill Type") + "\") and (field[Windchill ID]=\"" + fields.get("Windchill ID") + "\"))"));
        	Response response = client.execute(imissues, null);
        	if(response.getWorkItemListSize() > 0) {
        		WorkItem workItem = response.getWorkItems().next();
        		String url = Doc_Studio_URL
        			.replace("${hostname}", client.getProps().getHostname())
        			.replace("${port}", client.getProps().getPort())
        			.replace("${docId}", workItem.getId());
        		result.put("id", workItem.getId());
        		result.put("doc_studio_urrl", url);
        	}
        }
        
        return result;
	}
	
	private String formatFields(Map<String, String> fields) {
		return fields.entrySet().stream()
			.map(item->"\"" + item.getKey() + "\"=\"" + item.getValue() + "\"")
			.collect(Collectors.joining(";"));
	}

}
