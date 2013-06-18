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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.ajax.FollowerAjaxCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;


/**
 * This controller catches ajax requests on the /ajax/handleFollower url
 * and handles the add and remove actions of following a user.
 * 
 * @author Christian Kramer
 * @version $Id: FollowerAjaxController.java,v 1.12 2011-03-21 10:31:46 dbe Exp $
 */
public class FollowerAjaxController extends AjaxController implements MinimalisticController<FollowerAjaxCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(FollowerAjaxController.class);
	
	private Errors errors;

	@Override
	public FollowerAjaxCommand instantiateCommand() {
		return new FollowerAjaxCommand();
	}

	@Override
	public View workOn(FollowerAjaxCommand command) {
		log.debug(this.getClass().getSimpleName());
		
		if (!command.getContext().getUserLoggedIn()){
			log.debug("someone tried to access this ajax controller manually and isn't logged in");
			return new ExtendedRedirectView("/");
		}
		
		log.debug("AJAX controller; userName: " + command.getRequestedUserName() + ", action: " + command.getAction() + ", ckey: " + command.getContext().getCkey() + ", forward: " + command.getForward());
		
		//check if ckey is valid
		if (!command.getContext().isValidCkey()) {
			errors.reject("error.field.valid.ckey");
			return Views.ERROR;
		}
		
		// switch between add or remove and call the right method
		if ("addFollower".equals(command.getAction()) && command.getRequestedUserName() != null){
			this.addFollower(command);
		}		
		if ("removeFollower".equals(command.getAction()) && command.getRequestedUserName() != null){
			this.removeFollower(command);
		}
				
		// forward to a certain page, if requested 
		if (present(command.getForward())) {
			// TODO: remove?!?
//			if (EnumUtils.searchEnumByName(Views.values(), command.getForward()) == null) {
//				errors.reject("error.invalid_forward_page");
//				return Views.ERROR;
//			}
			return new ExtendedRedirectView("/" + command.getForward());
		}
		
		return Views.AJAX_TEXT;
	}
	
	/**
	 * use this method to add a user to the followers table
	 * 
	 * @param command
	 */
	private void addFollower(FollowerAjaxCommand command){
		User user = new User(command.getRequestedUserName());
		logic.createUserRelationship(command.getContext().getLoginUser().getName(),user.getName(), UserRelation.FOLLOWER_OF, null);
	}
	
	/**
	 * use this method to remove someone from the followers table
	 * 
	 * @param command
	 */
	private void removeFollower(FollowerAjaxCommand command){
		User user = new User(command.getRequestedUserName());
		logic.deleteUserRelationship(command.getContext().getLoginUser().getName(),user.getName(), UserRelation.FOLLOWER_OF, null);
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

}
