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

package org.bibsonomy.model.sync;

/**
 * The different states a synchronization can be in. 
 * 
 * @author rja
 * @version $Id: SynchronizationStatus.java,v 1.5 2011-08-05 13:08:36 bibsonomy Exp $
 */
public enum SynchronizationStatus {
	/*
	 * NOTE: column is a varchar(8), so please use short names
	 */
	/**
	 * A synchronization plan was requested. 
	 */
	PLANNED("planned"),
	/**
	 * A client is currently working on the plan = synchronizing.
	 */
	RUNNING("running"),
	/**
	 * Synchronization is complete. 
	 */
	DONE("done"),
	/**
	 * An error during sync occurred. 
	 */
	ERROR("error");
	
	
	private String status;

	private SynchronizationStatus(final String status) {
		this.status = status;
	}
	
	/**
	 * @return The string representation for the synchronization status.
	 */
	public String getSynchronizationStatus() {
		return status;
	}
}
