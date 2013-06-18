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

package helpers.database;

import helpers.constants;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import beans.AdminBean;


/**
 * Provides methods only available for admins (like handling spammers, groups, api keys).
 * 
 * @author rja
 * @version $Id: DBAdminManager.java,v 1.14 2010-07-02 09:29:47 nosebrain Exp $
 */
@Deprecated
public class DBAdminManager extends DBManager {
	
	/*
	 * gets settings for this user and saves them in bean
	 */
	public static void addGroupToSystem (AdminBean bean) {
		final DBContext c = new DBContext();
		try {
			if (bean.getUser() != null && c.init()) { // initialize database
				final String user = bean.getUser().trim();
				// get next group id
				c.conn.setAutoCommit(false); // we do this in a transaction
				c.stmt = c.conn.prepareStatement("SELECT MAX(`group`) + 1 AS id FROM groupids");
				c.rst  = c.stmt.executeQuery();
				if (c.rst.next()) {
					int next_groupid = c.rst.getInt("id");
					// check, if user name exists
					c.stmt = c.conn.prepareStatement("SELECT user_name FROM user WHERE user_name = ?");
					c.stmt.setString(1, user);
					c.rst  = c.stmt.executeQuery();
					if (c.rst.next()) {
						// user exists, check, if group exists
						c.stmt = c.conn.prepareStatement("SELECT group_name FROM groupids WHERE group_name = ?");
						c.stmt.setString(1, user);
						c.rst  = c.stmt.executeQuery();
						if (c.rst.next()) {
							// group already exists --> do nothing
							bean.addError("This group already exists.");
						} else {
							// group does not exists --> add it
							c.stmt = c.conn.prepareStatement("INSERT INTO groupids (`group_name`, `group`, privlevel) VALUES (?,?,?)");
							c.stmt.setString(1, user);
							c.stmt.setInt(2, next_groupid);
							c.stmt.setInt(3, bean.getPrivlevel());
							c.stmt.executeUpdate();
							c.stmt = c.conn.prepareStatement("INSERT INTO groups (`user_name`, `group`, `defaultgroup`) VALUES (?,?,?)");
							c.stmt.setString(1, user);
							c.stmt.setInt(2, next_groupid);
							c.stmt.setInt(3, next_groupid);
							c.stmt.executeUpdate();
							bean.addInfo("Added group with id " + next_groupid + " to the system.");
						}
					} else {
						bean.addError("This user does not exist.");
					}
				}
				c.conn.commit(); // commit transaction
			}
		} catch (SQLException e) {
			try {
				c.conn.rollback();
			} catch (SQLException f) {
				/*
				 * TODO: first attempt to do logging when exceptions are thrown - code "stolen" from Jens'
				 * Database backend classes
				 */
				final Log log = LogFactory.getLog(DBAdminManager.class);
				log.fatal("could not roll transaction back " + e.getMessage());
			}
			bean.addError("Sorry, an error occured: " + e);
		} finally {
			c.close(); // close database connection
		}

	}

	/**
	 * add a user to the negative spammerlist. So he is marked NOT as a spammer and will not appear longer in any suggestion list
	 * @param bean the AdminBean reference
	 */
	public static void removeUserFromSpammerlist(AdminBean bean) {
		DBContext c = new DBContext();

		try {
			if (c.init()) {
				c.stmt = c.conn.prepareStatement("UPDATE user " +
						                         "  SET " +
						                         "    spammer_suggest = 0, " +
						                         "    updated_by = ?, " +
						                         "    updated_at = ?, " +
						                         "    to_classify = " + constants.SQL_CONST_TO_CLASSIFY_FALSE + 						                         
						                         "  WHERE user_name = ?");
				c.stmt.setString(1, bean.getCurrUser());
				c.stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
				c.stmt.setString(3, bean.getUser());

				if(c.stmt.executeUpdate() == 1) {
					bean.addInfo("user '" + bean.getUser() + "' was removed from spammer suggestion list.");
				} else {
					bean.addError("user '" + bean.getUser() + "' could not be removed from the list. The user was not found.");
				}
			}
		} catch (SQLException e) {		
			bean.addError("Sorry, an error occured: " + e);
		}		
	}


	/**  
	 * add or remove a tagname on the 'blacklist' of tags used by spammers (
	 * or add a clean tag  to the list which won't be listed in the recommendation list in future
	 * 
	 * @param bean the AdminBean reference
	 * @param flag 
	 * 		<code>true</code>: flag tag as spammertag
	 * 		<code>false</code>: remove tag from spammertag list
	 * @param type
	 * 		if <code>0</code> tag is added to negative spammertag list so it is no longer in the suggestions lists
	 * 		
	 */
	public static void flagSpammerTag(AdminBean bean, boolean flag, int type) {
		DBContext c = new DBContext();

		try {
			if (c.init()) {

				// remove tag from list
				if (!flag) {				
					c.stmt = c.conn.prepareStatement("DELETE FROM spammer_tags WHERE tag_name = ?");				
					c.stmt.setString(1, bean.getTag());				
					if (c.stmt.executeUpdate() == 1) {					
						bean.addInfo("tag '" + bean.getTag() + "' removed from list.");
					} else {
						bean.addError("tag '" + bean.getTag() + "' could not be removed. It was not found in the list.");
					}				
				} else {  // add tag to list (1 = spammertag, 0 = clean tag from suggestion list)
					c.stmt = c.conn.prepareStatement("INSERT INTO spammer_tags (tag_name,spammer) VALUES (?,?)");
					c.stmt.setString(1, bean.getTag());
					c.stmt.setInt(2, type);
					if (c.stmt.executeUpdate() == 1) {
						if (type == 1) 
							bean.addInfo("tag '" + bean.getTag() + "' was added to the list.");
						else
							bean.addInfo("tag '" + bean.getTag() + "' was removed from recommendation list.");
					} else {
						if (type == 1)
							bean.addError("tag '" + bean.getTag() + "' is already in the list.");					
					}
				}		
			}
		} catch (SQLException e) {			
			bean.addError("Sorry, an error occured: " + e);
		}
	}

}