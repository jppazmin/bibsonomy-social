/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model;

import java.io.Serializable;
import java.util.TimeZone;

import org.bibsonomy.common.enums.ProfilePrivlevel;

/**
 * Holds settings for a user.
 * 
 * @version $Id: UserSettings.java,v 1.31 2011-06-16 11:37:23 nosebrain Exp $
 */
public class UserSettings implements Serializable {
	private static final long serialVersionUID = 501200873739971813L;

	/**
	 * the profile privacy level
	 */
	private ProfilePrivlevel profilePrivlevel = ProfilePrivlevel.PUBLIC;
	
	/**
	 * tagbox style; 0 = cloud, 1 = list
	 */
	private int tagboxStyle = 0;

	/**
	 * sorting of tag box; 0 = alph, 1 = freq
	 */
	private int tagboxSort = 0;

	/**
	 * minimum frequency for tags to be displayed in tag box
	 */
	private int tagboxMinfreq = 0;

	/**
	 * top x posts shown in the tag box
	 */
	private int tagboxMaxCount = 50;
	
	/**
	 * Show the tooltips for tags in the tag cloud? 0 = don't show, 1 = show 
	 */
	private int tagboxTooltip = 0;

	/**
	 * number of list items per page; how many posts to show in post lists
	 */
	private int listItemcount = 20;

	private boolean showBookmark = true;
	
	private boolean showBibtex = true;
	
	private boolean simpleInterface = true;
	
	/**
	 * the default language for i18n
	 */
	private String defaultLanguage;
	
	/**
	 * The timeZone the user lives in. Used for rendering posts in the HTML 
	 * output. 
	 * FIXME: let user choose on the /settings page. FIXME: then we must store
	 * UTC times in the database!
	 * 
	 * FIXME: what to do with non-logged in users? They must have a valid
	 * time zone, too! Otherwise, we will get NPEs
	 */
	private final TimeZone timeZone = TimeZone.getDefault();
	
	/**
	 * How much data about the user behavior (clicking, etc.) is logged.
	 * 
	 * 0 = yes (log clicks to external pages)
	 * 1 = no  (don't log clicks to external pages)
	 */
	private int logLevel;

	/**
	 * Shall the web interface ask the user before it really deletes something?
	 */
	private boolean confirmDelete;
	
	/**
	 * User wants maxCount (true) or maxFreq (false)
	 */
	private boolean isMaxCount = true;
	
	/**
	 * @return tagboxStyle
	 */
	public int getTagboxStyle() {
		return this.tagboxStyle;
	}

	/**
	 * @param tagboxStyle
	 */
	public void setTagboxStyle(int tagboxStyle) {
		this.tagboxStyle = tagboxStyle;
	}

	/**
	 * @return tagboxSort
	 */
	public int getTagboxSort() {
		return this.tagboxSort;
	}

	/**
	 * @param tagboxSort
	 */
	public void setTagboxSort(int tagboxSort) {
		this.tagboxSort = tagboxSort;
	}

	/**
	 * @return tagboxMinfreq
	 */
	public int getTagboxMinfreq() {
		return this.tagboxMinfreq;
	}

	/**
	 * @param tagboxMinfreq
	 */
	public void setTagboxMinfreq(int tagboxMinfreq) {
		this.tagboxMinfreq = tagboxMinfreq;
	}

	/**
	 * @param tagboxMaxCount
	 */
	public void setTagboxMaxCount(int tagboxMaxCount) {
		this.tagboxMaxCount = tagboxMaxCount;
	}
	
	/**
	 * @return tagboxMaxCount
	 */
	public int getTagboxMaxCount() {
		return tagboxMaxCount;
	}

	/**
	 * @return isMaxCount
	 */
	public boolean getIsMaxCount() {
		return isMaxCount;
	}

	/**
	 * @param isMaxCount
	 */
	public void setIsMaxCount(boolean isMaxCount) {
		this.isMaxCount = isMaxCount;
	}

	/**
	 * @return tagboxTooltip
	 */
	public int getTagboxTooltip() {
		return this.tagboxTooltip;
	}

	/**
	 * @param tagboxTooltip
	 */
	public void setTagboxTooltip(int tagboxTooltip) {
		this.tagboxTooltip = tagboxTooltip;
	}

	/**
	 * @return listItemcount
	 */
	public int getListItemcount() {
		return this.listItemcount;
	}

	/**
	 * @param listItemcount
	 */
	public void setListItemcount(int listItemcount) {
		this.listItemcount = listItemcount;
	}

	/**
	 * @return the default language
	 */
	public String getDefaultLanguage() {
		return this.defaultLanguage;
	}

	/**
	 * @param defaultLanguage the default language
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}
	
	/**
	 * @return the logLevel
	 */
	public int getLogLevel() {
		return this.logLevel;
	}

	/**
	 * gets currents confirmation status
	 * @return boolean 
	 */
	public boolean isConfirmDelete() {
		return this.confirmDelete;
	}

	/**
	 * sets the current confirmations status
	 * @param confirmDelete
	 */
	public void setConfirmDelete(boolean confirmDelete) {
		this.confirmDelete = confirmDelete;
	}	
	
	/**
	 * retrieves the current confirmation status
	 * @return boolean
	 */
	public boolean getConfirmDelete() {
		return this.confirmDelete;
	}

	/**
	 * @param profilePrivlevel the profilePrivlevel to set
	 */
	public void setProfilePrivlevel(final ProfilePrivlevel profilePrivlevel) {
		this.profilePrivlevel = profilePrivlevel;
	}

	/**
	 * @return the profilePrivlevel
	 */
	public ProfilePrivlevel getProfilePrivlevel() {
		return profilePrivlevel;
	}
	/**
	 * @return the showBookmark
	 */
	public boolean isShowBookmark() {
		return this.showBookmark;
	}

	/**
	 * @param showBookmark the showBookmark to set
	 */
	public void setShowBookmark(boolean showBookmark) {
		this.showBookmark = showBookmark;
	}

	/**
	 * @return the showPublication
	 */
	public boolean isShowBibtex() {
		return this.showBibtex;
	}

	/**
	 * @param showPublication the showPublication to set
	 */
	public void setShowBibtex(boolean showPublication) {
		this.showBibtex = showPublication;
	}

	/**
	 * @return the simpleInterface
	 */
	public boolean isSimpleInterface() {
		return this.simpleInterface;
	}

	/**
	 * @param simpleInterface the simpleInterface to set
	 */
	public void setSimpleInterface(boolean simpleInterface) {
		this.simpleInterface = simpleInterface;
	}

	/**
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return this.timeZone;
	}
}