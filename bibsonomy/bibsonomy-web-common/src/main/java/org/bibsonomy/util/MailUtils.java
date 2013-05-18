/**
 *
 *  BibSonomy-Web-Common - A blue social bookmark and publication sharing system.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.User;
import org.springframework.context.MessageSource;


/**
 * @author rja
 * @version $Id: MailUtils.java,v 1.5 2011-07-22 06:42:36 doerfel Exp $
 */
public class MailUtils {

	private static final Log log = LogFactory.getLog(MailUtils.class);
	/*
	 * The following constants are configured
	 */
	private String projectName;
	private String projectHome;
	private String projectBlog;
	private String projectEmail;
	private String projectRegistrationFromAddress;
	private String projectJoinGroupRequestFromAddress;
	
	private MessageSource messageSource;

	/**
	 * Stores the properties for mailing (mail host). 
	 */
	final Properties props = new Properties();
	

	/** Sends the registration mail to the user and to the project admins.
	 * 
	 * @param userName - the name of the user which registered. 
	 * @param userEmail - the email address of the user which registered.
	 * @param inetAddress - TODO: unused
	 * @param locale - a locale to use for localization
	 * @return <code>true</code>, if the email could be send without errors.
	 */
	public boolean sendActivationMail(final String userName, final String userEmail, final String inetAddress, final Locale locale) {
		final Object[] messagesParameters = new Object[]{userName, projectName, projectHome, projectBlog, projectEmail};
		/*
		 * Format the message "mail.registration.body" with the given parameters.
		 */
		final String messageBody    = messageSource.getMessage("mail.activation.body", messagesParameters, locale);
		final String messageSubject = messageSource.getMessage("mail.activation.subject", messagesParameters, locale);

		/*
		 * set the recipients
		 */
		final String[] recipient = {userEmail};
		try {
			sendMail(recipient,  messageSubject, messageBody, projectRegistrationFromAddress);
			return true;
		} catch (final MessagingException e) {
			log.fatal("Could not send registration mail: " + e.getMessage());
		}
		return false;
	}
	
	/** 
	 * Sends the registration mail to the user and to the project admins.
	 * 
	 * @param userName - the name of the user which registered. 
	 * @param userEmail - the email address of the user which registered.
	 * @param activationCode - user activation code
	 * @param inetAddress - TODO: unused!!!
	 * @param locale - a locale to use for localization
	 * @return <code>true</code>, if the email could be send without errors.
	 */
	public boolean sendRegistrationMail (final String userName, final String userEmail, final String activationCode, final String inetAddress, final Locale locale) {
		final Object[] messagesParameters = new Object[]{userName, projectName, projectHome, projectBlog, projectEmail, activationCode};
		/*
		 * Format the message "mail.registration.body" with the given parameters.
		 */
		final String messageBody    = messageSource.getMessage("mail.registration.body", messagesParameters, locale);
		final String messageSubject = messageSource.getMessage("mail.registration.subject", messagesParameters, locale);

		/*
		 * set the recipients
		 */
		final String[] recipient = {userEmail};
		try {
			sendMail(recipient,  messageSubject, messageBody, projectRegistrationFromAddress);
			return true;
		} catch (final MessagingException e) {
			log.fatal("Could not send registration mail: " + e.getMessage());
		}
		return false;
	}
	
	/** 
	 * Sends the registration mail to the user and to the group admins.
	 */
	public boolean sendJoinGroupRequest(final String groupName, final String groupMail, final User loginUser, final String reason, final Locale locale) {
		Object[] messagesParameters;
		try {
			messagesParameters = new Object[]{
					groupName,
					loginUser.getName(),
					reason,
					projectHome,
					URLEncoder.encode(groupName, "UTF-8").toLowerCase(),
					URLEncoder.encode(loginUser.getName(), "UTF-8").toLowerCase(),
					projectName.toLowerCase(),
					projectEmail
			};
		} catch (UnsupportedEncodingException e1) {
			throw new InternServerException(e1.getMessage());
		}
		/*
		 * Format the message "mail.registration.body" with the given parameters.
		 */
		final String messageBody    = messageSource.getMessage("mail.joinGroupRequest.body", messagesParameters, locale);
		final String messageSubject = messageSource.getMessage("mail.joinGroupRequest.subject", messagesParameters, locale);

		/*
		 * send an e-Mail to the group (from our registration Adress)
		 */
		try {
			sendMail(new String[] {groupMail},  messageSubject, messageBody, projectJoinGroupRequestFromAddress);
			return true;
		} catch (final MessagingException e) {
			log.fatal("Could not send join group request mail: " + e.getMessage());
		}
		return false;
	}
	
