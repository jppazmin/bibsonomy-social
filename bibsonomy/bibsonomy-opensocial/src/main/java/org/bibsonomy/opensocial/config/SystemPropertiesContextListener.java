package org.bibsonomy.opensocial.config;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SystemPropertiesContextListener implements ServletContextListener {
	private static final String SHINDIG_PORT = "shindig.port";
	private static final String SHINDIG_HOST = "shindig.host";
	private static final String PROJECT_HOME = "projectHome";
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_PORT = "80";
	ServletContext context;

	public void contextInitialized(ServletContextEvent event) {
		this.context = event.getServletContext();

		String projectHome = this.context.getInitParameter("projectHome");

		String hostName = "localhost";
		String hostPort = "80";
		try {
			URL url = new URL(projectHome);
			hostName = url.getHost();

			if (url.getPort() > 0)
				hostPort = Integer.toString(url.getPort());
		} catch (MalformedURLException e) {
		}
		System.setProperty("shindig.host", hostName);
		System.setProperty("shindig.port", hostPort);
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}