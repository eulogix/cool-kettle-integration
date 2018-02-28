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

public class FileRepositoryApi 
{
	protected App app;
	
	public FileRepositoryApi(App app)
	{
		this.app = app;
	}
	
	public String getVersion() {
		Client client = this.app.getClient();
		
		WebResource webResource = client.resource(this.app.getApiUrl("/cool/api/filerepo/version"));   
		
		ClientResponse response = webResource.get(ClientResponse.class);
	    EntityTag e = response.getEntityTag();
	    String entity = response.getEntity(String.class);
		
		if( response.getStatus() == 200) {
		
			JsonElement root = new JsonParser().parse(entity);
			return root.getAsJsonObject().get("version").getAsString();
		}
		
		return String.valueOf( response.getStatus() );
	}
	
	public boolean createFolder(String repositoryId, String repositoryParameters, String folderPath, String folderName) {
		Client client = this.app.getClient();
				
		Form form = new Form();
		form.add("repositoryId", repositoryId);
		form.add("repositoryParameters", repositoryParameters);
		form.add("folderPath", folderPath);
		form.add("folderName", folderName);
		  
		WebResource webResource = client.resource(this.app.getApiUrl("/cool/api/filerepo/createFolder"));   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
		return ( response.getStatus() == 200);
	}
	
	public JsonObject uploadFile(String repositoryId, String repositoryParameters, String filePath, String fileName, String collisionStrategy, byte[] fileContent) {
		Client client = this.app.getClient();
		
		Form form = new Form();
		form.add("repositoryId", repositoryId);
		form.add("repositoryParameters", repositoryParameters);
		form.add("filePath", filePath);
		form.add("fileName", fileName);
		form.add("fileContent",  Base64.getEncoder().encodeToString(fileContent));
		form.add("collisionStrategy",  collisionStrategy);
		  
		WebResource webResource = client.resource(this.app.getApiUrl("/cool/api/filerepo/upload"));   
		
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
	
	public JsonObject uploadFile(String repositoryId, String repositoryParameters, String filePath, String fileName, String collisionStrategy, String fileSystemPath) throws IOException {
		Path path = Paths.get(fileSystemPath);
		byte[] fileContent = Files.readAllBytes(path);
		return uploadFile(repositoryId, repositoryParameters, filePath, fileName, collisionStrategy, fileContent);
	}
	
	public boolean deleteFile(String repositoryId, String repositoryParameters, String filePath) {
		Client client = this.app.getClient();
				
		Form form = new Form();
		form.add("repositoryId", repositoryId);
		form.add("repositoryParameters", repositoryParameters);
		form.add("filePath", filePath);
		  
		WebResource webResource = client.resource(this.app.getApiUrl("/cool/api/filerepo/delete"));   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
		return ( response.getStatus() == 200);
	}
}
