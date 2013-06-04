package org.bibsonomy.opensocial.config;

import org.apache.shindig.common.PropertiesModule;

import com.google.inject.AbstractModule;

public class CustomPropertiesModule extends AbstractModule {
	protected void configure() {
		install(new PropertiesModule("bibsonomy-shindig.properties"));
	}
}