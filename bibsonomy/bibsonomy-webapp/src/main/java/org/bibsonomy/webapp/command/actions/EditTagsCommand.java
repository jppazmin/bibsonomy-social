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

package org.bibsonomy.webapp.command.actions;



/**
 * @author philipp
 * @version $Id: EditTagsCommand.java,v 1.1 2010-05-28 13:50:45 econ11 Exp $
 */
public class EditTagsCommand {
	
	private String delTags = "";
	
	private String addTags = "";
	
	private boolean updateRelations = false;

	/**
	 * @param delTags the delTags to set
	 */
	public void setDelTags(String delTags) {
		this.delTags = delTags;
	}

	/**
	 * @return the delTags
	 */
	public String getDelTags() {
		return delTags;
	}

	/**
	 * @param addTags the addTags to set
	 */
	public void setAddTags(String addTags) {
		this.addTags = addTags;
	}

	/**
	 * @return the addTags
	 */
	public String getAddTags() {
		return addTags;
	}

	/**
	 * @param updateRelations the updateRelations to set
	 */
	public void setUpdateRelations(boolean updateRelations) {
		this.updateRelations = updateRelations;
	}

	/**
	 * @return the updateRelations
	 */
	public boolean isUpdateRelations() {
		return updateRelations;
	}

}
