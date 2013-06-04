package org.bibsonomy.opensocial.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.shindig.common.util.ResourceLoader;

import com.google.inject.AbstractModule;
import com.google.inject.CreationException;
import com.google.inject.name.Names;
import com.google.inject.spi.Message;

public class BibSonomyPropertiesModule extends AbstractModule {
	private static final String SHINDIG_PROPERTIES = "shindig.properties";
	private final Properties shindigProperties;

	public BibSonomyPropertiesModule() {
		this.shindigProperties = readPropertyFile("shindig.properties");
	}

	public BibSonomyPropertiesModule(String shindigPropertyFile) {
		this.shindigProperties = readPropertyFile(shindigPropertyFile);
	}

	public BibSonomyPropertiesModule(Properties shindigProperties) {
		this.shindigProperties = shindigProperties;
	}

	protected void configure() {
		Names.bindProperties(binder(), this.shindigProperties);
		String hostname = getServerHostname();
	}

	protected String getServerHostname() {
		return System.getProperty("jetty.host") != null ? System
				.getProperty("jetty.host")
				: System.getProperty("shindig.host") != null ? System
						.getProperty("shindig.host") : "localhost";
	}

	private Properties readPropertyFile(String propertyFile) {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = ResourceLoader.openResource(propertyFile);
			properties.load(is);
		} catch (IOException e) {
			throw new CreationException(
					Arrays.asList(new Message[] { new Message(
							"Unable to load properties: " + propertyFile) }));
		} finally {
			IOUtils.closeQuietly(is);
		}

		return properties;
	}
}