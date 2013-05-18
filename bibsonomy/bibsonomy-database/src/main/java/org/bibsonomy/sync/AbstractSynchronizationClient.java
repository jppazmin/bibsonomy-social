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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.errors.DuplicatePostErrorMessage;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.database.DBLogicApiInterfaceFactory;
import org.bibsonomy.database.DBLogicUserInterfaceFactory;
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.database.util.IbatisSyncDBSessionFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.sync.SyncLogicInterface;
import org.bibsonomy.model.sync.SyncService;
import org.bibsonomy.model.sync.SynchronizationData;
import org.bibsonomy.model.sync.SynchronizationDirection;
import org.bibsonomy.model.sync.SynchronizationPost;
import org.bibsonomy.model.sync.SynchronizationStatus;

/**
 * This client synchronizes PUMA with BibSonomy.
 * PUMA is the server, BibSonomy is the client.
 * 
 * 
 * @author wla
 * @version $Id: AbstractSynchronizationClient.java,v 1.7 2011-08-05 12:16:35 rja Exp $
 */
public abstract class AbstractSynchronizationClient {
	/*
	 * own URI
	 */
	protected URI ownUri;
	
	/*
	 * FIXME: must be a different one for different servers 
	 */
	private final DBLogicUserInterfaceFactory serverLogicFactory;

	public AbstractSynchronizationClient() {
		this(new IbatisSyncDBSessionFactory());
	}
	
	public AbstractSynchronizationClient(final DBSessionFactory dbSessionFactory) {
		this.serverLogicFactory = new DBLogicApiInterfaceFactory();
		this.serverLogicFactory.setDbSessionFactory(dbSessionFactory);	
	}
	

	/**
	 * Looks up the credentials for the given syncServer. If no credentials
	 * could be found, <code>null</code> is returned.
	 *  
	 * @param clientLogic
	 * @param service
	 * @return syncService
	 */
	public SyncService getServerByURI(final LogicInterface clientLogic, final URI service) {
		final List<SyncService> syncServers = ((SyncLogicInterface)clientLogic).getSyncServer(clientLogic.getAuthenticatedUser().getName());
		
		for (final SyncService syncService : syncServers) {
			if (service.equals(syncService.getService())) {
				return syncService;
			}
		}
		return null;
	}

	/**
	 * Creates an instance of the LogicInterface for the given syncService
	 * 
	 * @param serverUser
	 * @return
	 */
	protected LogicInterface getServerLogic(final Properties serverUser) {
		return serverLogicFactory.getLogicAccess(serverUser.getProperty("userName"), serverUser.getProperty("apiKey"));
	}
	

	/**
	 * Used in a synchronization process, in case that server logic already created 
	 * 
	 * @param serverLogic
	 * @param serverUserName
	 * @param contentType
	 * @return
	 */
	protected SynchronizationData getLastSyncData(final LogicInterface serverLogic, final String serverUserName, Class<? extends Resource> resourceType) {
		/*
		 * FIXME: errorhandling
		 */
		return ((SyncLogicInterface) serverLogic).getLastSyncData(serverUserName, ownUri, resourceType);
	}
	
	/**
	 * Used in SettingsPageController, to show syncData, gets own logic
	 * @param syncService
	 * @param contentType
	 * @return
	 */
	public SynchronizationData getLastSyncData(final SyncService syncService, final Class<? extends Resource> resourceType) {
		/*
		 * FIXME errorhandling
		 */
		
		final LogicInterface serverLogic = getServerLogic(syncService.getServerUser());
		
		return getLastSyncData(serverLogic, serverLogic.getAuthenticatedUser().getName(), resourceType);
	}
	
