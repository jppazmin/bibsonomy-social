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

package org.bibsonomy.model.extra;

import java.net.URL;
import java.util.Date;

/**
 * Holds additional information about BibTexs.
 * 
 * @author Christian Schenk
 * @version $Id: BibTexExtra.java,v 1.9 2011-04-29 06:45:07 bibsonomy Exp $
 */
public class BibTexExtra {

	private URL url;
	private String text;
	private Date date;

	/**
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return url
	 */
	public URL getUrl() {
		return this.url;
	}

	/**
	 * @param url
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
	/**
	 * default constructor
	 */
	public BibTexExtra() {
		
	}

	/**
	 * Constructor setting all three fields.
	 * @param url
	 * @param text
	 * @param date
	 */
	public BibTexExtra(URL url, String text, Date date) {
		super();
		this.url = url;
		this.text = text;
		this.date = date;
	}
}