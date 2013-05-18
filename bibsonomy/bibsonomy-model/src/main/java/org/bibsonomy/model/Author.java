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

/**
 * @author Christian Claus
 * @version $Id: Author.java,v 1.8 2011-04-29 06:45:05 bibsonomy Exp $
 */
public class Author {

	private String authorId = null;
	private String firstName = null;
	private String middle = null;
	private String lastName = null;
	private int ctr = 0;

	/**
	 * @return the authorId
	 */
	public String getAuthorId() {
		return this.authorId;
	}

	/**
	 * @param authorId the authorId to set
	 */
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return middle name
	 */
	public String getMiddle() {
		return this.middle;
	}

	/**
	 * @param middle the middle name to set
	 */
	public void setMiddle(String middle) {
		this.middle = middle;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the ctr
	 */
	public int getCtr() {
		return this.ctr;
	}

	/**
	 * @param ctr the ctr to set
	 */
	public void setCtr(int ctr) {
		this.ctr = ctr;
	}

}
