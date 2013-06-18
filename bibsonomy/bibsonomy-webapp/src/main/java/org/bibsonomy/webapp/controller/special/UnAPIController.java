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

package org.bibsonomy.webapp.controller.special;

import javax.servlet.http.HttpServletResponse;

import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.util.ValidationUtils;
import org.bibsonomy.webapp.command.special.UnAPICommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.ResponseAware;
import org.bibsonomy.webapp.util.ResponseLogic;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

/**
 * Controller for UnAPI requests according to the <a href="http://unapi.info/specs/">UnAPI</a> spec.
 * 
 * @author rja
 * @version $Id: UnAPIController.java,v 1.4 2011-04-21 15:09:53 rja Exp $
 */
public class UnAPIController implements MinimalisticController<UnAPICommand>, ResponseAware, ErrorAware {
	private ResponseLogic responseLogic;
	private Errors errors;
	
	@Override
	public View workOn(final UnAPICommand command) {
		final String unapiFormat = command.getFormat();
		final String id     = command.getId();

		/*
		 * no id given:
		 * print the list of supported formats in XML  
		 */
		if (!ValidationUtils.present(id)) {
			return Views.UNAPI_SUPPORTED_FORMATS;
		}
		/*
		 * no format given:
		 * print the list of supported formats in XML, including the post ID
		 */
		if (!ValidationUtils.present(unapiFormat)) {
			/*
			 * set HTTP status to 300: Multiple Choices, as suggested by UnAPI spec
			 * 
			 * XXX: normally, MinimalisticControllerSpringWrapper is responsible for
			 * manipulating the HTTP status ... the "official" way would be to throw
			 * an exception and then catch it there, generating the correct response.
			 * Since the effort is higher (writing of new exception + modification of
			 * the wrapper), I the responseLogic instead. (rja, 2009-01-12)
			 */
			responseLogic.setHttpStatus(HttpServletResponse.SC_MULTIPLE_CHOICES);
			return Views.UNAPI_SUPPORTED_FORMATS;
		}

		/*
		 * format given: check, if it is valid
		 */
		final String mappedFormat = getFormat(unapiFormat);
		if (mappedFormat == null) {
			/*
			 * invalid format: return a 406
			 * 
			 * "406 Not Acceptable for requests for an identifier that is available 
			 * on the server in a format that is not available for that identifier"
			 *
			 * XXX: see previous XXX comment. 
			 */
			responseLogic.setHttpStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			errors.reject("error.format_not_available", "The requested format is not available for the requested id.");
			return Views.ERROR;
		}

		/*
		 * valid format and id given: 
		 * redirect to appropriate controller
		 */
		return new ExtendedRedirectView("/" + mappedFormat + "/bibtex/" + HashID.INTRA_HASH.getId() + id);
	}	


	/** 
	 * Maps UnAPI format identifiers to BibSonomy format identifiers.
	 * 
	 * @param format - the UnAPI format identifier
	 * @return The BibSonomy format identifier, or <code>null</code> if the format is unknown.
	 */
	private String getFormat(final String format) {
		/*
		 * since we currently support just one format, we do this with a simple "if".
		 * -> later implementations should do this in a more general way.
		 */
		if ("bibtex".equals(format)) return Views.FORMAT_STRING_BIB;
		/*
		 * format not known: return NULL.
		 */
		return null;
	}

	@Override
	public UnAPICommand instantiateCommand() {
		return new UnAPICommand();
	}
	
	/** 
	 * Supplies the responseLogic to this controller. The controller needs it to 
	 * set the correct HTTP status codes according to the UnAPI specification.
	 * 
	 * @see org.bibsonomy.webapp.util.ResponseAware#setResponseLogic(org.bibsonomy.webapp.util.ResponseLogic)
	 */
	@Override
	public void setResponseLogic(final ResponseLogic responseLogic) {
		this.responseLogic = responseLogic;
	}
	
	@Override
	public Errors getErrors() {
		return errors;
	}
	
	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}
}