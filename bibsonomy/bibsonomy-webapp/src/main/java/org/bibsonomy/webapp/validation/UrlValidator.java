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

import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.webapp.command.ajax.AjaxURLCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * 
 * FIXME: duplicate of {@link BookmarkValidator}.
 * 
 * @author rja
 * @version $Id: UrlValidator.java,v 1.3 2011-06-20 14:00:28 nosebrain Exp $
 */
public class UrlValidator implements Validator<AjaxURLCommand> {
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return AjaxURLCommand.class.equals(clazz);
	}
	
	@Override
	public void validate(final Object obj, final Errors errors) {
		Assert.notNull(obj);
		
		if (obj instanceof AjaxURLCommand) {
			final AjaxURLCommand command = (AjaxURLCommand) obj;
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "error.field.required");

			
			/*
			 * clean url
			 * 
			 * FIXME: a validator MUST NOT modify objects
			 */
			command.setUrl(UrlUtils.cleanUrl(command.getUrl()));
			
			/*
			 * check url
			 */
			final String url = command.getUrl();
			if (url == null || url.equals("http://") || url.startsWith(UrlUtils.BROKEN_URL)) {
				errors.rejectValue("url", "error.field.valid.url");
			}
		}
	}

}
