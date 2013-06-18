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

import org.bibsonomy.model.Resource;
import org.bibsonomy.webapp.command.actions.EditPostCommand;
import org.springframework.validation.Errors;


/**
 * 
 * @param <R> 
 * @author dzo
 * @version $Id: GoldStandardPostValidator.java,v 1.2 2011-05-28 14:23:33 nosebrain Exp $
 */
public class GoldStandardPostValidator<R extends Resource> extends PostValidator<R> {
	
	@Override
	protected void validateTags(Errors errors, EditPostCommand<R> command) {
		// goldstandards have no tags
	}
	
	@Override
	public void validateGroups(Errors errors, EditPostCommand<R> commmand) {
		// golstandards have no groups
	}
}
