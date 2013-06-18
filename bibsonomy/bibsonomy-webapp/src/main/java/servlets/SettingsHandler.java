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

/*
 * SettingsHandler is used by processSettings.jsp 
 * to check the identity of users, which are trying
 * to update their list of friends. Every username,
 * which is used on the list is also checked in advance.
 *
 */
package servlets;

import helpers.constants;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.database.systemstags.search.NetworkRelationSystemTag;
import org.bibsonomy.database.systemstags.search.UserRelationSystemTag;
import org.bibsonomy.model.User;
import org.bibsonomy.util.ValidationUtils;
import org.bibsonomy.util.spring.security.AuthenticationUtils;

import filters.ActionValidationFilter;

/**
 * @author Serak
 */
@Deprecated
public class SettingsHandler extends HttpServlet{

	private static final long serialVersionUID = 4051324539558769200L;
	private DataSource dataSource;
	private static final Log log = LogFactory.getLog(SettingsHandler.class);

	@Override
	public void init(final ServletConfig config) throws ServletException{	
		super.init(config); 
		try{
			final Context initContext = new InitialContext();
			final Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/bibsonomy");
		} catch (final NamingException ex) {
			throw new ServletException("Cannot retrieve java:/comp/env/bibsonomy",ex);
		}
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		Connection conn         = null;
		ResultSet rst           = null;
		PreparedStatement stmtP = null;
		
		final User user = AuthenticationUtils.getUser();
		final String currUser = user.getName(); 

		/*
		 * TODO: Fehlerbehandlung fehlt total (User existiert nicht, is schon in Gruppe/Freund, etc.
		 * 
		 */

		// authenticate User
		if (currUser == null) {
			response.sendRedirect("/login?referer=/settings");
			return;
		}

		// check credential TODO: make this more general, clean it!
		if (!ActionValidationFilter.isValidCkey(request)) {
			response.sendRedirect("/login?referer=/settings");
			return;
		}

		try {
			synchronized(dataSource) {
				if(dataSource != null){
					conn = dataSource.getConnection();
				} else {
					throw new Exception("No Datasource");
				}
			}
			try{
				conn.setAutoCommit(false);    // deactivate auto-commit to enable transaction
				String friend = null;
				String relationTag = null;

				/*
				 * remove a friend
				 */
				friend = request.getParameter("del_friend");
				relationTag = request.getParameter("del_relationTag");
				if (!ValidationUtils.present(relationTag)) {
					relationTag = NetworkRelationSystemTag.BibSonomyFriendSystemTag;
				} else {
					relationTag = SystemTagsUtil.buildSystemTagString(UserRelationSystemTag.NAME, relationTag);
				}
				if (friend != null) {
					// logging
					stmtP = conn.prepareStatement("INSERT INTO log_friends (friends_id, user_name, f_user_name, tag_name, f_network_user_id, friendship_date) SELECT * FROM friends WHERE user_name = ? AND f_user_name = ? AND tag_name = ?");
					stmtP.setString(1, currUser);
					stmtP.setString(2, friend);
					stmtP.setString(3, relationTag);
					stmtP.executeUpdate();
					// end friendship
					stmtP = conn.prepareStatement("DELETE FROM friends WHERE user_name = ? AND f_user_name = ? AND tag_name = ?");
					stmtP.setString(1, currUser);
					stmtP.setString(2, friend);
					stmtP.setString(3, relationTag);
					stmtP.executeUpdate();
				}
				/*
				 * add a friend
				 */
				friend = request.getParameter("add_friend");
				relationTag = request.getParameter("add_relationTag");
				if (!ValidationUtils.present(relationTag)) {
					relationTag = NetworkRelationSystemTag.BibSonomyFriendSystemTag;
				} else {
					relationTag = SystemTagsUtil.buildSystemTagString(UserRelationSystemTag.NAME, relationTag);
				}
				// don't be friend with yourself!
				if (friend != null && !friend.equalsIgnoreCase(currUser)) {
					// check, if username exists
					stmtP = conn.prepareStatement ("SELECT user_name FROM user WHERE user_name = ?");
					stmtP.setString(1, friend);
					rst = stmtP.executeQuery();
					if (rst.next()) {
						// user exists --> check if they're already friends
						stmtP = conn.prepareStatement ("SELECT f_user_name FROM friends WHERE user_name = ? AND f_user_name = ? AND tag_name = ?");
						stmtP.setString(1, currUser);
						stmtP.setString(2, friend);
						stmtP.setString(3, relationTag);
						rst = stmtP.executeQuery();
						if (!rst.next()) {
							// they're not friends: add the friend
							stmtP = conn.prepareStatement("INSERT INTO friends (user_name, f_user_name, tag_name, friendship_date) VALUES (?,?,?,?)");
							stmtP.setString(1, currUser);
							stmtP.setString(2, friend);
							stmtP.setString(3, relationTag);
							stmtP.setTimestamp(4, new Timestamp(new Date().getTime()));
							stmtP.executeUpdate();
						}
					}
				}	
				/*
				 * add a user to users group
				 */
				final String addGroupUser = request.getParameter("add_group_user");
				if (addGroupUser != null) {
					addUserToGroup(addGroupUser, currUser, stmtP, rst, conn);
				}

				/*
				 * del a user from users group
				 */
				friend = request.getParameter("del_group_user");
				if (friend != null && !friend.equalsIgnoreCase(currUser)) {
					// check, if user is owner of group and get groupid 
					stmtP = conn.prepareStatement("SELECT i.group FROM groups g, groupids i WHERE g.user_name = ? AND i.group_name = ? AND g.group = i.group");
					stmtP.setString(1, currUser);
					stmtP.setString(2, currUser);
					rst = stmtP.executeQuery();
					if (rst.next()) {
						// user is admin of this group
						final int groupid = rst.getInt(1);
						// logging
						stmtP = conn.prepareStatement("INSERT INTO log_groups (`user_name`, `group`, `defaultgroup`, `start_date`, `user_status`) SELECT g.user_name, g.group, g.defaultgroup, g.start_date, g.user_status FROM groups g WHERE g.user_name = ? AND g.group = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();						
						// remove user from group
						stmtP = conn.prepareStatement("DELETE FROM groups WHERE user_name = ? AND `group` = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();
						// update tas table
						stmtP = conn.prepareStatement("UPDATE tas b SET b.group = " + constants.SQL_CONST_GROUP_PRIVATE + " WHERE b.user_name = ? AND b.group = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();
						// log bibtex
						stmtP = conn.prepareStatement("INSERT INTO log_bibtex (content_id, `group`, user_name) SELECT content_id, `group`, user_name FROM bibtex WHERE user_name = ? AND `group` = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();												
						// update bibtex table
						stmtP = conn.prepareStatement("UPDATE bibtex b SET b.group = " + constants.SQL_CONST_GROUP_PRIVATE + " WHERE b.user_name = ? AND b.group = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();						
						// log bookmark
						/*
						 * FIXME: this statement forgets to insert some columns which are defined to be NOT NULL and thus breaks this functionality!
						 */
						stmtP = conn.prepareStatement("INSERT INTO log_bookmark (content_id, `group`, user_name) SELECT content_id, `group`, user_name FROM bookmark WHERE user_name = ? AND `group` = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();												
						// update bookmark table
						stmtP = conn.prepareStatement("UPDATE bookmark b SET b.group = " + constants.SQL_CONST_GROUP_PRIVATE + " WHERE b.user_name = ? AND b.group = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();
						
						/*
						 * discussion for the group
						 */	
						// log discussion
						stmtP = conn.prepareStatement("INSERT INTO log_discussion (discussion_id, interHash, user_name, text, rating, date, change_date, anonymous, parent_hash, hash, type, `group`) SELECT discussion_id, interHash, user_name, text, rating, date, change_date, anonymous, parent_hash, hash, type, `group` FROM discussion WHERE user_name = ? AND `group` = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();
						/*
						 * FIXME: here we assume that a discussion item has only
						 * one group this is currently ok because we currently
						 * don't support more than one group
						 */
						// update discussion table
						stmtP = conn.prepareStatement("UPDATE discussion d SET d.group = " + constants.SQL_CONST_GROUP_PRIVATE + " WHERE d.user_name = ? AND d.group = ?");
						stmtP.setString(1, friend);
						stmtP.setInt(2, groupid);
						stmtP.executeUpdate();
					}
				}
				
				conn.commit();
				final String referer = request.getHeader("Referer");
				response.sendRedirect(referer != null ? referer : "/settings");

			} catch(final SQLException e) {
				conn.rollback();     // rollback all queries
				log.fatal("Could not change settings for user " + currUser + ".", e);
				getServletConfig().getServletContext().getRequestDispatcher("/errors/databaseError.jsp").forward(request, response);
			}       
		} catch (final Exception e) {
			log.fatal(e);
			response.sendRedirect("/errors/databaseError.jsp");
		} finally {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (rst   != null) { try {	rst.close();	} catch (final SQLException e) {}	rst   = null;}
			if (stmtP != null) { try {	stmtP.close();	} catch (final SQLException e) {}	stmtP = null;}
			if (conn  != null) { try {	conn.close();	} catch (final SQLException e) {}	conn  = null;}
		}
	}
	
	private static boolean addUserToGroup (String user, final String group, PreparedStatement stm, ResultSet rst, final Connection conn) throws SQLException {
		// friend: user to be added
		if (user != null) {
			user = user.toLowerCase();
			// check, if username exists
			stm = conn.prepareStatement ("SELECT user_name FROM user WHERE user_name = ?");
			stm.setString(1, user);
			rst = stm.executeQuery();
			if (rst.next()) {
				// check, if user is owner of group and get groupid and default group to use for copying
				stm = conn.prepareStatement("SELECT i.group,g.defaultgroup FROM groups g, groupids i WHERE g.user_name = ? AND i.group_name = ? AND g.group = i.group");
				stm.setString(1, group);
				stm.setString(2, group);
				rst = stm.executeQuery();
				if (rst.next()) {
					final int groupid = rst.getInt(1);
					final int defaultgroup = rst.getInt(2);
					// user is admin of this group --> check, if group_user is already in group
					stm = conn.prepareStatement ("SELECT g.user_name FROM groups g WHERE g.user_name = ? AND g.group = ?");
					stm.setString(1, user);
					stm.setInt(2, groupid);
					rst = stm.executeQuery();
					if (!rst.next()) {
						// group_user is not in group --> add it
						stm = conn.prepareStatement("INSERT INTO groups (user_name, `group`, defaultgroup) VALUES (?,?,?)");
						stm.setString(1, user);
						stm.setInt(2, groupid);
						stm.setInt(3,defaultgroup);
						return (stm.executeUpdate() == 1);
						// send E-Mail to added user
					}
				} 
			}
		}
		return false;
	}

}
