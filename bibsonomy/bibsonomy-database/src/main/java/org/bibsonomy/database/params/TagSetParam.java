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

/**
 * @author mwa
 * @version $Id: TagSetParam.java,v 1.3 2010-04-09 09:04:39 nosebrain Exp $
 */
public class TagSetParam {
	private String setName;
	private String tagName;
	private int groupId;
	
	/** Get the name of the tag set. 
	 * Each tag set has a name which uniquely identifies it among the groups tagsets.
	 * 
	 * @return The name of this tag set.
	 */
	public String getSetName() {
		return this.setName;
	}
	
	/** Set the name of the tag set.
	 * Each tag set has a name which uniquely identifies it among the groups tagsets.
	 * @param setName
	 */
	public void setSetName(String setName) {
		this.setName = setName;
	}
	
	/** Get the name of the tag associated with this tag set.
	 * @return The name of the tag.
	 */
	public String getTagName() {
		return this.tagName;
	}
	
	/** Set the name of the tag associated with this tag set. 
	 * @param tagName
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	/**
	 * @return The id of the group this tag set belongs to.
	 */
	public int getGroupId() {
		return this.groupId;
	}
	
	/**
	 * @param groupId
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
