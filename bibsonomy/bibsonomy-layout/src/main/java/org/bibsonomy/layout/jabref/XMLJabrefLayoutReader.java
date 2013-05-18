/**
 *
 *  BibSonomy-Layout - Layout engine for the webapp.
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

package org.bibsonomy.layout.jabref;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Reads a jabref layout definition XML file and it returns a list of {@link JabrefLayout}s.
 * 
 * @author: rja
 * @version: $Id: XMLJabrefLayoutReader.java,v 1.7 2011-04-29 07:34:00 bibsonomy Exp $ $Author: bibsonomy $
 * 
 */
public class XMLJabrefLayoutReader {

	private Reader reader;

	public XMLJabrefLayoutReader(final Reader reader) {
		this.reader = reader;
	}

	/**
	 * Reads a list of {@link JabrefLayout}s.
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<JabrefLayout> getJabrefLayoutsDefinitions() throws IOException {
		try {
			final XMLReader xr = XMLReaderFactory.createXMLReader();
			/*
			 * SAX callback handler
			 */
			final JabrefLayoutXMLHandler handler = new JabrefLayoutXMLHandler();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			xr.parse(new InputSource(reader));

			return handler.getLayouts();
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}
}