	/** 
	 * Sends the registration mail to the user and to the group admins.
	 */
	public boolean sendJoinGroupDenied(final String groupName, final String deniedUserName, final String deniedUserEMail, final String reason, final Locale locale) {
		Object[] messagesParameters;
		messagesParameters = new Object[]{
					groupName,
					deniedUserName,
					reason,
					projectHome,
					null,
					null,
					projectName.toLowerCase(),
					projectEmail
			};
		/*
		 * Format the message "mail.registration.body" with the given parameters.
		 */
		final String messageBody    = messageSource.getMessage("mail.joinGroupRequest.denied.body", messagesParameters, locale);
		final String messageSubject = messageSource.getMessage("mail.joinGroupRequest.denied.subject", messagesParameters, locale);

		/*
		 * set the recipients
		 */
		final String[] recipient = {deniedUserEMail};
		try {
			sendMail(recipient,  messageSubject, messageBody, projectJoinGroupRequestFromAddress);
			return true;
		} catch (final MessagingException e) {
			log.fatal("Could not send Deny JoinGrouprequest mail: " + e.getMessage());
		}
		return false;
	}
	
	
	/**
	 * Method to send the password reminder mail
	 * 
	 * @param userName
	 * @param userEmail
	 * @param inetAddress TODO: unused!!!
	 * @param locale
	 * @param maxmin 
	 * @param tmppw 
	 * @return true, if the mail could be send without errors
	 */
	public boolean sendPasswordReminderMail (final String userName, final String userEmail, final String inetAddress, final Locale locale, final int maxmin, final String tmppw){
		final Object[] messagesParameters = new Object[]{userName, projectName, projectHome, projectBlog, projectEmail, maxmin, tmppw};
		
		final String messageBody	= messageSource.getMessage("reminder.mail.body", messagesParameters, locale);
		final String messageSubject = messageSource.getMessage("reminder.mail.subject", messagesParameters, locale);
		
		/*
		 * set the recipients
		 */
		final String[] recipient = {userEmail};
		try {
			sendMail(recipient,  messageSubject, messageBody, projectRegistrationFromAddress);
			return true;
		} catch (final MessagingException e) {
			log.fatal("Could not send reminder mail: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Sends a mail to the given recipients
	 * 
	 * @param recipients
	 * @param subject
	 * @param message
	 * @param from
	 * @throws MessagingException
	 */
	private void sendMail(final String[] recipients, final String subject, final String message, final String from) throws MessagingException {
		boolean debug = false;

		// create some properties and get the default Session
		final Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

		// create a message
		final MimeMessage msg = new MimeMessage(session);
		

		// set the from and to address
		final InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		final InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Optional : You can also set your custom headers in the Email if you Want
		//msg.addHeader("X-Sent-By", "Bibsonomy-Bot");

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setText(message, "UTF-8");
		Transport.send(msg);
	}

	/**
	 * The name of the project.
	 * 
	 * @param projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * The base URL of the project.
	 * 
	 * @param projectHome
	 */
	public void setProjectHome(String projectHome) {
		this.projectHome = projectHome;
	}

	/** 
	 * A URL to the blog of the project.
	 * 
	 * @param projectBlog
	 */
	public void setProjectBlog(String projectBlog) {
		this.projectBlog = projectBlog;
	}

	/**
	 * The email address users can use to contact the project admins. 
	 * 
	 * @param projectEmail
	 */
	public void setProjectEmail(String projectEmail) {
		this.projectEmail = projectEmail;
	}

	/**
	 * The From: address of registration mails. 
	 * 
	 * @param projectRegistrationFromAddress
	 */
	public void setProjectRegistrationFromAddress(String projectRegistrationFromAddress) {
		this.projectRegistrationFromAddress = projectRegistrationFromAddress;
	}

	/**
	 * The From: address of join group request mails. 
	 * 
	 * @param projectJoinGroupRequestFromAddress
	 */
	public void setProjectJoinGroupRequestFromAddress(String projectJoinGroupRequestFromAddress) {
		this.projectJoinGroupRequestFromAddress = projectJoinGroupRequestFromAddress;
	}

	/**
	 * A host which accepts SMTP requests and should be used for sending mails.
	 * 
	 * @param mailHost
	 */
	public void setMailHost(String mailHost) {
		props.put("mail.smtp.host", mailHost);
	}

	/** A message source to format mail messages.
	 * @param messageSource
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
