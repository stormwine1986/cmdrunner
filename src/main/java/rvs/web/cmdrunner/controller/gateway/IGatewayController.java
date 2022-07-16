package rvs.web.cmdrunner.controller.gateway;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 使用 Gateway 工具的能力实现导入导出 Word 文档
 * 注意：
 * 	这个接口需要在 Server 端部署 RVS Client 并确保 Gateway 功能可以正常使用
 * 
 * @author Stormwine
 *
 */

public interface IGatewayController {
	
	/**
	 * 导入 Word，支持格式 docx。
	 * 
	 * @param file docx文件
	 * @param configName 导入配置
	 * @param title 文档名称
	 * @param prorject 所属项目
	 * 
	 * @return 
	 * 
	 * @throws Exception
	 */
	public void doImport(MultipartFile file, String configName, String title, String prorject, HttpServletRequest request) throws Exception;
	
	/**
	 * 导出文档到docx
	 * 
	 * @param id 文档ID
	 * @param configName 配置
	 * 
	 * @throws Exception
	 */
	public void doExport(String id, String configName, HttpServletResponse response) throws Exception;

}
