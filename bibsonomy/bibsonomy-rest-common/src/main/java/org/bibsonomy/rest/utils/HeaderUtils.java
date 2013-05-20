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

package org.bibsonomy.rest.utils;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dzo
 * @author rja
 * @version $Id: HeaderUtils.java,v 1.10 2011-05-23 08:24:03 bibsonomy Exp $
 */
public class HeaderUtils {
	private final static Log log = LogFactory.getLog(HeaderUtils.class);
	
	/**
	 * the header key for authorization
	 */
	public static final String HEADER_AUTHORIZATION = "Authorization";
	
	/**
	 * the header key for user agent
	 */
	public static final String HEADER_USER_AGENT = "User-Agent";
	
	/**
	 * the header key for accept
	 */
	public static final String HEADER_ACCEPT = "Accept";
	
	private static final String HEADER_AUTH_BASIC = "Basic ";
	
	private static final String UTF8 = "UTF-8";	

	private HeaderUtils() {}

	/**
	 * 
	 * @param acceptHeader 
	 * 			the HTML ACCEPT Header
	 * 			<br/>example: 
	 * 				<code>ACCEPT: text/xml,text/html;q=0.9,text/plain;q=0.8,image/png</code>
	 * 				would be interpreted in the following precedence:
	 *              <ol>
	 * 				<li>text/xml</li>
	 * 				<li>image/png</li>
	 * 				<li>text/html</li>
	 * 				<li>text/plain</li>
	 *              </ol>
	 * 			) 	
	 * @return a sorted map with the precedences
	 */
	public static SortedMap<Double, Vector<String>> getPreferredTypes(final String acceptHeader) {
		// maps the q-value to output format (reverse order)
		final SortedMap<Double,Vector<String>> preferredTypes = new TreeMap<Double,Vector<String>>(new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
				if (o1.doubleValue() > o2.doubleValue())
					return -1;
				else if (o1.doubleValue() < o2.doubleValue())
					return 1;
				else
					return o1.hashCode() - o2.hashCode();
			}				
		});		
		
		if (!present(acceptHeader)) {
			return preferredTypes;
		}
	
		// fill map with q-values and formats
		final Scanner scanner = new Scanner(acceptHeader.toLowerCase());
		scanner.useDelimiter(",");
	
		while(scanner.hasNext()) {
			final String[] types = scanner.next().split(";");
			final String type = types[0];
			double qValue = 1;
	
			if (types.length != 1) {
				/*
				 * FIXME: we get 
				 *   java.lang.NumberFormatException: For input string: "screen"
				 * in the error log because the format we assume seems to be 
				 * different by some clients. Until we find out, what is really 
				 * wrong (our parsing or the client), we are more careful with
				 * parsing external data.
				 */
				try {
					qValue = Double.parseDouble(types[1].split("=")[1]);
				} catch (NumberFormatException e) {
					qValue = 0;
					log.error("Couldn't parse accept header '"+acceptHeader+"'");
				}
			}
			
			Vector<String> v = preferredTypes.get(qValue);
			if (!preferredTypes.containsKey(qValue)) {
				v = new Vector<String>();			
				preferredTypes.put(qValue, v);			
			}
			v.add(type);
		}
		return preferredTypes;
	}

	/**
	 * Encode the username and password for BASIC authentication
	 * @param username	the username
	 * @param password 	the password
	 * 
	 * @return "Basic " + Base64 encoded(username + ':' + password)
	 */
	public static String encodeForAuthorization(final String username, final String password) {
		try {
			return HEADER_AUTH_BASIC + new String(Base64.encodeBase64((username + ":" + password).getBytes()), UTF8);
		} catch (final UnsupportedEncodingException e) {
		}
		return HEADER_AUTH_BASIC + new String(Base64.encodeBase64((username + ":" + password).getBytes()));
	}
	
	/**
	 * Check whether a request contains HTTP-Basic credentials by looking at 
	 * the request's authorization string
	 * 
	 * @param authentication a request's authorization header
	 * 
	 * @return true, if the given authorization header is a Http Basic authorization header
	 */
	public static boolean isHttpBasicAuthorization(final String authentication) {
		return (present(authentication) && authentication.startsWith(HEADER_AUTH_BASIC));
	}
	
}
