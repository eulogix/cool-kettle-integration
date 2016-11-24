package com.eulogix.cool.lib;

public class KettleAppConfiguration {

	private String
		name, url, user, password;
	
	public KettleAppConfiguration(String name, String url, String user,
			String password) {
		super();
		this.name = name;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
		
}
