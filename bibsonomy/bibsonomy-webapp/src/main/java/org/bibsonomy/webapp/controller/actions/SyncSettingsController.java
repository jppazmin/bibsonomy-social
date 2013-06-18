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

import java.util.LinkedList;
import java.util.List;

import org.bibsonomy.common.exceptions.AccessDeniedException;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.sync.ConflictResolutionStrategy;
import org.bibsonomy.model.sync.SyncLogicInterface;
import org.bibsonomy.model.sync.SyncService;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.webapp.command.SettingsViewCommand;
import org.bibsonomy.webapp.controller.SettingsPageController;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.validation.SyncSettingsValidator;
import org.bibsonomy.webapp.view.ExtendedRedirectView;

/**
 * @author wla
 * @version $Id: SyncSettingsController.java,v 1.11 2011-07-27 15:37:34 wla Exp $
 */
public class SyncSettingsController extends SettingsPageController implements MinimalisticController<SettingsViewCommand>, ValidationAwareController<SettingsViewCommand> {
	
	@Override
	public SettingsViewCommand instantiateCommand() {
		final SettingsViewCommand command = new SettingsViewCommand();
		command.setSyncServer(new LinkedList<SyncService>());
		return command;
	}

	/**
	 * FIXME remove casts to {@link SyncLogicInterface} after integration of 
	 * {@link SyncLogicInterface} into {@link LogicInterface}
	 * 
	 * @see org.bibsonomy.webapp.controller.SettingsPageController#workOn(org.bibsonomy.webapp.command.SettingsViewCommand)
	 */
	@Override
	public View workOn(final SettingsViewCommand command) {
		
		final RequestWrapperContext context = command.getContext();
		
		/*
		 * some security checks
		 */
		if (!context.isUserLoggedIn()) {
			throw new org.springframework.security.access.AccessDeniedException("please log in");
		}
		final User loginUser = context.getLoginUser();
		if (loginUser.isSpammer()) {
			throw new AccessDeniedException("error.method_not_allowed");
		}
		if (!context.isValidCkey()) {
			/*
			 * If user is coming from one of the configured sync services, we
			 * should better show a hint that he should now acknowledge the sync
			 * setting. 
			 */
			final String referer = requestLogic.getReferer();
			final SyncService newSyncServer = command.getNewSyncServer();
			if (present(referer) && present(newSyncServer) && present(newSyncServer.getService()) && referer.startsWith(newSyncServer.getService().toString())) { 
				this.errors.rejectValue("newSyncServer.service", "synchronization.server.add.acknowledge", new Object[]{newSyncServer.getService()}, "please acknowledge the new synchronization server");
			} else {
				this.errors.reject("error.field.valid.ckey");
			}
		}
		
		/*
		 * Get the service whose form has been sent (i.e., that 
		 * should be updated or deleted)
		 */
		final SyncService syncServer = getSyncServer(command.getSyncServer());

		final HttpMethod httpMethod = this.requestLogic.getHttpMethod();
		
		if (errors.hasErrors()) {
			final View view = super.workOn(command);
			/*
			 * On update, we replace the sync service from the DB with the one
			 * the user wants to update.
			 */
			if (HttpMethod.PUT.equals(httpMethod)) {
				replaceSyncService(command.getSyncServer(), syncServer);
			} 
			return view; 
		}

		
		final String loginUserName = loginUser.getName();

		/*
		 * FIXME make ConflictResolutionStrategy settable
		 */
		final ConflictResolutionStrategy strategy = ConflictResolutionStrategy.LAST_WINS;
		switch (httpMethod) {
		case POST:
			final SyncService newSyncServer = command.getNewSyncServer();
			((SyncLogicInterface) logic).createSyncServer(loginUserName, newSyncServer.getService(), newSyncServer.getResourceType(), newSyncServer.getServerUser(), newSyncServer.getDirection(), strategy);
			break;
		case PUT:
			((SyncLogicInterface) logic).updateSyncServer(loginUserName, syncServer.getService(), syncServer.getResourceType(), syncServer.getServerUser(), syncServer.getDirection(), strategy);
			break;
		case DELETE:
			((SyncLogicInterface) logic).deleteSyncServer(loginUserName, syncServer.getService());
			break;
		default:
			errors.reject("error.general");
			break;
		}

		return new ExtendedRedirectView("/settings?selTab=" + command.getSelTab());
	}
	
	/**
	 * Finds the sync service in the list whose update/create form was send. 
	 * 
	 * @param syncServices
	 * @return
	 */
	private SyncService getSyncServer(final List<SyncService> syncServices) {
		for (final SyncService syncService : syncServices) {
			if (present(syncService.getService())) return syncService;
		}
		return null;
	}
	
	/**
	 * Finds the given syncService in the list and replaces it.
	 *  
	 * @param syncServices
	 */
	private void replaceSyncService(final List<SyncService> syncServices, final SyncService syncService) {
		for (int i = 0; i < syncServices.size(); i++) {
			final SyncService next = syncServices.get(i);
			if (next.getService().equals(syncService.getService())) {
				syncServices.set(i, syncService);
			}
		}
	}

	@Override
	public Validator<SettingsViewCommand> getValidator() {
		return new SyncSettingsValidator();
	}

	@Override
	public boolean isValidationRequired(SettingsViewCommand command) {
		return true;
	}
	
}
