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

import java.util.Map;

import org.bibsonomy.webapp.command.TagResourceViewCommand;


/**
 * @author dzo
 * @version $Id: BatchEditCommand.java,v 1.11 2010-11-17 11:00:53 nosebrain Exp $
 */
public class BatchEditCommand extends TagResourceViewCommand {

	/**
	 * should publications be edited before they're stored? 
	 */
	private boolean editBeforeImport = false;
	/**
	 * this flag determines, whether the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	private boolean deleteCheckedPosts;	
	/**
	 * when batchedit is used after importing posts, this flag
	 * stores if the user wants to overwrite existing posts 
	 */
	private boolean overwrite;
	/**
	 * these tags will be added to all resources
	 */
	private String tags;
	/** 
	 * old tags of the resources; resource hashes as keys of the map and old tags as values
	 */
	private Map<String, String> oldTags;
	/** 
	 * newTags of the resources; resource hashes as keys of the map [hash = new tags], ...
	 */
	private Map<String, String> newTags;
	/**
	 * hashes of the resources which will be deleted (hash as key and "on" as value (checkbox))
	 */
	private Map<String, Boolean> delete;
	
	/**
	 * @return the flag that determines, weather the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	public boolean getDeleteCheckedPosts() {
		return this.deleteCheckedPosts;
	}

	/**
	 * @return the flag that determines, weather the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	public boolean isDeleteCheckedPosts() {
		return this.deleteCheckedPosts;
	}
	
	/**
	 * @param deleteCheckedPosts the flag that determines, weather the dialogue called was configured to 
	 * edit(delete) or edit(create) existing posts.
	 */
	public void setDeleteCheckedPosts(final boolean deleteCheckedPosts) {
		this.deleteCheckedPosts = deleteCheckedPosts;
	}
	
	/**
	 * @return the tags
	 */
	public String getTags() {
		return this.tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(final String tags) {
		this.tags = tags;
	}

	/**
	 * @return the oldTags
	 */
	public Map<String, String> getOldTags() {
		return this.oldTags;
	}

	/**
	 * @param oldTags the oldTags to set
	 */
	public void setOldTags(final Map<String, String> oldTags) {
		this.oldTags = oldTags;
	}

	/**
	 * @return the newTags
	 */
	public Map<String, String> getNewTags() {
		return this.newTags;
	}

	/**
	 * @param newTags the newTags to set
	 */
	public void setNewTags(final Map<String, String> newTags) {
		this.newTags = newTags;
	}

	/**
	 * @return the delete
	 */
	public Map<String, Boolean> getDelete() {
		return this.delete;
	}

	/**
	 * @param delete the delete to set
	 */
	public void setDelete(final Map<String, Boolean> delete) {
		this.delete = delete;
	}

	/**
	 * @return the editBeforeImport
	 */
	public boolean isEditBeforeImport() {
		return this.editBeforeImport;
	}

	/**
	 * @param editBeforeImport the editBeforeImport to set
	 */
	public void setEditBeforeImport(final boolean editBeforeImport) {
		this.editBeforeImport = editBeforeImport;
	}

	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return this.overwrite;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public void setOverwrite(final boolean overwrite) {
		this.overwrite = overwrite;
	}
}
