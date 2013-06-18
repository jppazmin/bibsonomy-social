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

package org.bibsonomy.webapp.controller;

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.layout.jabref.JabrefLayoutUtils;
import org.bibsonomy.layout.jabref.LayoutPart;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.sync.SyncLogicInterface;
import org.bibsonomy.model.sync.SyncService;
import org.bibsonomy.model.util.UserUtils;
import org.bibsonomy.webapp.command.SettingsViewCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.AccessDeniedNoticeException;
import org.bibsonomy.webapp.view.Views;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

/**
 * @author Steffen
 * @version $Id: SettingsPageController.java,v 1.44 2011-07-27 08:27:48 rja Exp $
 */
public class SettingsPageController implements MinimalisticController<SettingsViewCommand>, ErrorAware, RequestAware {

	/**
	 * hold current errors
	 */
	protected Errors errors = null;

	protected LogicInterface logic;
	protected RequestLogic requestLogic;
	private MessageSource messageSource;

	/**
	 * @param command
	 * @return the view
	 */
	@Override
	public View workOn(final SettingsViewCommand command) {
		if (!command.getContext().isUserLoggedIn()) {
			throw new AccessDeniedNoticeException("please log in", "error.general.login");
		}

		final User loginUser = command.getContext().getLoginUser();
		command.setUser(loginUser);

		// used to set the user specific value of maxCount/minFreq 
		command.setChangeTo((loginUser.getSettings().getIsMaxCount() ? loginUser.getSettings().getTagboxMaxCount() : loginUser.getSettings().getTagboxMinfreq()));

		// check whether the user is a group		
		if (UserUtils.userIsGroup(loginUser)) {
			command.setHasOwnGroup(true);
			command.showGroupTab(true);
		}

		// show sync tab only for non-spammers
		command.showSyncTab(!loginUser.isSpammer());

		switch (command.getSelTab()) {
		case 0:
			// called by the my profile tab
			workOnMyProfileTab(command);
			break;
		case 1:
			// called by the setting tab
			break;
		case 2:
			checkInstalledJabrefLayout(command);
			break;
		case 3:
			workOnGroupTab(command);
			break;
		case 4:
			workOnSyncSettingsTab(command);
			break;
		default:
			errors.reject("error.settings.tab");
			break;
		}

		return Views.SETTINGSPAGE;
	}

	private void workOnMyProfileTab(final SettingsViewCommand command) {
		final User user = command.getUser();


		command.setUserFriends(logic.getUserRelationship(user.getName(), UserRelation.FRIEND_OF, null));
		command.setFriendsOfUser(logic.getUserRelationship(user.getName(), UserRelation.OF_FRIEND, null));
		// retrieve profile privacy level setting
		command.setProfilePrivlevel(user.getSettings().getProfilePrivlevel().name().toLowerCase());
	}

	/**
	 * checks whether the user has already uploaded jabref layout definitions
	 * 
	 * @param command
	 */
	private void checkInstalledJabrefLayout(final SettingsViewCommand command) {

		final LayoutPart[] values = LayoutPart.values();

		for (final LayoutPart layoutpart : values) {

			final String fileHash = JabrefLayoutUtils.userLayoutHash(command
					.getContext().getLoginUser().getName(), layoutpart);

			final Document document = this.logic.getDocument(command.getContext()
					.getLoginUser().getName(), fileHash);

			if (document != null) {
				if ("begin".equals(layoutpart.getName())) {
					command.setBeginHash(fileHash);
					command.setBeginName(document.getFileName());
				} else if ("end".equals(layoutpart.getName())) {
					command.setEndHash(fileHash);
					command.setEndName(document.getFileName());
				} else if ("item".equals(layoutpart.getName())) {
					command.setItemHash(fileHash);
					command.setItemName(document.getFileName());
				}
			}
		}
	}

	private void workOnGroupTab(final SettingsViewCommand command) {
		final String groupName = command.getContext().getLoginUser().getName();
		//the group to update
		final Group group = logic.getGroupDetails(groupName);
		if (present(group)) {
			command.setGroup(group);
			/*
			 * get group users
			 */
			group.setUsers(this.logic.getUsers(null, GroupingEntity.GROUP, groupName, null, null, null, null, null, 0, 1000));
			/*
			 * FIXME: use the group in the command instead of 
			 * this hand-written conversion
			 */
			command.setPrivlevel(group.getPrivlevel().ordinal());
			int sharedDocsAsInt =  0;
			if (group.isSharedDocuments()) {
				sharedDocsAsInt = 1;
			}
			command.setSharedDocuments(sharedDocsAsInt);
		}
	}

	/**
	 * handles synchronization tab
	 * @param command
	 */
	private void workOnSyncSettingsTab(final SettingsViewCommand command) {

		// TODO remove cast and use logic after adding SyncLogicInterface to LogicInterface
		final SyncLogicInterface syncLogic = (SyncLogicInterface) logic;

		final List<SyncService> userServers = syncLogic.getSyncServer(command.getUser().getName());
		final List<URI> allServers = syncLogic.getSyncServices(true);

		/*
		 * Remove all servers the user already has.
		 */
		for (final SyncService service : userServers) {
			final URI serviceUri = service.getService();
			if (allServers.contains(serviceUri)) { // FIXME: not efficient
				allServers.remove(serviceUri);
			}
		}
		command.setAvailableSyncServers(allServers);
		command.setSyncServer(userServers);
		command.setAvailableSyncClients(syncLogic.getSyncServices(false));
	}

	/**
	 * @return the current command
	 */
	@Override
	public SettingsViewCommand instantiateCommand() {
		final SettingsViewCommand command = new SettingsViewCommand();
		command.setUser(new User());
		return command;
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * @param requestLogic the requestLogic to set
	 */
	@Override
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
