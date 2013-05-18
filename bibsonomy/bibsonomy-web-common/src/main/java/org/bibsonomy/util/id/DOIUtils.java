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

package org.bibsonomy.util.id;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.util.WebUtils;

/**
 * Helper methods to simplify <a href="http://www.doi.org/">DOI</a>s (digital object identifiers).
 *  
 *  
 *  FIXME: how to support DOI URLs like
 *  http://doi.acm.org/10.1145/160688.160713
 *  i.e., how to extract/match them?  
 * 
 * @author rja
 * @version $Id: DOIUtils.java,v 1.3 2011-03-29 15:56:44 clemensbaier Exp $
 */
public class DOIUtils {

	/**
	 * Host name of the DOI resolver. 
	 */
	public static final String DX_DOI_ORG = "dx.doi.org";
	/**
	 * URL of the DOI resolver.
	 */
	public static final String DX_DOI_ORG_URL = "http://" + DX_DOI_ORG + "/";

	private static final Pattern HOST_PATTERN = Pattern.compile(".*" + DX_DOI_ORG);
	
	private static final String NON_NUMBERS_OR_LETTERS = "[^0-9\\p{L}]*?";
	
	
	/**
	 * The first variant is too strict, according to 
	 *  
	 * http://stackoverflow.com/questions/27910/finding-a-doi-in-a-document-or-page 
	 * 
	 * Changes:
	 * <ul>
	 * <li>added "doi:" as possible prefix</li>
	 * <li>replaced the last \d by [^\s"'], thus allowing everything but whitespace and some
	 * "closing" characters (which are in principle allowed but makes detection of the end of a 
	 * DOI almost impossible). We in particular disallow "}", because it is at the end of BibTeX 
	 * DOIs (which we otherwise would extract wrong).</li>
	 * </ul>
	 * 
	 * 
	 */
//	private static final String DOI = "(10\\.\\d+\\/\\d+?)";
	private static final String DOI = "(doi:\\s*)?(10\\.\\d+\\/[^\\s\"'}]+)";
	private static final String DOI_END = "[\\s\"'}]*";
	
	/**
	 * Matches a pure DOI. Disregards case. 
	 */
	private static final Pattern DOI_PATTERN = Pattern.compile("^" + DOI + "$", Pattern.CASE_INSENSITIVE);

	/**
	 * Before the DOI we allow everything but numbers or letters (i.e., whitespace, symbols, etc.)
	 * Since the DOI might include almost every character (in principle) we stop when we find the
	 * first non-matching symbol.
	 */
	private static final Pattern STRICT_DOI_PATTERN = Pattern.compile("^" + NON_NUMBERS_OR_LETTERS + DOI + DOI_END + NON_NUMBERS_OR_LETTERS + "$", Pattern.CASE_INSENSITIVE);
	private static final Pattern SLOPPY_DOI_PATTERN = Pattern.compile(".*?" + DOI + DOI_END + ".*", Pattern.CASE_INSENSITIVE);
	
	/**
	 * Pattern to clean up a string containing a doi.
	 */
	private static final String CLEAN_DOI = "(doi\\s*=.*?)(doi:\\s*|http://.*?)?(10\\.\\d+\\/[^\\s\"'}]+)";
	private static final Pattern CLEAN_DOI_PATTERN = Pattern.compile(CLEAN_DOI, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

	/** Checks, if the given URL is a DOI URL (i.e., points to dx.doi.org) 
	 * 
	 * @param url
	 * @return <code>true</code> if the URL points to the DOI resolver.
	 */
	public static boolean isDOIURL(final URL url) {
		return url != null && HOST_PATTERN.matcher(url.getHost()).find();
	}
	
	
	/**
	 * Resolves DOI to a URL using the official DOI resolver {@value #DX_DOI_ORG}.
	 * 
	 * @param doi DOI as String
	 * @return URL from the referenced DOI resource, null if resolve failed
	 */
	public static URL getUrlForDoi(final String doi) {
		try {
			return WebUtils.getRedirectUrl(getURL(doi));
		} catch (MalformedURLException ex) {
			return null;
		}
	}


	/**
	 * Creates a URL for the given DOI which points to the DOI resolver.
	 * 
	 * @param doi - the DOI which the URL should resolve
	 * @return A URL pointing to the DOI resolver.
	 * 
	 * @throws MalformedURLException
	 */
	public static URL getURL(final String doi) throws MalformedURLException {
		return new URL(DOIUtils.DX_DOI_ORG_URL + doi);
	}

	/**
	 * Extracts a DOI from the given string.
	 * 
	 * @param string
	 * @return The extracted DOI.
	 */
	public static String extractDOI(final String string) {
		if (string != null) {
			final Matcher matcher = SLOPPY_DOI_PATTERN.matcher(string);
			if (matcher.find()) {
				return matcher.group(2);
			}
		}
		return null;
	}
	
	/**
	 * Checks, if the given string contains a DOI.
	 * This method checks, if <strong>somewhere</strong> in the string something
	 * like a DOI can be found. If you want to check, if the string contains almost
	 * only a DOI, use {@link #containsOnlyDOI(String)}.
	 * 
	 * @param string
	 * @return <code>true</code>, if the given string contains a DOI.
	 */
	public static boolean containsDOI(final String string) {
		return string != null && SLOPPY_DOI_PATTERN.matcher(string).find();
	}
	
	/**
	 * Checks, if the given string contains (almost only) a DOI.
	 * 
	 * @param string
	 * @return <code>true</code>, if the given string contains almost only the DOI, i.e.,
	 * no surrounding text (only whitespace or symbols like ",", "'", ")", etc.)
	 */
	public static boolean containsOnlyDOI(final String string) {
		return string != null && STRICT_DOI_PATTERN.matcher(string).find();
	}
	
	/**
	 * Checks, if the given string represents a DOI.
	 * 
	 * @param doi
	 * @return <code>true</code>, if the given string is a DOI
	 */
	public static boolean isDOI(final String doi) {
		return doi != null && DOI_PATTERN.matcher(doi).find();
	}
	
	/**
	 * Cleans up a doi entry. The string s can be a single Line or
	 * a whole BibTeX string.
	 * 
	 * @param s
	 * @return a modification of the String s. Changes occurences of
	 * <i>doi = {http://dx.doi....</i> or <i>doi = {doi:...</i> to
	 * <i>doi = {...}</i>.
	 */
	public static String cleanDOI(String s) {
		String replace = null;
		String target = null;
		Matcher _m = CLEAN_DOI_PATTERN.matcher(s);
		while (_m.find()) {
			target = _m.group(0);
			if (_m.group(2) != null && _m.group(3) != null) {
				replace = _m.group(1) + _m.group(3);
				break;
			}
		}
		if (replace != null && target != null) {
			s = s.replace(target, replace);
		}
		return s;
	}
 
}
