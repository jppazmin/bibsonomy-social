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

import org.bibsonomy.webapp.command.actions.ImportCommand;
import org.bibsonomy.webapp.util.Validator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/**
 * @author mwa
 * @version $Id: ImportValidator.java,v 1.4 2010-07-13 16:03:37 nosebrain Exp $
 */
public class ImportValidator implements Validator<ImportCommand>{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		return ImportCommand.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final ImportCommand command = (ImportCommand) target;
		
		Assert.notNull(command);
		
		Assert.notNull(command.getImportType());
		
		/** look into the for each importType required fields **/
		if("delicious".equals(command.getImportType())) {
			if(command.getUserName().length() == 0){
				errors.rejectValue("userName", "error.field.required");
			}
			if(command.getPassWord().length() == 0){
				errors.rejectValue("passWord", "error.field.required");
			}
		} else if("firefox".equals(command.getImportType())) {
			if(command.getFile() == null || command.getFile().getSize() == 0){
				errors.rejectValue("file", "error.field.required");
			}
		}
	}

}
