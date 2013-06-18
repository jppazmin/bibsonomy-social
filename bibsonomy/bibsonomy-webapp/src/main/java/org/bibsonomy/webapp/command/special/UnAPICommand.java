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

package org.bibsonomy.webapp.command.special;

import org.bibsonomy.webapp.command.BaseCommand;

/**
 * 
 * The data of a UnAPI request. See <a href="http://unapi.info">UnAPI.info</a>.
 * 
 * Represents a post (by an id) in a certain format.
 * 
 * @author rja
 * @version $Id: UnAPICommand.java,v 1.1 2009-01-12 13:03:51 rja Exp $
 */
public class UnAPICommand extends BaseCommand {

	private String id;
	private String format;
	
	/**
	 * @return The requested id of the post.
	 */
	public String getId() {
		return this.id;
	}
	/** 
	 * @param id - the id of the post.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return The requested format.
	 */
	public String getFormat() {
		return this.format;
	}
	/**
	 * @param format - the format the post should be returned.
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
}