	public void deleteSyncData(final SyncService syncService, final Class<? extends Resource> resourceType, final Date syncDate) {
		final LogicInterface serverLogic = getServerLogic(syncService.getServerUser());
		((SyncLogicInterface) serverLogic).deleteSyncData(serverLogic.getAuthenticatedUser().getName(), ownUri, resourceType, syncDate);
	}
	
	
	/**
	 * Updates the status of the last synchronization to newStatus, but only if 
	 * its status is oldStatus.
	 * 
	 * @param serverLogic
	 * @param serverUserName
	 * @param resourceType
	 * @param oldStatus
	 * @param newStatus
	 * @param info
	 */
	protected void updateSyncData(final LogicInterface serverLogic, final String serverUserName, final Class<? extends Resource> resourceType, final SynchronizationStatus oldStatus, final SynchronizationStatus newStatus, final String info) {
		final SynchronizationData data = ((SyncLogicInterface) serverLogic).getLastSyncData(serverUserName, ownUri, resourceType);
		if (!present(data)) {
			/*
			 * sync data seems not to have been stored --> error!
			 */
			throw new RuntimeException("No sync data found for " + serverUserName + " on " + ownUri + " and resource type " + resourceType.getSimpleName());
		}
		/*
		 * check if oldStatus is correct
		 */
		if (oldStatus.equals(data.getStatus())) {
			((SyncLogicInterface) serverLogic).updateSyncData(serverUserName, ownUri, resourceType, data.getLastSyncDate(), newStatus, info);
		} else {
			throw new RuntimeException("no " + oldStatus + " synchronization found for " + serverUserName + " on " + ownUri + " to store result");
		}
	}

