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

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.SynchronizationRunningException;
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.sync.ConflictResolutionStrategy;
import org.bibsonomy.model.sync.SyncLogicInterface;
import org.bibsonomy.model.sync.SyncService;
import org.bibsonomy.model.sync.SynchronizationData;
import org.bibsonomy.model.sync.SynchronizationDirection;
import org.bibsonomy.model.sync.SynchronizationPost;
import org.bibsonomy.model.sync.SynchronizationStatus;
import org.bibsonomy.model.util.ResourceUtils;

/**
 * This client synchronizes PUMA with BibSonomy.
 * PUMA is the server, BibSonomy is the client.
 * 
 * 
 * @author wla
 * @version $Id: SynchronizationClient.java,v 1.46 2011-07-29 11:15:46 rja Exp $
 */
public class SynchronizationClient extends AbstractSynchronizationClient {
	private static final Log log = LogFactory.getLog(SynchronizationClient.class);
	
	public SynchronizationClient() {
		super();
	}
	
	public SynchronizationClient(final DBSessionFactory dbSessionFactory) {
		super(dbSessionFactory);	
	}

	/**
	 * Synchronized the user's posts between the clientLogic and the syncServer
	 * according to the configured sync direction and resource types.
	 * 
	 * @param clientLogic
	 * @param syncServerUri
	 * @param resourceType
	 * @return
	 */
	public Map<Class<? extends Resource>, SynchronizationData> synchronize(final LogicInterface clientLogic, final URI syncServerUri) {
		
		final SyncService syncServer = getServerByURI(clientLogic, syncServerUri);
		final Class<? extends Resource> resourceType = syncServer.getResourceType();
		final SynchronizationDirection direction = syncServer.getDirection();

		/*
		 * retrieve instance of server logic
		 */
		final LogicInterface serverLogic = getServerLogic(syncServer.getServerUser());
		
		if (!present(serverLogic)) {
			throw new IllegalArgumentException("Synchronization for " + syncServerUri + " not configured for user " + clientLogic.getAuthenticatedUser());
		}
		final String serverUserName = serverLogic.getAuthenticatedUser().getName();
		
		/*
		 * sync each configured resource type
		 */
		final Map<Class<? extends Resource>, SynchronizationData> result = new HashMap<Class<? extends Resource>, SynchronizationData>();
		
		for (final Class<? extends Resource> resource : ResourceUtils.getResourceTypesByClass(resourceType)) {
			result.put(resource, synchronize(clientLogic, serverLogic, serverUserName, resource, direction, syncServer.getStrategy()));
		}
		return result;
	}

		
	/**
	 * Synchronizes the user's posts of the given resource type 
	 * on the client and server according to the given direction. 
	 * 
	 * @param clientLogic
	 * @param serverLogic
	 * @param serverUserName
	 * @param resourceType
	 * @param direction
	 * @return
	 */
	protected SynchronizationData synchronize(final LogicInterface clientLogic, final LogicInterface serverLogic, final String serverUserName, final Class<? extends Resource> resourceType, final SynchronizationDirection direction, final ConflictResolutionStrategy strategy) {
		SynchronizationStatus newStatus;
		String info;
		try {
			/*
			 * try to synchronize
			 */
			/*
			 * get posts from client
			 */
			final List<SynchronizationPost> clientPosts = ((SyncLogicInterface)clientLogic).getSyncPosts(clientLogic.getAuthenticatedUser().getName(), resourceType);
			
			/*
			 * get synchronization actions and posts from server
			 */
			final List<SynchronizationPost> syncPlan = ((SyncLogicInterface)serverLogic).getSyncPlan(serverUserName, ownUri, resourceType, clientPosts, strategy, direction);
			/*
			 * flag sync as running
			 */
			updateSyncData(serverLogic, serverUserName, resourceType, SynchronizationStatus.PLANNED, SynchronizationStatus.RUNNING, "");
			/*
			 * sync
			 */
			info = synchronize(clientLogic, serverLogic, syncPlan, direction);
			newStatus = SynchronizationStatus.DONE;
		} catch (final SynchronizationRunningException e) {
			/*
			 * FIXME handling of this exception type. I think we can break "running" synchronization after timeout.
			 * Currently return only "running" status.
			 */
			throw e;
		} catch (final Exception e) {
			info = "";
			newStatus = SynchronizationStatus.ERROR;
			log.error("Error in synchronization", e);
		}
		/*
		 * store sync result
		 */
		updateSyncData(serverLogic, serverUserName, resourceType, SynchronizationStatus.RUNNING, newStatus, info);
		
		/*
		 * Get synchronization data from server. Can not be constructed here 
		 * because last_sync_date is only known by the server
		 */
		return getLastSyncData(serverLogic, serverUserName, resourceType);
	}

}
