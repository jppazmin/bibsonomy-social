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

import java.io.Reader;
import java.io.Writer;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.bibsonomy.rest.renderer.UrlRenderer;
import org.bibsonomy.rest.renderer.xml.BibsonomyXML;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;

/**
 * @author dzo
 * @version $Id: JSONRenderer.java,v 1.5 2011-06-09 11:59:14 rja Exp $
 */
public class JSONRenderer extends JAXBRenderer {
	
	public JSONRenderer(final UrlRenderer urlRenderer) {
		super(urlRenderer);
	}
	
	@Override
	protected JAXBContext getJAXBContext() throws JAXBException {
		return new JSONJAXBContext(JSONConfiguration.natural().build(), JAXB_PACKAGE_DECLARATION, this.getClass().getClassLoader(), Collections.<String, Object>emptyMap());
	}
	
	@Override
	protected void marshal(Marshaller marshaller, JAXBElement<BibsonomyXML> webserviceElement, Writer writer) throws JAXBException {
		final JSONMarshaller jsonMarshaller = (JSONMarshaller) marshaller;
		jsonMarshaller.marshallToJSON(webserviceElement, writer);
	}
	
	@Override
	protected JAXBElement<BibsonomyXML> unmarshal(Unmarshaller unmarshaller, Reader reader) throws JAXBException {
		final JSONUnmarshaller jsonUnmarshaller = (JSONUnmarshaller) unmarshaller;
		return jsonUnmarshaller.unmarshalJAXBElementFromJSON(reader, BibsonomyXML.class);
	}
}
