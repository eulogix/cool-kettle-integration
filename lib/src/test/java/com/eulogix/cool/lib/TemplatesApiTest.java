package com.eulogix.cool.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eulogix.cool.lib.App;
import com.eulogix.cool.lib.mocks.mockFactory;
import com.eulogix.cool.lib.pentaho.Environment;
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

public class TemplatesApiTest {
	
	@Test
    public void testApiVersion()
    {
		App app = Utils.getApp();
    	Assert.assertEquals( "0.1", app.getTemplatesApi().getVersion() );
    }
    
    @Test
    public void testRenderTemplate()
    {
    	App app = Utils.getApp();
    	String insertedId;
		insertedId = app.getFileRepositoryApi().uploadFile(
				Utils.getTestFileRepositoryId(), 
				"{}",
				"/", 
				"template.html.twig", 
				"overwrite", 
				Utils.getFileContent("simpleTemplate.html.twig")
			).get("id").getAsString();
		
		Assert.assertTrue(insertedId.length()>0);
		
		JsonObject o = app.getTemplatesApi().render(
				Utils.getTestFileRepositoryId(), 
				"{}", 
				"/template.html.twig", 
				"{\"simpleVar\":\"hello\"}",
				"pdf",
				"{}");
		
		System.out.println(o);
    }
}