	protected String synchronize(final LogicInterface clientLogic, final LogicInterface serverLogic, final List<SynchronizationPost> syncPlan, final SynchronizationDirection direction) {
		/*
		 * add sync access to both users = allow users to modify the dates of
		 * posts
		 * FIXME: must be secured using crypto
		 */
		final User serverUser = serverLogic.getAuthenticatedUser();
		final Role serverUserRole = serverUser.getRole();
		serverUser.setRole(Role.SYNC); 
		final User clientUser = clientLogic.getAuthenticatedUser();
		final Role clientUserRole = clientUser.getRole();
		clientUser.setRole(Role.SYNC);
		/*
		 * create target lists
		 */
		final List<Post<? extends Resource>> createOnClient = new ArrayList<Post<?>>();
		final List<Post<? extends Resource>> createOnServer = new ArrayList<Post<?>>();
		final List<Post<? extends Resource>> updateOnClient = new ArrayList<Post<?>>();
		final List<Post<? extends Resource>> updateOnServer = new ArrayList<Post<?>>();
		final List<String> deleteOnServer = new ArrayList<String>();
		final List<String> deleteOnClient = new ArrayList<String>();

		/*
		 * iterate over all posts and put each post into the target list
		 */
		for (final SynchronizationPost post: syncPlan) {
			final String postIntraHash = post.getIntraHash();
			
			final Post<? extends Resource> postToHandle;
			switch (post.getAction()) {
			case CREATE_SERVER:
				postToHandle = clientLogic.getPostDetails(postIntraHash, clientUser.getName());
				postToHandle.setUser(serverUser);
				createOnServer.add(postToHandle);
				break;
			case CREATE_CLIENT:
				postToHandle = post.getPost();
				postToHandle.setUser(clientUser);
				createOnClient.add(postToHandle);
				break;
			case DELETE_SERVER:
				deleteOnServer.add(postIntraHash);
				break;
			case DELETE_CLIENT:
				deleteOnClient.add(postIntraHash);
				break;
			case UPDATE_SERVER:
				postToHandle = clientLogic.getPostDetails(postIntraHash, clientUser.getName());
				postToHandle.setUser(serverUser);
				updateOnServer.add(postToHandle);
				break;
			case UPDATE_CLIENT:
				postToHandle = post.getPost();
				postToHandle.setUser(clientUser);
				updateOnClient.add(postToHandle);
				break;
			default:
				break;
			}
		}

		/*
		 *  Apply changes to both systems.
		 */
		final StringBuilder result = new StringBuilder();
		
		/*
		 * create posts on client 
		 */
		int duplicatesOnClient = 0;
		if (!createOnClient.isEmpty()) {
			assert !SynchronizationDirection.CLIENT_TO_SERVER.equals(direction); 
			try {
				clientLogic.createPosts(createOnClient);
				result.append("created on client: " + createOnClient.size() + ", ");
			} catch (final DatabaseException e) {
				/*
				 *  This can happen if some duplicate posts exists.
				 *  FIXME: currently, we only check for duplicate errors
				 *  check other possibilities to throw Database Exception
				 */
				duplicatesOnClient = getDuplicateCount(e);
			}
		}

		/*
		 * create posts on server
		 */
		int duplicatesOnServer = 0;
		if (!createOnServer.isEmpty()) {
			assert !SynchronizationDirection.SERVER_TO_CLIENT.equals(direction);
			try {
				serverLogic.createPosts(createOnServer);
				result.append("created on server: " + createOnServer.size() + ", ");
			} catch (final DatabaseException e) {
				/*
				 *  This can happen if some duplicate posts exists.
				 *  FIXME: currently, we only check for duplicate errors
				 *  check other possibilities to throw Database Exception
				 */
				duplicatesOnServer = getDuplicateCount(e);
			}
		}

		/*
		 * update posts on client 
		 */
		if (!updateOnClient.isEmpty()) {
			assert !SynchronizationDirection.CLIENT_TO_SERVER.equals(direction); 
			clientLogic.updatePosts(updateOnClient, PostUpdateOperation.UPDATE_ALL);
			result.append("updated on client: " + updateOnClient.size() + ", ");
		}

		/*
		 * update posts on server
		 */
		if (!updateOnServer.isEmpty()) {
			assert !SynchronizationDirection.SERVER_TO_CLIENT.equals(direction);
			serverLogic.updatePosts(updateOnServer, PostUpdateOperation.UPDATE_ALL);
			result.append("updated on server: " + updateOnServer.size() + ", ");
		}

		/*
		 * delete posts on client
		 */
		if (!deleteOnClient.isEmpty()) {
			assert !SynchronizationDirection.CLIENT_TO_SERVER.equals(direction); 
			clientLogic.deletePosts(clientUser.getName(), deleteOnClient);
			result.append("deleted on client: " + deleteOnClient.size() + ", ");
		}

		/*
		 * delete posts no server
		 */
		if (!deleteOnServer.isEmpty()) {
			assert !SynchronizationDirection.SERVER_TO_CLIENT.equals(direction);
			serverLogic.deletePosts(serverUser.getName(), deleteOnServer);
			result.append("deleted on server: " + deleteOnServer.size());
		}

		/*
		 * generate result string
		 */
		if (duplicatesOnClient > 0) 
			result.insert(0, duplicatesOnClient + "duplicates on client detected, ");
		if (duplicatesOnServer > 0) 
			result.insert(0, duplicatesOnServer + "duplicates on server detected, ");
		
	
		int length = result.length();
		if (length == 0) {
			result.append("no changes");
		} else if (result.lastIndexOf(", ") == length - 2) {
			result.delete(length - 2, length);
		}

		serverUser.setRole(serverUserRole);
		clientUser.setRole(clientUserRole);
		return result.toString();
	}

	/**
	 * Counts duplicate error messages 
	 * 
	 * @param exception
	 * @return
	 */
	private int getDuplicateCount(final DatabaseException exception) {
		int duplicatesOnClient = 0;
		final Set<Entry<String, List<ErrorMessage>>> entrySet = exception.getErrorMessages().entrySet();
		for (final Entry<String, List<ErrorMessage>> entry : entrySet) {
			final List<ErrorMessage> errorMessages = entry.getValue();
			for (final ErrorMessage em: errorMessages) {
				if (em instanceof DuplicatePostErrorMessage) {
					em.getErrorCode();
					em.getParameters();
					em.getDefaultMessage();
					duplicatesOnClient++;
				}		
			}
		}
		return duplicatesOnClient;
	}
	
	/**
	 * @param ownUri the ownUri to set
	 */
	public void setOwnUri(final URI ownUri) {
		this.ownUri = ownUri;
	}

	/**
	 * @return the ownUri
	 */
	public URI getOwnUri() {
		return ownUri;
	}
}
