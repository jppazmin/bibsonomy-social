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

import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.BasketParam;
import org.bibsonomy.database.params.UserParam;
import org.bibsonomy.model.DiscussionItem;

/**
 * This class should be used by plugins. This way they don't have to implement
 * all methods from the interface DatabasePlugin. Furthermore they have access
 * to some basic database methods.
 * 
 * @author Jens Illig
 * @author Christian Schenk
 * @author Anton Wilhelm
 * @author Stefan Stützer
 * @version $Id: AbstractDatabasePlugin.java,v 1.20 2011-06-16 13:55:01 nosebrain Exp $
 */
public class AbstractDatabasePlugin extends AbstractDatabaseManager implements DatabasePlugin {

	@Override
	public Runnable onPublicationInsert(final int contentId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onPublicationDelete(final int contentId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onPublicationUpdate(final int newContentId, final int contentId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onGoldStandardPublicationCreate(final String interhash, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onGoldStandardPublicationDelete(final String interhash, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onGoldStandardPublicationUpdate(final String newInterhash, final String interhash, final DBSession session) {
		return null;
	}
	
	@Override
	public Runnable onGoldStandardPublicationReferenceCreate(final String userName, final String interHashPublication, final String interHashReference) {
		return null;
	}

	@Override
	public Runnable onGoldStandardPublicationReferenceDelete(final String userName, final String interHashPublication, final String interHashReference, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onBookmarkInsert(final int contentId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onBookmarkDelete(final int contentId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onBookmarkUpdate(final int newContentId, final int contentId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onTagRelationDelete(final String upperTagName, final String lowerTagName, final String userName, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onConceptDelete(final String conceptName, final String userName, final DBSession session) {
		return null;
	}
	
	@Override
	public Runnable onTagDelete(final int contentId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onRemoveUserFromGroup(final String userName, final int groupId, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onUserDelete(final String userName, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onUserInsert(final String userName, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onUserUpdate(final String userName, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onDeleteFellowship(final UserParam param, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onDeleteFriendship(final UserParam param, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onDeleteBasketItem(final BasketParam param, final DBSession session) {
		return null;
	}
	
	@Override
	public Runnable onDeleteAllBasketItems(final String userName, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onDiscussionUpdate(final String interHash, final DiscussionItem comment, final DiscussionItem oldComment, final DBSession session) {
		return null;
	}

	@Override
	public Runnable onDiscussionItemDelete(final String interHash, final DiscussionItem deletedComment, final DBSession session) {
		return null;
	}
}