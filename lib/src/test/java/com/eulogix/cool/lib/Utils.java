package com.eulogix.cool.lib;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Utils {
		
	public static App getApp() {
		return new App("http://hams.devel/app_test.php", "admin", "demo");
	}
	
	public static String getTestFileRepositoryId() {
		return "hams_templates";
	}
	
	public static byte[] getFileContent(String fileName) {

		byte[] content;
		ClassLoader classLoader = Utils.class.getClassLoader();
		try {
			 content = IOUtils.toByteArray(classLoader.getResourceAsStream(fileName));
			 return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	  }
}
