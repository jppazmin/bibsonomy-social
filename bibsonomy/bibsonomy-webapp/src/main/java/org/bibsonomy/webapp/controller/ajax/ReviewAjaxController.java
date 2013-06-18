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

import org.bibsonomy.model.Review;
import org.bibsonomy.webapp.command.ajax.DiscussionItemAjaxCommand;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.validation.ReviewValidator;

/**
 * - ajax/reviews
 * 
 * @author dzo
 * @version $Id: ReviewAjaxController.java,v 1.4 2011-06-21 13:54:25 nosebrain Exp $
 */
public class ReviewAjaxController extends DiscussionItemAjaxController<Review> {

	@Override
	protected Review initDiscussionItem() {
		return new Review();
	}

	@Override
	public Validator<DiscussionItemAjaxCommand<Review>> getValidator() {
		return new ReviewValidator();
	}
	
}
