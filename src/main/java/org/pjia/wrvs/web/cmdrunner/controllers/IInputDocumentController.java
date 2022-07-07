package org.pjia.wrvs.web.cmdrunner.controllers;

import org.pjia.wrvs.web.cmdrunner.vo.InputDocumentVO;

/**
 * Input Document 接口
 * 
 * @author pjia
 *
 */
public interface IInputDocumentController {
	
	/**
	 * 获取指定ID的Input Document
	 * 
	 * @param id id
	 * @return InputDocumentVO
	 * @throws Exception Exception
	 */
	public InputDocumentVO getById(String id) throws Exception;
}
