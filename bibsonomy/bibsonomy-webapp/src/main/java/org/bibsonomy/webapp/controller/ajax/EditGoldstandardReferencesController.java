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

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.webapp.command.ajax.EditGoldstandardReferencesCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

/**
 * Controller for editing references of a gold standard post
 *    - ajax/goldstandards/references
 * 
 * @author dzo
 * @version $Id: EditGoldstandardReferencesController.java,v 1.6 2011-04-01 15:24:25 nosebrain Exp $
 */
public class EditGoldstandardReferencesController extends AjaxController implements MinimalisticController<EditGoldstandardReferencesCommand>, ErrorAware {
	
	@Override
	public EditGoldstandardReferencesCommand instantiateCommand() {
		return new EditGoldstandardReferencesCommand();
	}

	private Errors errors;
	
	@Override
	public View workOn(final EditGoldstandardReferencesCommand command) {

		final RequestWrapperContext context = command.getContext();
		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(context.getLoginUser().getRole())) {
			throw new AccessDeniedException("You are not allowed to edit references of a goldstandard");
		}

		//check if ckey is valid
		if (!context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
			return Views.ERROR;
		}
		
		final String hash = command.getHash();
		final Set<String> references = command.getReferences();
		
		if (!present(hash) || !present(references)) {
			this.responseLogic.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
			return Views.AJAX_TEXT;
		}
		
		final HttpMethod httpMethod = this.requestLogic.getHttpMethod();
		
		switch (httpMethod) {
		case POST: 
			this.logic.createReferences(hash, references);
			break;
		case DELETE: 
			this.logic.deleteReferences(hash, references);
			break;
		default: 
			this.responseLogic.setHttpStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		
		return Views.AJAX_TEXT;
	}

	@Override
	public Errors getErrors() {
		return errors;
	}

	@Override
	public void setErrors(Errors errors) {
		this.errors = errors;
	}

}
