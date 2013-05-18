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

package org.bibsonomy.database;

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Classifier;
import org.bibsonomy.common.enums.ClassifierSettings;
import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.ConceptUpdateOperation;
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.GroupUpdateOperation;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.common.enums.InetAddressStatus;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.enums.SpamStatus;
import org.bibsonomy.common.enums.StatisticsConstraint;
import org.bibsonomy.common.enums.TagSimilarity;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.common.errors.UnspecifiedErrorMessage;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.common.exceptions.QueryTimeoutException;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.common.exceptions.SynchronizationRunningException;
import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.database.managers.AdminDatabaseManager;
import org.bibsonomy.database.managers.AuthorDatabaseManager;
import org.bibsonomy.database.managers.BasketDatabaseManager;
import org.bibsonomy.database.managers.BibTexDatabaseManager;
import org.bibsonomy.database.managers.BibTexExtraDatabaseManager;
import org.bibsonomy.database.managers.BookmarkDatabaseManager;
import org.bibsonomy.database.managers.CrudableContent;
import org.bibsonomy.database.managers.DocumentDatabaseManager;
import org.bibsonomy.database.managers.GoldStandardPublicationDatabaseManager;
import org.bibsonomy.database.managers.GroupDatabaseManager;
import org.bibsonomy.database.managers.InboxDatabaseManager;
import org.bibsonomy.database.managers.PermissionDatabaseManager;
import org.bibsonomy.database.managers.StatisticsDatabaseManager;
import org.bibsonomy.database.managers.TagDatabaseManager;
import org.bibsonomy.database.managers.TagRelationDatabaseManager;
import org.bibsonomy.database.managers.UserDatabaseManager;
import org.bibsonomy.database.managers.WikiDatabaseManager;
import org.bibsonomy.database.managers.discussion.CommentDatabaseManager;
import org.bibsonomy.database.managers.discussion.DiscussionDatabaseManager;
import org.bibsonomy.database.managers.discussion.DiscussionItemDatabaseManager;
import org.bibsonomy.database.managers.discussion.ReviewDatabaseManager;
import org.bibsonomy.database.params.BibTexParam;
import org.bibsonomy.database.params.BookmarkParam;
import org.bibsonomy.database.params.GenericParam;
import org.bibsonomy.database.params.StatisticsParam;
import org.bibsonomy.database.params.TagParam;
import org.bibsonomy.database.params.TagRelationParam;
import org.bibsonomy.database.params.UserParam;
import org.bibsonomy.database.systemstags.SystemTagsExtractor;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.database.systemstags.search.SearchSystemTag;
import org.bibsonomy.database.util.LogicInterfaceHelper;
import org.bibsonomy.model.Author;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Comment;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.GoldStandardPublication;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Review;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.Wiki;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.extra.BibTexExtra;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.sync.ConflictResolutionStrategy;
import org.bibsonomy.model.sync.SyncLogicInterface;
import org.bibsonomy.model.sync.SyncService;
import org.bibsonomy.model.sync.SynchronizationData;
import org.bibsonomy.model.sync.SynchronizationDirection;
import org.bibsonomy.model.sync.SynchronizationPost;
import org.bibsonomy.model.sync.SynchronizationStatus;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.PostUtils;
import org.bibsonomy.model.util.UserUtils;
import org.bibsonomy.sync.SynchronizationDatabaseManager;
import org.bibsonomy.util.ObjectUtils;

/**
 * Database Implementation of the LogicInterface
 * 
 * @author Jens Illig
 * @author Christian Kramer
 * @author Christian Claus
 * @author Dominik Benz
 * @author Robert Jäschke
 * 
 * @version $Id: DBLogic.java,v 1.254 2011-07-26 15:34:32 rja Exp $
 */
public class DBLogic implements LogicInterface, SyncLogicInterface {
    private static final Log log = LogFactory.getLog(DBLogic.class);

    /*
     * help maps for post managers and discussion managers
     */
    private final Map<Class<? extends Resource>, CrudableContent<? extends Resource, ? extends GenericParam>> allDatabaseManagers;
    private final Map<Class<? extends DiscussionItem>, DiscussionItemDatabaseManager<? extends DiscussionItem>> allDiscussionManagers;
    
    private final AuthorDatabaseManager authorDBManager;
    private final DocumentDatabaseManager docDBManager;
    private final PermissionDatabaseManager permissionDBManager;
    
    private final BookmarkDatabaseManager bookmarkDBManager;
    private final BibTexDatabaseManager publicationDBManager;
    private final GoldStandardPublicationDatabaseManager goldStandardPublicationDBManager;
    private final BibTexExtraDatabaseManager bibTexExtraDBManager;
    
    private final DiscussionDatabaseManager discussionDatabaseManager;
    private final ReviewDatabaseManager reviewDBManager;
    private final CommentDatabaseManager commentDBManager;
    
    private final UserDatabaseManager userDBManager;
    private final GroupDatabaseManager groupDBManager;
    private final TagDatabaseManager tagDBManager;
    private final AdminDatabaseManager adminDBManager;
    private final DBSessionFactory dbSessionFactory;
    private final StatisticsDatabaseManager statisticsDBManager;
    private final TagRelationDatabaseManager tagRelationsDBManager;
    private final BasketDatabaseManager basketDBManager;
    private final InboxDatabaseManager inboxDBManager;
    private final WikiDatabaseManager wikiDBManager;
    
    private final SynchronizationDatabaseManager syncDBManager;

    private final User loginUser;

    /**
     * Returns an implementation of the DBLogic.
     * 
     * @param loginUser
     *            - the user which wants to use the logic.
     * @param dbSessionFactory
     */
    protected DBLogic(final User loginUser, final DBSessionFactory dbSessionFactory) {
	this.loginUser = loginUser;

	this.allDatabaseManagers = new HashMap<Class<? extends Resource>, CrudableContent<? extends Resource, ? extends GenericParam>>();
	// publication db manager
	this.publicationDBManager = BibTexDatabaseManager.getInstance();
	this.allDatabaseManagers.put(BibTex.class, this.publicationDBManager);
	// bookmark db manager
	this.bookmarkDBManager = BookmarkDatabaseManager.getInstance();
	this.allDatabaseManagers.put(Bookmark.class, this.bookmarkDBManager);
	
	// gold standard publication db manager
	this.goldStandardPublicationDBManager = GoldStandardPublicationDatabaseManager.getInstance();
	this.allDatabaseManagers.put(GoldStandardPublication.class, this.goldStandardPublicationDBManager);

	// discussion and discussion item db manager
	this.commentDBManager = CommentDatabaseManager.getInstance();
	this.reviewDBManager = ReviewDatabaseManager.getInstance();	
	this.discussionDatabaseManager = DiscussionDatabaseManager.getInstance();
	
	this.allDiscussionManagers = new HashMap<Class<? extends DiscussionItem>, DiscussionItemDatabaseManager<? extends DiscussionItem>>();
	this.allDiscussionManagers.put(Comment.class, this.commentDBManager);
	this.allDiscussionManagers.put(Review.class, this.reviewDBManager);
	
	this.authorDBManager = AuthorDatabaseManager.getInstance();
	this.docDBManager = DocumentDatabaseManager.getInstance();
	this.userDBManager = UserDatabaseManager.getInstance();
	this.groupDBManager = GroupDatabaseManager.getInstance();
	this.tagDBManager = TagDatabaseManager.getInstance();
	this.adminDBManager = AdminDatabaseManager.getInstance();
	this.permissionDBManager = PermissionDatabaseManager.getInstance();
	this.statisticsDBManager = StatisticsDatabaseManager.getInstance();
	this.tagRelationsDBManager = TagRelationDatabaseManager.getInstance();

	this.basketDBManager = BasketDatabaseManager.getInstance();
	this.inboxDBManager = InboxDatabaseManager.getInstance();

	this.wikiDBManager = WikiDatabaseManager.getInstance();
	
	this.syncDBManager = SynchronizationDatabaseManager.getInstance();
	
	
	this.bibTexExtraDBManager = BibTexExtraDatabaseManager.getInstance();
	
	
	this.dbSessionFactory = dbSessionFactory;
    }

