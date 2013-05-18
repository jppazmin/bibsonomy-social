/**
 *
 *  BibSonomy-Web-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.util;

import it.unimi.dsi.fastutil.chars.CharOpenHashSet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * Some utility functions for working with XML
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: XmlUtils.java,v 1.2 2010-09-28 12:01:08 nosebrain Exp $
 */
public class XmlUtils {

	/** the following is used by the removeControlCharacters-methods below */	
	private static final CharOpenHashSet illegalChars;
	/** replacement character for illegal characters */
	private static final char ILLEGAL_CHAR_SUBSTITUTE = '\uFFFD'; 	
	
	/** 
	 * define disallowed characters in XML 1.0
	 * see http://www.w3.org/International/questions/qa-controls.en.php for details 
	 */
    static {
        final String escapeString = "\u0000\u0001\u0002\u0003\u0004\u0005" +
            "\u0006\u0007\u0008\u000B\u000C\u000E\u000F\u0010\u0011\u0012" +
            "\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C" +
            "\u001D\u001E\u001F\uFFFE\uFFFF";

        illegalChars = new CharOpenHashSet();
        for (int i = 0; i < escapeString.length(); i++) {
            illegalChars.add(escapeString.charAt(i));
        }
    }
    
    /**
     * Substitutes all illegal characters in the given string by the value of
     * {@link XmlUtils#ILLEGAL_CHAR_SUBSTITUTE}. 
     *
     * @param string
     * @param substitute 
     * @return a string with control characters removed
     */
    public static String removeXmlControlCharacters(String string, final boolean substitute) {
    	if (string == null) return string;
    	char[] ch = string.toCharArray();
    	StringBuilder sb = new StringBuilder(ch.length);    	
        for (int i = 0; i < ch.length; i++) {
            if (XmlUtils.illegalChars.contains(ch[i])) {
            	sb.append(substitute ? XmlUtils.ILLEGAL_CHAR_SUBSTITUTE : "");
            }
            else {
            	sb.append(ch[i]);
            }
        }
        return sb.toString();    	    	
    }
    
    /**
     * wrapper method for {@link #removeXmlControlCharacters(String, Boolean)} 
     * 
     * @param string a string
     * @return a string with control characters removed
     */
    public static String removeXmlControlCharacters(String string) {
    	return XmlUtils.removeXmlControlCharacters(string, false);
    }
    
    /**
     * Substitutes all illegal characters in the given char array by the value of
     * {@link XmlUtils#ILLEGAL_CHAR_SUBSTITUTE}. If no illegal characters
     * were found, no copy is made and the given char array
     * 
     * @param ch a char array
     * @param substitute 
     * @return a char array with control characters removed
     */
    public static char[] removeXmlControlCharacters(char[] ch, final boolean substitute) {
    	StringBuilder sb = new StringBuilder(ch.length);
        for (int i = 0; i < ch.length; i++) {
            if (XmlUtils.illegalChars.contains(ch[i])) {
            	sb.append(substitute ? XmlUtils.ILLEGAL_CHAR_SUBSTITUTE : "");
            }
            else {
            	sb.append(ch[i]);
            }
        }
        return sb.toString().toCharArray();    
    }
    
    /**
     * wrapper method for {@link #removeXmlControlCharacters(char[] ch, Boolean substitute)}
     * 
     * @param ch a char array
     * @return a char array with control characters removed
     */
    public static char[] removeXmlControlCharacters(char[] ch) {
    	return XmlUtils.removeXmlControlCharacters(ch, false);
    }
    
    /**
     * Checks if a given char c is a control character; if yes, a replacement
     * is returned, if no, the char c is returned
     * 
     * @param c a char
     * @param substitute 
     * @return a char with control characters removed
     */
    public static char removeXmlControlCharacter(char c, final boolean substitute) {
    	if (XmlUtils.illegalChars.contains(c)) {
    		return substitute ? XmlUtils.ILLEGAL_CHAR_SUBSTITUTE : ' ' ;
    	}
    	return c;
    }
    
