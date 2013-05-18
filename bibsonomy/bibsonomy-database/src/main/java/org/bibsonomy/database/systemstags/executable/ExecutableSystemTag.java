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

import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.systemstags.SystemTag;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * @author sdo
 * @version $Id: ExecutableSystemTag.java,v 1.2 2010-07-02 11:59:40 nosebrain Exp $
 */
public interface ExecutableSystemTag extends SystemTag {

	/**
	 * Action to perform before the creation of a post
	 * 
	 * @param <T> = Resource Type of the post
	 * @param post = a VALID post for which action should be performed
	 * @param session 
	 */
	public <T extends Resource> void performBeforeCreate(Post<T> post, final DBSession session);

	/**
	 * Action to perform before the update of a post
	 * 
	 * @param <T> Resource Type of the post
	 * @param newPost = updated post
	 * @param oldPost = post to be updated If operation is not UPDATE_TAGS the post MUST be VALID
	 * @param operation = type of UpdateOperation
	 * @param session 
	 */
	public <T extends Resource> void performBeforeUpdate(Post<T> newPost, final Post<T> oldPost, final PostUpdateOperation operation, final DBSession session);

	/**
	 * Action to perform after the creation of a post
	 * 
	 * @param <T> = Resource Type of the post
	 * @param post = a VALID post for which action should be performed
	 * @param session 
	 */
	public <T extends Resource> void performAfterCreate(Post<T> post, final DBSession session);
	
	/**
	 * Action to perform after the update of a post
	 * 
	 * @param <T> Resource Type of the post
	 * @param newPost = updated post
	 * @param oldPost = post to be updated If operation is not UPDATE_TAGS the post MUST be VALID
	 * @param operation = type of UpdateOperation
	 * @param session 
	 */
	public <T extends Resource> void performAfterUpdate(Post<T> newPost, final Post<T> oldPost, final PostUpdateOperation operation, final DBSession session);

	/**
	 * Creates a new instance of this kind of ExecutableSystemTag
	 * @return the new instance
	 */
	public ExecutableSystemTag newInstance();


}
