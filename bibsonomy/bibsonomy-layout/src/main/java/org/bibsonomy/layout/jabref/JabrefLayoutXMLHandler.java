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

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Callback handler for the SAX parser.
 * 
 * @author:  rja
 * @version: $Id: JabrefLayoutXMLHandler.java,v 1.12 2011-04-29 07:34:00 bibsonomy Exp $
 * $Author: bibsonomy $
 * 
 */
public class JabrefLayoutXMLHandler extends DefaultHandler {

	private StringBuffer buf = new StringBuffer();

	private List<JabrefLayout> layoutDefinitions;
	
	private JabrefLayout currentLayoutDefinition;
	
	// need to save the attribute from the description element in the
	// startElement callback method to use it in the endElement method	
	private String languageAttribute;
	
	@Override
	public void startDocument() {
		 layoutDefinitions = new LinkedList<JabrefLayout>();
	}

	@Override
	public void endDocument() {
		// nothing to do
	}

	@Override
	public void startElement (final String uri, final String name, final String qName, final Attributes atts) {
		buf = new StringBuffer();
		if ("layout".equals(name)) {
			currentLayoutDefinition = new JabrefLayout(atts.getValue("name"));
			if (atts.getValue("public") != null){
				currentLayoutDefinition.setPublicLayout(Boolean.parseBoolean(atts.getValue("public")));
			}
		} else 	if ("description".equals(name)) {
			this.languageAttribute = atts.getValue("xml:lang");
		}
		
	}

	/** Collect characters.
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters (final char ch[], final int start, final int length) {
		/*
		 * replace arbitrary long sequences of whitespace by one space.
		 */
		final String s = new String(ch, start, length).replaceAll("\\s+", " ");
		buf.append(s);
		
	}

	@Override
	public void endElement (final String uri, final String name, final String qName) {
		
		if ("layout".equals(name)) {
			layoutDefinitions.add(currentLayoutDefinition);
		} else if ("displayName".equals(name)) {
			currentLayoutDefinition.setDisplayName(getBuf());
		} else if ("baseFileName".equals(name)) {
			currentLayoutDefinition.setBaseFileName(getBuf());
		} else if ("directory".equals(name)) {
			currentLayoutDefinition.setDirectory(getBuf());
		} else if ("description".equals(name)) {
			currentLayoutDefinition.setDescription(this.languageAttribute, getBuf());
		} else if ("extension".equals(name)) {
			currentLayoutDefinition.setExtension(getBuf());
		} else if ("mimeType".equals(name)) {
			currentLayoutDefinition.setMimeType(getBuf());
		} 
	}

	private String getBuf() {
		return buf.toString().trim();
	}

	public List<JabrefLayout> getLayouts() {
		return layoutDefinitions;
	}

}