    /**
     * Returns a new database session. If a user is logged in, he gets the
     * master connection, if not logged in, the secondary connection
     */
    private DBSession openSession() {
	return this.dbSessionFactory.getDatabaseSession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getUserDetails(java.lang.String)
     * TODO: if userName = loginUser.getName() we could just return loginUser.
     */
    @Override
    public User getUserDetails(final String userName) {
	final DBSession session = this.openSession();
	try {
	    /*
	     * We don't use userName but user.getName() in the remaining part of
	     * this method, since the name gets normalized in getUserDetails().
	     */
	    final User user = this.userDBManager.getUserDetails(userName, session);

	    /*
	     * only admin and myself may see which group I'm a member of
	     */
	    if (this.permissionDBManager.isAdminOrSelf(this.loginUser, user.getName())) {
		    user.setGroups(this.groupDBManager.getGroupsForUser(user.getName(), true, session));
		    // fill user's spam informations
		    this.adminDBManager.getClassifierUserDetails(user, session);
		return user;
	    }

	    /*
	     * respect user privacy settings
	     * clear all profile attributes if current login user isn't allowed to see the profile
	     */
	    if (!this.permissionDBManager.isAllowedToAccessUsersProfile(user, this.loginUser, session)) {
		/*
		 * TODO: this practically clears /all/ user information
		 */
	    return new User(user.getName());
	    }

	    /*
	     * clear the private stuff
	     */
	    user.setEmail(null);

	    user.setApiKey(null);
	    user.setPassword(null);

	    user.setReminderPassword(null);
	    user.setReminderPasswordRequestDate(null);

	    user.setSettings(null);

	    /*
	     * FIXME: other things set in userDBManager.getUserDetails() maybe not cleared!
	     */

	    return user;
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#getSynchronization(java.lang.String, java.lang.Class, java.util.List, org.bibsonomy.model.sync.ConflictResolutionStrategy, java.lang.String)
     */
    @Override
    public List<SynchronizationPost> getSyncPlan(final String userName, final URI service, final Class<? extends Resource> resourceType, final List<SynchronizationPost> clientPosts, final ConflictResolutionStrategy strategy, final SynchronizationDirection direction) {
    	this.permissionDBManager.ensureWriteAccess(loginUser, userName);
    	Date lastSuccessfulSyncDate = null;

    	final Map<String, SynchronizationPost> serverPosts;
    	final List<SynchronizationPost> posts;

    	final DBSession session = this.openSession();
    	try {
    		final SynchronizationData data = this.syncDBManager.getLastSyncData(userName, service, resourceType, null, session);
    		/*
    		 * check for a running synchronization
    		 */
    		if (present(data) && SynchronizationStatus.RUNNING.equals(data.getStatus())) {
    			// running synchronization
    			// FIXME: if synchronization fails, we can't recover 
    			throw new SynchronizationRunningException();
    		}
    		/*
    		 * check for last successful synchronization 
    		 */
    		final SynchronizationData lsd = this.syncDBManager.getLastSyncData(userName, service, resourceType, SynchronizationStatus.DONE, session);
    		if (present(lsd)) {
    			lastSuccessfulSyncDate = lsd.getLastSyncDate();	
    		}
    		/*
    		 * flag synchronization as planned
    		 * FIXME: if the client is not in the sync_services table, this 
    		 * statements silently fails. :-(
    		 */
    		this.syncDBManager.insertSynchronizationData(userName, service, resourceType, new Date(), SynchronizationStatus.PLANNED, session);
    		
    		/*
    		 * get posts from server (=this machine)
    		 */
    		if (BibTex.class.equals(resourceType)) {
    			serverPosts = publicationDBManager.getSyncPostsMapForUser(userName, session);
    		} else if(Bookmark.class.equals(resourceType)){
    			serverPosts = bookmarkDBManager.getSyncPostsMapForUser(userName, session);
    		} else {
    			throw new UnsupportedResourceTypeException();
    		}
    		
    		/*
    		 * if necessary, set the synchronization date to some distant old value  
    		 */
    		if (!present(lastSuccessfulSyncDate)) {
    			lastSuccessfulSyncDate = new Date(0);
    		}
    		/*
    		 * calculate synchronization plan
    		 */
    		posts = this.syncDBManager.getSyncPlan(serverPosts, clientPosts, lastSuccessfulSyncDate, strategy, direction);
    		
    		/*
    		 * attach "real" posts to the synchronization posts, which will be updated (or created) on the client
    		 */
    		for (final SynchronizationPost post : posts) {
    			switch (post.getAction()) {
    			case CREATE_CLIENT:
    			case UPDATE_CLIENT:
    				post.setPost(this.getPostDetails(post.getIntraHash(), userName));
    				break;
    			default:
    				break;
    			}
    		}
    		
    	} finally {
    		session.close();
    	}

		
		return posts;
    }
    
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#createSyncService()
     */
    @Override
	public void createSyncService(final URI service, final boolean server) {
    	this.permissionDBManager.ensureAdminAccess(loginUser);
    	final DBSession session = this.openSession();
    	try {
    		syncDBManager.createSyncService(session, service, server);
    	} finally {
    		session.close();
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#deleteSyncService(java.net.URI, boolean)
     */
    @Override
	public void deleteSyncService(final URI service, final boolean server) {
    	this.permissionDBManager.ensureAdminAccess(loginUser);
    	final DBSession session = this.openSession();
    	try {
    		syncDBManager.deleteSyncService(session, service, server);
    	} finally {
    		session.close();
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#createSyncServer(java.lang.String, int, java.util.Properties)
     */
    @Override
    public void createSyncServer(final String userName, final URI service, final Class<? extends Resource> resourceType, final Properties userCredentials, final SynchronizationDirection direction, final ConflictResolutionStrategy strategy) {
    	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
		final DBSession session = this.openSession();
		try {
		    syncDBManager.createSyncServerForUser(session, userName, service, resourceType, userCredentials, direction, strategy);
		} finally {
		    session.close();
		}
    }
    
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#updateSyncServer(java.lang.String, java.net.URI, java.util.Properties)
     */
    @Override
    public void updateSyncServer(final String userName, final URI service, final Class<? extends Resource> resourceType, final Properties userCredentials, final SynchronizationDirection direction, final ConflictResolutionStrategy strategy) {
    	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
    	final DBSession session = this.openSession();
    	try {
    		syncDBManager.updateSyncServerForUser(session, userName, service, resourceType, userCredentials, direction, strategy);
    	} finally {
    		session.close();
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#deleteSyncServer(java.lang.String, java.net.URI)
     */
    @Override
    public void deleteSyncServer(final String userName, final URI service) {
    	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
    	final DBSession session = this.openSession();
    	try {
    		syncDBManager.deleteSyncServerForUser(session, userName, service);
    	} finally {
    		session.close();
    	}
    }
    
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.sync.SyncLogicInterface#getSyncServerForUser(java.lang.String)
	 */
    @Override
    public List<SyncService> getSyncServer(final String userName) {
    	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
    	final DBSession session = this.openSession();
    	try {
    		return syncDBManager.getSyncServersForUser(userName, session);
    	} finally {
    		session.close();
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#getAvlSyncServer()
     */
    @Override
    public List<URI> getSyncServices(final boolean server) {
    	final DBSession session = this.openSession();
    	try {
    		return syncDBManager.getSyncServices(session, server);
    	} finally {
    		session.close();
    	}
    }
     
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#getLastSynchronizationData(java.lang.String, int, int)
     */
     @Override
     public SynchronizationData getLastSyncData(final String userName, final URI service, final Class<? extends Resource> resourceType) {
    	 this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
    	 final DBSession session = this.openSession();
    	 try {
    		 return syncDBManager.getLastSyncData(userName, service, resourceType, null, session);
    	 } finally {
    		 session.close();
    	 }
     }

    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#setCurrentSyncDone(org.bibsonomy.model.sync.SynchronizationData)
     */
    @Override
    public void updateSyncData(final String userName, final URI service, final Class<? extends Resource> resourceType, final Date syncDate, final SynchronizationStatus status, final String info) {
    	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
    	final DBSession session = this.openSession();
    	try {
    		syncDBManager.updateSyncData(session, userName, service, resourceType, syncDate, status, info);
    	} finally {
    		session.close();
    	}
    }
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#setCurrentSyncDone(org.bibsonomy.model.sync.SynchronizationData)
     */
    @Override
    public void deleteSyncData(final String userName, final URI service, final Class<? extends Resource> resourceType, final Date syncDate) {
    	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
    	final DBSession session = this.openSession();
    	try {
    		syncDBManager.deleteSyncData(session, userName, service, resourceType, syncDate);
    	} finally {
    		session.close();
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.sync.SyncLogicInterface#getPostsForSync(java.lang.Class, java.lang.String)
     */
    @Override
    public List<SynchronizationPost> getSyncPosts (final String userName, final Class<? extends Resource> resourceType) {
    	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);
        final DBSession session = this.openSession();
        try {
    		if (resourceType == BibTex.class) {
    		    return this.publicationDBManager.getSyncPostsListForUser(userName, session);
    		} else if(resourceType == Bookmark.class) {
    		    return this.bookmarkDBManager.getSyncPostsListForUser(userName, session);
    		} else {
    			throw new UnsupportedResourceTypeException();
    		}
        } finally {
            session.close();
        }
    }
    
    /**
     * TODO: rename method doesn't validate anything
     * Method to handle privacy settings of posts for synchronization
     * @param post
     */
    private void validateGroupsForSynchronization(final Post<? extends Resource> post) {
    	/*
    	 * check if not public or private group 
    	 */
    	if (!GroupUtils.containsExclusiveGroup(post.getGroups())) {
    		/*
    		 * post has group -> change to private
    		 */
    		post.setGroups(Collections.singleton(GroupUtils.getPrivateGroup()));
    	}
    	/*
    	 * if public or private is nothing to do
    	 */
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.PostLogicInterface#getPosts(java.lang.Class,
     * org.bibsonomy.common.enums.GroupingEntity, java.lang.String,
     * java.util.List, java.lang.String, org.bibsonomy.model.enums.Order,
     * org.bibsonomy.common.enums.FilterEntity, int, int, java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T extends Resource> List<Post<T>> getPosts(final Class<T> resourceType, final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final FilterEntity filter, final int start, final int end, final String search) {
	// check allowed start-/end-values
	if (GroupingEntity.ALL.equals(grouping) && !present(tags) && !present(search)) {
	    this.permissionDBManager.checkStartEnd(loginUser, start, end, "post");
	}

	// check maximum number of allowed tags
	if (this.permissionDBManager.exceedsMaxmimumSize(tags)) {
	    return new ArrayList<Post<T>>();
	}
	// check for systemTags disabling this resourceType
	if (!this.systemTagsAllowResourceType(tags, resourceType)) {
	    return new ArrayList<Post<T>>();
	}
	final DBSession session = this.openSession();
	try {
	    /*
	     * if (resourceType == Resource.class) { yes, this IS unsave and
	     * indeed it BREAKS restrictions on generic-constraints. it is the
	     * result of two designs: 1. @ibatis: database-results should be
	     * accessible as a stream or should at least be saved using the
	     * visitor pattern (collection<? super X> arguments would do fine)
	     * 2. @bibsonomy: this method needs runtime-type-checking which is
	     * not supported by generics so what: copy each and every entry
	     * manually or split this method to become type-safe WITHOUT falling
	     * back to <? extends Resource> (which means read-only) in the whole
	     * project result = bibtexDBManager.getPosts(authUser, grouping,
	     * groupingName, tags, hash, popular, added, start, end, false); //
	     * TODO: solve problem with limit+offset:
	     * result.addAll(bookmarkDBManager.getPosts(authUser, grouping,
	     * groupingName, tags, hash, popular, added, start, end, false));
	     * 
	     */
	    if (resourceType == BibTex.class) {
		final BibTexParam param = LogicInterfaceHelper.buildParam(BibTexParam.class, grouping, groupingName, tags, hash, order, start, end, search, filter, this.loginUser);
		// check permissions for displaying links to documents
		final boolean allowedToAccessUsersOrGroupDocuments = this.permissionDBManager.isAllowedToAccessUsersOrGroupDocuments(this.loginUser, grouping, groupingName, filter, session);

		if (!allowedToAccessUsersOrGroupDocuments) {
		    param.setFilter(FilterEntity.JUST_POSTS);
		} else if (!present(filter)) {
		    param.setFilter(FilterEntity.POSTS_WITH_DOCUMENTS);
		}

		// this is save because of RTTI-check of resourceType argument
		// which is of class T
		final List<Post<T>> publications = (List) this.publicationDBManager.getPosts(param, session);
		SystemTagsExtractor.handleHiddenSystemTags(publications, loginUser.getName());
		return publications;
	    } 

	    if (resourceType == Bookmark.class) {
		// check filters
		// can not add filter to BookmarkParam yet, but need to add
		// group before buildParam
		if (this.permissionDBManager.checkFilterPermissions(filter, this.loginUser)) {
		    /*
		     * FIXME: it is not safe, what is done here!
		     * checkFilterPermissions only checks, if ANY filter is
		     * applicable by loginUser. But here we assume
		     * ADMIN_SPAM_POSTS has been checked!
		     */
		    loginUser.addGroup(new Group(GroupID.PUBLIC_SPAM));
		}

		final BookmarkParam param = LogicInterfaceHelper.buildParam(BookmarkParam.class, grouping, groupingName, tags, hash, order, start, end, search, filter, this.loginUser);
		final List<Post<T>> bookmarks= (List) this.bookmarkDBManager.getPosts(param, session);
		SystemTagsExtractor.handleHiddenSystemTags(bookmarks, loginUser.getName());
		return bookmarks;
	    }

	    if (resourceType == GoldStandardPublication.class) {
		final BibTexParam param = LogicInterfaceHelper.buildParam(BibTexParam.class, grouping, groupingName, tags, hash, order, start, end, search, filter, this.loginUser);
		return (List) this.goldStandardPublicationDBManager.getPosts(param, session);
	    }

	    throw new UnsupportedResourceTypeException();
	} catch (final QueryTimeoutException ex) {
	    // if a query times out, we return an empty list
	    return new ArrayList<Post<T>>();
	} finally {
	    session.close();
	}
    }

    private boolean systemTagsAllowResourceType(final Collection<String> tags, final Class<? extends Resource> resourceType) {
	if (present(tags)) {
	    for (final String tagName : tags) {
		final SearchSystemTag sysTag = SystemTagsUtil.createSearchSystemTag(tagName);
		if (present(sysTag)) {
		    if (!sysTag.allowsResource(resourceType)) {
			return false;
		    }
		}
	    }
	}
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bibsonomy.model.logic.PostLogicInterface#getPostDetails(java.lang.String, java.lang.String)
     */
    @Override
    public Post<? extends Resource> getPostDetails(final String resourceHash, final String userName) throws ResourceMovedException, ResourceNotFoundException {
	final DBSession session = this.openSession();
	try {
	    for (final CrudableContent<? extends Resource, ? extends GenericParam> manager : this.allDatabaseManagers.values()) {
		final Post<? extends Resource> post = manager.getPostDetails(this.loginUser.getName(), resourceHash, userName, UserUtils.getListOfGroupIDs(this.loginUser), session);
		/*
		 * if a manager found a post, return it
		 */
		if (present(post)) {
			/*
			 * XXX: can't be added to the postDatabaseManager; calls
			 * getPostDetails with an emtpy list of visible groups
			 */
			final Resource resource = post.getResource();
			final List<DiscussionItem> discussionSpace = this.discussionDatabaseManager.getDiscussionSpace(this.loginUser, resource.getInterHash(), session);
			resource.setDiscussionItems(discussionSpace);
			return post;
		}
		/*
		 * check next manager
		 */
	    }
	} finally {
	    session.close();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#getGroups(int, int)
     */
    @Override
    public List<Group> getGroups(final int start, final int end) {
	final DBSession session = this.openSession();
	try {
	    return this.groupDBManager.getAllGroups(start, end, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getGroupDetails(java.lang.String
     * )
     */
    @Override
    public Group getGroupDetails(final String groupName) {
	final DBSession session = this.openSession();
	try {
	    final Group myGroup = this.groupDBManager.getGroupByName(groupName, session);
	    if (present(myGroup)) {
		myGroup.setTagSets(this.groupDBManager.getGroupTagSets(groupName, session));
	    }
	    return myGroup;
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#getTags(java.lang.Class,
     * org.bibsonomy.common.enums.GroupingEntity, java.lang.String,
     * java.lang.String, java.util.List, java.lang.String,
     * org.bibsonomy.model.enums.Order, int, int, java.lang.String,
     * org.bibsonomy.common.enums.TagSimilarity)
     */
    @Override
    public List<Tag> getTags(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final String regex, final List<String> tags, final String hash, final Order order, final int start, final int end, final String search, final TagSimilarity relation) {
	if (GroupingEntity.ALL.equals(grouping)) {
	    this.permissionDBManager.checkStartEnd(loginUser, start, end, "Tag");
	}

	final DBSession session = this.openSession();
	try {
	    final TagParam param = LogicInterfaceHelper.buildParam(TagParam.class, grouping, groupingName, tags, hash, order, start, end, search, null, this.loginUser);
	    param.setTagRelationType(relation);

	    if (resourceType == BibTex.class || resourceType == Bookmark.class || resourceType == Resource.class) {
		// this is save because of RTTI-check of resourceType argument
		// which is of class T
		param.setRegex(regex);
		// need to switch from class to string to ensure legibility of
		// Tags.xml
		param.setContentTypeByClass(resourceType);
		return this.tagDBManager.getTags(param, session);
	    }

	    throw new UnsupportedResourceTypeException("The requested resourcetype (" + resourceType.getClass().getName() + ") is not supported.");
	} catch (final QueryTimeoutException ex) {
	    // if a query times out, we return an empty list
	    return new ArrayList<Tag>();
	} finally {
	    session.close();
	}
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getTagDetails(java.lang.String)
     */
    @Override
    public Tag getTagDetails(final String tagName) {
	final DBSession session = openSession();
	try {
	    final TagParam param = LogicInterfaceHelper.buildParam(TagParam.class, null, this.loginUser.getName(), Arrays.asList(tagName), null, null, 0, 1, null, null, this.loginUser);
	    return this.tagDBManager.getTagDetails(param, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(final String userName) {
	// TODO: take care of toLowerCase()!
	this.ensureLoggedIn();
	/*
	 * only an admin or the user himself may delete the account
	 */
	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, userName);

	final DBSession session = openSession();
	try {
	    userDBManager.deleteUser(userName, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#deleteGroup(java.lang.String)
     */
    @Override
    public void deleteGroup(final String groupName) {
	throw new UnsupportedOperationException("not yet available");

	// final DBSession session = openSession();
	// try {
	// groupDBManager.deleteGroup(groupName, session);
	// } finally {
	// session.close();
	// }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#removeUserFromGroup(java.lang
     * .String, java.lang.String)
     */
    @Override
    public void deleteUserFromGroup(final String groupName, final String userName) {
	// TODO: take care of toLowerCase()!
	// FIXME: IMPORTANT: not everybody may do this!
	// better do nothing than anything horribly wrong:
	throw new UnsupportedOperationException("not yet available");
	// final DBSession session = openSession();
	// try {
	// groupDBManager.removeUserFromGroup(groupName, userName, session);
	// } finally {
	// session.close();
	// }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.PostLogicInterface#deletePosts(java.lang.String
     * , java.util.List)
     */
    @Override
    public void deletePosts(final String userName, final List<String> resourceHashes) {
	/*
	 * check permissions
	 */
	this.ensureLoggedIn();
	this.permissionDBManager.ensureWriteAccess(this.loginUser, userName);
	/*
	 * to store hashes of missing resources
	 */
	final List<String> missingResources = new LinkedList<String>();

	final DBSession session = openSession();
	try {
	    final String lowerCaseUserName = userName.toLowerCase();
	    for (final String resourceHash : resourceHashes) {
		/*
		 * delete one resource
		 */
		boolean resourceFound = false;
		// TODO would be nice to know about the resourcetype or the
		// instance behind this resourceHash
		for (final CrudableContent<? extends Resource, ? extends GenericParam> man : this.allDatabaseManagers.values()) {
		    if (man.deletePost(lowerCaseUserName, resourceHash, session)) {
			resourceFound = true;
			break;
		    }
		}
		/*
		 * remember missing resources
		 */
		if (!resourceFound) {
		    missingResources.add(resourceHash);
		}
	    }
	} finally {
	    session.close();
	}
	/*
	 * throw exception for missing resources
	 */
	if (missingResources.size() > 0) {
	    throw new IllegalStateException("The resource(s) with ID(s) " + missingResources + " do(es) not exist and could hence not be deleted.");
	}
    }

    /** 
     * Check for each group actually exist and if the
     * posting user is allowed to post. If yes, insert the correct group ID into
     * the given post's groups.
     * 
     * @param groups the groups to validate
     */
    protected void validateGroups(final User user, final Set<Group> groups, final DBSession session) {
	/*
	 * First check for "public" and "private". Those two groups are special,
	 * they can't be assigned with another group.
	 */
	if (GroupUtils.containsExclusiveGroup(groups)) {
	    if (groups.size() > 1) {
		/*
		 * Those two groups are exclusive - they can not appear together
		 * or with any other group.
		 */
		throw new ValidationException("Group 'public' (or 'private') can not be combined with other groups.");
	    }
	    /*
	     * only one group and it is "public" or "private" -> set group id
	     * and return post
	     */
	    final Group group = groups.iterator().next();
	    if (group.equals(GroupUtils.getPrivateGroup())) {
		group.setGroupId(GroupUtils.getPrivateGroup().getGroupId());
	    } else {
		group.setGroupId(GroupUtils.getPublicGroup().getGroupId());
	    }
	} else {
	    /*
	     * only non-special groups remain (including "friends") - check
	     * those
	     */
	    /*
	     * retrieve the user's groups
	     */
	    final Set<Integer> groupIds = new HashSet<Integer>(this.groupDBManager.getGroupIdsForUser(user.getName(), session));
	    /*
	     * add "friends" group
	     */
	    groupIds.add(GroupID.FRIENDS.getId());
	    /*
	     * check that there are only groups the user is allowed to post to.
	     */
	    for (final Group group : groups) {
		final Group testGroup = this.groupDBManager.getGroupByName(group.getName().toLowerCase(), session);
		if (testGroup == null) {
		    // group does not exist
		    throw new ValidationException("Group " + group.getName() + " does not exist");
		}
		if (!groupIds.contains(testGroup.getGroupId())) {
		    // the posting user is not a member of this group
		    throw new ValidationException("User " + user.getName() + " is not a member of group " + group.getName());
		}
		group.setGroupId(testGroup.getGroupId());
	    }
	}

	// no group specified -> make it public
	if (groups.size() == 0) {
	    groups.add(GroupUtils.getPublicGroup());
	}
    }

    /**
     * Helper method to retrieve an appropriate database manager
     * 
     * @param <T>
     *            extends Resource - the resource type
     * @param post
     *            - a post of type T
     * @return an appropriate database manager
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T extends Resource> CrudableContent<T, GenericParam> getFittingDatabaseManager(final Post<T> post) {
	final Class<?> resourceClass = post.getResource().getClass();
	CrudableContent<? extends Resource, ? extends GenericParam> man = this.allDatabaseManagers.get(resourceClass);
	if (man == null) {
	    for (final Map.Entry<Class<? extends Resource>, CrudableContent<? extends Resource, ? extends GenericParam>> entry : this.allDatabaseManagers.entrySet()) {
		if (entry.getKey().isAssignableFrom(resourceClass)) {
		    man = entry.getValue();
		    break;
		}
	    }
	    if (man == null) {
		throw new UnsupportedResourceTypeException();
	    }
	}
	return ((CrudableContent) man);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#addUserToGroup(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void addUserToGroup(final String groupName, final String userName) {
	// TODO: take care of toLowerCase()!
	throw new UnsupportedOperationException("not yet available");
	// final DBSession session = openSession();
	// try {
	// groupDBManager.addUserToGroup(groupName, userName, session);
	// } finally {
	// session.close();
	// }
    }

    /**
     * helper method to check if a user is currently logged in
     */
    private void ensureLoggedIn() {
	if (this.loginUser.getName() == null) {
	    throw new AccessDeniedException("Please log in!");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#createGroup(org.bibsonomy.model
     * .Group)
     */
    @Override
    public String createGroup(final Group group) {
	this.ensureLoggedIn();
	/*
	 * check permissions
	 */
	this.permissionDBManager.ensureAdminAccess(loginUser);

	final DBSession session = this.openSession();
	try {
	    this.groupDBManager.createGroup(group, session);

	    return group.getName();
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#updateGroup(org.bibsonomy.model
     * .Group)
     */
    @Override
    public String updateGroup(final Group group, final GroupUpdateOperation operation) {
	this.ensureLoggedIn();

	final String groupName = group.getName();
	if (!(present(groupName) && present(group.getGroupId()) && present(group.getPrivlevel()) && present(group.isSharedDocuments()))) {
	    throw new ValidationException("The given group is not valid.");
	}

	final DBSession session = this.openSession();

	try	{
	    switch(operation) {
	    case UPDATE_ALL:
		//					this.groupDBManager.updateGroupSettings(group, session);
		//handle users
		throw new UnsupportedOperationException("The method " + GroupUpdateOperation.UPDATE_ALL + " is not yet implemented.");

	    case UPDATE_SETTINGS:
		this.groupDBManager.updateGroupSettings(group, session);
		break;
	    case ADD_NEW_USER:
		throw new UnsupportedOperationException("The method " + GroupUpdateOperation.ADD_NEW_USER + " is not yet implemented.");
	    default:
		throw new UnsupportedOperationException("The given method is not yet implemented.");
	    }
	} 
	finally	{
	    session.close();
	}

	return groupName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.PostLogicInterface#createPosts(java.util.List)
     */
    @Override
    public List<String> createPosts(final List<Post<?>> posts) {
	// TODO: Which of these checks should result in a DatabaseException,
	this.ensureLoggedIn();
	/*
	 * check permissions
	 */
	for (final Post<?> post : posts) {
	    PostUtils.populatePostWithUser(post, this.loginUser);
	    PostUtils.populatePostWithDate(post, this.loginUser);
	    this.permissionDBManager.ensureWriteAccess(post, this.loginUser);
	}

	/*
	 * insert posts TODO: more efficient implementation (transactions,
	 * deadlock handling, asynchronous, etc.)
	 */
	final List<String> hashes = new LinkedList<String>();
	/*
	 * open session to store all the posts
	 */
	final DBSession session = openSession();
	final DatabaseException collectedException = new DatabaseException();
	try {
	    for (final Post<?> post : posts) {
		try {
		    hashes.add(this.createPost(post, session));
		} catch (final DatabaseException dbex) {
		    collectedException.addErrors(dbex);
		} catch (final Exception ex) {
		    // some exception other than those covered in the DatabaseException was thrown					
		    collectedException.addToErrorMessages(post.getResource().getIntraHash(), new UnspecifiedErrorMessage(ex));
		}
	    }
	} finally {
	    session.close();
	}

	if (collectedException.hasErrorMessages()) {
	    throw collectedException;
	}

	return hashes;
    }


    /**
     * Adds a post in the database.
     */
private <T extends Resource> String createPost(final Post<T> post, final DBSession session) {
	final CrudableContent<T, GenericParam> manager = this.getFittingDatabaseManager(post);
	post.getResource().recalculateHashes();
	
	/*
	 *check and set post visibility for synchronization 
	 */
	if (Role.SYNC.equals(this.loginUser.getRole())) {
		validateGroupsForSynchronization(post);
	}
	this.validateGroups(post.getUser(), post.getGroups(), session);
	
	/*
	 * change group IDs to spam group IDs
	 */
	PostUtils.setGroupIds(post, this.loginUser);

	manager.createPost(post, session);

	// if we don't get an exception here, we assume the resource has
	// been successfully created
	return post.getResource().getIntraHash();
    }

    /** 
     * The given posts are updated. If the operation is {@link PostUpdateOperation#UPDATE_TAGS}, 
     * the posts must only contain the 
     * <ul>
     * <li>date, </li>
     * <li>tags,</li>
     * <li>intraHash,</li>
     * <li>and optionally a username.
     * </ul>
     * 
     * @see
     * org.bibsonomy.model.logic.PostLogicInterface#updatePosts(java.util.List, org.bibsonomy.common.enums.PostUpdateOperation)
     */
    @Override
    public List<String> updatePosts(final List<Post<?>> posts, final PostUpdateOperation operation) {
	// TODO: Which of these checks should result in a DatabaseException,
	// which do we want to handle otherwise (=status quo)
    // TODO: not everybody can update gold standard publication posts
	this.ensureLoggedIn();
	/*
	 * check permissions
	 */
	for (final Post<?> post : posts) {
	    PostUtils.populatePostWithUser(post, this.loginUser);
	    PostUtils.populatePostWithDate(post, this.loginUser);
	    this.permissionDBManager.ensureWriteAccess(post, this.loginUser);
	}
	final List<String> hashes = new LinkedList<String>();
	/*
	 * open session
	 */
	final DBSession session = openSession();
	final DatabaseException collectedException = new DatabaseException();
	try {
	    for (final Post<?> post : posts) {
		try {
		    hashes.add(this.updatePost(post, operation, session));
		} catch (final DatabaseException dbex) {
		    collectedException.addErrors(dbex);
		} catch (final Exception ex){
		    // some exception other than those covered in the DatabaseException was thrown					
		    collectedException.addToErrorMessages(post.getResource().getIntraHash(), new UnspecifiedErrorMessage(ex));
		}
	    }
	} finally {
	    session.close();
	}

	if (collectedException.hasErrorMessages()) {
	    throw collectedException;
	}

	return hashes;
    }


    /**
     * Updates a post in the database.
     */
    private <T extends Resource> String updatePost(final Post<T> post, final PostUpdateOperation operation, final DBSession session) {
	final CrudableContent<T, GenericParam> manager = getFittingDatabaseManager(post);
	final String oldIntraHash = post.getResource().getIntraHash();
	
	if(Role.SYNC.equals(loginUser.getRole())){
		validateGroupsForSynchronization(post);
	}
	this.validateGroups(post.getUser(), post.getGroups(), session);

	/*
	 * change group IDs to spam group IDs
	 */
	PostUtils.setGroupIds(post, this.loginUser);
	
	/*
	 * XXX: this is a "hack" and will be replaced soon
	 * If the operation is UPDATE_URLS then create/delete the url right here and
	 * return the intra hash.
	 */
	
	if (PostUpdateOperation.UPDATE_URLS_ADD.equals(operation)) {  
	    log.debug("Adding URL in updatePost()/DBLogic.java");
	    final BibTex resource = (BibTex) post.getResource();
	    final BibTexExtra resourceExtra = resource.getExtraUrls().get(0);
	    resourceExtra.setDate(new Date());
	    bibTexExtraDBManager.createURL(post.getResource()
		.getIntraHash(), this.loginUser.getName(),
		resourceExtra.getUrl().toExternalForm(),
		resourceExtra.getText(), session);

	    return post.getResource().getIntraHash();
	} else if (PostUpdateOperation.UPDATE_URLS_DELETE.equals(operation)) {
	    log.debug("Deleting URL in updatePost()/DBLogic.java");
	    final BibTex resource = (BibTex) post.getResource();
	    final BibTexExtra resourceExtra = resource.getExtraUrls().get(0);
	    bibTexExtraDBManager.deleteURL(post.getResource()
		    .getIntraHash(), this.loginUser.getName(),
		    resourceExtra.getUrl().toExternalForm(), session);
	    
	    return post.getResource().getIntraHash();
	}

	/* 
	 * update post
	 */
	manager.updatePost(post, oldIntraHash, operation, session, loginUser);

	// if we don't get an exception here, we assume the resource has
	// been successfully updated
	return post.getResource().getIntraHash();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#updateTags(org.bibsonomy.model
     * .User, java.util.List, java.util.List) <p>TODO: possible options which
     * one might want to add:</p> <ul> <li>ignore case</li> </ul>
     */
    @Override
    public int updateTags(final User user, final List<Tag> tagsToReplace, final List<Tag> replacementTags, final boolean updateRelations) {
	this.ensureLoggedIn();
	this.permissionDBManager.ensureWriteAccess(loginUser, user.getName());

	/*
	 * 
	 */
	final DBSession session = this.openSession();
	try {

	    if(updateRelations) {
		if(tagsToReplace.size() != 1 || replacementTags.size() != 1) {
		    throw new ValidationException("tag relations can only be updated, when exactly one tag is exchanged by exactly one other tag.");
		}

		this.tagRelationsDBManager.updateTagRelations(user, tagsToReplace.get(0), replacementTags.get(0), session);

	    }

	    /*
	     * finally delegate to tagDBManager
	     */
	    return this.tagDBManager.updateTags(user, tagsToReplace, replacementTags, session);

	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#createUser(org.bibsonomy.model
     * .User)
     */
    @Override
    public String createUser(final User user) {
	/*
	 * We ensure, that the user is logged in and has admin privileges. This
	 * seems to be a contradiction, because if a user wants to register, he
	 * is not logged in.
	 * 
	 * The current solution to this paradox is, that registration is done
	 * using an instance of the DBLogic which contains a user with role
	 * "admin".
	 */
	this.ensureLoggedIn();
	this.permissionDBManager.ensureAdminAccess(loginUser);

	return this.storeUser(user, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#updateUser(org.bibsonomy.model
     * .User)
     */
    @Override
    public String updateUser(final User user, final UserUpdateOperation operation) {
	/*
	 * only logged in users can update user settings.
	 */
	if(!UserUpdateOperation.ACTIVATE.equals(operation)) {
	    this.ensureLoggedIn();
	    /*
	     * only admins can change settings of /other/ users
	     */
	    this.permissionDBManager.ensureIsAdminOrSelf(loginUser, user.getName());
	}
	final DBSession session = openSession();

	try {
	    switch (operation) {
	    case UPDATE_PASSWORD:
		return this.userDBManager.updatePasswordForUser(user, session);

	    case UPDATE_SETTINGS:
		return this.userDBManager.updateUserSettingsForUser(user, session);

	    case UPDATE_API:
		this.userDBManager.updateApiKeyForUser(user.getName(), session);
		break;

	    case UPDATE_CORE:
		return this.userDBManager.updateUserProfile(user, session);

	    case ACTIVATE:
		return this.userDBManager.activateUser(user, session);

	    case UPDATE_ALL:
		/*
		 * update only (!) spammer settings
		 * 
		 * FIXME: use a separate operation for that!
		 */
		if (user.getPrediction() != null || user.getSpammer() != null) {
		    /*
		     * only admins are allowed to change spammer settings
		     */
		    log.debug("Start update this framework");

		    this.permissionDBManager.ensureAdminAccess(loginUser);
		    /*
		     * open session and update spammer settings
		     */
		    final String mode = this.adminDBManager.getClassifierSettings(ClassifierSettings.TESTING, session);
		    log.debug("User prediction: " + user.getPrediction());
		    return this.adminDBManager.flagSpammer(user, this.getAuthenticatedUser().getName(), mode, session);
		}

		return this.storeUser(user, true);
	    }


	} finally {
	    session.close();
	}

	return null;
    }

    /**
     * TODO: extract the method to create and update user
     * 
     * Adds/updates a user in the database.
     */
    private String storeUser(final User user, final boolean update) {
	final DBSession session = openSession();

	try {
	    final User existingUser = userDBManager.getUserDetails(user.getName(), session);
	    final List<User> pendingUserList = userDBManager.getPendingUserByUsername(user.getName(), 0, Integer.MAX_VALUE, session);
	    if (update) {
		/*
		 * update the user
		 */
		if (!present(existingUser.getName())) {
		    /*
		     * error: user name does not exist
		     */
		    throw new ValidationException("user " + user.getName() + " does not exist");
		}

		return this.userDBManager.updateUser(user, session);
	    }

	    /*
	     * create a new user
	     */
	    if (present(existingUser.getName()) || pendingUserList.size() > 0) {
		/*
		 * error: user name already exists
		 */
		throw new ValidationException("user " + user.getName() + " already exists");
	    }
	    return this.userDBManager.createUser(user, session);
	} finally {
	    /*
	     * TODO: check, if rollback is handled correctly!
	     */
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#getAuthenticatedUser()
     */
    @Override
    public User getAuthenticatedUser() {
	return this.loginUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getAuthors(org.bibsonomy.common
     * .enums.GroupingEntity, java.lang.String, java.util.List,
     * java.lang.String, org.bibsonomy.model.enums.Order,
     * org.bibsonomy.common.enums.FilterEntity, int, int, java.lang.String)
     */
    @Override
    public List<Author> getAuthors(final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final FilterEntity filter, final int start, final int end, final String search) {
	/*
	 * FIXME: implement a chain or something similar
	 */
	final DBSession session = openSession();

	try {
	    if (GroupingEntity.ALL.equals(grouping)) {
		return this.authorDBManager.getAuthors(session);
	    }

	    throw new UnsupportedOperationException("Currently only ALL authors can be listed.");
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#addDocument(org.bibsonomy.model
     * .Document, java.lang.String)
     */
    @Override
    public String createDocument(final Document document, final String resourceHash) {
	this.ensureLoggedIn();
	final String userName = document.getUserName();

	/*
	 * users can only modify their own documents
	 */
	this.permissionDBManager.ensureWriteAccess(this.loginUser, userName);

	final DBSession session = openSession();
	try {
	    if (resourceHash != null) {
		/*
		 * document shall be attached to a post
		 */
		Post<BibTex> post = null;
		try {
		    post = publicationDBManager.getPostDetails(this.loginUser.getName(), resourceHash, userName, UserUtils.getListOfGroupIDs(this.loginUser), session);
		} catch (final ResourceMovedException ex) {
		    // ignore
		} catch (final ResourceNotFoundException ex) {
		    // ignore
		}
		if (present(post)) {
		    /*
		     * post really exists!
		     */
		    final boolean existingDoc = this.docDBManager.checkForExistingDocuments(userName, resourceHash, document.getFileName(), session);
		    if (existingDoc) {
			/*
			 * the post has already a file with that name attached
			 * ...
			 */
			this.docDBManager.updateDocument(post.getContentId(), document.getFileHash(), document.getFileName(), document.getMd5hash(), session);

		    } else {
			// add
			this.docDBManager.addDocument(userName, post.getContentId(), document.getFileHash(), document.getFileName(), document.getMd5hash(), session);
		    }

		} else {
		    throw new ValidationException("Could not find a post with hash '" + resourceHash + "'.");
		}

	    } else {
		// checks whether a layout definition is already uploaded
		// if not the new one will be stored in the database
		if (this.docDBManager.getDocument(userName, document.getFileHash(), session) == null) {
		    this.docDBManager.addDocument(userName, DocumentDatabaseManager.DEFAULT_CONTENT_ID, document.getFileHash(), document.getFileName(), document.getMd5hash(), session);
		}
	    }
	} finally {
	    session.close();
	}

	log.info("created new file " + document.getFileName() + " for user " + userName);
	return document.getFileHash();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getDocument(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Document getDocument(final String userName, final String fileHash) {
	this.ensureLoggedIn();

	final String lowerCaseUserName = userName.toLowerCase();
	this.permissionDBManager.ensureWriteAccess(this.loginUser, lowerCaseUserName);

	final DBSession session = openSession();

	try {
	    return docDBManager.getDocument(lowerCaseUserName, fileHash, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getDocument(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Document getDocument(final String userName, final String resourceHash, final String fileName) {
	this.ensureLoggedIn();

	final String lowerCaseUserName = userName.toLowerCase();

	final DBSession session = openSession();
	try {
	    if (present(resourceHash)) {
		/*
		 * we just forward this task to getPostDetails from the
		 * BibTeXDatabaseManager and extract the documents.
		 */
		Post<BibTex> post = null;
		try {
		    post = this.publicationDBManager.getPostDetails(this.loginUser.getName(), resourceHash, lowerCaseUserName, UserUtils.getListOfGroupIDs(this.loginUser), session);
		} catch (final ResourceMovedException ex) {
		    // ignore
		} catch (final ResourceNotFoundException ex) {
		    // ignore
		}
		if (post != null && post.getResource().getDocuments() != null) {
		    /*
		     * post found and post contains documents (bibtexdbmanager
		     * checks, if user might access documents and only then
		     * inserts them)
		     */
		    for (final Document document : post.getResource().getDocuments()) {
			if (document.getFileName().equals(fileName)) {
			    return document;
			}
		    }
		}
	    } else {
		/*
		 * users can only access their own documents
		 */
		this.permissionDBManager.ensureWriteAccess(this.loginUser, lowerCaseUserName);
		/*
		 * TODO: implement access to non post-connected documents
		 */
	    }
	} finally {
	    session.close();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#deleteDocument(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void deleteDocument(final Document document, final String resourceHash) {
	this.ensureLoggedIn();

	final String userName = document.getUserName();
	/*
	 * users can only modify their own documents
	 */
	this.permissionDBManager.ensureWriteAccess(this.loginUser, userName);

	final DBSession session = openSession();
	try {
	    if (resourceHash != null) {
		/*
		 * the document belongs to a post --> check if the user owns the
		 * post
		 */
		Post<BibTex> post = null;
		try {
		    post = publicationDBManager.getPostDetails(this.loginUser.getName(), resourceHash, userName, UserUtils.getListOfGroupIDs(this.loginUser), session);
		} catch (final ResourceMovedException ex) {
		    //ignore
		} catch (final ResourceNotFoundException ex) {
		    // ignore
		}
		if (post != null) {
		    /*
		     * the given resource hash belongs to a post of the user ->
		     * delete the corresponding document
		     */
		    if (this.docDBManager.checkForExistingDocuments(userName, resourceHash, document.getFileName(), session)) {
			this.docDBManager.deleteDocument(post.getContentId(), userName, document.getFileName(), session);
		    }
		} else {
		    throw new ValidationException("Could not find a post with hash '" + resourceHash + "'.");
		}
	    } else {
		/*
		 * the document does not belong to a post
		 */
		this.docDBManager.deleteDocumentWithNoPost(DocumentDatabaseManager.DEFAULT_CONTENT_ID, userName, document.getFileHash(), session);
	    }
	} finally {
	    session.close();
	}
	log.debug("deleted document " + document.getFileName() + " from user " + userName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#addInetAddressStatus(java.net
     * .InetAddress, org.bibsonomy.common.enums.InetAddressStatus)
     */
    @Override
    public void createInetAddressStatus(final InetAddress address, final InetAddressStatus status) {
	this.ensureLoggedIn();
	// only admins are allowed to change the status of an address
	this.permissionDBManager.ensureAdminAccess(this.loginUser);
	final DBSession session = openSession();
	try {
	    this.adminDBManager.addInetAddressStatus(address, status, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#deleteInetAdressStatus(java.
     * net.InetAddress)
     */
    @Override
    public void deleteInetAdressStatus(final InetAddress address) {
	this.ensureLoggedIn();
	// only admins are allowed to change the status of an address
	this.permissionDBManager.ensureAdminAccess(this.loginUser);
	final DBSession session = openSession();
	try {
	    this.adminDBManager.deleteInetAdressStatus(address, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getInetAddressStatus(java.net
     * .InetAddress)
     */
    @Override
    public InetAddressStatus getInetAddressStatus(final InetAddress address) {
	// everybody is allowed to ask for the status of an address
	/*
	 * TODO: is this really OK? At least it is neccessary, because otherwise
	 * the RegistrationHandler can not check the status of an address.
	 */
	// this.ensureLoggedIn();
	// this.permissionDBManager.ensureAdminAccess(loginUser);
	final DBSession session = openSession();
	try {
	    return this.adminDBManager.getInetAddressStatus(address, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.PostLogicInterface#getPostStatistics(java.lang
     * .Class, org.bibsonomy.common.enums.GroupingEntity, java.lang.String,
     * java.util.List, java.lang.String, org.bibsonomy.model.enums.Order,
     * org.bibsonomy.common.enums.FilterEntity, int, int, java.lang.String,
     * org.bibsonomy.common.enums.StatisticsConstraint)
     */
    @Override
    public int getPostStatistics(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final FilterEntity filter, final int start, final int end, final String search, final StatisticsConstraint constraint) {
	final DBSession session = openSession();

	try {
	    if (this.permissionDBManager.checkFilterPermissions(filter, this.loginUser)) {
		loginUser.addGroup(new Group(GroupID.PUBLIC_SPAM));
	    }

	    final StatisticsParam param = LogicInterfaceHelper.buildParam(StatisticsParam.class, grouping, groupingName, tags, hash, order, start, end, search, filter, this.loginUser);

	    if (resourceType == GoldStandardPublication.class || resourceType == BibTex.class || resourceType == Bookmark.class || resourceType == Resource.class) {
		param.setContentTypeByClass(resourceType);
		return this.statisticsDBManager.getPostStatistics(param, session);
	    }

	    throw new UnsupportedResourceTypeException("The requested resourcetype (" + resourceType.getClass().getName() + ") is not supported.");
	} catch (final QueryTimeoutException ex) {
	    // if a query times out, we return 0 (cause we also retun empty list when a query timeout exception is thrown)
	    return 0;
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getConcepts(java.lang.Class,
     * org.bibsonomy.common.enums.GroupingEntity, java.lang.String,
     * java.lang.String, java.util.List,
     * org.bibsonomy.common.enums.ConceptStatus, int, int)
     */
    @Override
    public List<Tag> getConcepts(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final String regex, final List<String> tags, final ConceptStatus status, final int start, final int end) {
	final DBSession session = openSession();
	try {
	    final TagRelationParam param = LogicInterfaceHelper.buildParam(TagRelationParam.class, grouping, groupingName, tags, null, null, start, end, null, null, this.loginUser);
	    param.setConceptStatus(status);
	    return this.tagRelationsDBManager.getConcepts(param, session);
	} finally {
	    session.close();
	}
    }

    /**
     * @return a concept, i.e. a tag with its assigned subtags
     * 
     * in both queries getConceptForUser and getGlobalConceptByName 
     * the case of parameter conceptName is ignored 
     *  
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getConceptDetails(java.lang.
     * String, org.bibsonomy.common.enums.GroupingEntity, java.lang.String)
     */
    @Override
    public Tag getConceptDetails(final String conceptName, final GroupingEntity grouping, final String groupingName) {
	final DBSession session = openSession();
	try {
	    if (GroupingEntity.USER.equals(grouping) || GroupingEntity.GROUP.equals(grouping) && present(groupingName)) {
		return this.tagRelationsDBManager.getConceptForUser(conceptName, groupingName, session);
	    } else if (GroupingEntity.ALL.equals(grouping)) {
		return this.tagRelationsDBManager.getGlobalConceptByName(conceptName, session);
	    }

	    throw new RuntimeException("Can't handle request");
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#createConcept(org.bibsonomy.
     * model.Tag, org.bibsonomy.common.enums.GroupingEntity, java.lang.String)
     */
    @Override
    public String createConcept(final Tag concept, final GroupingEntity grouping, final String groupingName) {
	if (GroupingEntity.USER.equals(grouping)) {
	    this.permissionDBManager.ensureIsAdminOrSelf(loginUser, groupingName);
	    return this.storeConcept(concept, grouping, groupingName, false);
	}
	throw new UnsupportedOperationException("Currently, tag relations can only be created for users.");
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#deleteConcept(java.lang.String,
     * org.bibsonomy.common.enums.GroupingEntity, java.lang.String)
     */
    @Override
    public void deleteConcept(final String concept, final GroupingEntity grouping, final String groupingName) {
	if (GroupingEntity.USER.equals(grouping)) {
	    this.permissionDBManager.ensureIsAdminOrSelf(loginUser, groupingName);

	    final DBSession session = openSession();
	    try {
		this.tagRelationsDBManager.deleteConcept(concept, groupingName, session);
	    } finally {
		session.close();
	    }
	    return;
	}
	throw new UnsupportedOperationException("Currently, tag relations can only be deleted for users.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#deleteRelation(java.lang.String,
     * java.lang.String, org.bibsonomy.common.enums.GroupingEntity,
     * java.lang.String)
     */
    @Override
    public void deleteRelation(final String upper, final String lower, final GroupingEntity grouping, final String groupingName) {
	if (GroupingEntity.USER.equals(grouping)) {
	    this.permissionDBManager.ensureIsAdminOrSelf(loginUser, groupingName);

	    final DBSession session = openSession();
	    try {
		this.tagRelationsDBManager.deleteRelation(upper, lower, groupingName, session);
		return;
	    } finally {
		session.close();
	    }
	}
	throw new UnsupportedOperationException("Currently, tag relations can only be created for users.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#updateConcept(org.bibsonomy.
     * model.Tag, org.bibsonomy.common.enums.GroupingEntity, java.lang.String)
     */
    @Override
    public String updateConcept(final Tag concept, final GroupingEntity grouping, final String groupingName, final ConceptUpdateOperation operation) {
	if (!GroupingEntity.USER.equals(grouping)) {
	    throw new UnsupportedOperationException("Currently only user's can have concepts.");
	}

	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, groupingName);

	final DBSession session = openSession();
	// now switch the operation and call the right method in the taglRelationsDBManager or DBLogic
	try {
	    switch (operation) {
	    case UPDATE:
		return this.storeConcept(concept, grouping, groupingName, true);
	    case PICK:
		this.tagRelationsDBManager.pickConcept(concept, groupingName, session);
		break;
	    case UNPICK:
		this.tagRelationsDBManager.unpickConcept(concept, groupingName, session);
		break;
	    case UNPICK_ALL:
		this.tagRelationsDBManager.unpickAllConcepts(groupingName, session);
		return null;
	    case PICK_ALL:
		this.tagRelationsDBManager.pickAllConcepts(groupingName, session);
		return null;
	    }

	    return concept.getName();
	} finally {
	    session.close();
	}

    }

    /**
     * Helper metod to store a concept
     * 
     * @param concept
     * @param grouping
     * @param groupingName
     * @param update
     * @return
     */
    private String storeConcept(final Tag concept, final GroupingEntity grouping, final String groupingName, final boolean update) {
	final DBSession session = openSession();
	if (update) {
	    this.tagRelationsDBManager.insertRelations(concept, groupingName, session);
	} else {
	    this.deleteConcept(concept.getName(), grouping, groupingName);
	    this.tagRelationsDBManager.insertRelations(concept, groupingName, session);
	}
	return concept.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#getUsers(java.lang.Class,
     * org.bibsonomy.common.enums.GroupingEntity, java.lang.String,
     * java.util.List, java.lang.String, org.bibsonomy.model.enums.Order,
     * org.bibsonomy.common.enums.UserRelation, java.lang.String, int, int)
     */
    @Override
    public List<User> getUsers(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final UserRelation relation, final String search, final int start, final int end) {
	// assemble param object
	final UserParam param = LogicInterfaceHelper.buildParam(UserParam.class, grouping, groupingName, tags, hash, order, start, end, search, null, loginUser);
	param.setUserRelation(relation);

	// check start/end values
	if (GroupingEntity.ALL.equals(grouping)) {
	    this.permissionDBManager.checkStartEnd(loginUser, start, end, "User");
	}

	final DBSession session = openSession();
	try {
	    // start chain
	    return this.userDBManager.getUsers(param, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getClassifiedUsers(org.bibsonomy
     * .common.enums.Classifier, org.bibsonomy.common.enums.SpamStatus, int)
     */
    @Override
    public List<User> getClassifiedUsers(final Classifier classifier, final SpamStatus status, final int limit) {
	this.permissionDBManager.ensureAdminAccess(this.loginUser);
	final DBSession session = openSession();
	try {
	    return this.adminDBManager.getClassifiedUsers(classifier, status, limit, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getClassifierSettings(org.bibsonomy
     * .common.enums.ClassifierSettings)
     */
    @Override
    public String getClassifierSettings(final ClassifierSettings key) {
	this.permissionDBManager.ensureAdminAccess(this.loginUser);
	final DBSession session = openSession();
	try {
	    return this.adminDBManager.getClassifierSettings(key, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#updateClassifierSettings(org
     * .bibsonomy.common.enums.ClassifierSettings, java.lang.String)
     */
    @Override
    public void updateClassifierSettings(final ClassifierSettings key, final String value) {
	this.permissionDBManager.ensureAdminAccess(this.loginUser);
	final DBSession session = openSession();
	try {
	    this.adminDBManager.updateClassifierSettings(key, value, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getClassifiedUserCount(org.bibsonomy
     * .common.enums.Classifier, org.bibsonomy.common.enums.SpamStatus, int)
     */
    @Override
    public int getClassifiedUserCount(final Classifier classifier, final SpamStatus status, final int interval) {
	this.permissionDBManager.ensureAdminAccess(this.loginUser);
	final DBSession session = openSession();
	try {
	    return this.adminDBManager.getClassifiedUserCount(classifier, status, interval, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getClassifierHistory(java.lang
     * .String)
     */
    @Override
    public List<User> getClassifierHistory(final String userName) {
	this.permissionDBManager.ensureAdminAccess(this.loginUser);
	final DBSession session = openSession();
	try {
	    return this.adminDBManager.getClassifierHistory(userName, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getClassifierComparison(int)
     */
    @Override
    public List<User> getClassifierComparison(final int interval, final int limit) {
    	this.permissionDBManager.ensureAdminAccess(this.loginUser);
    	final DBSession session = openSession();
    	try {
    		return this.adminDBManager.getClassifierComparison(interval, limit, session);
    	} finally {
    		session.close();
    	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getOpenIDUser(java.lang.String)
     */
    @Override
    public String getOpenIDUser(final String openID) {
	final DBSession session = openSession();
	try {
	    final String username = this.userDBManager.getOpenIDUser(openID, session);
	    return username;
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.bibsonomy.model.logic.LogicInterface#getTagStatistics(java.lang.Class
     * , org.bibsonomy.common.enums.GroupingEntity, java.lang.String,
     * java.lang.String, java.util.List,
     * org.bibsonomy.common.enums.ConceptStatus, int, int)
     */
    @Override
    public int getTagStatistics(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final String regex, final List<String> tags, final ConceptStatus status, final int start, final int end) {
	Integer result;

	final DBSession session = openSession();
	try {
	    final StatisticsParam param = LogicInterfaceHelper.buildParam(StatisticsParam.class, grouping, groupingName, tags, null, null, start, end, null, null, this.loginUser);

	    result = this.statisticsDBManager.getTagStatistics(param, session);
	} finally {
	    session.close();
	}

	return result;
    }

    /*
     * We create a UserRelation of the form (sourceUser, targetUser)\in relation
     * This Method only works for the FOLLOWER_OF and the OF_FRIEND relation
     * Other relation will result in an UnsupportedRelationException
     * 
     * TODO: the "tag" parameter is currently ignored by this function. As soon
     * as tagged relationships are needed, please implement the handling of 
     * the "tag" parameter from here on (mainly in the UserDBManager)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#insertUserRelationship()
     */
    @Override
    public void createUserRelationship(final String sourceUser, final String targetUser, final UserRelation relation, final String tag) {
	this.ensureLoggedIn();
	/*
	 * relationships can only be created by the logged-in user or admins
	 */
	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, sourceUser);
	/*
	 * check if relationship may be created (e.g. some special users
	 * like 'dblp' are disallowed)
	 */
	final DBSession session = openSession();
	this.permissionDBManager.checkUserRelationship(loginUser, this.userDBManager.getUserDetails(targetUser, session), relation, tag);
	/*
	 * finally try to create relationship
	 */
	try {
	    this.userDBManager.createUserRelation(sourceUser, targetUser, relation, tag, session);
	} finally {
	    session.close();
	}
    }

    /*
     * 
     * TODO: the "tag" parameter is currently ignored by this function. As soon
     * as tagged relationships are needed, please implement the handling of 
     * the "tag" parameter from here on (mainly in the UserDBManager)
     * 
     * (non-Javadoc)
     * @see org.bibsonomy.model.logic.LogicInterface#getUserRelationship(java.lang.String, org.bibsonomy.common.enums.UserRelation)
     */
    @Override
    public List<User> getUserRelationship(final String sourceUser, final UserRelation relation, final String tag) {
	this.ensureLoggedIn();
	// ask Robert about this method
	// this.permissionDBManager.checkUserRelationship(sourceUser, targetUser, relation);
	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, sourceUser);

	final DBSession session = openSession();
	try {
	    // get all users that are in relation with sourceUser
	    return this.userDBManager.getUserRelation(sourceUser, relation, tag, session);
	} finally {
	    // unsupported Relations will cause an UnsupportedRelationException
	    session.close();
	}
    }

    /*
     * We delete a UserRelation of the form (sourceUser, targetUser)\in relation
     * This Method only works for the FOLLOWER_OF and the OF_FRIEND relation
     * Other relation will result in an UnsupportedRelationException FIXME: use
     * Strings (usernames) instead of users
     * 
     * TODO: the "tag" parameter is currently ignored by this function. As soon
     * as tagged relationships are needed, please implement the handling of 
     * the "tag" parameter from here on (mainly in the UserDBManager)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#deleteUserRelationship()
     */
    @Override
    public void deleteUserRelationship(final String sourceUser, final String targetUser, final UserRelation relation, final String tag) {
	this.ensureLoggedIn();
	// ask Robert about this method
	// this.permissionDBManager.checkUserRelationship(sourceUser, targetUser, relation);
	this.permissionDBManager.ensureIsAdminOrSelf(loginUser, sourceUser);

	final DBSession session = openSession();
	try {
	    this.userDBManager.deleteUserRelation(sourceUser, targetUser, relation, tag, session);
	} finally {
	    // unsupported Relations will cause an UnsupportedRelationException
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#createBasketItems()
     */
    @Override
    public int createBasketItems(final List<Post<? extends Resource>> posts) {
	this.ensureLoggedIn();

	final DBSession session = openSession();
	try {
	    for (final Post<? extends Resource> post : posts) {
		if (post.getResource() instanceof Bookmark) {
		    throw new UnsupportedResourceTypeException("Bookmarks can't be stored in the basket");
		}
		/*
		 * get the complete post from the database
		 */
		final String intraHash = post.getResource().getIntraHash();
		final String postUserName = post.getUser().getName();
		final Post<BibTex> copy = this.publicationDBManager.getPostDetails(this.loginUser.getName(), intraHash, postUserName, UserUtils.getListOfGroupIDs(this.loginUser), session);

		/*
		 * post might be null, because a) it does not exist b) user may
		 * not access it
		 */
		if (copy == null) {
		    /*
		     * TODO: exception handling?!
		     */
		    throw new ValidationException("Post with hash " + intraHash + " of user " + postUserName + " not found!");
		}

		/*
		 * insert the post from the user's basket
		 */
		this.basketDBManager.createItem(this.loginUser.getName(), copy.getContentId(), session);
	    }

	    // get actual basket size
	    return this.basketDBManager.getNumBasketEntries(this.loginUser.getName(), session);
	} catch (final Exception ex) {
	    log.error(ex);
	} finally {
	    session.close();
	}

	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#deleteBasketItems()
     */
    @Override
    public int deleteBasketItems(final List<Post<? extends Resource>> posts, final boolean clearBasket) {
	this.ensureLoggedIn();

	final DBSession session = openSession();

	try {
	    // decide which delete function will be called
	    if (clearBasket) {
		// clear all in basket
		this.basketDBManager.deleteAllItems(this.loginUser.getName(), session);
	    } else {
		// delete specific post
		for (final Post<? extends Resource> post : posts) {
		    if (post.getResource() instanceof Bookmark) {
			throw new UnsupportedResourceTypeException("Bookmarks can't be stored in the basket");
		    }
		    /*
		     * get the complete post from the database
		     */
		    final Post<BibTex> copy = this.publicationDBManager.getPostDetails(this.loginUser.getName(), post.getResource().getIntraHash(), post.getUser().getName(), UserUtils.getListOfGroupIDs(this.loginUser), session);

		    /*
		     * post might be null, because a) it does not exist b) user
		     * may not access it
		     */
		    if (copy == null) {
			/*
			 * FIXME: proper exception message!
			 */
			throw new ValidationException("You are not authorized to perform the requested operation");
		    }

		    /*
		     * delete the post from the user's basket
		     */
		    this.basketDBManager.deleteItem(this.loginUser.getName(), copy.getContentId(), session);
		}
	    }

	    // get actual basketsize
	    return this.basketDBManager.getNumBasketEntries(this.loginUser.getName(), session);
	} catch (final Exception ex) {
	    log.error(ex);
	} finally {
	    session.close();
	}

	return 0;
    }

    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.logic.LogicInterface#deleteInboxMessages(java.util.List, boolean)
     */
    @Override
    public int deleteInboxMessages(final List<Post<? extends Resource>> posts, final boolean clearInbox) {
	/*
	 * check permissions
	 */
	this.ensureLoggedIn();
	/*
	 * delete one message from the inbox
	 */
	final DBSession session = openSession();
	try {
	    if (clearInbox) {
		this.inboxDBManager.deleteAllInboxMessages(loginUser.getName(), session);
	    } else {
		for (final Post<? extends Resource> post : posts) {
		    final String sender = post.getUser().getName();
		    final String receiver = loginUser.getName();
		    final String resourceHash = post.getResource().getIntraHash();
		    if (!present(receiver) || !present(resourceHash)) {
			/*
			 * FIXME: proper exception message!
			 */
			throw new ValidationException("You are not authorized to perform the requested operation");
		    }
		    this.inboxDBManager.deleteInboxMessage(sender, receiver, resourceHash, session);
		}
	    }
	    return this.inboxDBManager.getNumInboxMessages(loginUser.getName(), session);
	} finally {
	    session.close();
	}
    }

    /*
     * FIXME: implement this method as chain element of getUsers()
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#getUsernameByLdapUserId()
     */
    @Override	
    public String getUsernameByLdapUserId(final String userId) {
	final DBSession session = openSession();
	try {
	    return this.userDBManager.getUsernameByLdapUser(userId, session);
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.logic.GoldStandardPostLogicInterface#createReferences(java.lang.String, java.util.Set)
     */
    @Override
    public void createReferences(final String postHash, final Set<String> references) {
	this.permissionDBManager.ensureAdminAccess(loginUser); // only admins can create references

	final DBSession session = this.openSession();
	try {
	    this.goldStandardPublicationDBManager.addReferencesToPost(this.loginUser.getName(), postHash, references, session);
	} finally {
	    session.close();
	}	
    }

    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.logic.GoldStandardPostLogicInterface#deleteReferences(java.lang.String, java.util.Set)
     */
    @Override
    public void deleteReferences(final String postHash, final Set<String> references) {
	this.permissionDBManager.ensureAdminAccess(loginUser); // only admins can delete references

	final DBSession session = this.openSession();
	try {
	    this.goldStandardPublicationDBManager.removeReferencesFromPost(this.loginUser.getName(), postHash, references, session);
	} finally {
	    session.close();
	}	
    }

    @Override
    public void createWiki(final String userName, final Wiki wiki) {
	this.permissionDBManager.ensureIsAdminOrSelf(this.loginUser, userName);

	final DBSession session = openSession();
	try {
	    this.wikiDBManager.createWiki(userName, wiki, session);
	} finally {
	    session.close();
	}
    }

    @Override
    public void deleteWiki(final String userName) {
	throw new UnsupportedOperationException();
    }

    /**
     * @param date - if <code>null</code>, the latest version of the wiki is 
     * returned. Otherwise, the latest version before <code>date</code>.
     * 
     * @see org.bibsonomy.model.logic.LogicInterface#getWiki(java.lang.String, java.util.Date)
     */
    @Override
    public Wiki getWiki(final String userName, final Date date) {
	final DBSession session = openSession();
	
	try {
		final User requUser = this.getUserDetails(userName);
		/*
		 * We return an empty wiki for users who are not allowed to access
		 * this wiki.
		 */
		if (!this.permissionDBManager.isAllowedToAccessUsersProfile(requUser, this.loginUser, session)) {
		    return new Wiki();
		}
		
	    if (date == null) {
		return this.wikiDBManager.getActualWiki(userName, session);
	    }

	    return this.wikiDBManager.getPreviousWiki(userName, date, session);
	} finally {
	    session.close();
	}
    }

    @Override
    public List<Date> getWikiVersions(final String userName) {
	this.permissionDBManager.ensureIsAdminOrSelf(this.loginUser, userName);

	final DBSession session = openSession();
	try {
	    return this.wikiDBManager.getWikiVersions(userName, session);
	} finally {
	    session.close();
	}
    }

    @Override
    public void updateWiki(final String userName, final Wiki wiki) {
	this.permissionDBManager.ensureIsAdminOrSelf(this.loginUser, userName);

	final DBSession session = openSession();

	try {
	    final Wiki actual = this.wikiDBManager.getActualWiki(userName, session);
	    /*
	     * Check, if the wiki has changed (otherwise we don't update it).
	     */
	    final String actualWikiText = actual.getWikiText();
	    if (present(actualWikiText) && !actualWikiText.equals(wiki.getWikiText())) {
		this.wikiDBManager.updateWiki(userName, wiki, session);
		this.wikiDBManager.logWiki(userName, actual, session);
	    }
	} finally {
	    session.close();
	}
    }

    @Override
    public void createExtendedField(final Class<? extends Resource> resourceType, final String userName, final String intraHash, final String key, final String value) {
	final DBSession session = openSession();

	try {
	    if (BibTex.class == resourceType) {
		this.publicationDBManager.createExtendedField(userName, intraHash, key, value, session);
	    } else {
		throw new UnsupportedResourceTypeException("The requested resourcetype (" + resourceType.getClass().getName() + ") is not supported.");
	    }
	} finally {
	    session.close();
	}
    }

    @Override
    public void deleteExtendedField(final Class<? extends Resource> resourceType, final String userName, final String intraHash, final String key, final String value) {
	final DBSession session = this.openSession();

	try {
	    if (BibTex.class == resourceType) {
		if(!present(key)) {
		    this.publicationDBManager.deleteAllExtendedFieldsData(userName, intraHash, session);
		} else {
		    if(!present(value)) {
			this.publicationDBManager.deleteExtendedFieldsByKey(userName, intraHash, key, session);
		    } else { 
			this.publicationDBManager.deleteExtendedFieldByKeyValue(userName, intraHash, key, value, session);
		    }
		}
	    } else {
		throw new UnsupportedResourceTypeException("The requested resourcetype (" + resourceType.getClass().getName() + ") is not supported.");
	    }
	} finally {
	    session.close();
	}
    }

    @Override
    public Map<String, List<String>> getExtendedFields(final Class<? extends Resource> resourceType, final String userName, final String intraHash, final String key) {
	final DBSession session = this.openSession();

	try {
	    if (BibTex.class == resourceType) {
		return this.publicationDBManager.getExtendedFields(userName, intraHash, key, session);
	    }

		throw new UnsupportedResourceTypeException("The requested resourcetype (" + resourceType.getClass().getName() + ") is not supported.");
	} finally {
	    session.close();
	}
    }

    /*
     * (non-Javadoc)
     * @see org.bibsonomy.model.logic.ReviewLogicInterface#getReviews(java.lang.String)
     */
	@Override
	public List<DiscussionItem> getDiscussionSpace(final String interHash) {
		final DBSession session = this.openSession();
		try {
			return this.discussionDatabaseManager.getDiscussionSpace(this.loginUser, interHash, session);
		} finally {
			session.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.logic.DiscussionLogicInterface#createDiscussionItem(java.lang.String, java.lang.String, org.bibsonomy.model.DiscussionItem)
	 */
	@Override
	public void createDiscussionItem(final String interHash, final String username, final DiscussionItem discussionItem) {
		this.permissionDBManager.ensureIsAdminOrSelf(this.loginUser, username);
		
		final DBSession session = this.openSession();
		try {
			/*
			 * first check if gold standard post exists
			 */
			@SuppressWarnings("unchecked") // should be a gold standard publication
			final Post<GoldStandardPublication> goldStandardPostinDB = (Post<GoldStandardPublication>) this.getPostDetails(interHash, "");
			/*
			 * if not create one
			 * TODO: add a test
			 */
			if (!present(goldStandardPostinDB)) {
				log.debug("no gold standard publication found for interHash " + interHash + ". Creating new gold standard publication");
				final String hash = HashID.INTER_HASH.getId() + interHash;
				// TODO: bookmarks are missing
				// final List<Post<Bookmark>> bookmarkPost = this.getPosts(Bookmark.class, GroupingEntity.USER, null, Collections.<String>emptyList(), hash, null, null, 0, 1, null);
				// FIXME: this list maybe also contains private posts of the logged in user!
				final List<Post<BibTex>> publicationPosts = this.getPosts(BibTex.class, GroupingEntity.ALL, null, Collections.<String>emptyList(), hash, null, null, 0, 1, null);
				if (present(publicationPosts)) {
					final Post<GoldStandardPublication> goldStandardPost = new Post<GoldStandardPublication>();
					final GoldStandardPublication goldStandardPublication = new GoldStandardPublication();
					ObjectUtils.copyPropertyValues(publicationPosts.get(0).getResource(), goldStandardPublication);
					
					/*
					 * clear some private stuff
					 */
					goldStandardPublication.setPrivnote("");
					
					goldStandardPost.setResource(goldStandardPublication);
					
					this.createPosts(Collections.<Post<?>>singletonList(goldStandardPost));
				} else {
					// TODO: log?
				}
			}
			
			/*
			 * create the discussion item
			 */
			final User commentUser = this.userDBManager.getUserDetails(username, session);
			discussionItem.setUser(commentUser);
			
			this.createDiscussionItem(interHash, discussionItem, session);
		} finally {
			session.close();
		}
	}
	
	private void prepareComment(final User commentUser, final Set<Group> groups, final DBSession session) {
		this.validateGroups(commentUser, groups, session);
		
		// transfer to spammer group id's if neccessary
		GroupUtils.prepareGroups(groups, commentUser.isSpammer());
	}
	
	private <D extends DiscussionItem> void createDiscussionItem(final String interHash, final D discussionItem, final DBSession session) {
		this.prepareComment(discussionItem.getUser(), discussionItem.getGroups(), session);
		this.getCommentDatabaseManager(discussionItem).createDiscussionItemForResource(interHash, discussionItem, session);
	}

	@SuppressWarnings("unchecked")
	private <D extends DiscussionItem> DiscussionItemDatabaseManager<D> getCommentDatabaseManager(final DiscussionItem discussionItem) {
		return (DiscussionItemDatabaseManager<D>) this.allDiscussionManagers.get(discussionItem.getClass());
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.logic.DiscussionLogicInterface#updateDiscussionItem(java.lang.String, java.lang.String, org.bibsonomy.model.DiscussionItem)
	 */
	@Override
	public void updateDiscussionItem(final String username, final String interHash, final DiscussionItem discussionItem) {
		this.permissionDBManager.ensureIsAdminOrSelf(this.loginUser, username);
		
		final DBSession session = this.openSession();
		try {
			final User commentUser = this.userDBManager.getUserDetails(username, session);
			discussionItem.setUser(commentUser);
			
			this.updateCommentForUser(interHash, discussionItem, session);
		} finally {
			session.close();
		}
	}

	private <D extends DiscussionItem> void updateCommentForUser(final String interHash, final D discussionItem, final DBSession session) {
		this.prepareComment(discussionItem.getUser(), discussionItem.getGroups(), session);
		this.getCommentDatabaseManager(discussionItem).updateDiscussionItemForResource(interHash, discussionItem.getHash(), discussionItem, session);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.model.logic.DiscussionLogicInterface#deleteDiscussionItem(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteDiscussionItem(final String username, final String interHash, final String commentHash) {
		this.permissionDBManager.ensureIsAdminOrSelf(this.loginUser, username);
		
		final DBSession session = this.openSession();
		try {
			final User user = this.userDBManager.getUserDetails(username, session);
			
			for (final DiscussionItemDatabaseManager<? extends DiscussionItem> discussionItemManager : this.allDiscussionManagers.values()) {
				if (discussionItemManager.deleteDiscussionItemForResource(interHash, user, commentHash, session)) {
					return;
				}
			}
		} finally {
			session.close();
		}
	}
}