package org.pjia.wrvs.web.cmdrunner.vo;

import lombok.Data;

/**
 * Input Document
 * 
 * @author pjia
 *
 */
@Data
public class InputDocumentVO {
	private String id;
	private String documentType;
	private String documentShortTitle;
	private String state;
	private String project;
	private String assignedUser;
	private String sharedText;
}
