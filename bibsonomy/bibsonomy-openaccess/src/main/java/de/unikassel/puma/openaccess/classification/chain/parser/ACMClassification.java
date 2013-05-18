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


import static org.bibsonomy.util.ValidationUtils.present;

import java.util.LinkedHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import de.unikassel.puma.openaccess.classification.ClassificationObject;
import de.unikassel.puma.openaccess.classification.ClassificationXMLParser;


public class ACMClassification extends ClassificationXMLParser {

	private static final String NAME = "ACM";

	private static final String DELIMITER = ".";
	
	private StringBuffer buf = new StringBuffer();
	
	private boolean skip = false;
	private String skipElement = "";

	private String startNode = "";
	private String startDescription = "";
	
	
	@Override
	public void startDocument() {
		classifications = new LinkedHashMap<String, ClassificationObject>();
		buf = new StringBuffer();
	}

	@Override
	public void endDocument() {
	}

	@Override
	public void startElement (final String uri, final String name, final String qName, final Attributes atts) throws SAXException {
		
		if(skip)
			return;

		if ("node".equals(qName)) {

			if(atts.getLength() == 2) {
				
				if(atts.getLocalName(0).equals("id") && atts.getLocalName(1).equals("label")) {
					String id = atts.getValue(0);
					
					if(id.equals("acmccs98"))
						return;
					
					if(id.length() < 4 && !id.endsWith(".")) {
						startNode = id;
						startDescription = atts.getValue(1);
					} else {
//						 += ".";
						
						if(present(startNode)) {
							startNode += ".";
							classificate(startNode, startDescription);
							
							startDescription = startNode = "";
						}
						
						classificate(id, atts.getValue(1));
					}
				}
			}
			
		} else if("isComposedBy".equals(qName)) {

		} else if("isRelatedTo".equals(qName)) {
			skip = true;
			skipElement = "isRelatedTo";
				
		} else if("hasNote".equals(qName)) {
			skip = true;
			skipElement = "hasNote";
			
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
		if(skip) {
			
			if(qName.equals(skipElement)) {
				skip = false;
				skipElement = "";
			}
			
		} else if(present(startNode)) {
			classificate(startNode, startDescription);
			startNode = startDescription = "";
		}
		
	}
	
	private void requClassificate(String name, String description, ClassificationObject object) {
		if(name.isEmpty())
			return;
		
		int delimiter = name.indexOf('.') +1;
		String actual;
		
		if(delimiter != 0) {
			actual = name.substring(0, delimiter);
			name = name.substring(delimiter, name.length());
		} else {
			actual = name;
			name = "";
		}
	
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
	
	/*
	private String removeUnusedChars(String name) {
		StringBuffer str = new StringBuffer(name);
		
		int i = 0;
		while(i < str.length()) {
			if(str.charAt(i) == '.') {
				str.deleteCharAt(i);
				continue;
			}
			i++;
		}
		
		return str.toString();
	}
	*/
	
	private void classificate(String name, String description) {
		int delimiter = name.indexOf('.') +1;
		String actual;
		
		if(delimiter != 0) {
			actual = name.substring(0, delimiter);
			name = name.substring(delimiter, name.length());
		} else {
			actual = name;
			name = "";
		}
	
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
		return DELIMITER;
	}


}
