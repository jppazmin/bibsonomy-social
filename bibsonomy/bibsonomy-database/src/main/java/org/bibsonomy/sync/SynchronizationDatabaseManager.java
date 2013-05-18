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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.managers.GeneralDatabaseManager;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.sync.ConflictResolutionStrategy;
import org.bibsonomy.model.sync.SyncService;
import org.bibsonomy.model.sync.SynchronizationAction;
import org.bibsonomy.model.sync.SynchronizationData;
import org.bibsonomy.model.sync.SynchronizationDirection;
import org.bibsonomy.model.sync.SynchronizationPost;
import org.bibsonomy.model.sync.SynchronizationStatus;

/**
 * @author wla
 * @version $Id: SynchronizationDatabaseManager.java,v 1.32 2011-08-05 12:16:45 rja Exp $
 */
public class SynchronizationDatabaseManager extends AbstractDatabaseManager {
	private static final Log log = LogFactory.getLog(SynchronizationDatabaseManager.class);

	private static final SynchronizationDatabaseManager singleton = new SynchronizationDatabaseManager();

	private final GeneralDatabaseManager generalDb;

	/**
	 * Singleton 
	 * @return SynchronizationDatabaseManager
	 */
	public static SynchronizationDatabaseManager getInstance() {
		return singleton;
	}

	private SynchronizationDatabaseManager() {
		this.generalDb = GeneralDatabaseManager.getInstance();
	}

	/**
	 * Add a sync service. Callers should check, if a client/server with that
	 * URI already exists. Otherwise, a DUPLICATE KEY exception will be thrown.
	 * 
	 * @param session
	 * @param service - the URI of the service to be added
	 * @param server - <code>true</code> if the service may act as a server, <code>false</code> if it may act as a client
	 */
	public void createSyncService(final DBSession session, final URI service, final boolean server) {
		session.beginTransaction();
		try {
			final SyncParam param = new SyncParam();
			param.setService(service);
			param.setServer(server);
			param.setServiceId(generalDb.getNewId(ConstantID.IDS_SYNC_SERVICE, session));
			session.insert("insertSyncService", param);
			session.commitTransaction();
		} finally {
			session.endTransaction();
		}
	}

	/**
	 * Remove a sync service.
	 * 
	 * @param session
	 * @param service - the URI of the service to be removed
	 * @param server - <code>true</code> if the server part should be deleted, <code>false</code> if the client client part should be deleted
	 */
	public void deleteSyncService(final DBSession session, final URI service, final boolean server) {
		final SyncParam param =  new SyncParam();
		param.setService(service);
		param.setServer(server);
		session.delete("deleteSyncService", param);
	}

