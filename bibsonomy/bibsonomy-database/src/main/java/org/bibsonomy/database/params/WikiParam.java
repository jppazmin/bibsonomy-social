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

package org.bibsonomy.database.params;

import java.util.Date;

/**
 * @author philipp
 * @version $Id: WikiParam.java,v 1.2 2011-06-16 13:55:00 nosebrain Exp $
 */
public class WikiParam {
    
    private String userName;
    
    private String wikiText;
    
    private Date date;

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
	this.userName = userName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
	return userName;
    }

    /**
     * @param wikiText the wiki to set
     */
    public void setWikiText(String wikiText) {
	this.wikiText = wikiText;
    }

    /**
     * @return the wiki
     */
    public String getWikiText() {
	return wikiText;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * @return the date
     */
    public Date getDate() {
	return date;
    }

}
