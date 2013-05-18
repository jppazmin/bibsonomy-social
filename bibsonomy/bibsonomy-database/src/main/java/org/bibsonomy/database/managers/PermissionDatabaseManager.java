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

package org.bibsonomy.database.managers;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collection;
import java.util.List;

import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.ProfilePrivlevel;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.UserUtils;


/**
 * Database Manager for permissions
 * 
 * @author Dominik Benz
 * @version $Id: PermissionDatabaseManager.java,v 1.49 2011-07-21 18:05:56 doerfel Exp $
 */
public class PermissionDatabaseManager extends AbstractDatabaseManager {
    private static final int MAX_TAG_SIZE = 10;
    private static final int END_MAX = 1000;

    private final static PermissionDatabaseManager singleton = new PermissionDatabaseManager();

    /**
     * @return PermissionDatabaseManager
     */
    public static PermissionDatabaseManager getInstance() {
	return singleton;
    }


    private final GroupDatabaseManager groupDb;
    private final GeneralDatabaseManager generalDb;

    private PermissionDatabaseManager() {
	this.groupDb = GroupDatabaseManager.getInstance();
	this.generalDb = GeneralDatabaseManager.getInstance();
    }

    /**
     * Checks whether the requested start- / end-values are OK
     * 
     * @param loginUser	
     * @param start TODO: unused
     * @param end
     * @param itemType
     */
    public void checkStartEnd(final User loginUser, final int start, final int end, final String itemType) {
	if (!isAdmin(loginUser) && end > END_MAX) {
	    throw new AccessDeniedException("You are not authorized to retrieve more than the last " + END_MAX + " " + itemType + " items.");
	}
    }

    /**
     * Check if the logged in user has write access to the given post.
     * 
     * @param post
     * @param loginUser
     */
    public void ensureWriteAccess(final Post<? extends Resource> post, final User loginUser) {
	// delegate write access check
	this.ensureIsAdminOrSelf(loginUser, post.getUser().getName());
    }

    /**
     * Throws an exception if the loginUser.getName and userName doesn't match.
     * 
     * @param loginUser
     * @param userName
     */
    public void ensureWriteAccess(final User loginUser, final String userName) {
	if (loginUser.getName() == null || !loginUser.getName().toLowerCase().equals(userName.toLowerCase())) {
	    throw new AccessDeniedException();
	}
    }

