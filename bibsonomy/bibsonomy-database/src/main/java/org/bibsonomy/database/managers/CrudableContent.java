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

import java.util.List;

import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.GenericParam;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;

/**
 * For every content type there should exist a separate class which implements
 * this interface. It supplies basic CRUD: create, read, update and delete.
 * @param <T> extends Resource
 * @param <P> extends GenericParam
 * 
 * @author Christian Schenk
 * @version $Id: CrudableContent.java,v 1.24 2011-07-14 15:23:58 rja Exp $
 */
public interface CrudableContent<T extends Resource, P extends GenericParam> {
	/**
	 * Read
	 * 
	 * @param param
	 * @param session
	 * @return list of posts
	 */
	public List<Post<T>> getPosts(P param, DBSession session);

	/**
	 * Read
	 * 
	 * @param loginUserName
	 * @param resourceHash
	 * @param userName
	 * @param visibleGroupIDs
	 * @param session
	 * 
	 * @throws ResourceMovedException - when no resource 
	 * with that hash exists for that user, but once a resource 
	 * with that hash existed that has been moved. The new hash 
	 * is returned inside the exception. 
	 * @throws ResourceNotFoundException
	 * 
	 * @return list of posts
	 */
	public Post<T> getPostDetails(String loginUserName, String resourceHash, String userName, List<Integer> visibleGroupIDs, DBSession session) throws ResourceMovedException, ResourceNotFoundException;

	/**
	 * Delete
	 * 
	 * @param userName 
	 * @param resourceHash 
	 * @param session 
	 * 
	 * @return true, if entry existed and was deleted
	 */
	public boolean deletePost(String userName, String resourceHash, DBSession session);

	/**
	 * create
	 * 
	 * @param post
	 * @param session
	 * @return true if entry was created
	 */
	public boolean createPost(Post<T> post, DBSession session);

	/**
	 * update
	 * new version contains synchronization handling 
	 * @param post
	 * @param oldHash
	 * @param operation
	 * @param session
	 * @param loginUser
	 * @return
	 */
	public boolean updatePost(Post<T> post, String oldHash, PostUpdateOperation operation, DBSession session, User loginUser);
}