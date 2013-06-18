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

package org.bibsonomy.webapp.command.ajax;


/**
 * Command for ajax requests from admin page
 * 
 * @author Stefan Stützer
 * @version $Id: LoggingCommand.java,v 1.1 2010-04-28 15:30:31 nosebrain Exp $
 */
public class LoggingCommand extends AjaxCommand {
	
	/** user for which we want to add a group or mark as spammer */
	private String userName;


	/** path in dom, backwards */
	private String dompath;

	/** page url */
	private String pageurl;

	/** anchor title */
	private String atitle;

	/** anchor hyper-reference */
	private String ahref;

	/** referer */
	private String referer;

	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @return the dompath
	 */
	public String getDompath() {
		return this.dompath;
	}

	/**
	 * @param dompath the dompath to set
	 */
	public void setDompath(final String dompath) {
		this.dompath = dompath;
	}

	/**
	 * @return the pageurl
	 */
	public String getPageurl() {
		return this.pageurl;
	}

	/**
	 * @param pageurl the pageurl to set
	 */
	public void setPageurl(final String pageurl) {
		this.pageurl = pageurl;
	}

	/**
	 * @return the atitle
	 */
	public String getAtitle() {
		return this.atitle;
	}

	/**
	 * @param atitle the atitle to set
	 */
	public void setAtitle(final String atitle) {
		this.atitle = atitle;
	}

	/**
	 * @return the ahref
	 */
	public String getAhref() {
		return this.ahref;
	}

	/**
	 * @param ahref the ahref to set
	 */
	public void setAhref(final String ahref) {
		this.ahref = ahref;
	}

}