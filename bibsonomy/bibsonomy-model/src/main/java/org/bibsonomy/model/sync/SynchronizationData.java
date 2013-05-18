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

import java.net.URI;
import java.util.Date;

import org.bibsonomy.model.Resource;

/**
 * @author wla
 * @version $Id: SynchronizationData.java,v 1.7 2011-07-26 08:34:19 bibsonomy Exp $
 */
public class SynchronizationData {

	private URI service;
	private String userName;
	private Class<? extends Resource> resourceType;
	private Date lastSyncDate;
	private SynchronizationStatus status;
	private String info;
	
		
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the lastSyncDate
	 */
	public Date getLastSyncDate() {
		return this.lastSyncDate;
	}
	/**
	 * @param lastSyncDate the lastSyncDate to set
	 */
	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}
	/**
	 * @return the status
	 */
	public SynchronizationStatus getStatus() {
		return this.status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(SynchronizationStatus status) {
		this.status = status;
	}
	/**
	 * 
	 * @return URI
	 */
	public URI getService() {
		return this.service;
	}
	/**
	 * 
	 * @param service
	 */
	public void setService(URI service) {
		this.service = service;
	}
	/**
	 * 
	 * @return class of the resource
	 */
	public Class<? extends Resource> getResourceType() {
		return this.resourceType;
	}
	/**
	 * 
	 * @param resourceType
	 */
	public void setResourceType(Class<? extends Resource> resourceType) {
		this.resourceType = resourceType;
	}
	
	@Override
	public String toString() {
		return userName + "@" + service + " for " + resourceType.getSimpleName() + " in status '" + status + "' (lastSyncDate=" + lastSyncDate + ")"; 
	}
	public String getInfo() {
		return this.info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
