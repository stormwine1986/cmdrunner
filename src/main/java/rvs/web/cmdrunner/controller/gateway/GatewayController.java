package rvs.web.cmdrunner.controller.gateway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	private static String EXPORT_CMD = "gateway export --hostname=${hostname} --port=${port} --user=${user} --password=${pwd} " 
			+ "--config=\"${config}\" " 
			+ "--file=\"${tempfile}\" --silent ${id}";

	@Override
	@PostMapping("/import")
	public void doImport(MultipartFile file, @RequestParam("Title") String title, @RequestParam("Configuration") String configName, @RequestParam("Project") String project, HttpServletRequest request) throws Exception {
		
		if(!Feature.isEnable(Feature.GATEWAY)) throw new IllegalStateException("节点未启用Gateway特性");
		
		if(!file.getOriginalFilename().endsWith(".docx")) throw new IllegalArgumentException("only support .docx");
		if(StringUtils.isEmpty(title)) throw new IllegalArgumentException("'Title' must NOT be empty");
		if(StringUtils.isEmpty(project)) throw new IllegalArgumentException("'Project' must NOT be empty");
		if(StringUtils.isEmpty(configName)) throw new IllegalArgumentException("'Configuration' must NOT be empty");
		
		File tempFile = File.createTempFile((UUID.randomUUID().toString().replace("-", "")), ".docx");
		FileOutputStream fos = new FileOutputStream(tempFile);
		IOUtils.write(file.getBytes(), fos);
		fos.flush();
		fos.close();
		
		Map<String, String> fields = new HashMap<>();
        fields.put("Category", "Comment");
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()) {
        	String paramName = parameterNames.nextElement();
        	fields.put(paramName, request.getParameter(paramName));
        }
        fields.remove("Configuration");
		
		// 组装命令
		String cmd = IMPORT_CMD
			.replace("${hostname}", client.getProps().getHostname())
			.replace("${port}", client.getProps().getPort())
			.replace("${user}", client.getProps().getConnectUser())
			.replace("${pwd}", client.getProps().getPassword())
			.replace("${config}", configName)
			.replace("${file}", tempFile.getAbsolutePath())
			.replace("${fields}", formatFields(fields));
		log.info(cmd);
		
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
	}
	
	private String formatFields(Map<String, String> fields) {
		return fields.entrySet().stream()
			.map(item->"\"" + item.getKey() + "\"=\"" + item.getValue() + "\"")
			.collect(Collectors.joining(";"));
	}
	
	@Override
	@PostMapping("/export")
	public void doExport(@RequestParam("id") String id, @RequestParam("Configuration") String configName, HttpServletResponse response) throws Exception {
		if(!Feature.isEnable(Feature.GATEWAY)) throw new IllegalStateException("节点未启用Gateway特性");
		
		// 创建临时文件
		String tempfileName = System.getProperty("user.dir") + File.separator + UUID.randomUUID().toString().replace("-", "") + ".docx";
		// 组装导出命令
		String cmd = EXPORT_CMD
			.replace("${hostname}", client.getProps().getHostname())
			.replace("${port}", client.getProps().getPort())
			.replace("${user}", client.getProps().getConnectUser())
			.replace("${pwd}", client.getProps().getPassword())
			.replace("${config}", configName)
			.replace("${tempfile}", tempfileName)
			.replace("${id}", id);
		log.info(cmd);
		
		// 启动导出进程
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
        
        File tempfile = null;
        try {
        	tempfile = new File(tempfileName);
        	if(!tempfile.canRead()) throw new IllegalAccessException("导出执行失败，具体内容需要查询日志");
        	byte[] buffer = FileUtils.readFileToByteArray(tempfile);
        	response.setContentType("application/octet-stream");
        	response.addHeader("Content-Disposition", "attachment;fileName=" + tempfile.getName());
        	IOUtils.write(buffer, response.getOutputStream());        	
        } finally {
			if(tempfile!=null && tempfile.exists()) {
				tempfile.delete();
			}
		}
	} 

}