	/**
	 * Update the synchronization status in the database.
	 * 
	 * @param session - the database session
	 * @param userName - identifies 
	 * @param service - identifies the status
	 * @param resourceType - identifies the status
	 * @param syncDate - identifies the status
	 * @param status - the new status
	 * @param info - some additional information to be stored
	 */
	public void updateSyncData(final DBSession session, final String userName, final URI service, final Class<? extends Resource> resourceType, final Date syncDate, final SynchronizationStatus status, final String info) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		param.setService(service);
		param.setResourceType(resourceType);
		param.setLastSyncDate(syncDate);
		param.setStatus(status); // this is changed
		param.setInfo(info); // and this is changed
		param.setServer(false);
		session.update("updateSyncStatus", param);
	}

	/**
	 * Delete the given synchronization data's status in the database. If syncDate is null, delete all sync data, which matches other parameters
	 * 
	 * @param session - the database session
	 * @param status - the status to set
	 * @param data SynchronizationData
	 */
	public void deleteSyncData(final DBSession session, final String userName, final URI service, final Class<? extends Resource> resourceType, final Date syncDate) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		param.setService(service);
		param.setResourceType(resourceType);
		param.setLastSyncDate(syncDate);
		param.setServer(false);
		session.update("deleteSyncStatus", param);
	}
	
	/**
	 * Insert new synchronization data for user.
	 * 
	 * @param session
	 * @param userName
	 * @param credentials
	 * @param serviceId
	 */
	public void createSyncServerForUser(final DBSession session, final String userName, final URI service, final Class<? extends Resource> resourceType, final Properties userCredentials, final SynchronizationDirection direction, final ConflictResolutionStrategy strategy) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		param.setCredentials(userCredentials);
		param.setDirection(direction);
		param.setStrategy(strategy);
		param.setResourceType(resourceType);
		param.setService(service);
		param.setServer(true);
		session.insert("insertSyncServiceForUser", param);
	}

	/**
	 * Removes synchronization data for user.
	 * @param session
	 * @param userName
	 * @param service
	 */
	public void deleteSyncServerForUser(final DBSession session, final String userName, final URI service) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		param.setService(service);
		param.setServer(true);
		session.delete("deleteSyncServerForUser", param);
	}

	/**
	 * Updates the synchronization data for a user
	 * 
	 * @param session
	 * @param userName
	 * @param service
	 * @param credentials
	 * 
	 */
	public void updateSyncServerForUser(final DBSession session, final String userName, final URI service, final Class<? extends Resource> resourceType, final Properties userCredentials, final SynchronizationDirection direction, final ConflictResolutionStrategy strategy) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		param.setService(service);
		param.setDirection(direction);
		param.setResourceType(resourceType);
		param.setServer(true);
		param.setCredentials(userCredentials);
		param.setStrategy(strategy);
		session.update("updateSyncServerForUser", param);
	}

	/**
	 * Returns all available synchronization services. 
	 * 
	 * @param session
	 * @return
	 */
	public List<URI> getSyncServices(final DBSession session, final boolean server) {
		return this.queryForList("getSyncServices", server, URI.class, session);
	}

	/**
	 * Inserts synchronization data with GIVEN status into db. 
	 * @param userName
	 * @param service
	 * @param contentType
	 * @param lastSyncDate
	 * @param status
	 * @param session
	 */
	public void insertSynchronizationData (final String userName, final URI service, Class<? extends Resource> resourceType, final Date lastSyncDate, final SynchronizationStatus status, final DBSession session) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		param.setService(service);
		param.setResourceType(resourceType);
		param.setLastSyncDate(lastSyncDate);
		param.setStatus(status);
		param.setServer(false);
		session.insert("insertSync", param);
	}

	/**
	 * 
	 * @param userName
	 * @param service
	 * @param contentType
	 * @param session
	 * @param status - optional. If provided, only data with that state is returned.
	 * @return returns last synchronization data for given user, service and content with {@link SynchronizationStatus#RUNNING}.
	 */
	public SynchronizationData getLastSyncData(final String userName, final URI service, final Class<? extends Resource> resourceType, final SynchronizationStatus status, final DBSession session) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		param.setResourceType(resourceType);
		param.setService(service);
		param.setStatus(status);
		param.setServer(false);
		return queryForObject("getLastSyncData", param, SynchronizationData.class, session);
	}

	/**
	 * 
	 * @param userName
	 * @param session
	 * @return all synchronization server for user
	 */
	public List<SyncService> getSyncServersForUser(final String userName, final DBSession session) {
		final SyncParam param = new SyncParam();
		param.setUserName(userName);
		return queryForList("getSyncServersForUser", param, SyncService.class, session);
	}

	/**
	 * Computes the synchronization plan.
	 * 
	 * @param serverPosts - Note: this map is modified by this method - posts are removed.
	 * @param clientPosts - Note: this list is modified by this method - posts are added. It's the same list that is returned by this method.
	 * @param lastSyncDate
	 * @param conflictResolutionStrategy
	 * @return The clientPosts with {@link SynchronizationAction}'s and posts from the server added.
	 */
	public List<SynchronizationPost> getSyncPlan(final Map<String, SynchronizationPost> serverPosts, final List<SynchronizationPost> clientPosts, final Date lastSyncDate, final ConflictResolutionStrategy conflictResolutionStrategy, final SynchronizationDirection direction) {

		// is there something to synchronize?
		if (!present(serverPosts) && !present(clientPosts)) {
			throw new IllegalArgumentException("both serverPosts and clientPosts must be given");
		}

		if (!present(lastSyncDate)) {
			throw new IllegalArgumentException("lastSyncDate not present");
		}

		/*
		 * check all client posts
		 */
		for (final SynchronizationPost clientPost: clientPosts) {
			final SynchronizationPost serverPost = serverPosts.get(clientPost.getIntraHash());

			if (!present(serverPost)) {
				/*  
				 * no such post on server 
				 */
				if (clientPost.getCreateDate().before(lastSyncDate)) {
				    /*
				     * post was created before last sync, but when was it changed?
				     */
					if (clientPost.getChangeDate().before(lastSyncDate)) {
						/*
						 * client post was created and last changed before last synchronization 
						 * -> post was deleted on server
						 */
						if (!SynchronizationDirection.CLIENT_TO_SERVER.equals(direction))
							clientPost.setAction(SynchronizationAction.DELETE_CLIENT);
						else
							clientPost.setAction(SynchronizationAction.OK);
					} else {
						/*
						 * CONFLICT! (we can't solve, currently :-(
						 * 
						 * Post was changed after last sync but does not exist on server
						 * --> either it was deleted on server, or it's hash has changed
						 * Since it is neither simple to find out if the post has been deleted
						 * or its hash has changed, we create the post on the server.   
						 * FIXME: This can result in 
						 * a) a duplicate post (if the hash has changed on the client but the
						 * post still exists on the server), or 
						 * b) an unwanted post (if the post has been deleted on the server, but
						 * according to the strategy this deletion should be carried out on
						 * the client, too).
						 */
						if (!SynchronizationDirection.SERVER_TO_CLIENT.equals(direction))
							clientPost.setAction(SynchronizationAction.CREATE_SERVER);
						else 
							clientPost.setAction(SynchronizationAction.OK);
					}
				} else {
					/*
					 * post was created on client after last sync
					 */
					if (!SynchronizationDirection.SERVER_TO_CLIENT.equals(direction))
						clientPost.setAction(SynchronizationAction.CREATE_SERVER);
					else 
						clientPost.setAction(SynchronizationAction.OK);
				}
				continue;
			}

			if (!present(serverPost.getChangeDate())) {
				log.error("post on server has no changedate");
				//FIXME what is to do in this case?
			}


			if (serverPost.getChangeDate().after(lastSyncDate)) {
				/*  
				 * changed on server since last sync 
				 */
				if (clientPost.getChangeDate().after(lastSyncDate)) {
					
					if (clientPost.getChangeDate().equals(serverPost.getChangeDate())) {
						/*
						 * both have the same change date -> do nothing
						 */
						clientPost.setAction(SynchronizationAction.OK);
					} else {
						/*
						 * changed on client, too -> conflict!
						 */
						resolveConflict(clientPost, serverPost, conflictResolutionStrategy, direction);
					}
				} else {
					/*
					 * must be updated on client
					 */
					if (!SynchronizationDirection.CLIENT_TO_SERVER.equals(direction))
						clientPost.setAction(SynchronizationAction.UPDATE_CLIENT);
					else
						clientPost.setAction(SynchronizationAction.OK);
				}
			} else {
				/*
				 * post is in sync on the server
				 */
				if (clientPost.getChangeDate().after(lastSyncDate) && !SynchronizationDirection.SERVER_TO_CLIENT.equals(direction)) {
					/*
					 * ... but not on the client -> update
					 */
					clientPost.setAction(SynchronizationAction.UPDATE_SERVER);
				} else {
					clientPost.setAction(SynchronizationAction.OK);
				}

			}
			/*
			 * In the next loop we go over all *remaining* server posts and
			 * compare them. To not handle this post twice, we remove it from
			 * the server posts list.
			 */
			serverPosts.remove(clientPost.getIntraHash());
		}

		
		/*
		 * handle the remaining posts that do not exist on the client
		 */
		for (final SynchronizationPost serverPost: serverPosts.values()) {
			if (serverPost.getCreateDate().before(lastSyncDate)) {
				/*
				 * post is older than lastSyncDate but does not exist on client
				 */
				if (serverPost.getChangeDate().before(lastSyncDate)) {
					/*
					 * post was deleted on client and must now be deleted on server
					 */
					if (!SynchronizationDirection.SERVER_TO_CLIENT.equals(direction))
						serverPost.setAction(SynchronizationAction.DELETE_SERVER);
					else 
						serverPost.setAction(SynchronizationAction.OK);
				} else {
					/*
					 * CONFLICT (see above! FIXME: currently, we can't resolve this)
					 * 
					 * we create the post on the client
					 */
					if (!SynchronizationDirection.CLIENT_TO_SERVER.equals(direction))
						serverPost.setAction(SynchronizationAction.CREATE_CLIENT);
					else 
						serverPost.setAction(SynchronizationAction.OK);
				}
			} else {
				/*
				 * post was created after last sync -> create on client
				 */
				if (!SynchronizationDirection.CLIENT_TO_SERVER.equals(direction))
					serverPost.setAction(SynchronizationAction.CREATE_CLIENT);
				else 
					serverPost.setAction(SynchronizationAction.OK);
			}
			/*
			 * add post to list of client posts
			 */
			clientPosts.add(serverPost);
		}

		/*
		 * FIXME posts with OK-state could be omitted.
		 */
		return clientPosts;
	}

	/**
	 * When a post was changed on both the server and the client /after/ 
	 * synchronization, this method resolved the corresponding conflict.
	 * 
	 * @param clientPost
	 * @param serverPost
	 * @param conflictResolutionStrategy
	 * @param direction
	 */
	private void resolveConflict(final SynchronizationPost clientPost, final SynchronizationPost serverPost, final ConflictResolutionStrategy conflictResolutionStrategy, final SynchronizationDirection direction) {
		switch (conflictResolutionStrategy) {
		case CLIENT_WINS:
			if(!SynchronizationDirection.SERVER_TO_CLIENT.equals(direction))
				clientPost.setAction(SynchronizationAction.UPDATE_SERVER);
			else 
				clientPost.setAction(SynchronizationAction.OK);
			break;
		case SERVER_WINS:
			if(!SynchronizationDirection.CLIENT_TO_SERVER.equals(direction))
				clientPost.setAction(SynchronizationAction.UPDATE_CLIENT);
			else
				clientPost.setAction(SynchronizationAction.OK);
			break;
//		case ASK_USER: temporary disabled
//			clientPost.setAction(SynchronizationAction.ASK);
//			break;
		case FIRST_WINS:
			if (clientPost.getChangeDate().before(serverPost.getChangeDate())) {
				if(!SynchronizationDirection.SERVER_TO_CLIENT.equals(direction))
					clientPost.setAction(SynchronizationAction.UPDATE_SERVER);
				else 
					clientPost.setAction(SynchronizationAction.OK);
			} else {
				if(!SynchronizationDirection.CLIENT_TO_SERVER.equals(direction))
					clientPost.setAction(SynchronizationAction.UPDATE_CLIENT);
				else
					clientPost.setAction(SynchronizationAction.OK);
			}
			break;
		case LAST_WINS:
			if (clientPost.getChangeDate().after(serverPost.getChangeDate())) {
				if(!SynchronizationDirection.SERVER_TO_CLIENT.equals(direction))
					clientPost.setAction(SynchronizationAction.UPDATE_SERVER);
				else 
					clientPost.setAction(SynchronizationAction.OK);

			} else {
				if(!SynchronizationDirection.CLIENT_TO_SERVER.equals(direction))
					clientPost.setAction(SynchronizationAction.UPDATE_CLIENT);
				else
					clientPost.setAction(SynchronizationAction.OK);
			}
			break;
		default:
			clientPost.setAction(SynchronizationAction.UNDEFINED);
			break;
		}
	}

}
