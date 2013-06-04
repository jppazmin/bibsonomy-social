/**
 *
 *  BibSonomy-Scraper - Web page scrapers returning BibTeX for BibSonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.scraper.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.util.ValidationUtils;

/**
 * 
 * TODO: remove this class and use Spring for configuration.
 * 
 * @author rja
 * @version $Id: ConfigUtil.java,v 1.10 2011-04-29 07:24:26 bibsonomy Exp $
 */
public class ConfigUtil {
	/**
	 * Logger
	 */
	private static final Log log = LogFactory.getLog(ConfigUtil.class);

	/**
	 * path so settings.properties
	 */
	private static final String PATH_TO_SETTINGSFILE = "scraper.properties";

	/** Loads the configuration file for the scrapers. If no file could be found,
	 *  empty properties are returned.
	 * 
	 * @return The configuration properties.
	 */
	public static Properties loadProperties() {
		final Properties properties = new Properties();
		try {
			properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream(PATH_TO_SETTINGSFILE));
		} catch (FileNotFoundException ex) {
			log.error("Could not load properties.", ex);
		} catch (IOException ex) {
			log.error("Could not load properties.", ex);
		}
		return properties;
	}
	
	/** Returns the given environment variable, or <code>null</code> if it could not be found.
	 * 
	 * @param key - the name of the environment variable.
	 * @return The value.
	 */
	public static String getEnvironmentVariable(final String key) {
		String string = null;
		try {
			string = ((String) ((Context) new InitialContext().lookup("java:/comp/env")).lookup(key));
			if (ValidationUtils.present(string)) log.debug("Got config variable '" + key + "' via JNDI.");
		} catch (NamingException ex) {
			log.debug("Could not get config variable '" + key + "'.", ex);
		}
		
		/*
		 * try to use system environment variables
		 */
		if (!ValidationUtils.present(string)) {
			string = System.getenv(key);
			if (ValidationUtils.present(string)) log.debug("Got config variable '" + key + "' via system environment.");
		}
		
		if (!ValidationUtils.present(string)) {
			log.warn("Could not get config variable '" + key + "'.");
		}
		
		return string;
	}
	
}
