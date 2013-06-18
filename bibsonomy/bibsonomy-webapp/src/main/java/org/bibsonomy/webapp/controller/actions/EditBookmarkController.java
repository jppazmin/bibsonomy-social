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

package org.bibsonomy.webapp.controller.actions;

import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.actions.EditBookmarkCommand;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.validation.PostValidator;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

/**
 * @author fba
 * @version $Id: EditBookmarkController.java,v 1.6 2010-03-12 11:11:34 rja Exp $
 */
public class EditBookmarkController extends EditPostController<Bookmark, EditBookmarkCommand> {
	
	@Override
	protected View getPostView() {
		return Views.EDIT_BOOKMARK; // TODO: this could be configured using Spring!
	}

	@Override
	protected Bookmark instantiateResource() {
		final Bookmark bookmark = new Bookmark();
		/*
		 * set default values.
		 */
		bookmark.setUrl("http://");
		return bookmark;
	}

	@Override
	protected PostValidator<Bookmark> getValidator() {
		return new PostValidator<Bookmark>();
	}

	@Override
	protected EditBookmarkCommand instantiateEditPostCommand() {
		return new EditBookmarkCommand();
	}

	@Override
	protected void setDuplicateErrorMessage(Post<Bookmark> post, Errors errors) {
		errors.rejectValue("post.resource.url", "error.field.valid.url.alreadybookmarked");
	}

	@Override
	protected void workOnCommand(final EditBookmarkCommand command, User loginUser) {
		// noop
	}

}
