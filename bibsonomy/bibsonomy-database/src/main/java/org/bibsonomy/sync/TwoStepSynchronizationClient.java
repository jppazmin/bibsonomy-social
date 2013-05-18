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
 * It uses a two step approach, where one first requests a synchronization plan
 * and later executes this plan.
 * 
 * @author wla
 * @version $Id: TwoStepSynchronizationClient.java,v 1.4 2011-08-05 12:17:18 rja Exp $
 */
public class TwoStepSynchronizationClient extends AbstractSynchronizationClient {
	private static final Log log = LogFactory.getLog(TwoStepSynchronizationClient.class);
	
	public TwoStepSynchronizationClient() {
		super();
	}
	
	public TwoStepSynchronizationClient(final DBSessionFactory dbSessionFactory) {
		super(dbSessionFactory);
	}
	
	/**
	 * Synchronized the user's posts between the clientLogic and the syncServer
	 * according to the configured sync direction and resource types.
	 * 
	 * @param clientLogic
	 * @param service
	 * @param resourceType
	 * @return
	 */
	public Map<Class<? extends Resource>, List<SynchronizationPost>> getSyncPlan(final LogicInterface clientLogic, final SyncService syncServer) {

		/*
		 * retrieve instance of server logic
		 */
		final LogicInterface serverLogic = getServerLogic(syncServer.getServerUser());
		
		if (!present(serverLogic)) {
			throw new IllegalArgumentException("Synchronization for " + syncServer.getService() + " not configured for user " + clientLogic.getAuthenticatedUser());
		}
		final String serverUserName = serverLogic.getAuthenticatedUser().getName();
		
		/*
		 * get sync config
		 */
		final SynchronizationDirection direction = syncServer.getDirection();
		final Class<? extends Resource>[] resourceTypes = ResourceUtils.getResourceTypesByClass(syncServer.getResourceType());
		final ConflictResolutionStrategy strategy = syncServer.getStrategy();

		/*
		 * get sync plan for each configured resource type
		 */
		final Map<Class<? extends Resource>, List<SynchronizationPost>> result = new HashMap<Class<? extends Resource>, List<SynchronizationPost>>();
		
		for (final Class<? extends Resource> resourceType : resourceTypes) {
			/*
			 * get posts from client
			 */
			final List<SynchronizationPost> clientPosts = ((SyncLogicInterface)clientLogic).getSyncPosts(clientLogic.getAuthenticatedUser().getName(), resourceType);
			/*
			 * get sync plan from server
			 */
			final List<SynchronizationPost> syncPlan = ((SyncLogicInterface)serverLogic).getSyncPlan(serverUserName, ownUri, resourceType, clientPosts, strategy, direction);
			
			result.put(resourceType, syncPlan);
		}
		return result;
	}


	/**
	 * Synchronized the user's posts between the clientLogic and the syncServer
	 * according to the configured sync direction and resource types.
	 * 
	 * @param clientLogic
	 * @param service
	 * @param resourceType
	 * @return
	 */
	public Map<Class<? extends Resource>, SynchronizationData> synchronize(final LogicInterface clientLogic, final SyncService syncServer, final Map<Class<? extends Resource>, List<SynchronizationPost>> syncPlan) {
		/*
		 * retrieve instance of server logic
		 */
		final LogicInterface serverLogic = getServerLogic(syncServer.getServerUser());
		
		if (!present(serverLogic)) {
			throw new IllegalArgumentException("Synchronization for " + syncServer.getService() + " not configured for user " + clientLogic.getAuthenticatedUser());
		}
		final String serverUserName = serverLogic.getAuthenticatedUser().getName();

		/*
		 * get config
		 */
		final SynchronizationDirection direction = syncServer.getDirection();
		final Class<? extends Resource>[] resourceTypes = ResourceUtils.getResourceTypesByClass(syncServer.getResourceType());

		/*
		 * sync each configured resource type
		 */
		final Map<Class<? extends Resource>, SynchronizationData> result = new HashMap<Class<? extends Resource>, SynchronizationData>();
		
		for (final Class<? extends Resource> resourceType : resourceTypes) {
			/*
			 * synchronize
			 */
			final SynchronizationData syncData = synchronize(clientLogic, serverLogic, serverUserName, resourceType, direction, syncPlan.get(resourceType));
			
			result.put(resourceType, syncData);
		}
		return result;
	}
	
	protected SynchronizationData synchronize(final LogicInterface clientLogic, final LogicInterface serverLogic, final String serverUserName, final Class<? extends Resource> resourceType, final SynchronizationDirection direction, final List<SynchronizationPost> syncPlan) {
		SynchronizationStatus newStatus;
		String info;
		try {
			/*
			 * flag sync as running
			 */
			updateSyncData(serverLogic, serverUserName, resourceType, SynchronizationStatus.PLANNED, SynchronizationStatus.RUNNING, "");
			/*
			 * try to synchronize
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
