package com.eulogix.cool.lib.pentaho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.variables.Variables;

import com.eulogix.cool.lib.KettleAppConfiguration;

public class Environment {
	
	protected Variables variables;
	protected VariableSpace space;
	
	public Environment(Variables vars) {
		this.variables = vars;
	}
	
	public Environment(VariableSpace space) {
		Variables vars = new Variables();	
		vars.initializeVariablesFrom(space);
		this.variables = vars;
	}
	
	public ArrayList<KettleAppConfiguration> getConfiguredCoolApps() {
		ArrayList<KettleAppConfiguration> apps = new ArrayList<KettleAppConfiguration>();
		
		String[] vars = variables.listVariables();
		Pattern containerRegex = Pattern.compile("^COOL_APP_(.+?)_I$"), propertyRegex;
		Matcher m, m2; 
		
		String appToken;
		Map<String, String> appProperties;
		
		for(int i=0;i<vars.length;i++) {
			m = containerRegex.matcher(vars[i]);
			if (m.find( )) {
				appProperties = new HashMap<String, String>();
				appProperties.put("NAME", variables.getVariable(vars[i]));
				
				appToken = m.group(1);
				propertyRegex = Pattern.compile("^COOL_APP_" + appToken + "_(?!I)(.+?)$");
				for(int j=0;j<vars.length;j++) {
					m2 = propertyRegex.matcher(vars[j]);
					if(m2.find()) {
						appProperties.put(m2.group(1), variables.getVariable(vars[j]));	
					}
				}
				
				apps.add(new KettleAppConfiguration(appProperties.get("NAME"), appProperties.get("URL"), appProperties.get("USER"), appProperties.get("PASS")));
		    }
		}
		
		return apps;
	}
}
