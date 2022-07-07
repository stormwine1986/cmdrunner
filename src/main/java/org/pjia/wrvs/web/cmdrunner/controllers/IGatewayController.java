package org.pjia.wrvs.web.cmdrunner.controllers;

import java.util.Map;

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
	 * 导入 Word，支持格式 docx。<br>注意</br> 此方法线程不安全。
	 * 启用  Windchill OID 参数需要确保对应的 Document类型应该支持 Windchill Type 和  Windchill ID 的导入
	 * 
	 * @param file docx文件
	 * @param config 导入配置
	 * @param title 文档名称
	 * @param prorject 所属项目
	 * @return 
	 * 
	 * @throws Exception
	 */
	public Map<String, String> doImport(MultipartFile file, String title, String prorject, String oid) throws Exception;

}
