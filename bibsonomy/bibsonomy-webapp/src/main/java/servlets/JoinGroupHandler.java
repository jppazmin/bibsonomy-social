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

package servlets;

import helpers.mail;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.User;
import org.bibsonomy.util.spring.security.AuthenticationUtils;

import servlets.listeners.InitialConfigListener;

@Deprecated
public class JoinGroupHandler extends HttpServlet {

	private static final long serialVersionUID = 385676755112385793L;

	private static final Log log = LogFactory.getLog(JoinGroupHandler.class);

	private static final String reCaptchaPublicKey = InitialConfigListener.getInitParam("ReCaptchaPublicKey");
	private static final String reCaptchaPrivateKey = InitialConfigListener.getInitParam("ReCaptchaPrivateKey");
	
	private DataSource dataSource;
	private static String projectHome = null;
	private static String projectName = null;
	private static final int MAX_REASON_LENGTH = 200;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config); 
		try {
			dataSource  = (DataSource) ((Context) new InitialContext().lookup("java:/comp/env")).lookup("jdbc/bibsonomy");
			projectHome = config.getServletContext().getInitParameter("projectHome");
			projectName = config.getServletContext().getInitParameter("projectName");
		} catch (NamingException ex) {
			throw new ServletException("Cannot retrieve java:/comp/env/bibsonomy",ex);
		}
	}	

	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("error", "Sorry, unidentified request!");
		getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Get the session attribute of current user  */
		HttpSession session = request.getSession(true);

		final User userBean = AuthenticationUtils.getUser();
		String currUser = userBean.getName(); 
		if (currUser == null) {
			// TODO: does this work on bibsonomy.org? I guess, /bibsonomy/ is added, because
			// the servlet API spec says something about that
			String refer = "/login?referer=" + URLEncoder.encode("/JoinGroupHandler?" + request.getQueryString(), "UTF-8");
			response.sendRedirect(refer);
			return;
		}

		Connection        conn = null;
		ResultSet         rst  = null;
		PreparedStatement stmt = null;

		try {
			synchronized(dataSource) {
				if(dataSource != null){
					conn = dataSource.getConnection();
				} else { 
					throw new SQLException("No Datasource");	   	
				}
			}

			String 	cancel    = request.getParameter("cancel");
			String 	requGroup = request.getParameter("group");

			if (requGroup == null && "true".equals(cancel)) {
				/*
				 * process CANCEL request
				 * TODO: it should be possible to disallow the user to ask again for joining the group (next step, idea exists)
				 */
				String user = request.getParameter("user");

				//check if currUser is group
				stmt = conn.prepareStatement("SELECT group_name FROM groupids WHERE group_name = ? ");
				stmt.setString(1, currUser);
				rst = stmt.executeQuery();

				if (rst.next()) {
					//get user email
					stmt = conn.prepareStatement("SELECT user_email FROM `user` WHERE user_name = ?");
					stmt.setString(1, user);
					rst = stmt.executeQuery();

					if (rst.next()) {
						String user_mail = rst.getString("user_email");
						String reason    = request.getParameter("reason");

						log.fatal("admin of " + requGroup + "cancels request of " + user + " with reason " + reason);

						String message ="\nHello " + user + "\n" +
						"\nyour request to join the group " + currUser + " has been canceled."+
						"\n"+
						"\n Reason: " + reason +
						"\n"+
						"\nHave a look on the help page and the FAQ:" +
						"\n" + projectHome + "help" +
						"\n" + projectHome + "faq" + "\n" +
						"\nNews regarding " + projectName + " can be found in our blog:" +
						"\nhttp://blog.bibsonomy.org" +
						"\n" +
						"\nReplies to this e-mail address are deleted, please send questions to webmaster@" + projectName.toLowerCase() + ".org.\n"; 

						mail.sendMail(new String[] {user_mail},  "Your group join request", message, "groups@" + projectName.toLowerCase() + ".org");
						mail.sendMail(new String[] {"register@bibsonomy.org"},  "Your group join request", message, "groups@" + projectName.toLowerCase() + ".org");

						response.sendRedirect("/settings");
					} else {
						request.setAttribute("error", "The user does not exist.");
						getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
					}
				} else {
					request.setAttribute("error", "You're not a group admin.");
					getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
				}
			} else if (requGroup != null && cancel == null)	{
				requGroup = requGroup.toLowerCase().trim();
				/*
				 * process JOIN request
				 */

			     // check captcha (see http://tanesha.net/projects/recaptcha4j/)
		        ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(reCaptchaPublicKey, reCaptchaPrivateKey, false);
		        ReCaptchaResponse captchaAnswer = captcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
				
				
				if (captchaAnswer == null) { 
					/* We could not get the original captcha. 
					 * The most likely error is that the user has disabled Cookies and therefore
					 * we can't track his session and get the captcha.
					 */
					request.setAttribute("error", "Please enable cookies in your browser for the system to work.");
					getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
					return;					
				} else if (!captchaAnswer.isValid()){
					// entered code is false, send user back
					request.setAttribute("error",captchaAnswer.getErrorMessage()); // TODO: is it possible to get a localized message?
					getServletConfig().getServletContext().getRequestDispatcher("/join_group").forward(request, response);
					return;
				}


				// get id and admin-email of group the user wants to join
				stmt = conn.prepareStatement("SELECT g.group, u.user_email FROM groupids g, user u WHERE u.user_name = ? AND g.group_name = u.user_name");
				stmt.setString(1, requGroup);
				rst = stmt.executeQuery();

				if (rst.next()) {
					int groupid       = rst.getInt("group");
					String group_mail = rst.getString("user_email");

					// check if user is not already in group
					stmt = conn.prepareStatement("SELECT user_name FROM groups WHERE user_name = ? AND `group` = ?");
					stmt.setString(1, currUser);
					stmt.setInt(2, groupid);
					rst = stmt.executeQuery();

					if (!rst.next()) {
						/* TODO: implement this cleaner (i.e. have spammer status in session, similar to currUser)
						 * user is not already in group --> check, if it is a spammer
						 */
						stmt = conn.prepareStatement("SELECT spammer FROM user WHERE user_name = ? AND spammer = 0");
						stmt.setString(1, currUser);
						rst = stmt.executeQuery();

						if (rst.next()) {
							/*
							 * spammer = 0 ... no spammer!
							 */
							log.info("user " + currUser + " tries to join group " + requGroup); 

							/*
							 * get reason for joining this group and shorten it
							 */
							String reason = request.getParameter("reason");
							if (reason.length() > MAX_REASON_LENGTH) {
								reason = reason.substring(0, MAX_REASON_LENGTH);
							}

							// send mail to groupadmin
							String message = "\nHello " + requGroup + "\n" +
							"\n" +currUser+" has send a request to join your group." + "\n" +
							"\nReason: " + reason + "\n" + 
							"\nVisit " + projectHome + "settings?requGroup="+URLEncoder.encode(requGroup, "UTF-8").toLowerCase()+"&user="+URLEncoder.encode(currUser, "UTF-8").toLowerCase()+"&selTab=3"+
							"\n"+
							"\nHave a look on the help page and the FAQ:" +
							"\n" + projectHome + "help" +
							"\n" + projectHome + "faq" + "\n" +
							"\nNews regarding " + projectName + " can be found in our blog:" +
							"\nhttp://blog.bibsonomy.org" +
							"\n" +
							"\nReplies to this e-mail address are deleted, please send questions to webmaster@" + projectName.toLowerCase() + ".org." + "\n";

							mail.sendMail(new String[] {group_mail},  "User join request for " + requGroup , message, "groups@" + projectName.toLowerCase() + ".org");
							mail.sendMail(new String[] {"register@bibsonomy.org"},  "User join request for " + requGroup , message, "groups@" + projectName.toLowerCase() + ".org");

							request.setAttribute("success", "Your join request has been noticed.");
							getServletConfig().getServletContext().getRequestDispatcher("/success.jsp").forward(request, response);
						} else {
							log.warn("user " + currUser + " (spammer!) tries to join group " + requGroup);
							request.setAttribute("error", "Joining this group is not possible.");
							getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
						}
					} else {
						request.setAttribute("error", "You're already a member of this group.");
						getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
					}
				}
			} else {
				request.setAttribute("error", "Not enough parameters given.");
				getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
			}

		} catch (SQLException e) {
			log.fatal("Error in JoinGroupHandler: " + e);
			response.sendRedirect("/errors/databaseError.jsp");
		} catch (MessagingException e) {
			log.fatal("Could not send join-/cancel-request mail: " + e);
			request.setAttribute("error", "Could not handle your request. Please try again later.");
			getServletConfig().getServletContext().getRequestDispatcher("/errors/error.jsp").forward(request, response);
		}

		finally {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (rst  != null) {try {rst.close(); } catch (SQLException e) {}rst  = null;}
			if (stmt != null) {try {stmt.close();} catch (SQLException e) {}stmt = null;}
			if (conn != null) {try {conn.close();} catch (SQLException e) {}conn = null;}
		}
	}	
}
