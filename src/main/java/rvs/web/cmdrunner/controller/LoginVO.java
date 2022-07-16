package rvs.web.cmdrunner.controller;

import lombok.Data;

@Data
public class LoginVO {
	private String user;
	private String token;
	private String message;
}
