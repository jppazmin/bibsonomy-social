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

package org.bibsonomy.database.plugin;

import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.BasketParam;
import org.bibsonomy.database.params.UserParam;
import org.bibsonomy.model.DiscussionItem;

/**
 * This interface supplies hooks which can be implemented by plugins. This way
 * the code for basic operations, like updating a bookmark or publication, can
 * be kept concise and is easier to maintain.<br/>
 * 
 * If a method returns <code>null</code> its execution will be skipped.
 * 
 * @author Jens Illig
 * @author Christian Schenk
 * @author Stefan Stützer
 * @author Anton Wilhelm
 * @version $Id: DatabasePlugin.java,v 1.20 2011-06-16 13:55:01 nosebrain Exp $
 */
public interface DatabasePlugin {

	/**
	 * Called when a publication is inserted.
	 * 
	 * @param contentId
	 * @param session
	 * @return runnable
	 */
	public Runnable onPublicationInsert(int contentId, DBSession session);

	/**
	 * Called when a publication is deleted.
	 * 
	 * @param contentId
	 * @param session
	 * @return runnable
	 */
	public Runnable onPublicationDelete(int contentId, DBSession session);

	/**
	 * Called when a publication is updated.
	 * 
	 * @param newContentId
	 * @param contentId
	 * @param session
	 * @return runnable
	 */
	public Runnable onPublicationUpdate(int newContentId, int contentId, DBSession session);

	/**
	 * Called when a gold standard publication is created.
	 * 
	 * @param interhash
	 * @param session
	 * @return runnable
	 */
	public Runnable onGoldStandardPublicationCreate(String interhash, DBSession session);

	/**
	 * Called when a gold standard publication will be updated.
	 * 
	 * @param newInterhash
	 * @param interhash
	 * @param session
	 * @return runnable
	 */
	public Runnable onGoldStandardPublicationUpdate(String newInterhash, String interhash, DBSession session);
	
	/**
	 * Called when a reference of a gold standard publication will be created
	 * @param userName
	 * @param interHash_publication
	 * @param interHash_reference
	 * @return runnable
	 */
	public Runnable onGoldStandardPublicationReferenceCreate(String userName, String interHash_publication, String interHash_reference);
	
	/**
	 * Called when a reference of a gold standard publication will be deleted
	 * 
	 * @param userName
	 * @param interHash_publication
	 * @param interHash_reference
	 * @param session
	 * @return runnable
	 */
	public Runnable onGoldStandardPublicationReferenceDelete(String userName, String interHash_publication, String interHash_reference, DBSession session);
	
	/**
	 * Called when a gold standard publication is deleted.
	 * 
	 * @param interhash
	 * @param session
	 * @return runnable
	 */
	public Runnable onGoldStandardPublicationDelete(String interhash, DBSession session);
	
	/**
	 * Called when a Bookmark is inserted.
	 * 
	 * @param contentId
	 * @param session
	 * @return runnable
	 */
	public Runnable onBookmarkInsert(int contentId, DBSession session);

	/**
	 * Called when a Bookmark is deleted.
	 * 
	 * @param contentId
	 * @param session
	 * @return runnable
	 */
	public Runnable onBookmarkDelete(int contentId, DBSession session);

	/**
	 * Called when a Bookmark is updated.
	 * 
	 * @param newContentId
	 * @param contentId
	 * @param session
	 * @return runnable
	 */
	public Runnable onBookmarkUpdate(int newContentId, int contentId, DBSession session);
	
	/**
	 * Called when a TagRelation is deleted.
	 * 
	 * @param upperTagName
	 * @param lowerTagName
	 * @param userName
	 * @param session
	 * @return runnable
	 */
	public Runnable onTagRelationDelete(String upperTagName, String lowerTagName, String userName, DBSession session);
	
	/**
	 * Called when a Concept is deleted.
	 * 
	 * @param conceptName
	 * @param userName
	 * @param session
	 * @return runnable
	 */
	public Runnable onConceptDelete(String conceptName, String userName, DBSession session);
	
	/**
	 * Called when a Tag is deleted.
	 * 
	 * @param contentId
	 * @param session
	 * @return runnable
	 */
	public Runnable onTagDelete(int contentId, DBSession session);
	
	/**
	 * Called when a User is inserted.
	 * 
	 * @param userName
	 * @param session
	 * @return runnable
	 */
	public Runnable onUserInsert(String userName, DBSession session);

	/**
	 * Called when a User is deleted.
	 * 
	 * @param userName
	 * @param session
	 * @return runnable
	 */
	public Runnable onUserDelete(String userName, DBSession session);

	/**
	 * Called when a User is updated.
	 * 
	 * @param userName
	 * @param session
	 * @return runnable
	 */
	public Runnable onUserUpdate(String userName, DBSession session);	

	/**
	 * Called when a user is removed from a group.
	 * 
	 * @param userName
	 * @param groupId
	 * @param session
	 * @return runnable
	 */
	public Runnable onRemoveUserFromGroup(String userName, int groupId, DBSession session);
	
	/**
	 * Called when a fellowship will be deleted
	 * 
	 * @param param
	 * @param session
	 * @return runnable
	 */
	public Runnable onDeleteFellowship(final UserParam param, final DBSession session);
	
	/**
	 * Called when a friendship will be deleted
	 * 
	 * @param param
	 * @param session
	 * @return runnable
	 */
	public Runnable onDeleteFriendship(final UserParam param, final DBSession session);
	
	/**
	 * Called when a basket item will be deleted
	 * 
	 * @param param
	 * @param session
	 * @return runnable
	 */
	public Runnable onDeleteBasketItem(final BasketParam param, final DBSession session);
	
	/**
	 * Called when all basket items will be deleted
	 * 
	 * @param userName 
	 * @param session 
	 * @return runnable
	 * 
	 */
	public Runnable onDeleteAllBasketItems(final String userName, final DBSession session);
	
	/**
	 * called when a comment was updated
	 * 
	 * @param interHash
	 * @param comment
	 * @param oldComment
	 * @param session
	 * @return runnable
	 */
	public Runnable onDiscussionUpdate(final String interHash, DiscussionItem comment, DiscussionItem oldComment, DBSession session);	
	
	/**
	 * called when a comment will be deleted
	 * 
	 * @param interHash
	 * @param deletedComment
	 * @param session
	 * @return runnable
	 */
	public Runnable onDiscussionItemDelete(final String interHash, final DiscussionItem deletedComment, final DBSession session);
}