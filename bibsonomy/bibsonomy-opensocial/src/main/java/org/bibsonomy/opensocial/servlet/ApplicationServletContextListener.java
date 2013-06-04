package org.bibsonomy.opensocial.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bibsonomy.opensocial.config.ApplicationContextFactory;

public class ApplicationServletContextListener implements
		ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
	}

	public void contextDestroyed(ServletContextEvent event) {
		ApplicationContextFactory.getApplicationContext().destroy();
	}
}