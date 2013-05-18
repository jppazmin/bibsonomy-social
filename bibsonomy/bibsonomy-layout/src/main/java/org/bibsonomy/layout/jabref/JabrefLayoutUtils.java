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

import java.io.InputStream;

import org.bibsonomy.util.StringUtils;

/**
 * 
 * @author:  rja
 * @version: $Id: JabrefLayoutUtils.java,v 1.8 2011-04-29 07:34:00 bibsonomy Exp $
 * $Author: bibsonomy $
 * 
 */
public class JabrefLayoutUtils {

	/**
	 * The file extension of layout filter file names.
	 */
	protected final static String layoutFileExtension = ".layout";

	/** Builds the hash for the custom layout files of the user. Depending on the 
	 * layout part the hash differs.
	 * 
	 * @param user
	 * @param part
	 * @return
	 */
	public static String userLayoutHash (final String user, final LayoutPart part) {
		return StringUtils.getMD5Hash("user." + user.toLowerCase() + "." + part + layoutFileExtension).toLowerCase();
	}
	
	/** Builds the name of a custom user layout, for the map and elsewhere. Typically "custom_" + userName.
	 * 
	 * @param userName
	 * @return
	 */
	public static String userLayoutName (final String userName) {
		return "custom_" + userName;
	}

	/** Loads a resource using the classloader.
	 * 
	 * @param location
	 * @return
	 */
	public static InputStream getResourceAsStream (final String location) {
		final InputStream resourceAsStream = JabrefLayoutRenderer.class.getClassLoader().getResourceAsStream(location);
		if (resourceAsStream != null) 
			return resourceAsStream;
		return JabrefLayoutRenderer.class.getResourceAsStream(location);
	}

	/** Constructs the name of a layout file.
	 * 
	 * @param layout
	 * @param part
	 * @return
	 */
	protected static String getLayoutFileName(final String layout, final String part) {
		return layout + "." + part + layoutFileExtension;
	}

	protected static String getLayoutFileName(final String layout) {
		return layout + layoutFileExtension;
	}
}

