package org.pjia.wrvs.web.cmdrunner.controllers.util;

public class HTMLConvertor {
	
	/**
	 * MKS文本转换成HTML文本
	 * 
	 * @param mksString
	 * @return
	 */
	public static String toHTML(String mksString) {
		return mksString.replace("<!-- MKS HTML -->", "<!-- HTML -->");
	}
}
