package org.pjia.wrvs.web.cmdrunner.vo;

import lombok.Data;

@Data
public class AboutVO {
	private String title;
	private String apiversion;
	private String version;
	private String build;
	private String patchLevel;
}
