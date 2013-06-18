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

import static org.bibsonomy.util.ValidationUtils.present;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.webapp.command.actions.GroupAdminCommand;
import org.bibsonomy.webapp.controller.SingleResourceListController;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

/**
 * TODO: check methods (auto generated)
 * 
 * @author mwa
 * @version $Id: GroupAdminPageController.java,v 1.5 2010-07-14 14:21:44 nosebrain Exp $
 */
public class GroupAdminPageController extends SingleResourceListController implements ErrorAware, ValidationAwareController<GroupAdminCommand>, RequestAware {
	private static final Log log = LogFactory.getLog(GroupAdminPageController.class);
	
	@Override
	public View workOn(final GroupAdminCommand command) {
		log.debug("GroupAdminPageController: workOn() called");
		
		final String groupName = command.getRequestedGroup();
		
		// no group given -> error
		if (!present(groupName)) {
			log.error("Invalid query /group without groupname");
			throw new MalformedURLSchemeException("error.group_page_without_groupname");
		}
		
		command.setGroup(this.logic.getGroupDetails(groupName));
		log.debug("tagsets: " + command.getGroup().getTagSets().size());
		return Views.ADMIN_GROUP;
	}
	
	@Override
	public GroupAdminCommand instantiateCommand() {
		return new GroupAdminCommand();
	}
	
	@Override
	public Errors getErrors() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setErrors(Errors errors) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Validator<GroupAdminCommand> getValidator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isValidationRequired(GroupAdminCommand command) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setRequestLogic(RequestLogic requestLogic) {
		// TODO Auto-generated method stub
	}

}
