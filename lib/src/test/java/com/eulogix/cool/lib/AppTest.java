package com.eulogix.cool.lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.eulogix.cool.lib.App;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.pentaho.di.core.row.RowDataUtil;

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
    public void testUploadFileFromFsAndProperties() throws IOException
    {
    	App app = this.getApp();
    	String insertedId = app.uploadFile("core", "core", "account", "1", "eula.txt", "UNCATEGORIZED", "overwrite", "D:\\eula.1031.txt")
    			.get("id")
    			.getAsString();
    	Assert.assertTrue( insertedId != null);
    
    	JsonObject props = new JsonParser().parse("{\"jprop1\":\"jvalue1\",\"jprop2\":\"jvalue2\"}").getAsJsonObject();
    	app.setFileProperties("core", "core", insertedId, props.toString(), false);
    	
    	Map<String, Object> retrievedProps = app.getFilePropertiesAsMap("core", "core", insertedId);
    	Assert.assertEquals(2, retrievedProps.size());
    	Assert.assertEquals("jvalue1", retrievedProps.get("jprop1"));
    	
    	HashMap<String,String> someProps = new HashMap<String,String>(); 
    	someProps.put("jprop1", "jvalue11");
    	someProps.put("jprop3", "jvalue3");
    	
    	app.setFileProperties("core", "core", insertedId, someProps, true);
    	
    	retrievedProps = app.getFilePropertiesAsMap("core", "core", insertedId);
    	Assert.assertEquals(3, retrievedProps.size());
    	Assert.assertEquals("jvalue11", retrievedProps.get("jprop1"));
    	Assert.assertEquals("jvalue2", retrievedProps.get("jprop2"));
    	Assert.assertEquals("jvalue3", retrievedProps.get("jprop3"));
    	
    }
    
    @Test
    public void testSearchFiles() throws IOException
    {
    	App app = this.getApp();
    	
    	JsonObject search;
		JsonObject file;
		JsonArray files;
		
		int totalPages, page = 0;
		
    	search = app.searchFiles("core", "core", null, null, "UNCATEGORIZED", null, true, page);
    	    	 
    	System.out.println( search );
		totalPages = search.get("pages_count").getAsInt();
		files = search.get("files").getAsJsonArray();
		 
		 for(int i=0; i<files.size(); i++) {
			 file = files.get(i).getAsJsonObject();
			 System.out.println(file.get("file_id").getAsString() + " " + file.get("file_name").getAsString() + " " + file.get("path").getAsString());
		 }
    	
    }
    
    @Test
    public void testSendNotification() throws IOException
    {
    	App app = this.getApp();
    	boolean notificationSent = app.sendNotification(1, "Java test", "hams_test", "");
    	Assert.assertTrue( notificationSent );    	
    }
}
