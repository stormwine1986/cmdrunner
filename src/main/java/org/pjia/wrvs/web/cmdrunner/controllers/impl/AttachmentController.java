package org.pjia.wrvs.web.cmdrunner.controllers.impl;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.pjia.wrvs.web.cmdrunner.controllers.IAttachmentController;
import org.pjia.wrvs.web.cmdrunner.wrvsclient.IWRVSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/attachment")
public class AttachmentController implements IAttachmentController {
	
	@Autowired
	private IWRVSClient client;

	@Override
	@GetMapping("/download")
	public void download(@RequestParam("itemId") String id, @RequestParam("fieldName") String fieldName,
			@RequestParam("fileName") String fileName, HttpServletResponse response) throws Exception {
		File tempFile = null;
		try {
			Command command = new Command("im", "extractattachments");
			command.addOption(new Option("issue", id));
			command.addOption(new Option("field", fieldName));
			tempFile = File.createTempFile(UUID.randomUUID().toString(), "");
			command.addOption(new Option("outputFile", "remote://" + tempFile.getAbsolutePath()));
			command.addSelection(fileName);
			client.execute(command, null);
			byte[] buffer = FileUtils.readFileToByteArray(tempFile);
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
			IOUtils.write(buffer, response.getOutputStream());
		} finally {
			if(tempFile != null ) {
				tempFile.delete();
			}
		}
	}

	@Override
	@PostMapping("/update")
	public void update(@RequestParam("itemId") String id, @RequestParam("fieldName") String fieldName, MultipartFile file) throws Exception {
		File tempFile = null;
		try {
			String fileName = file.getOriginalFilename();
			tempFile = File.createTempFile(UUID.randomUUID().toString(), "");
			file.transferTo(tempFile);
			Command cmd = new Command("im", "editissue");
			cmd.addSelection(id);
			cmd.addOption(new Option("updateAttachment", String.format("field=%s,path=%s,name=%s", fieldName, "remote://" + tempFile.getAbsolutePath(), fileName)));
			client.execute(cmd, null);
		} catch (APIException e) {
			log.error("{}", e.getResponse().toString());
			throw e;
		}finally {
			if(tempFile != null) {
				tempFile.delete();
			}
		}
	}

}
