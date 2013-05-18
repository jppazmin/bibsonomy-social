/**
 *
 *  BibSonomy-Importer - Various importers for bookmarks and publications.
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

package org.bibsonomy.importer.bookmark.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.services.importer.RelationImporter;
import org.bibsonomy.services.importer.RemoteServiceBookmarkImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * Imports bookmarks and relations from Delicious. To get an instance of this 
 * class, use the {@link DeliciousImporterFactory}.
 * 
 * This currently works only with the /v1-API. 
 * 
 * @see http://www.delicious.com/help/api writes:
 * "All /v1 api's require https requests and HTTP-Auth.
 * To access data from accounts created using a Yahoo! ID, use the same API's as
 * below, but change the path to /v2, and make HTTP requests using OAuth as 
 * provided by the Yahoo! Developer Network."
 * 
 * 
 * 
 * @author:  rja
 * @version: $Id: DeliciousImporter.java,v 1.8 2011-01-19 06:38:15 rja Exp $
 * $Author: rja $
 * 
 */
public class DeliciousImporter implements RemoteServiceBookmarkImporter, RelationImporter {

	private static final Log log = LogFactory.getLog(DeliciousImporter.class);

	/**
	 * The URL to contact Delicious.
	 */
	private final URL apiURL;
	private final String userAgent;


	private String password;
	private String userName;
	
	private static final String HEADER_USER_AGENT = "User-Agent";
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String HEADER_AUTH_BASIC = "Basic ";
	private static final String UTF8 = "UTF-8";
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	


	/**
	 * Constructor which allows to give a specific {@link #apiURL}.
	 * @param apiUrl - the URL to contact delicious
	 * @param userAgent - the userAgent this importer shall use to identify 
	 * itself in the corresponding HTTP header
	 */
	protected DeliciousImporter(final URL apiUrl, final String userAgent) {
		this.apiURL = apiUrl;
		this.userAgent = userAgent;
	}

	/**
	 * This Method retrieves a list of Posts for a given user.
	 */
	public List<Post<Bookmark>> getPosts() throws IOException{
		
		final List<Post<Bookmark>> posts = new LinkedList<Post<Bookmark>>();
				
		//open a connection to delicious and retrieve a document
		final Document document = getDocument();
		
		// traverse document and put everything into Post<Bookmark> Objects
		final NodeList postList = document.getElementsByTagName("post");
		for (int i = 0; i < postList.getLength(); i++) {
			final Element resource = (Element)postList.item(i);
								
			final Post<Bookmark> post = new Post<Bookmark>();
			final Bookmark bookmark = new Bookmark();
			bookmark.setTitle(resource.getAttribute("description"));
			bookmark.setUrl(resource.getAttribute("href"));
			try {
				post.getTags().addAll(TagUtils.parse(resource.getAttribute("tag")));
			} catch (Exception e) {
				throw new IOException("Could not parse tags. ", e);
			}
			
			//no tags available? -> add one tag to the resource and mark it as "imported"
			if (post.getTags().isEmpty()) {
				post.setTags(Collections.singleton(TagUtils.getEmptyTag()));
			}
			
			post.setDescription(resource.getAttribute("extended"));
			try {
				post.setDate(df.parse(resource.getAttribute("time")));
			} catch (ParseException e) {
				log.warn("Could not parse date.", e);
				post.setDate(new Date());
			}
			
			//set the visibility of the imported resource
			if (resource.hasAttribute("shared")) {
				if ("no".equals(resource.getAttribute("shared"))) {
					post.getGroups().add(GroupUtils.getPrivateGroup());
				} else {
					post.getGroups().add(GroupUtils.getPublicGroup());
				}
			}
			post.setResource(bookmark);
			posts.add(post);
			
		}
		
		return posts;
	}

	/**
	 * This method retrieves a list of tags with subTags from Delicious.
	 */
	public List<Tag> getRelations() throws IOException {
		final List<Tag> relations = new LinkedList<Tag>();
		//open a connection to delicious and retrieve a document
		final Document document = getDocument();
		final NodeList bundles = document.getElementsByTagName("bundle");
		for(int i = 0; i < bundles.getLength(); i++){
			final Element resource = (Element)bundles.item(i);
			try {
				Tag tag = new Tag(resource.getAttribute("name"));
				tag.getSubTags().addAll(TagUtils.parse(resource.getAttribute("tags")));
				relations.add(tag);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		return relations;
	}

	@Override
	public void setCredentials(final String userName, final String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * Method opens a connection and parses the retrieved InputStream with a JAXP parser.
	 * @return The from the parse call returned Document Object
	 * @throws IOException
	 */
	private Document getDocument() throws IOException{
				
		final URLConnection connection = apiURL.openConnection();
		connection.setRequestProperty(HEADER_USER_AGENT, userAgent);
		connection.setRequestProperty(HEADER_AUTHORIZATION, encodeForAuthorization());
		final InputStream inputStream = connection.getInputStream();
		
		// Get a JAXP parser factory object
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// Tell the factory what kind of parser we want 
		dbf.setValidating(false);
		// Use the factory to get a JAXP parser object
		
		final DocumentBuilder parser;
		try {
			parser = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		}
		
		// Tell the parser how to handle errors.  Note that in the JAXP API,
		// DOM parsers rely on the SAX API for error handling
		parser.setErrorHandler(new ErrorHandler() {
			public void warning(SAXParseException e) {
				log.warn(e);
			}
			public void error(SAXParseException e) {
				log.error(e);
			}
			public void fatalError(SAXParseException e)
			throws SAXException {
				log.fatal(e);
				throw e;   // re-throw the error
			}
		});
		
		// Finally, use the JAXP parser to parse the file.  
		// This call returns a Document object. 
		
		final Document document;
		try {
			document = parser.parse(inputStream);
		} catch (SAXException e) {
			throw new IOException(e);
		}
		
		inputStream.close();
		
		return document;
			
	}	
	
	/**
	 * Encode the username and password for BASIC authentication
	 * 
	 * @return Basic + Base64 encoded(username + ':' + password)
	 */
	protected String encodeForAuthorization() {
		String retVal = HEADER_AUTH_BASIC;
		try {
			retVal += new String(Base64.encodeBase64((this.userName + ":" + this.password).getBytes()), UTF8 );
		} catch (UnsupportedEncodingException e1) {
			retVal += new String(Base64.encodeBase64((this.userName + ":" + this.password).getBytes()));
		}
		return retVal;
	}
	
}

