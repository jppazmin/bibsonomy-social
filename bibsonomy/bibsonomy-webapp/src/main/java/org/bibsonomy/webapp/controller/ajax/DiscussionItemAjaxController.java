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

package org.bibsonomy.webapp.controller.ajax;

import static org.bibsonomy.util.ValidationUtils.present;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.webapp.command.ajax.DiscussionItemAjaxCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.GroupingCommandUtils;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * @author dzo
 * @version $Id: DiscussionItemAjaxController.java,v 1.2 2011-06-21 13:54:25 nosebrain Exp $
 * @param <D> 
 */
public abstract class DiscussionItemAjaxController<D extends DiscussionItem> extends AjaxController implements ValidationAwareController<DiscussionItemAjaxCommand<D>>, ErrorAware {
	private static final Log log = LogFactory.getLog(DiscussionItemAjaxController.class);
	
	private Errors errors;

	@Override
	public DiscussionItemAjaxCommand<D> instantiateCommand() {
		final DiscussionItemAjaxCommand<D> commentCommand = new DiscussionItemAjaxCommand<D>();
		commentCommand.setDiscussionItem(this.initDiscussionItem());
		return commentCommand;
	}
	
	protected abstract D initDiscussionItem();

	@Override
	public View workOn(final DiscussionItemAjaxCommand<D> command) {
		final RequestWrapperContext context = command.getContext();
		if (!context.isUserLoggedIn()) {
			throw new AccessDeniedException();
		}
		
		if (!context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
		}
		
		final String hash = command.getHash();
		
		/*
		 * resource hash must be specified
		 */
		if (!present(hash)) {
			errors.rejectValue("hash", "error.field.valid.hash");
			return returnErrorView();
		}
		
		final String username = command.getContext().getLoginUser().getName();
		
		/*
		 * don't call the validator
		 */
		if (HttpMethod.DELETE.equals(this.requestLogic.getHttpMethod())) {
			this.logic.deleteDiscussionItem(username, hash, command.getDiscussionItem().getHash());
			return Views.AJAX_JSON;
		}
		
		/*
		 * validate the command (including discussionItem)
		 */
		ValidationUtils.invokeValidator(this.getValidator(), command, this.errors);
		
		/*
		 * if validation failed return to the ajax error view
		 */
		if (this.errors.hasErrors()) {
			return returnErrorView();
		}
		
		final D discussionItem = command.getDiscussionItem();
		
		/*
		 * init groups from grouping command
		 */
		GroupingCommandUtils.initGroups(command, discussionItem.getGroups());
		
		try {
			switch(this.requestLogic.getHttpMethod()) {
				case POST:
					this.logic.createDiscussionItem(hash, username, discussionItem);
					break;
				case PUT:
					this.logic.updateDiscussionItem(username, hash, discussionItem);
					break;
				default:
					this.responseLogic.setHttpStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
		} catch (final ValidationException ex) {
			log.warn("couldn't complete controller", ex);
			return returnErrorView();
		}
		
		/*
		 * add hash as response
		 */
		final JSONObject result = new JSONObject();
		result.put("hash", discussionItem.getHash());
		command.setResponseString(result.toString());		
		return Views.AJAX_JSON;
	}
	
	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	@Override
	public boolean isValidationRequired(final DiscussionItemAjaxCommand<D> command) {
		return false;
	}
}
