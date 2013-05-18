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

package org.bibsonomy.database.systemstags;

/**
 * @author Andreas Koch
 * @version $Id: SystemTag.java,v 1.15 2011-03-14 15:23:37 doerfel Exp $ 
 */
public interface SystemTag {
	
	/**
	 * @return the argument
	 */
	public String getArgument();
	
	/**
	 * @param argument the argument to set
	 */
	public void setArgument(String argument);
	
	/**
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Returns true if the tagName belongs to an instance of the SystemTag
	 * @param tagName
	 * @return
	 */
	public boolean isInstance(String tagName);
	
	/**
	 * Returns true if it should be hidden from tag clouds and posts
	 * @return
	 */
	public boolean isToHide();
}
