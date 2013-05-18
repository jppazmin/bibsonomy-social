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

package org.bibsonomy.model.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Date;
import java.util.Set;

import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;

/**
 * Static methods to handle Posts.
 * 
 * @author rja
 * @version $Id: PostUtils.java,v 1.10 2011-05-17 11:30:15 rja Exp $
 */
public class PostUtils {

	/**
	 * sets the owner of the post to the user if current owner is null or name of current owner isn't present
	 * @param post
	 * @param user
	 */
	public static void populatePostWithUser(final Post<? extends Resource> post, final User user) {
		final User postUser = post.getUser();
		if (!present(postUser) || !present(postUser.getName())) {
			post.setUser(user);
		}
 	}
	
	/**
	 * Overwrites the date of the post if the user is not allowed to set it.
	 * If the post does not contain a date, the current date is set.  
	 * 
	 * @param post
	 * @param loginUser
	 */
	public static void populatePostWithDate(final Post<? extends Resource> post, final User loginUser) {
		if (!Role.SYNC.equals(loginUser.getRole()) || !present(post.getDate())) {
	    	post.setDate(new Date());
	    }
	}
	
	/**
	 * Modifies the group IDs in the post to be spam group IDs or non-spam group IDs,
	 * depending on the spammer status of the given user.
	 *  
	 * @see #setGroupIds(Post, boolean)
	 * @param post
	 * @param user
	 * @throws ValidationException - if the user name of the post does not match the given user name.
	 */
	public static void setGroupIds(final Post<? extends Resource> post, final User user) throws ValidationException {
		if (!present(user.getName()) || !user.getName().equals(post.getUser().getName())) {
			throw new ValidationException("user name of post does not match user name of posting user");
		}
		setGroupIds(post, user.isSpammer());
	}
	
	/**
	 * Modifies the group IDs in the post to be spam group IDs or non-spam group IDs,
	 * depending on the given <code>spammer</code> flag.
	 * <br/>
	 * Note: the post must already contain the integer IDs of the groups, otherwise 
	 * flagging does not work! 
	 * 
	 * @param post - the post whose groups should be modified.
	 * @param isSpammer - <code>true</code> if the user of the post is a spammer.
	 */
	public static void setGroupIds(final Post<? extends Resource> post, final boolean isSpammer) {
		final Set<Group> groups = post.getGroups();
		for (final Group group : groups) {
			/*
			 * update the group id of the post
			 */
			group.setGroupId(GroupID.getGroupId(group.getGroupId(), isSpammer));
		}
	}
	
	/**
	 * 
	 * @param resourceType The type of resource that should be returned.
	 * 
	 * @return A new post containing an instance of a resource with the given type.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Post<?> getInstance(final String resourceType) throws InstantiationException, IllegalAccessException {
		final Post<Resource> post = new Post<Resource>();
		post.setResource(ResourceUtils.getInstance(resourceType));
		return post;
	}
	
}
