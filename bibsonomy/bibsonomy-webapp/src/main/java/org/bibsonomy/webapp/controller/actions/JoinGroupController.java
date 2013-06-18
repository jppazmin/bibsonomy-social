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
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.MailUtils;
import org.bibsonomy.webapp.command.actions.JoinGroupCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.ValidationAwareController;
import org.bibsonomy.webapp.util.Validator;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.captcha.Captcha;
import org.bibsonomy.webapp.util.captcha.CaptchaResponse;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;


/**
 * @author schwass
 * @version $Id: JoinGroupController.java,v 1.9 2011-07-25 15:38:52 doerfel Exp $
 */
/**
 * Handles 
 * * a user's request to join a group
 * * a group's response to deny the user membership
 * TODO: should also handle accept user and probably delete user too 
 */
public class JoinGroupController implements ErrorAware, ValidationAwareController<JoinGroupCommand>, RequestAware, Validator<JoinGroupCommand> {
	
	private Captcha captcha;
	private RequestLogic requestLogic;
	private Errors errors = null;
	private LogicInterface logic;
	private LogicInterface adminLogic;
	private MailUtils mailUtils;
	
	private String denyUserRedirectURI;
	
	/**
	 * maximum length for reason input.
	 */
	private int reasonMaxLen;

	@Override
	public JoinGroupCommand instantiateCommand() {
		return new JoinGroupCommand(this.reasonMaxLen);
	}

	
	@Override
	public View workOn(final JoinGroupCommand command) {
		/*
		 * The user has three options and needs:
		 * * see join site: loginUser, group
		 * * join Group: loginUser, group, reason, ckey, captcha
		 * * denyUser: loginUser = group, reason, denyUser
		 */
		final User loginUser = command.getContext().getLoginUser();

		// user logged in? 
		if (!command.getContext().isUserLoggedIn()) {
			throw new org.springframework.security.access.AccessDeniedException("please log in");
		}
		
		// get Group-Details and check if present
		String groupName = command.getGroup();
		Group group = logic.getGroupDetails(command.getGroup());
		if (!present(group)) {
			// no group given => user did not click join on the group page
			errors.reject("error.field.valid.groupName");
			return Views.ERROR;
		}

		String reason = command.getReason();
		String deniedUserName = command.getDeniedUser();
		
		if (!present(reason) && ! present(deniedUserName)) {
			// no deniedUser, no reason => probably wants to see the join_group page
			command.setCaptchaHTML(captcha.createCaptchaHtml(requestLogic.getLocale()));
			return Views.JOIN_GROUP;
		}

		/*
		 * check if ckey is valid
		 */
		if (!command.getContext().isValidCkey()) {
			errors.reject("error.field.valid.ckey");
		}
		
		// We can not check the ckey if "deny request" was chosen, since the deny
		// handle deny join request action
		if (present(deniedUserName)) {
			/*
			 * We have a deny Request
			 */
			// check if loginUser is the group
			if (!groupName.equals( command.getContext().getLoginUser().getName() )) {
				throw new AccessDeniedException("This action is only possible for a group. Please log in as a group!");
			}
			User deniedUser = adminLogic.getUserDetails(deniedUserName);
			if (!present(deniedUser)) {
				errors.reject("joinGroup.deny.noUser");
				return Views.ERROR;
			}
			mailUtils.sendJoinGroupDenied(loginUser.getName(), deniedUserName, deniedUser.getEmail(), reason, requestLogic.getLocale());
			return new ExtendedRedirectView(denyUserRedirectURI);
		}
		
		
		/*
		 * From here we assume, that the user has sent a join group request from the join group form
		 */
		
		// check if user is already in this group
		if (loginUser.getGroups().contains(group)) {
			// user wants to join a group that he's already a member of => error since he cannot use the join_group page
			errors.reject("joinGroup.already.member.error");
			return Views.ERROR;
		}
		
		// check user is spammer
		if (loginUser.isSpammer()) {
			// user is a spammer => cannot use this page
			errors.reject("joinGroup.spammerError");
			return Views.ERROR;
		}
		
		// FIXME: captcha checking; duplicate code EditPostController, PasswordReminderController, …
		if (!present(command.getRecaptcha_response_field())) {
			errors.rejectValue("recaptcha_response_field", "error.field.valid.captcha");
		} else {
			final CaptchaResponse resp = captcha.checkAnswer(command.getRecaptcha_challenge_field(), command.getRecaptcha_response_field(), requestLogic.getHostInetAddress());
			if (!present(resp)) {
				throw new InternServerException("error.captcha");
			}
			if (!resp.isValid()) {
				errors.rejectValue("recaptcha_response_field", "error.field.valid.captcha");
			} else {
				if (present(resp.getErrorMessage())) {
					errors.reject(resp.getErrorMessage());
				}
			}
		}
		
		if (errors.hasErrors()) {
			command.setCaptchaHTML(captcha.createCaptchaHtml(requestLogic.getLocale()));
			return Views.JOIN_GROUP;
		}
		
		// user is allowed to state join request and group exists => execute request
		
		// we need the user details (eMail) of the user that is the group
		final User groupUser = adminLogic.getUserDetails(groupName);
		mailUtils.sendJoinGroupRequest(group.getName(), groupUser.getEmail(), loginUser, command.getReason(), requestLogic.getLocale());
		List<String> params = new LinkedList<String>();
		params.add(groupName);
		command.setMessage("success.joinGroupRequest.sent", params);
		return Views.SUCCESS;
	}

	@Override
	public boolean isValidationRequired(final JoinGroupCommand command) {
		final RequestWrapperContext context = command.getContext();
		return context.isUserLoggedIn() && !context.getLoginUser().isSpammer();
	}

	@Override
	public Validator<JoinGroupCommand> getValidator() {
		return this;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return JoinGroupCommand.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		final JoinGroupCommand command = (JoinGroupCommand) target;

		// check length
		if (present(command.getReason()) && command.getReason().length() > reasonMaxLen) {
			errors.rejectValue("reason", "error.field.valid.limit_exceeded", new Object[] {reasonMaxLen}, "Message is too long");
			command.setReason(command.getReason().substring(0, reasonMaxLen));
		}
		
	}


	@Override
	public Errors getErrors() {
		return errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	/**
	 * Give this controller an instance of {@link Captcha}.
	 * @param captcha
	 */
	@Required
	public void setCaptcha(final Captcha captcha) {
		this.captcha = captcha;
	}

	/** The logic needed to access the request
	 * @param requestLogic 
	 */
	@Override
	@Required
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	/** Injects an instance of the MailUtils to send registration success mails.
	 * @param mailUtils
	 */
	@Required
	public void setMailUtils(final MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}

	/**
	 * @param logic
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}
	
	/**
	 * @param adminLogic the adminLogic to set
	 */
	public void setAdminLogic(final LogicInterface adminLogic) {
		this.adminLogic = adminLogic;
	}

	/**
	 * @param denieUserRedirectURI the denieUserRedirectURI to set
	 */
	public void setDenieUserRedirectURI(final String denieUserRedirectURI) {
		this.denyUserRedirectURI = denieUserRedirectURI;
	}

	/**
	 * @param reasonMaxLen the reasonMaxLen to set
	 */
	public void setReasonMaxLen(final int reasonMaxLen) {
		this.reasonMaxLen = reasonMaxLen;
	}
	
	
}
