/**
 *
 *  BibSonomy-Rest-Common - Common things for the REST-client and server.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.rest.renderer.impl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.bibsonomy.rest.renderer.UrlRenderer;

/**
 * This class creates xml documents valid to the xsd schema and vice-versa.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: XMLRenderer.java,v 1.79 2011-06-09 11:59:14 rja Exp $
 */
public class XMLRenderer extends JAXBRenderer {

	public XMLRenderer(final UrlRenderer urlRenderer) {
		super(urlRenderer);
	}

	
	@Override
	protected JAXBContext getJAXBContext() throws JAXBException {
		/*
		 * XXX: initialize JAXB context. We provide the classloader here because
		 * we experienced that under certain circumstances (e.g. when used
		 * within JabRef as a JPF-Plugin), the wrong classloader is used which
		 * has the following exception as consequence:
		 * 	javax.xml.bind.JAXBException: "org.bibsonomy.rest.renderer.xml" doesnt contain ObjectFactory.class or jaxb.index
		 * 
		 * (see also http://ws.apache.org/jaxme/apidocs/javax/xml/bind/JAXBContext.html)
		 */
		return JAXBContext.newInstance(JAXB_PACKAGE_DECLARATION, this.getClass().getClassLoader());
	}
}