    /**
     * Wrapper for {@link #removeXmlControlCharacter(char, Boolean)}
     * 
     * @param c a char
     * @return a char with control charaters removed
     */
    public static char removeXmlControlCharacters(char c) {
    	return XmlUtils.removeXmlControlCharacter(c, false);
    }

	/** Parses a page and returns the DOM
	 * 
	 * @param content - The XML as string.
	 * @return The DOM tree of the XML string.
	 */
	public static Document getDOM(final String content) {
		return getDOM(content, false);
	}

	/**
	 * @param content
	 * @param xmlTags <code>true</code>, if the content should be handled as XML (e.g., empty tags are not removed!)
	 * @return The DOM of the given XML string 
	 */
	public static Document getDOM(final String content, final boolean xmlTags) {
		return getDOM(new ByteArrayInputStream(content.getBytes()), xmlTags);
	}
	
	
	/**
	 * Parse html file from given URL into DOM tree.
	 * 
	 * @param inputURL file's url
	 * @return parsed DOM tree
	 * @throws IOException if html file could not be parsed. 
	 */
	public static Document getDOM(final URL inputURL) throws IOException {
		return getDOM(inputURL, false);
	}
	
	/** Parse html file from given URL into DOM tree.
	 * 
	 * @param inputURL file's url
	 * @param xmlTags <code>true</code>, if the content should be handled as XML (e.g., empty tags are not removed!)
	 * @return parsed DOM tree
	 * @throws IOException if html file could not be parsed. 
	 */
	public static Document getDOM(final URL inputURL, final boolean xmlTags) throws IOException {
		final Tidy tidy = getTidy(xmlTags);
		
		final String encodingName = WebUtils.extractCharset(((HttpURLConnection)inputURL.openConnection()).getContentType());
		tidy.setInputEncoding(encodingName);
		return tidy.parseDOM(inputURL.openConnection().getInputStream(), null);
	}

	/**
	 * Parse html file from given input stream into DOM tree.
	 * 
	 * @param inputStream 
	 * @return parsed DOM tree
	 */
	public static Document getDOM(final InputStream inputStream) {
		return getDOM(inputStream, false);
	}
	
	/**
	 * Parse html file from given input stream into DOM tree.
	 * 
	 * @param inputStream 
	 * @param xmlTags 
	 * @return parsed DOM tree
	 */
	public static Document getDOM(final InputStream inputStream, final boolean xmlTags) {
		final Tidy tidy = getTidy(xmlTags);
			
		// we don't know the encoding now ... so we assume utf8
		tidy.setInputEncoding("UTF-8");

		return tidy.parseDOM(inputStream, null);
	}
	
	
	/**
	 * Returns a version of tidy where {@link Tidy#setXmlTags(boolean)} is set 
	 * to xmlTags. 
	 * <br/>
	 * Note that <code>xmlTags = true</code> is in particular neccessary for the 
	 * UnAPI scraper to allow empty &lt;abbr&gt; tags.
	 * 
	 * @param xmlTags
	 * @return
	 */
	private static Tidy getTidy(final boolean xmlTags) {
		final Tidy tidy = new Tidy(); // tidy is not thread safe so we create a new instance each time
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);// turns off warning lines
		tidy.setShowErrors(0); // turn off error printing
		tidy.setXmlTags(xmlTags);
		return tidy;
	}
	
	/** Extract the text in one parent node and all its children (recursively!). 
	 * 
	 * @param node
	 * @return All text below the given node.
	 */
	public static String getText(final Node node) {
		final StringBuilder text = new StringBuilder();

		final String value = node.getNodeValue();

		if (value != null){
			text.append(value);
		}

		if (node.hasChildNodes()) {
			final NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				text.append(getText(children.item(i)));
			}
		}

		return text.toString();
	}
}
