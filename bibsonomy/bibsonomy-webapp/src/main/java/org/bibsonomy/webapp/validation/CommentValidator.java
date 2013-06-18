/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.validation;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.model.Comment;
import org.springframework.validation.Errors;

/**
 * @author dzo
 * @version $Id: CommentValidator.java,v 1.3 2011-06-27 16:00:23 nosebrain Exp $
 */
public class CommentValidator extends DiscussionItemValidator<Comment> {

	@Override
	protected void validateDiscussionItem(final Comment comment, final Errors errors) {
		final String text = comment.getText();
		if (!present(text)) {
			errors.rejectValue(DISCUSSION_ITEM_PATH + "text", "error.field.valid.comment.text");
		} else {
			if (text.length() > Comment.MAX_TEXT_LENGTH) {
				errors.rejectValue(DISCUSSION_ITEM_PATH + "text", "error.field.valid.comment.text.length", new Object[] { Comment.MAX_TEXT_LENGTH }, "The text is too long. Only " + Comment.MAX_TEXT_LENGTH + " characters allowed.");
			}
		}
	}
}
