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

package org.bibsonomy.database.managers.discussion;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.discussion.CommentParam;
import org.bibsonomy.database.params.discussion.DiscussionItemParam;
import org.bibsonomy.model.Comment;

/**
 * Used to create, read, update and delete comments from the database.
 * 
 * @author dzo
 * @version $Id: CommentDatabaseManager.java,v 1.2 2011-06-21 13:42:19 nosebrain Exp $
 */
public class CommentDatabaseManager extends DiscussionItemDatabaseManager<Comment> {
	
	private static final CommentDatabaseManager INSTANCE = new CommentDatabaseManager();

	/**
	 * @return the @{link:CommentDatabaseManager} instance
	 */
	public static CommentDatabaseManager getInstance() {
		return INSTANCE;
	}

	private CommentDatabaseManager() {
		// just call super
	}
	
	@Override
	protected void checkDiscussionItem(final Comment comment, final DBSession session) {
		/*
		 * comments need a text
		 */
		final String text = comment.getText();
		if (!present(text)) {
			throw new ValidationException("comment text is empty");
		}
		
		/*
		 * max text length
		 */
		if (text.length() > Comment.MAX_TEXT_LENGTH) {
			throw new ValidationException("comment text is too long");
		}
	}

	@Override
	protected DiscussionItemParam<Comment> createDiscussionItemParam() {
		return new CommentParam();
	}
}
