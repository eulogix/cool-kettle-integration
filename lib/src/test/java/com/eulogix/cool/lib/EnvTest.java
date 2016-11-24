package com.eulogix.cool.lib;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import com.eulogix.cool.lib.mocks.mockFactory;
import com.eulogix.cool.lib.pentaho.Environment;

public class EnvTest {

	@Test
	public void test() {
		Environment env = mockFactory.getMockEnvironment();
		ArrayList<KettleAppConfiguration> apps = env.getConfiguredCoolApps();
		Assert.assertEquals( 3, apps.size() );
	}

}
