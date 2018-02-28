package com.eulogix.cool.lib.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;

import com.eulogix.cool.lib.App;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class TemplatesApi 
{
	protected App app;
	
	public TemplatesApi(App app)
	{
		this.app = app;
	}
	
	public String getVersion() {
		Client client = this.app.getClient();
		
		WebResource webResource = client.resource(this.app.getApiUrl("/cool/api/templates/version"));   
		
		ClientResponse response = webResource.get(ClientResponse.class);
	    EntityTag e = response.getEntityTag();
	    String entity = response.getEntity(String.class);
		
		if( response.getStatus() == 200) {
		
			JsonElement root = new JsonParser().parse(entity);
			return root.getAsJsonObject().get("version").getAsString();
		}
		
		return String.valueOf( response.getStatus() );
	}
	
	public JsonObject render(String repositoryId, String repositoryParameters, String templatePath, String templateData, String outputFormat, String rendererParameters) {
		Client client = this.app.getClient();
		
		Form form = new Form();
		form.add("repositoryId", repositoryId);
		form.add("repositoryParameters", repositoryParameters);
		form.add("templatePath", templatePath);
		form.add("templateData", templateData);
		form.add("outputFormat",  outputFormat);
		form.add("rendererParameters",  rendererParameters);
		  
		WebResource webResource = client.resource(this.app.getApiUrl("/cool/api/templates/render"));   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
	    String entity = response.getEntity(String.class);
	    
	    System.out.println(entity);
	    
		if( response.getStatus() == 200) {	
			JsonElement root = new JsonParser().parse(entity);
			return root.getAsJsonObject();
		}
		
		return null;
	}
}
