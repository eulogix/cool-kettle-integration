package com.eulogix.cool.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;

import com.eulogix.cool.lib.api.FileRepositoryApi;
import com.eulogix.cool.lib.api.TemplatesApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;


public class App 
{
	private String url, userName, password;
	
	private FileRepositoryApi fileRepositoryApi;
	private TemplatesApi templatesApi;
	
	public App(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}
	
	public FileRepositoryApi getFileRepositoryApi() {
		if(fileRepositoryApi == null)
			fileRepositoryApi = new FileRepositoryApi(this);
		return fileRepositoryApi;
	}
	
	public TemplatesApi getTemplatesApi() {
		if(templatesApi == null)
			templatesApi = new TemplatesApi(this);
		return templatesApi;
	}

	public String getApiVersion() {
		Client client = this.getClient();
		
		WebResource webResource = client.resource(this.url + "/cool/api/files/version");   
		
		ClientResponse response = webResource.get(ClientResponse.class);
	    EntityTag e = response.getEntityTag();
	    String entity = response.getEntity(String.class);
		
		if( response.getStatus() == 200) {
		
			JsonElement root = new JsonParser().parse(entity);
			return root.getAsJsonObject().get("version").getAsString();
		}
		
		return String.valueOf( response.getStatus() );
	}
	
	public JsonObject searchFiles(String schemaName, String actualSchema, String tableName, String pk, String category, String fileName, boolean fetchContent, int page) {
		Client client = this.getClient();
		
		Form form = new Form();
		form.add("schemaName", schemaName);
		form.add("actualSchema", actualSchema);
		form.add("table", tableName);
		form.add("pk", pk);
		form.add("category", category);
		form.add("name", fileName);
		form.add("page", page);
		form.add("fetchContent", fetchContent ? "true" : "false");
		  
		WebResource webResource = client.resource(this.url + "/cool/api/files/search");   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
	    String entity = response.getEntity(String.class);
	    
		if( response.getStatus() == 200) {	
			JsonElement root = new JsonParser().parse(entity);
			return root.getAsJsonObject();
		}
		
		return null;
	}
	
	public JsonObject uploadFile(String schemaName, String actualSchema, String tableName, String pk, String fileName, String category, String collisionStrategy, byte[] fileContent) {
		Client client = this.getClient();
		
		Form form = new Form();
		form.add("schemaName", schemaName);
		form.add("actualSchema", actualSchema);
		form.add("table", tableName);
		form.add("pk", pk);
		form.add("fileName", fileName);
		form.add("category", category);
		form.add("fileContent",  Base64.getEncoder().encodeToString(fileContent));
		form.add("collisionStrategy",  collisionStrategy);
		  
		WebResource webResource = client.resource(this.url + "/cool/api/files/upload");   
		
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
	
	public JsonObject uploadFile(String schemaName, String actualSchema, String tableName, String pk, String fileName, String category, String collisionStrategy, String fileSystemPath) throws IOException {
		Path path = Paths.get(fileSystemPath);
		byte[] fileContent = Files.readAllBytes(path);
		return uploadFile(schemaName, actualSchema, tableName, pk, fileName, category, collisionStrategy, fileContent);
	}
	
	public boolean deleteFile(String schemaName, String actualSchema, String fileId) {
		Client client = this.getClient();
				
		Form form = new Form();
		form.add("schemaName", schemaName);
		form.add("actualSchema", actualSchema);
		form.add("fileId", fileId);
		  
		WebResource webResource = client.resource(this.url + "/cool/api/files/delete");   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
		return ( response.getStatus() == 200);
	}
	
	public boolean setFileProperties(String schemaName, String actualSchema, String fileId, String properties, boolean merge) {
		Client client = this.getClient();
				
		Form form = new Form();
		form.add("schemaName", schemaName);
		form.add("actualSchema", actualSchema);
		form.add("fileId", fileId);
		form.add("fileProperties", properties);
		if(merge) 
			form.add("merge", "1");
		  
		WebResource webResource = client.resource(this.url + "/cool/api/files/setProperties");   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
		return ( response.getStatus() == 200);
	}
	
	public boolean setFileProperties(String schemaName, String actualSchema, String fileId, Map<String,String> properties, boolean merge) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(properties);
		return setFileProperties(schemaName, actualSchema, fileId, json, merge);
	}
	
	public JsonObject getFileProperties(String schemaName, String actualSchema, String fileId) {
		Client client = this.getClient();
		
		Form form = new Form();
		form.add("schemaName", schemaName);
		form.add("actualSchema", actualSchema);
		form.add("fileId", fileId);
		  
		WebResource webResource = client.resource(this.url + "/cool/api/files/getProperties");   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
		String entity = response.getEntity(String.class);
	    
		if( response.getStatus() == 200) {	
			JsonElement root = new JsonParser().parse(entity);
			return root.getAsJsonObject();
		}
		
		return null;
	}
	
	public Map<String,Object> getFilePropertiesAsMap(String schemaName, String actualSchema, String fileId) {
		JsonObject jProps = getFileProperties(schemaName, actualSchema, fileId);
		if(jProps != null) {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			map = (Map<String,Object>) new Gson().fromJson(jProps, Map.class);
			return map;
		}
		return null;
	}
	
	public boolean sendNotification(int userId, String message, String context, String jsonData) {
		Client client = this.getClient();
				
		Form form = new Form();
		form.add("userId", userId);
		form.add("message", message);
		form.add("context", context);
		form.add("jsonData", jsonData);
				  
		WebResource webResource = client.resource(this.url + "/cool/api/notifications/sendUserNotification");   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		
		return ( response.getStatus() == 200);
	}
	
	public Client getClient() {
		HTTPBasicAuthFilter basicAuthentication = new HTTPBasicAuthFilter(this.userName, this.password);
		
		Client client = ApacheHttpClient.create();
		client.addFilter(basicAuthentication); 
		
		return client;
	}
	
	public String getApiUrl(String relativeUrl) {
		return this.url + relativeUrl;
	} 
}
