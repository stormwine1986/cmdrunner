package org.pjia.wrvs.web.cmdrunner.vo;

import lombok.Data;

@Data
public class LoginVO {
	private String user;
	private String token;
	private String message;
}
