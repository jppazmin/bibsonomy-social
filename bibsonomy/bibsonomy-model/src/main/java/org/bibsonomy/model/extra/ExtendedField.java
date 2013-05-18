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

import java.util.Date;

/**
 * @version $Id: ExtendedField.java,v 1.2 2011-04-29 06:45:07 bibsonomy Exp $
 */
public class ExtendedField {

	private Date created;
	private Date lastModified;

	private String key;
	private String value;

	/**
	 * @return created
	 */
	public Date getCreated() {
		return this.created;
	}

	/**
	 * @param created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return lastModified
	 */
	public Date getLastModified() {
		return this.lastModified;
	}

	/**
	 * @param lastModified
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return key
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}