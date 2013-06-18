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

package org.bibsonomy.webapp.controller.admin;

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.sync.SyncLogicInterface;
import org.bibsonomy.webapp.command.admin.AdminSyncCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author wla
 * @version $Id: AdminSyncViewController.java,v 1.3 2011-07-18 12:21:32 rja Exp $
 */
public class AdminSyncViewController implements MinimalisticController<AdminSyncCommand> {

	private static final Log log = LogFactory.getLog(AdminSyncViewController.class);
	private final String CREATE_SERVICE = "createService";
	private final String DELETE_SERVICE = "deleteService";
	
	private LogicInterface logic;
	
	@Override
	public AdminSyncCommand instantiateCommand() {
		return new AdminSyncCommand();
	}

	@Override
	public View workOn(final AdminSyncCommand command) {
		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		/* 
		 * Check user role
		 * If user is not logged in or not an admin: show error message 
		 */
		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("please log in as admin");
		}
		
		/*
		 * FIXME: remove after integration
		 */
		final SyncLogicInterface syncLogic = (SyncLogicInterface)logic;
		
		
		final String action = command.getAction();
		if (present(action)) {
			return performAction(command);
		}
		
		/*
		 * get services and clients from db
		 */
		command.setAvlClients(syncLogic.getSyncServices(false));
		command.setAvlServer(syncLogic.getSyncServices(true));
		
		return Views.ADMIN_SYNC;
	}
	
	private View performAction (final AdminSyncCommand command) {
		final URI service = uriFromString(command.getService());
		
		/*
		 * TODO remove after integration
		 */
		final SyncLogicInterface syncLogic = (SyncLogicInterface)logic;
		
		final String action = command.getAction();
		if(!present(service)){
			//something wrong with uri
			return new ExtendedRedirectView("/admin/sync");
		}
		if(action.equals(CREATE_SERVICE)) {
			try {
				syncLogic.createSyncService(service, command.isServer());
			} catch (final RuntimeException ex) {
				/*
				 * catch duplicates
				 */
				log.error(ex.getMessage(), ex);
			}
		} else if (action.equals(DELETE_SERVICE)) {
			syncLogic.deleteSyncService(service, command.isServer());
		} else {
			/*
			 * unknown action, do nothing
			 */
		}
		return new ExtendedRedirectView("/admin/sync");
	}
	
	private URI uriFromString(final String uriString) {
		if(present(uriString) && uriString.length() > 0) {
			try {
				return new URI(uriString);
			} catch (final URISyntaxException ex) {
				log.error("URI is malformed");
				ex.printStackTrace();
			}
		}
		log.error("URI is empty");
		return null;
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * @return the logic
	 */
	public LogicInterface getLogic() {
		return logic;
	}

}
