/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package servlets.listeners;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** Listener that looks up some configuration variables when
 * the Web application is first loaded. Stores this
 * name in some servlet context attributes.
 * Various servlets and JSP pages will extract them
 * from that location.
 * 
 * by using this listener to set certain static (in the sense of the project)
 * variables, these variables can be accessed in every JSP by just writing
 * ${projectName}
 * for example
 * 
 */
public class InitialConfigListener implements ServletContextListener {
	private static ServletContext servletContext = null;
	
	/** This method is called first! 
	 * Looks up the configuration variables, configures proxy.
	 * 
	 *	init parameters and puts them into the servlet context.
	 */
	@Override
	public void contextInitialized(final ServletContextEvent event) {
		servletContext = event.getServletContext();
		@SuppressWarnings("unchecked")
		final Enumeration<String> e = servletContext.getInitParameterNames();
		while (e.hasMoreElements()) {
			final String initParamName = e.nextElement();
			servletContext.setAttribute(initParamName, servletContext.getInitParameter(initParamName));
		}
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent event) {}

	public static String getInitParam(final String name) {
		return (String) servletContext.getAttribute(name);
	}
}