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

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.webapp.command.actions.EditPostCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;


/**
 * @author fba
 * @version $Id: PostValidator.java,v 1.8 2011-05-28 14:23:33 nosebrain Exp $
 * @param <RESOURCE> 
 */
public class PostValidator<RESOURCE extends Resource> implements Validator<EditPostCommand<RESOURCE>> {
	private static final Log log = LogFactory.getLog(PostValidator.class);
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		return EditPostCommand.class.isAssignableFrom(clazz);
	}

	/**
	 * Validates the given userObj.
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object obj, final Errors errors) {
		/*
		 * To ensure that the received command is not null, we throw an
		 * exception, if this assertion fails.
		 */
		Assert.notNull(obj);
		
		@SuppressWarnings("unchecked")
		final EditPostCommand<RESOURCE> command = (EditPostCommand<RESOURCE>) obj;
		/*
		 * Let's check, that the given command is not null.
		 */
		Assert.notNull(command);

		this.validatePost(errors, command);
		
		this.validateTags(errors, command);
	}

	/**
	 * @param errors
	 * @param command
	 */
	protected void validateTags(final Errors errors, final EditPostCommand<RESOURCE> command) {
		/*
		 * validate tags
		 */
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tags", "error.field.valid.tags");
		/*
		 * if no valid (after parsing) tags given, issue an error
		 */
		final Set<Tag> tags = command.getPost().getTags();
		if (tags != null && tags.isEmpty() && !errors.hasFieldErrors("tags")) {
			errors.rejectValue("tags", "error.field.valid.tags");
		}
		
		/*
		 * if tag string contains commas, user needs to confirm
		 */
		final String tagString = command.getTags();
		if (tagString != null && (tagString.contains(",") || tagString.contains(";")) && !command.isAcceptComma()) {
			command.setContainsComma(true);
			errors.rejectValue("tags", "error.field.valid.tags.comma");
		}
		
		log.debug("errors in " + EditPostCommand.class.getName() + ": " + errors);
	}

	/**
	 * Validates the post, in particular the resource it contains and 
	 * the setting of the group selection box.
	 * 
	 * @param errors
	 * @param post
	 * @param abstractGrouping
	 * @param groups
	 */
	protected void validatePost(final Errors errors, final EditPostCommand<RESOURCE> command) {
		errors.pushNestedPath("post");
		this.validateResource(errors, command.getPost().getResource());
		errors.popNestedPath(); // post
		
		this.validateGroups(errors, command);		
	}
	
	/**
	 * Validates the given resource.
	 * 
	 * @param errors
	 * @param resource
	 */
	protected void validateResource(final Errors errors, final RESOURCE resource) {
		/*
		 * validate the resource
		 */
		errors.pushNestedPath("resource");
		/*
		 * every resource has a title ...
		 */
		if (!present(resource.getTitle())) {
			errors.rejectValue("title", "error.field.valid.title");
		}
		/*
		 * resource-specific checks
		 */
		if (resource instanceof Bookmark) {
			ValidationUtils.invokeValidator(new BookmarkValidator(), resource, errors);
		} else if (resource instanceof BibTex) {
			ValidationUtils.invokeValidator(new PublicationValidator(), resource, errors);
		}
		errors.popNestedPath(); // resource
	}

	/** Validates the groups from the command. Only some combinations are allowed, e.g., 
	 * either private, public, or other - and with certain other groups only.
	 * 
	 * @param errors
	 * @param abstractGrouping
	 * @param groups
	 */
	protected void validateGroups(final Errors errors, final EditPostCommand<RESOURCE> command) {
		ValidationUtils.invokeValidator(new GroupingValidator(), command, errors);
	}

}
