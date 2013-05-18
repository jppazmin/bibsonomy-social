/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model;

import java.net.URL;

import org.bibsonomy.util.StringUtils;

/**
 * This is a bookmark, which is derived from {@link Resource}.
 * 
 * @version $Id: Bookmark.java,v 1.19 2011-04-29 06:45:05 bibsonomy Exp $
 */
public class Bookmark extends Resource {
	private static final long serialVersionUID = 8540672660698453421L;
	
	/**
	 * An {@link URL} pointing to some website.
	 * FIXME: Use URL instead of String
	 */
	private String url;

	/**
	 * @return url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Bookmarks use the same hash value for both intrahash and interhash
	 */
	@Override
	public String getInterHash() {
		return super.getIntraHash();
	}

	/**
	 * @return hash
	 */
	public String getHash() {
		return StringUtils.getMD5Hash(this.url);
	}

	@Override
	public void recalculateHashes() {
		this.setIntraHash(getHash());
	}
	
	@Override
	public String toString() {
		return super.toString() + " = <" + url + ">";
	}
}