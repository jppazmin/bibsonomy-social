/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.managers;

import java.util.Date;
import java.util.List;

import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.WikiParam;
import org.bibsonomy.model.Wiki;

/**
 * @author philipp
 * @version $Id: WikiDatabaseManager.java,v 1.4 2011-05-09 12:08:43 nosebrain Exp $
 */
public class WikiDatabaseManager extends AbstractDatabaseManager {
    private static final WikiDatabaseManager singleton = new WikiDatabaseManager();

    /**
     * @return UserDatabaseManager
     */
    public static WikiDatabaseManager getInstance() {
	return singleton;
    }

    private WikiDatabaseManager() {
    }

    public Wiki getActualWiki(final String userName, final DBSession session) {
	return this.queryForObject("getActualWikiForUser", userName, Wiki.class, session);
    }

    public List<Date> getWikiVersions(final String userName, final DBSession session) {
	return this.queryForList("getWikiVersionsForUser", userName, Date.class, session);
    }

    public void updateWiki(final String userName, final Wiki wiki, final DBSession session) {
	final WikiParam param = new WikiParam();
	param.setUserName(userName);
	param.setWikiText(wiki.getWikiText());
	param.setDate(new Date());
	
	this.update("updateWikiForUser", param, session);
    }

    public Wiki getPreviousWiki(final String userName, final Date date, final DBSession session) {
	final WikiParam param = new WikiParam();

	param.setDate(date);
	param.setUserName(userName);

	return this.queryForObject("getLoggedWiki", param, Wiki.class, session);
    }

    public void createWiki(final String userName, final Wiki wiki, final DBSession session) {
	session.beginTransaction();
	
	final WikiParam param = new WikiParam();
	param.setUserName(userName);
	param.setWikiText(wiki.getWikiText());
	param.setDate(new Date());
	
	try {
	    this.insert("insertWiki", param, session);
	    session.commitTransaction();
	} finally {
	    session.endTransaction();
	}
    }

    public void logWiki(final String userName, final Wiki wiki, final DBSession session) {
	session.beginTransaction();
	
	final WikiParam param = new WikiParam();
	param.setUserName(userName);
	param.setWikiText(wiki.getWikiText());
	/*
	 * FIXME: shouldn't we have an (additional) logging date here?
	 * 
	 * I.e., 
	 * date = wiki.getDate()
	 * logDate = new Date()
	 * 
	 */
	param.setDate(new Date());
	
	try {
	    this.insert("logWiki", param, session);
	    session.commitTransaction();
	} finally {
	    session.endTransaction();
	}
    }
}
