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
import java.util.List;
import java.util.Properties;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;


/**
 * @author wla
 * @version $Id: SyncLogicInterface.java,v 1.23 2011-08-02 12:08:30 wla Exp $
 */
public interface SyncLogicInterface {
	
	/* ********************************************************************
	 * create, read, update sync services - user independent
	 */
	
	/**
	 * Add service to the database
	 * @param service service to add 
	 * @param server server/client switch
	 */
	public void createSyncService(final URI service, final boolean server);
	
	
	/**
	 * Removes service from database
	 * @param service
	 * @param server
	 */
	public void deleteSyncService(final URI service, final boolean server);
	
	/**
	 * 
	 * @param server switch between server and clients
	 * @return List of allowed synchronization services
	 */
	public List<URI> getSyncServices(final boolean server);

	
	

	
	/* ********************************************************************
	 * create, read, update, delete sync services - user dependent
	 */
	
	/**
	 * 
	 * @param userName
	 * @param service
	 * @param resourceType - the type of resource that should be synchronized. {@link Resource} means booth {@link Bookmark} and {@link BibTex}.
	 * @param userCredentials
	 * @param direction 
	 * @param strategy
	 */
	public void createSyncServer(final String userName, final URI service, final Class<? extends Resource> resourceType, final Properties userCredentials, final SynchronizationDirection direction, final ConflictResolutionStrategy strategy);
	
	/**
	 * 
	 * @param userName
	 * @param service
	 */
	public void deleteSyncServer(final String userName, final URI service);
	
	/**
	 * 
	 * @param userName
	 * @param service
	 * @param resourceType - the type of resource that should be synchronized. {@link Resource} means booth {@link Bookmark} and {@link BibTex}.
	 * @param userCredentials 
	 * @param direction
	 * @param strategy 
	 */
	public void updateSyncServer(final String userName, final URI service, final Class<? extends Resource> resourceType, final Properties userCredentials, final SynchronizationDirection direction, final ConflictResolutionStrategy strategy);

	/**
	 * 
	 * @param userName
	 * @return List of synchronization servers for given user 
	 */
	public List<SyncService> getSyncServer(final String userName);
	
	
	/* ********************************************************************
	 * create, read, update, delete sync services - user dependent
	 */
	/**
	 * 
	 * @param userName
	 * @param resourceType (e. g. Bibtex, Bookmark....) 
	 * @return List of SnchronizationPosts for given user 
	 */
	public List<SynchronizationPost> getSyncPosts (final String userName, final Class<? extends Resource> resourceType);
	
	/**
	 *  
	 * 
	 * @param userName - the name of the user whose sync status shall be updated
	 * @param service  - the URI of the service for which the sync status shall be updated
	 * @param resourceType - the resource type for which the sync status shall be updated
	 * @param syncDate - the sync date for which the sync status shall be updated
	 * @param status - the new sync status 
	 * @param info - some additional information, like how many posts were updated, etc.
	 */
	public void updateSyncData(final String userName, final URI service, final Class<? extends Resource> resourceType, final Date syncDate, final SynchronizationStatus status, final String info);

	/**
	 * Deletes the specified synchronization status.
	 *   
	 * @param userName - the name of the user whose sync status shall be updated
	 * @param service  - the URI of the service for which the sync status shall be updated
	 * @param resourceType - the resource type for which the sync status shall be updated
	 * @param syncDate - the sync date for which the sync status shall be updated. If syncDate is null, all states will be deleted
	 */
	public void deleteSyncData(final String userName, final URI service, final Class<? extends Resource> resourceType, final Date syncDate);

	/**
	 * 
	 * @param userName
	 * @param service
	 * @param resourceType
	 * @return Synchronization data of last successful synchronization: date and status
	 */
	public SynchronizationData getLastSyncData(final String userName, final URI service, final Class<? extends Resource> resourceType);
	
	/**
	 * Calculates a new synchronization plan and inserts new synchronization data
	 * with {@link SynchronizationStatus#PLANNED}. When clients are working on the
	 * plan, they should update the status to {@link SynchronizationStatus#RUNNING}
	 * using {@link #updateSyncData(String, URI, Class, Date, SynchronizationStatus, String)}. 
	 * 
	 * @param userName 
	 * @param service 
	 * @param resourceType 
	 * @param clientPosts
	 * @param strategy 
	 * @param direction
	 * @return list of posts with set synchronization state
	 */
	public List<SynchronizationPost> getSyncPlan(final String userName, final URI service, Class<? extends Resource> resourceType, final List<SynchronizationPost> clientPosts, final ConflictResolutionStrategy strategy, SynchronizationDirection direction);
}
