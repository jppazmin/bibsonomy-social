/**
 *
 *  BibSonomy-OpenAccess - Check Open Access Policies for Publications
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

package de.unikassel.puma.openaccess.classification.chain.parser;

import java.util.LinkedHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import de.unikassel.puma.openaccess.classification.ClassificationObject;
import de.unikassel.puma.openaccess.classification.ClassificationXMLParser;

public class JELClassification extends ClassificationXMLParser {

	private static final String NAME = "JEL";
	
	private StringBuffer buf = new StringBuffer();
	
	private String code;
	private String description;
	
	@Override
	public void startDocument() {
		classifications = new LinkedHashMap<String, ClassificationObject>();
		buf = new StringBuffer();
		code = "";
		description = "";
	}

	@Override
	public void endDocument() {
	}

	@Override
	public void startElement (final String uri, final String name, final String qName, final Attributes atts) throws SAXException {
		if ("code".equals(qName)) {

		} else if("description".equals(qName)) {

		} else if("data".equals(qName) || "classification".equals(qName)) {
			//no op
		} else {
			throw new SAXException("Unable to parse");
		}
		buf = new StringBuffer();
	}

	/** Collect characters.
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters (final char ch[], final int start, final int length) {
		buf.append(ch, start, length);
	}

	@Override
	public void endElement (final String uri, final String name, final String qName) throws SAXException {
		if ("code".equals(qName)) {
			this.code = buf.toString();
		} else if("description".equals(qName)) {
			this.description = buf.toString();
			classificate(code, description);
			code = "";
			description = "";
			
		} else if("data".equals(qName) || "classification".equals(qName)) {
			//no op
		} else {
			throw new SAXException("Unable to parse");
		}
	}
	
	private void requClassificate(String name, String description, ClassificationObject object) {
		String actual = name.charAt(0) +"";
		name = name.substring(1);
	
		if(object.getChildren().containsKey(actual)) {
			requClassificate(name, description, object.getChildren().get(actual));
		
		} else {
			if(name.isEmpty()) {
				ClassificationObject co = new ClassificationObject(actual, description);
				object.addChild(actual, co);
				
			} else {
				ClassificationObject co = new ClassificationObject(actual, description);
				object.addChild(actual, co);
				requClassificate(name, description, co);
			}
		}
	}
	

	private void classificate(String name, String description) {
		String actual = name.charAt(0) +"";
		name = name.substring(1);
	
		if(classifications.containsKey(actual)) {
			requClassificate(name, description, classifications.get(actual));
		} else {
			ClassificationObject co = new ClassificationObject(actual, description);
			classifications.put(actual, co);
			requClassificate(name, description, co);
		}
	}
	
	public String getName() {
		return NAME;
	}

	@Override
	public String getDelimiter() {
		return null;
	}

}
