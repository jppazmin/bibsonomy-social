package org.bibsonomy.opensocial.config;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextFactory {
	private static final String[] LOCATIONS = { "application-context.xml" };

	private static final AbstractApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			LOCATIONS);

	public static AbstractApplicationContext getApplicationContext() {
		return CONTEXT;
	}
}