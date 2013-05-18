/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
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

package org.bibsonomy.common.exceptions;

import java.util.Date;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ResourceMovedException.java,v 1.9 2011-04-29 06:36:47 bibsonomy Exp $
 */
public class ResourceMovedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String newIntraHash;
	private final String userName;
	private final Date date;
	private final Class<?> resourceType;
	
	/**
	 * Constructs a new resource moved exception with the specified resource
	 * id.
	 * 
	 * @param intraHash
	 *            the intra hash of the resource that has been moved. This is written
	 *            into a detail message which is saved for later retrieval by
	 *            the {@link #getMessage()} method.
	 * @param resourceType - type of the resource that has moved
	 * @param newIntraHash 
	 * 			  the intra hash of the new resource when the resource changed 
	 * @param userName 
	 * 			  the name of the user who owns the resource
	 * @param date
	 * 			  the new date of the resource. This is necessary to identify 
	 *            resources whose date has not changed.  
	 */
	public ResourceMovedException(final String intraHash, final Class<?> resourceType, final String newIntraHash, final String userName, final Date date) {
		super("The requested resource (with ID " + intraHash + ") has been moved to new ID " + newIntraHash + ". \n");
		this.resourceType = resourceType;
		this.newIntraHash = newIntraHash;
		this.userName = userName;
		this.date = date;
	}
	
	/**
	 * @return The new intra hash of the requested resource. 
	 */
	public String getNewIntraHash() {
		return this.newIntraHash;
	}

	/**
	 * @return The name of the user who owns the resource.
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @return The new posting date of the resource. 
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return The type of the resource that has moved.
	 */
	public Class<?> getResourceType() {
		return this.resourceType;
	}
	
	
}