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

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.model.User;
import org.bibsonomy.model.UserSettings;
import org.bibsonomy.webapp.command.SettingsViewCommand;
import org.bibsonomy.webapp.controller.SettingsPageController;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.AccessDeniedNoticeException;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @author cvo
 * @version $Id: UpdateUserSettingsController.java,v 1.18 2011-07-25 14:13:54 rja Exp $
 */
public class UpdateUserSettingsController extends SettingsPageController implements MinimalisticController<SettingsViewCommand>, RequestAware {
	private static final Log log = LogFactory.getLog(DeletePostController.class);
		
	@Override
	public SettingsViewCommand instantiateCommand() {
		final SettingsViewCommand command = new SettingsViewCommand();
		command.setUser(new User());
		return command;
	}

	@Override
	public View workOn(final SettingsViewCommand command) {		
		final RequestWrapperContext context = command.getContext();

		/*
		 * user has to be logged in to delete himself
		 */
		if (!context.isUserLoggedIn()) {
			throw new AccessDeniedNoticeException("please log in", "error.general.login");
		}
		
		/*
		 * check the ckey
		 */
		if (!context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
		}
		
		if (errors.hasErrors()){
			super.workOn(command);
		}
		
		final User user = context.getLoginUser(); 

		
	
		//do set new settings here
		final String action = command.getAction();
		if ("logging".equals(action)) {
			/*
			 * change the log level
			 */
			updateLogging(command.getUser().getSettings(), user);
		} else if ("api".equals(action)) {
			/*
			 * change the api key of a user
			 */
			updateApiKey(user);
		} else if ("layoutTagPost".equals(action)) {
			/*
			 * changes the layout of tag and post for a user
			 */
			updateLayoutTagPost(command, user);
		} else {
			errors.reject("error.invalid_parameter");
		}
	
		// success: go back where you've come from
		// TODO: inform the user about the success!
		return super.workOn(command);
	}
	
	private void updateLogging(final UserSettings commandSettings, final User user) {
		final UserSettings userSettings = user.getSettings();
		userSettings.setLogLevel(commandSettings.getLogLevel());
		userSettings.setConfirmDelete(commandSettings.isConfirmDelete());

		final String updatedUser = logic.updateUser(user, UserUpdateOperation.UPDATE_SETTINGS);
		log.debug("logging settings of user " + updatedUser + " has been changed successfully");
	}
	
	private void updateApiKey(final User user) {
		this.logic.updateUser(user, UserUpdateOperation.UPDATE_API);
		log.debug("api key of " + user.getName() + " has been changed successfully");
	}
	
	private void updateLayoutTagPost(final SettingsViewCommand command, final User user) {
		final UserSettings commandSettings = command.getUser().getSettings();
		
		if(!commandSettings.isShowBibtex() && !commandSettings.isShowBookmark()) {
			errors.rejectValue("user.settings.showBookmark", "error.field.oneResourceMin");
			return;
		}
		final UserSettings userSettings = user.getSettings();
		
		userSettings.setDefaultLanguage(commandSettings.getDefaultLanguage());
		userSettings.setListItemcount(commandSettings.getListItemcount());
		userSettings.setTagboxTooltip(commandSettings.getTagboxTooltip());
		userSettings.setShowBookmark(commandSettings.isShowBookmark());
		userSettings.setShowBibtex(commandSettings.isShowBibtex());
		
		userSettings.setSimpleInterface(commandSettings.isSimpleInterface());

		userSettings.setIsMaxCount(commandSettings.getIsMaxCount());
		if (userSettings.getIsMaxCount()) {
			userSettings.setTagboxMaxCount(command.getChangeTo());
		} else {
			userSettings.setTagboxMinfreq(command.getChangeTo());
		}
		userSettings.setTagboxSort(commandSettings.getTagboxSort());
		userSettings.setTagboxStyle(commandSettings.getTagboxStyle());
		
		final String updatedUser = this.logic.updateUser(user, UserUpdateOperation.UPDATE_SETTINGS);
		log.debug("settings for the layout of tag boxes and post lists of user " + updatedUser + " has been changed successfully");
		/*
		 * trigger locale change
		 * 
		 * FIXME: There is code in InitUserFilter to change the locale and we
		 * have Spring classes (i.e., LocaleChangeInterceptor, SessionLocaleResolver) 
		 * to do this. We must unify this handling!
		 * 
		 * Another problem is, that we use low level setSessionAttribute() methods 
		 * instead of SessionLocaleResolver.setLocale(), because for the latter
		 * we would need the request + response which we don't have. 
		 */
		requestLogic.setSessionAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale(userSettings.getDefaultLanguage()));
	}

}
