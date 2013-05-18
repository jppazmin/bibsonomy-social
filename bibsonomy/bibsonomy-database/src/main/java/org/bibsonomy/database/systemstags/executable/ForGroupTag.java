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

package org.bibsonomy.database.systemstags.executable;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.database.DBLogicNoAuthInterfaceFactory;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.database.managers.PermissionDatabaseManager;
import org.bibsonomy.database.systemstags.AbstractSystemTagImpl;
import org.bibsonomy.database.systemstags.SystemTagsExtractor;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.util.GroupUtils;

/**
 * System tag 'sys:for:&lt;groupname&gt;'
 * Description: 
 *   If user tags a post with [sys:]for:&lt;groupname&gt;, a copy of the resource
 *   is created which is owned by the group. Furthermore, the copied resource is tagged 
 *   with from:&lt;username&gt; instead of for:&lt;groupname&gt;.
 *   
 *  Precondition: 
 *   User is member of given group 
 * @author fei
 * @version $Id: ForGroupTag.java,v 1.34 2011-07-21 18:08:04 doerfel Exp $
 */
public class ForGroupTag extends AbstractSystemTagImpl implements ExecutableSystemTag {

	private static final String NAME = "for";
	private static boolean toHide = true;

	private DBSessionFactory dbSessionFactory = null;

