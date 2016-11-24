package com.eulogix.cool.lib;

import java.io.IOException;

import com.eulogix.cool.lib.App;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import junit.framework.Assert;


public class AppTest {
    
	private App getApp() {
		return new App("http://hams.devel/app_test.php", "admin", "demo");
	}
	
	private byte[] getFileContent(String fileName) {

		byte[] content;
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			 content = IOUtils.toByteArray(classLoader.getResourceAsStream(fileName));
			 return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	  }
	
    /*@Test
    public void testApiVersion()
    {
        App app = this.getApp();
    	Assert.assertEquals( "0.1", app.getApiVersion() );
    }
    
    @Test
    public void testUploadFile()
    {
    	App app = this.getApp();
    	int fileId = app.uploadFile("core", "core", "account", "1", "test2.txt", "UNCATEGORIZED", this.getFileContent("test.txt"));
    	Assert.assertTrue( fileId > 0);
    }*/
    
    @Test
    public void testUploadFileFromFs() throws IOException
    {
    	App app = this.getApp();
    	int fileId = app.uploadFile("core", "core", "account", "1", "eula.txt", "UNCATEGORIZED", "D:\\eula.1031.txt");
    	Assert.assertTrue( fileId > 0);
    }

}
