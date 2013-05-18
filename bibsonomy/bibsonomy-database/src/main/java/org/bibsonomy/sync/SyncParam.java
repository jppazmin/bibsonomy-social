/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.sync;

import java.net.URI;
import java.util.Date;
import java.util.Properties;

import org.bibsonomy.model.Resource;
import org.bibsonomy.model.sync.ConflictResolutionStrategy;
import org.bibsonomy.model.sync.SynchronizationDirection;
import org.bibsonomy.model.sync.SynchronizationStatus;

/**
 * @author wla
 * @version $Id: SyncParam.java,v 1.12 2011-07-27 15:36:36 wla Exp $
 */
public class SyncParam {

	private String userName;
	private URI service;
	private int serviceId;
	private Date lastSyncDate;
	private SynchronizationStatus status;
	private SynchronizationDirection direction;
	private ConflictResolutionStrategy strategy;
	private Class<? extends Resource> resourceType;
	private String info;
	private boolean server;
	private Properties credentials;
	
	public SyncParam() {
		
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the serviceId
	 */
	public URI getService() {
		return service;
	}

	/**
	 * @return the serviceId
	 */
	public int getServiceId() {
		return serviceId;
	}

	/**
	 * @return the lastSyncDate
	 */
	public Date getLastSyncDate() {
		return lastSyncDate;
	}

	/**
	 * @return the status
	 */
	public SynchronizationStatus getStatus() {
		return status;
	}

	/**
	 * @return the credentials
	 */
	public Properties getCredentials() {
		return credentials;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setService(URI service) {
		this.service = service;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}

	public void setStatus(SynchronizationStatus status) {
		this.status = status;
	}

	public void setCredentials(Properties credentials) {
		this.credentials = credentials;
	}

	public boolean getServer() {
		return this.server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Class<? extends Resource> getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(Class<? extends Resource> resourceType) {
		this.resourceType = resourceType;
	}

	public SynchronizationDirection getDirection() {
		return this.direction;
	}

	public void setDirection(SynchronizationDirection direction) {
		this.direction = direction;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(ConflictResolutionStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return the strategy
	 */
	public ConflictResolutionStrategy getStrategy() {
		return strategy;
	}
}
