package com.eulogix.cool.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class App 
{
	private String
	url, userName, password;
	
	public App(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
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
	
	public int uploadFile(String schemaName, String actualSchema, String tableName, String pk, String fileName, String category, byte[] fileContent) {
		Client client = this.getClient();
		
		Form form = new Form();
		form.add("schemaName", schemaName);
		form.add("actualSchema", actualSchema);
		form.add("table", tableName);
		form.add("pk", pk);
		form.add("fileName", fileName);
		form.add("category", category);
		form.add("fileContent",  Base64.getEncoder().encodeToString(fileContent));
		  
		WebResource webResource = client.resource(this.url + "/cool/api/files/upload");   
		
		ClientResponse response = webResource.
		     type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
		     .post(ClientResponse.class, form);
		EntityTag e = response.getEntityTag();
	    String entity = response.getEntity(String.class);
	    
		if( response.getStatus() == 200) {	
			JsonElement root = new JsonParser().parse(entity);
			return root.getAsJsonObject().get("fileId").getAsInt();
		}
		
		return -1;
	}
	
	public int uploadFile(String schemaName, String actualSchema, String tableName, String pk, String fileName, String category, String fileSystemPath) throws IOException {
		Path path = Paths.get(fileSystemPath);
		byte[] fileContent = Files.readAllBytes(path);
		return uploadFile(schemaName, actualSchema, tableName, pk, fileName, category, fileContent) ;
	}
	
	private Client getClient() {
		HTTPBasicAuthFilter basicAuthentication = new HTTPBasicAuthFilter(this.userName, this.password);
		
		Client client = ApacheHttpClient.create();
		client.addFilter(basicAuthentication); 
		
		return client;
	}
}
