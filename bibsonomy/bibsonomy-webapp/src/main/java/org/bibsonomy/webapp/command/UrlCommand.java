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

package org.bibsonomy.webapp.command;

/**
 * 
 * Bean for providing the Url
 * 
 * @author Flori
 * @version $Id: UrlCommand.java,v 1.3 2008-10-13 11:30:11 ss05mwagner Exp $
 */
public class UrlCommand extends SimpleResourceViewCommand{
	
	/** String to search for requUrl */
	private String requUrl = "";
	
	/** String to search for requUrlHash */
	private String requUrlHash = "";

	/**
	 * @return the requested url 
	 */
	public String getRequUrl() {
		return this.requUrl;
	}

	/**
	 * @param requUrl set the url 
	 */
	public void setRequUrl(String requUrl) {
		this.requUrl = requUrl;
	}

	/**
	 * @return the requested url hash
	 */
	public String getRequUrlHash() {
		return this.requUrlHash;
	}

	/**
	 * @param requUrlHash set the url as hash 
	 */
	public void setRequUrlHash(String requUrlHash) {
		this.requUrlHash = requUrlHash;
	}
}
