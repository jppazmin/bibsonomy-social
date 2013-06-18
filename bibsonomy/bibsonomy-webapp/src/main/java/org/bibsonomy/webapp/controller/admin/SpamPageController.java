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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Classifier;
import org.bibsonomy.common.enums.ClassifierSettings;
import org.bibsonomy.common.enums.Role;
import org.bibsonomy.common.enums.SpamStatus;
import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.command.admin.AdminStatisticsCommand;
import org.bibsonomy.webapp.command.admin.AdminViewCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.security.access.AccessDeniedException;

/**
 * Controller for admin page
 * 
 * @author Stefan Stützer
 * @author Beate Krause
 * @version $Id: SpamPageController.java,v 1.7 2011-07-14 15:19:56 nosebrain Exp $
 **/
public class SpamPageController implements MinimalisticController<AdminViewCommand> {
	private static final Log log = LogFactory.getLog(SpamPageController.class);


	private LogicInterface logic;

	@Override
	public View workOn(final AdminViewCommand command) {
		log.debug(this.getClass().getSimpleName());

		final RequestWrapperContext context = command.getContext();
		final User loginUser = context.getLoginUser();

		/*
		 * Check user role If user is not logged in or not an admin: show error
		 * message
		 */
		if (!context.isUserLoggedIn() || !Role.ADMIN.equals(loginUser.getRole())) {
			throw new AccessDeniedException("please log in as admin");
		}
		
		this.setUsers(command);

		/*
		 * only compute counts for specific tabs to save 
		 * processing time for frequently used tabs
		 */
		if (command.getSelTab() == AdminViewCommand.CLASSIFIER_NOSPAMMER_INDEX
				|| command.getSelTab() == AdminViewCommand.CLASSIFIER_SPAMMER_INDEX) {
			this.setStatistics(command);
		}

		for (final ClassifierSettings s : ClassifierSettings.values()) {
			command.setClassifierSetting(s, this.logic.getClassifierSettings(s));
		}

		/*
		 * handle specific user
		 */
		if (present(command.getAclUserInfo())) {
			if ("flag_spammer".equals(command.getAction())) {
				if (!logic.getUserDetails(command.getAclUserInfo()).getSpammer()) {
					final User user = new User(command.getAclUserInfo());
					user.setToClassify(0);
					user.setAlgorithm("admin");
					user.setSpammer(true);
					this.logic.updateUser(user, UserUpdateOperation.UPDATE_ALL);
				} else {
					command.addInfo("The user was already flagged as a spammer.");
				}
			}
			command.setUser(logic.getUserDetails(command.getAclUserInfo()));
		}

		return Views.ADMIN_SPAM;

	}

	@Override
	public AdminViewCommand instantiateCommand() {
		return new AdminViewCommand();
	}

	private void setStatistics(final AdminViewCommand cmd) {
		final AdminStatisticsCommand command = cmd.getStatisticsCommand();

		for (final int interval : cmd.getInterval()) {
			command.setNumAdminSpammer(Long.valueOf(interval), this.logic.getClassifiedUserCount(Classifier.ADMIN, SpamStatus.SPAMMER, interval));
			command.setNumAdminNoSpammer(Long.valueOf(interval), this.logic.getClassifiedUserCount(Classifier.ADMIN, SpamStatus.NO_SPAMMER, interval));
			command.setNumClassifierSpammer(Long.valueOf(interval), this.logic.getClassifiedUserCount(Classifier.CLASSIFIER, SpamStatus.SPAMMER, interval));
			command.setNumClassifierSpammerUnsure(Long.valueOf(interval), this.logic.getClassifiedUserCount(Classifier.CLASSIFIER, SpamStatus.SPAMMER_NOT_SURE, interval));
			command.setNumClassifierNoSpammerUnsure(Long.valueOf(interval), this.logic.getClassifiedUserCount(Classifier.CLASSIFIER, SpamStatus.NO_SPAMMER_NOT_SURE, interval));
			command.setNumClassifierNoSpammer(Long.valueOf(interval), this.logic.getClassifiedUserCount(Classifier.CLASSIFIER, SpamStatus.NO_SPAMMER, interval));
		}
	}

	private void setUsers(final AdminViewCommand command) {
		if (command.getSelTab() == AdminViewCommand.CLASSIFIER_EVALUATE) {
			// TODO: check interval 
			command.setContent(this.logic.getClassifierComparison(command.getInterval()[0], command.getLimit()));
			return;
		}

		final Classifier classifier;
		final SpamStatus status;

		/* set content in dependence of the selected tab */
		switch (command.getSelTab()) {
		case AdminViewCommand.MOST_RECENT:
			classifier = Classifier.BOTH;
			status = null;
			break;
		case AdminViewCommand.ADMIN_SPAMMER_INDEX:
			classifier = Classifier.ADMIN;
			status = SpamStatus.SPAMMER;
			break;
		case AdminViewCommand.ADMIN_UNSURE_INDEX:
			classifier = Classifier.ADMIN;
			status = SpamStatus.UNKNOWN;
			break;
		case AdminViewCommand.ADMIN_NOSPAMMER_INDEX:
			classifier = Classifier.ADMIN;
			status = SpamStatus.NO_SPAMMER;
			break;
		case AdminViewCommand.CLASSIFIER_SPAMMER_INDEX:
			classifier = Classifier.CLASSIFIER;
			status = SpamStatus.SPAMMER;
			break;
		case AdminViewCommand.CLASSIFIER_SPAMMER_UNSURE_INDEX:
			classifier = Classifier.CLASSIFIER;
			status = SpamStatus.SPAMMER_NOT_SURE;
			break;
		case AdminViewCommand.CLASSIFIER_NOSPAMMER_INDEX:
			classifier = Classifier.CLASSIFIER;
			status = SpamStatus.NO_SPAMMER;
			break;
		case AdminViewCommand.CLASSIFIER_NOSPAMMER_UNSURE_INDEX:
			classifier = Classifier.CLASSIFIER;
			status = SpamStatus.NO_SPAMMER_NOT_SURE;
			break;
		default:
			classifier = null;
			status = null;
		}
		command.setContent(this.logic.getClassifiedUsers(classifier, status, command.getLimit()));
	}

	/**
	 * @param logic the logic to set
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

}