package com.eulogix.cool.lib.mocks;

import java.util.HashMap;
import java.util.Map;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.variables.Variables;

import com.eulogix.cool.lib.pentaho.Environment;

public class mockFactory {

	public static Environment getMockEnvironment() {
		Variables vars = new Variables();	
		vars.initializeVariablesFrom(vars);
		vars.injectVariables(getMockVars());
		return new Environment(vars);
	}
	
	private static Map<String, String> getMockVars() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("AUTO_MIS_DB_HOST","127.0.0.1");
		map.put("AUTO_MIS_DB_NAME","mis_am_agos");
		map.put("AUTO_MIS_DB_PASSWORD","");
		map.put("AUTO_MIS_DB_PORT","3306");
		map.put("AUTO_MIS_DB_USER","root");
		map.put("AUTO_MIS_SYSTEM_USER_ID","141");
		map.put("AUTO_MIS_WS_USER","wsuser");
		map.put("AUTO_MISSHARE_DB_HOST","127.0.0.1");
		map.put("AUTO_MISSHARE_DB_NAME","misshare_misbeta");
		map.put("AUTO_MISSHARE_DB_PASSWORD","");
		map.put("AUTO_MISSHARE_DB_PORT","3306");
		map.put("AUTO_MISSHARE_DB_USER","root");
		map.put("AUTO_MISSTATIC_DB_HOST","127.0.0.1");
		map.put("AUTO_MISSTATIC_DB_NAME","misstatic");
		map.put("AUTO_MISSTATIC_DB_PORT","5432");
		map.put("AUTO_MISSTATIC_DB_USER","postgres");
		map.put("AUTO_SMTP_FROMNAME","MIS_DEVEL");
		map.put("AUTO_SMTP_HOST","127.0.0.1");
		map.put("AUTO_SMTP_PASSWORD","");
		map.put("AUTO_SMTP_PORT","25");
		map.put("AUTO_SMTP_REPLYTO","mis@devel.it");
		map.put("AUTO_SMTP_USER","");
		map.put("NPA_DB_HOST","192.168.56.106");
		map.put("NPA_DB_NAME","npa");
		map.put("NPA_DB_PORT","5432");
		map.put("NPA_DB_USER","postgres");
		map.put("HAMS_DB_HOST","192.168.56.106");
		map.put("HAMS_DB_NAME","hams");
		map.put("HAMS_DB_PORT","5432");
		map.put("HAMS_DB_USER","postgres");
		map.put("HAMS_URL","http://hams.devel/app_dev.php");
		map.put("HAMS_WS_USER","admin");
		map.put("MIS_DB_HOST","192.168.56.106");
		map.put("MIS_DB_NAME","hip_mis_es_triton");
		map.put("FRESHCORE_DB_NAME","hip_mis_freshcore");
		map.put("MISSHARE_DB_NAME","hip_mis_misshare");
		map.put("MIS_DB_PORT","3306");
		map.put("MIS_DB_USER","root");
		map.put("COOL_APP_HAMS_TEST_I","Hams TEST");
		map.put("COOL_APP_HAMS_TEST_URL","http://hams.devel/app_test.php");
		map.put("COOL_APP_HAMS_TEST_USER","demo");
		map.put("COOL_APP_HAMS_TEST_PASS","admin");
		map.put("COOL_APP_HAMS_DEV_I","Hams DEV");
		map.put("COOL_APP_HAMS_DEV_URL","http://hams.devel/app_dev.php");
		map.put("COOL_APP_HAMS_DEV_USER","demo");
		map.put("COOL_APP_HAMS_DEV_PASS","Administrator40");
		map.put("COOL_APP_HAMS_DEV_PROD_I","Hams DEV");
		map.put("COOL_APP_HAMS_DEV_PROD_URL","http://hams.devel/app_dev.php");
		map.put("COOL_APP_HAMS_DEV_PROD_USER","demo");
		map.put("COOL_APP_HAMS_DEV_PROD_PASS","Administrator40");
		map.put("KETTLE_BATCHING_ROWSET","N");
		map.put("KETTLE_CARTE_OBJECT_TIMEOUT_MINUTES","1440");
		map.put("KETTLE_CHANNEL_LOG_DB","");
		map.put("KETTLE_CHANNEL_LOG_SCHEMA","");
		map.put("KETTLE_CHANNEL_LOG_TABLE","");
		map.put("KETTLE_CORE_JOBENTRIES_FILE","");
		map.put("KETTLE_CORE_STEPS_FILE","");
		map.put("KETTLE_EMPTY_STRING_DIFFERS_FROM_NULL","N");
		map.put("KETTLE_JOB_LOG_DB","");
		map.put("KETTLE_JOB_LOG_SCHEMA","");
		map.put("KETTLE_JOB_LOG_TABLE","");
		map.put("KETTLE_JOBENTRY_LOG_DB","");
		map.put("KETTLE_JOBENTRY_LOG_SCHEMA","");
		map.put("KETTLE_JOBENTRY_LOG_TABLE","");
		map.put("KETTLE_LOG_SIZE_LIMIT","0");
		map.put("KETTLE_MAX_JOB_ENTRIES_LOGGED","1000");
		map.put("KETTLE_MAX_JOB_TRACKER_SIZE","1000");
		map.put("KETTLE_MAX_LOG_SIZE_IN_LINES","5000");
		map.put("KETTLE_MAX_LOG_TIMEOUT_IN_MINUTES","1440");
		map.put("KETTLE_MAX_LOGGING_REGISTRY_SIZE","1000");
		map.put("KETTLE_PLUGIN_CLASSES","");
		map.put("KETTLE_ROWSET_GET_TIMEOUT","50");
		map.put("KETTLE_ROWSET_PUT_TIMEOUT","50");
		map.put("KETTLE_SHARED_OBJECTS","");
		
		return map;
	}
}
