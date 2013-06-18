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

import org.bibsonomy.model.Bookmark;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
/**
 * @author fba
 * @version $Id: BookmarkValidator.java,v 1.3 2010-07-13 16:03:37 nosebrain Exp $
 */
public class BookmarkValidator implements Validator<Bookmark> {
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		return Bookmark.class.equals(clazz);
	}
	
	@Override
	public void validate(final Object obj, final Errors errors) {
		
		Assert.notNull(obj);
		
		if (obj instanceof Bookmark) {
			final Bookmark bookmark = (Bookmark) obj;
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "error.field.required");

			
			/*
			 * clean url
			 * 
			 * FIXME: a validator MUST NOT modify objects
			 */
			bookmark.setUrl(UrlUtils.cleanUrl(bookmark.getUrl()));
			
			/*
			 * check url
			 */
			final String url = bookmark.getUrl();
			if (url == null || url.equals("http://") || url.startsWith(UrlUtils.BROKEN_URL)) {
				errors.rejectValue("url", "error.field.valid.url");
			}
			
		}
	}

}
