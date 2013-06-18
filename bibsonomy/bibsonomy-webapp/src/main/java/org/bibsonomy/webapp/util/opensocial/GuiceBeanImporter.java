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

package org.bibsonomy.webapp.util.opensocial;

import static org.bibsonomy.util.ValidationUtils.present;

import javax.servlet.ServletContext;

import org.apache.shindig.common.servlet.GuiceServletContextListener;
import org.springframework.web.context.ServletContextAware;

import com.google.inject.Injector;

/**
 * Class for making shindig beans available to the spring configuration
 * 
 * @author fei
 * @version $Id: GuiceBeanImporter.java,v 1.1 2011-05-11 13:45:01 folke Exp $
 */
public class GuiceBeanImporter implements ServletContextAware{
	
	/** the servlet context */
	private ServletContext servletContext;
	
	/** guice injector */
	private Injector injector;

	/** 
	 * called when all properties are configured
	 */
	public void init() {
		this.injector  = (Injector) this.servletContext.getAttribute(GuiceServletContextListener.INJECTOR_ATTRIBUTE);
	}
	
	/**
	 * try to get the referenced bean
	 * @throws ClassNotFoundException 
	 */
	public Object getGuiceBean(String className) throws ClassNotFoundException {
		if (present(injector)) {
			return (injector.getInstance(this.getClass().getClassLoader().loadClass(className)));
		} else {
			throw new RuntimeException("No guice injector found.");
		}
	}

	//------------------------------------------------------------------------
	// getter/setter
	//------------------------------------------------------------------------
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