	@Override
	public ForGroupTag newInstance() {
		return new ForGroupTag();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isToHide() {
		return toHide;
	}

	/**
	 * @param dbSessionFactory the dbSessionFactory to set
	 */
	public void setDBSessionFactory(final DBSessionFactory dbSessionFactory) {
		this.dbSessionFactory = dbSessionFactory;
	}

	@Override
	public <T extends Resource> void performBeforeCreate(final Post<T> post, final DBSession session) {
		log.debug("performing after access");
		// we assume, that the post itself is valid
		this.perform(post, post.getTags(), session);
	}

	@Override
	public <T extends Resource> void performAfterCreate(final Post<T> post, final DBSession session) {
		// nothing is performed after post is created
		log.debug("performing after access");
	}

	@Override
	public <T extends Resource> void performBeforeUpdate(final Post<T> newPost, final Post<T> oldPost, final PostUpdateOperation operation, final DBSession session) {
		if (operation == PostUpdateOperation.UPDATE_TAGS) {
			/*
			 *  in this case, newPost is not a valid post but contains the new tags, while oldPost is a valid post containing the old tags
			 */
			this.perform(oldPost, newPost.getTags(), session);
		} else {
			/*
			 *  in this case, newPost is a valid post containing the new tags
			 */
			this.perform(newPost, newPost.getTags(), session);
		}
	}

	@Override
	public <T extends Resource> void performAfterUpdate(final Post<T> oldPost, final Post<T> newPost, final PostUpdateOperation operation, final DBSession session) {
		// nothing is performed after post was updated
		log.debug("performing after access");
	}

	/**
	 * Make post for the group and store it in the database
	 * @param <T>
	 * @param userPost the post to store (we ignore its tags)
	 * @param userTags the tags for the post
	 * @param session
	 */
	private <T extends Resource> void perform(final Post<T> userPost, final Set<Tag> userTags, final DBSession session) {
		log.debug("performing after access");
		final String groupName = this.getArgument(); // the group's name
		final String userName = userPost.getUser().getName();
		final String intraHash = userPost.getResource().getIntraHash();

		if (!this.hasPermissions(groupName, userName, session)) {
			/*
			 *  user is not allowed to use this tag
			 */
			return;
		}
		/*
		 * Make a DBLogic for the group
		 */
		final DBLogicNoAuthInterfaceFactory logicFactory = new DBLogicNoAuthInterfaceFactory();
		logicFactory.setDbSessionFactory(this.dbSessionFactory);
		final LogicInterface groupDBLogic = logicFactory.getLogicAccess(groupName, "");
		/*
		 *  Check if the group exists and whether it owns the post already
		 */
		if (!present(groupDBLogic.getGroupDetails(groupName))) {
			/*
			 *  We decided to ignore errors in systemTags. Thus the user is free use any tag.
			 *  The drawback: If it is the user's intention to use a systemTag, he will never know if there was a typo! 
			 */
			return; // this tag can not be used => abort
		}
		try {
			if (present( groupDBLogic.getPostDetails(intraHash, groupName) )) {
				log.debug("Given post already owned by group. Skipping...");
				return;
			}
		} catch (final Exception ex) {
			// ignore
		}
		/*
		 *  Permissions are granted and the group doesn't own the post yet
		 *  => Copy the post and store it for the group
		 *  FIXME: How do we properly clone a post?
		 */
		final Post<T> groupPost = new Post<T>();
		groupPost.setResource(userPost.getResource());
		groupPost.setDescription(userPost.getDescription());
		groupPost.setDate(new Date());
		groupPost.setUser(new User(groupName));
		/* 
		 * Copy Tags: 
		 * remove all systemTags to avoid any side effects and contradictions 
		 */
		final Set<Tag> groupTags = new HashSet<Tag>(userTags);
		SystemTagsExtractor.removeAllSystemTags(groupTags);
		/*
		 * adding this tag also guarantees, that the new post will have noch empty tag set (which would be illegal)!
		 */
		groupTags.add(new Tag("from:"+userName));
		groupPost.setTags(groupTags);
		/*
		 *  Copy Groups: the visibility of the postCopy is:
		 *  original == public => copy = public
		 *  original != public => copy = dbGroup
		 *  => check if post.groups has only the public group
		 */
		if ((userPost.getGroups().size() == 1) && (userPost.getGroups().contains(GroupUtils.getPublicGroup()))) {
			// public is the only group (if visibility was public, there should be only one group)
			groupPost.setGroups(new HashSet<Group>());
			groupPost.getGroups().add(GroupUtils.getPublicGroup());
		} else {
			// visibility is different from public => post is only visible for dbGroup
			groupPost.addGroup(groupName);
		}
		/*
		 * groupPost is complete and can be stored for the group
		 */
		try {
			groupDBLogic.createPosts(Collections.<Post<?>>singletonList(groupPost));
		} catch (final DatabaseException dbex) {
			/*
			 *  Add the DatabaseException of the copied post to the Exception of the original one
			 */
			for (final String hash : dbex.getErrorMessages().keySet()) {
				for (final ErrorMessage errorMessage : dbex.getErrorMessages(hash)) {
					errorMessage.setDefaultMessage("This error occured while executing the for: tag: "+errorMessage.getDefaultMessage());
					errorMessage.setErrorCode("database.exception.systemTag.forGroup.copy");
					session.addError(intraHash, errorMessage);
					log.warn("Added SystemTagErrorMessage (for group: errors while storing group's post) for post " + intraHash);
				}
			}
		}
		log.debug("copied post was stored successfully");
	}


	/**
	 * Checks the preconditions to this tags usage, adds errorMessages
	 * @param groupName
	 * @param userName
	 * @param session
	 * @return true iff user is allowed to use the tag
	 */
	private boolean hasPermissions(final String groupName, final String userName, final DBSession session) {
		final PermissionDatabaseManager permissionDb = PermissionDatabaseManager.getInstance();
		if (permissionDb.isSpecialGroup(groupName) ) {
			/*
			 *  We decided to ignore errors in systemTags. Thus the user is free use any tag.
			 *  The drawback: If it is the user's intention to use a systemTag, he will never know if there was a typo! 
			 */
			return false;			
		} 
		if (!permissionDb.isMemberOfGroup(userName, groupName, session)) {
			return false;			
		}
		return true;
	}

	/*
	 * We overwrite this method because we want to interpret also the send tag 
	 * without prefix (sys/system) as systemTag and we need an argument
	 * @see org.bibsonomy.database.systemstags.AbstractSystemTagImpl#isInstance(java.lang.String)
	 */
	@Override
	public boolean isInstance(String tagName) {
		// the send tag must have an argument, the prefix is not required
		return SystemTagsUtil.hasTypeAndArgument(tagName) && NAME.equals(SystemTagsUtil.extractType(tagName));
	}
}

