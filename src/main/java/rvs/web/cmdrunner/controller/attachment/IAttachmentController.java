package rvs.web.cmdrunner.controller.attachment;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * 附件上传，下载接口
 * 
 * @author pjia
 *
 */
public interface IAttachmentController {
	/**
	 * 下载文件
	 * 
	 * @param fileDTO fileDTO
	 * @param response response
	 * @throws Exception Exception
	 */
	void download(String id, String fieldName, String fileName, HttpServletResponse response) throws Exception;
	
	/**
	 * 更新文件
	 * 
	 * @param fileDTO fileDTO
	 * @param file file
	 * @throws Exception
	 */
	void update(String id, String fieldName, MultipartFile file) throws Exception;
}