    /**
     * This method checks, whether the user is allowed to access the posts
     * documents. The user is allowed to access the documents,
     * 
     * 
     * <ul>
     * <li>if userName = post.userName</li> 
     * <li>if the post is public and the posts user is together with the user
     * in a group, which allows to share documents, or
     * <li>if the post is viewable for a specific group, in which both users
     * are and which allows to share documents.
     * </ul>
     * 
     * TODO: eventually, we don't want to have the post as parameter, but only
     * its groups?
     * 
     * @param userName -
     *            the name of the user which wants to access the posts
     *            documents.
     * @param post -
     *            the post which contains the documents the user wants to
     *            access.
     * @param session -
     *            a DBSession.
     * @return <code>true</code> if the user is allowed to access the
     *         documents of the post.
     */
    public boolean isAllowedToAccessPostsDocuments(final String userName, final Post<? extends Resource> post, final DBSession session) {
	final String postUserName = post.getUser().getName();
	/*
	 * if userName = postUserName, return true
	 */
	if ((userName != null && userName.equalsIgnoreCase(postUserName))) return true;
	/*
	 * else: check groups stuff ....
	 */
	final Collection<Group> postGroups = post.getGroups();

	/*
	 * Get the groups in which both users are.
	 */
	final List<Group> commonGroups = this.groupDb.getCommonGroups(userName, postUserName, session);

	/*
	 * Construct the public group.
	 */
	final Group publicGroup = GroupUtils.getPublicGroup();

	/*
	 * Find a common group of both users, which allows to share documents.
	 */
	for (final Group group : commonGroups) {
	    if (group.isSharedDocuments()) {
		// both users are in a group which allows to share documents
		if (postGroups.contains(publicGroup) || postGroups.contains(group)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * This method checks whether the logged-in user is allowed to see documents of 
     * the requested user or a requested group. The user is allowed to access the documents,
     *
     * <ul>
     * <li>if the logged-in user requests his own posts, i.e. loginUser = requestedUser
     * <li>if the logged-in user is a member of the requested group AND the group allows shared documents.
     * </ul>
     * 
     * @param loginUser - 
     * 				the name of the logged-in user
     * @param grouping -
     * 				the requested grouping (GROUP or USER) 
     * @param groupingName -
     * 				the name of the requested user / group
     * @param filter -
     *              the requested filter entity
     * @param session -
     *           	DB session
     * @return <code>true</code> if the logged-in user is allowed to access the
     *         documents of the requested user / group.
     */
    public boolean isAllowedToAccessUsersOrGroupDocuments(final User loginUser, final GroupingEntity grouping, final String groupingName, final FilterEntity filter, final DBSession session) {
	boolean isAllowed = false;
	if (grouping != null) {
	    // user
	    if (grouping.equals(GroupingEntity.USER)) {
		if (loginUser.getName() != null) {
		    isAllowed = loginUser.getName().equals(groupingName);
		    if (!isAllowed && FilterEntity.JUST_PDF.equals(filter)) {
			throw new AccessDeniedException("error.pdf_only_not_authorized_for_user");
		    }
		}
	    }
	    // group
	    if (grouping.equals(GroupingEntity.GROUP)) {
		final Group group = this.groupDb.getGroupByName(groupingName, session);
		/*
		 * check group membership and if the group allows shared documents
		 */
		isAllowed = group != null && UserUtils.getListOfGroupIDs(loginUser).contains(group.getGroupId()) && group.isSharedDocuments();
		if (!isAllowed && FilterEntity.JUST_PDF.equals(filter)) {
		    throw new AccessDeniedException("error.pdf_only_not_authorized_for_group");
		}
	    }
	}
	return isAllowed;
    }

    /**
     * checks if the loginUser is allowed to access the profile of user
     * 
     * @param user
     * @param loginUser
     * @param session
     * @return <code>true</code> iff loginUser is allowed to access users profiles
     */
    public boolean isAllowedToAccessUsersProfile(final User user, final User loginUser, final DBSession session) {
	if (!present(user)) {
	    return false;
	}

	/*
	 * check if user is self or admin
	 */
	if (this.isAdminOrSelf(loginUser, user.getName())) {
	    return true;
	}

	/*
	 * get privacy level of user from database and respect it
	 */
	ProfilePrivlevel privacyLevel = ProfilePrivlevel.PRIVATE; // private is default setting

	/*
	 * if the settings weren't loaded yet, load the profile privacy setting now
	 */
	if (!present(user.getSettings()) || !present(user.getSettings().getProfilePrivlevel())) {
	    final ProfilePrivlevel result = this.queryForObject("getProfilePrivlevel", user, ProfilePrivlevel.class, session);

	    if (present(result)) {
		privacyLevel = result;
	    }
	} else {
	    privacyLevel = user.getSettings().getProfilePrivlevel();
	}

	switch (privacyLevel) {
	case PUBLIC:
	    return true;
	case PRIVATE:
	    return false;
	case FRIENDS:
	    return this.generalDb.isFriendOf(loginUser.getName(), user.getName(), session);
	}		

	return false;
    }

    /**
     * Ensures that the user is member of given group.
     * 
     * @param userName 
     * @param groupName 
     * @param session 
     */
    public void ensureMemberOfNonSpecialGroup(final String userName, final String groupName, final DBSession session) {
	if( GroupID.isSpecialGroup(groupName))
	    throw new ValidationException("Special groups not allowed for this system tag.");
	final Integer groupID = this.groupDb.getGroupIdByGroupNameAndUserName(groupName, userName, session);
	if( groupID==GroupID.INVALID.getId() )
	    throw new AccessDeniedException();
    }


    /**
     * @param groupName
     * @return if a group is a special group
     */
    public boolean isSpecialGroup (final String groupName) {
	return GroupID.isSpecialGroup(groupName);
    }


    /**
     * @param userName
     * @param groupName
     * @param session
     * @return if the given user is a member of the specified group
     */
    public boolean isMemberOfGroup(final String userName, final String groupName, final DBSession session) {
	final Integer groupID = this.groupDb.getGroupIdByGroupNameAndUserName(groupName, userName, session);
	if( groupID==GroupID.INVALID.getId() ) {
	    return false;
	}
	return true;
    }

    /**
     * Ensures that the user is an admin.
     * 
     * @param loginUser
     */
    public void ensureAdminAccess(final User loginUser) {
	if (!present(loginUser.getName()) || !isAdmin(loginUser)) {
	    throw new AccessDeniedException();
	}
    }

    /**
     * Check maximum number of allowed tags per request
     * 
     * @param tags
     * @return true if maximum size is exceeded, false otherwise
     */
    public boolean exceedsMaxmimumSize(final List<String> tags) {
	return tags != null && tags.size() >= MAX_TAG_SIZE;
    }

    /**
     * Check permissions to decide if filter can be set
     * 
     * @param loginUser
     * 		- the user whose permissions need to be checked
     * @param filter 
     * 	    - the filter under question
     * @return <code>true</code> if the logged-in user is allowed to set the specific 
     * filter
     */
    public boolean checkFilterPermissions(final FilterEntity filter, final User loginUser){
	if (filter == null) return false;

	
	if (FilterEntity.ADMIN_SPAM_POSTS.equals(filter) && isAdmin(loginUser)) {
		return true;
	}
	return false; 
    }

    /**
     * Checks, if the given login user is either an admin, or the user requested
     * by user name.
     * 
     * @param loginUser - the logged in user.
     * @param userName - the name of the requested user.
     * @return <code>true</code> if loginUser is an admin or userName.
     */
    public boolean isAdminOrSelf(final User loginUser, final String userName) {
	return (
		(present(loginUser.getName()) && loginUser.getName().equals(userName)) // loginUser = userName  
		||
		isAdmin(loginUser)                                // loginUser is admin
	);
    }

    /**
     * Checks if the given user is an admin.
     * 
     * @param loginUser
     * @return <code>true</code> iff user is admin
     */
    public boolean isAdmin(final User loginUser) {
	return Role.ADMIN.equals(loginUser.getRole());
    }

    /**
     * if {@link #isAdminOrSelf(User, String)} returns false this method throws a validation exception
     * @param loginUser
     * @param userName
     */
    public void ensureIsAdminOrSelf(final User loginUser, final String userName) {
	if (!this.isAdminOrSelf(loginUser, userName)) {
	    throw new AccessDeniedException();
	}
    }

    /**
     * FIXME: WENN DIE RICHTIGEN GRUPPENADMINS EXISTIEREN MUSS DIESE FUNKTION GEÄNDERT WERDEN 
     * 
     * @param loginUser
     * @param group
     * @return loginUser equals group.getName
     */
    public boolean userIsGroupAdmin(final User loginUser, final Group group){
	/*
	 * user name == group name
	 */
	return loginUser.getName().equals(group.getName());
    }

    /**
     * FIXME: Why do we need loginUser and relation?
     * 
     * Checks if a user relationship between the logged-in user 
     * and a requested user may be created.
     * 
     * @param loginUser - the logged-in user
     * @param relation - the relation to be created
     * @param tag TODO
     * @param targetUser - the target user
     * @return true if everyhing is OK and the relationship may be created
     * 
     * 
     */
    public boolean checkUserRelationship(final User loginUser, final User targetUser, final UserRelation relation, final String tag) {
	/*
	 * when we add an internal relation, the target user must exist
	 * (and some special users like 'dblp' are not allowed)  
	 */
	if (relation.isInternal() ) {
	    if (!present(targetUser.getName())) {
		throw new ValidationException("Relationship with non-existing user cannot be established.");
	    }
	    if ("dblp".equalsIgnoreCase(targetUser.getName())) {
		throw new ValidationException("error.relationship_with_dblp");
	    }
	}
	return true;
    }    
    
}