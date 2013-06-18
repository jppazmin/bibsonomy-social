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

import org.bibsonomy.webapp.command.BaseCommand;

/**
 * @author cvo
 * @version $Id: SettingsCommand.java,v 1.3 2010-05-11 12:24:52 nosebrain Exp $
 */
public class SettingsCommand extends BaseCommand {

	private int logLevel;
	
	private String defaultLanguage;
	
	private int itemcount;
	
	private int tagboxTooltip;
	
	private int tagboxMinfreq;
	
	private int tagSort;
	
	private int tagboxStyle;

	private boolean confirmDelete;

	/**
	 * @return the logLevel
	 */
	public int getLogLevel() {
		return this.logLevel;
	}

	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * @return the defaultLanguage
	 */
	public String getDefaultLanguage() {
		return this.defaultLanguage;
	}

	/**
	 * @param defaultLanguage the defaultLanguage to set
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @return the itemcount
	 */
	public int getItemcount() {
		return this.itemcount;
	}

	/**
	 * @param itemcount the itemcount to set
	 */
	public void setItemcount(int itemcount) {
		this.itemcount = itemcount;
	}

	/**
	 * @return the tagboxTooltip
	 */
	public int getTagboxTooltip() {
		return this.tagboxTooltip;
	}

	/**
	 * @param tagboxTooltip the tagboxTooltip to set
	 */
	public void setTagboxTooltip(int tagboxTooltip) {
		this.tagboxTooltip = tagboxTooltip;
	}

	/**
	 * @return the tagboxMinfreq
	 */
	public int getTagboxMinfreq() {
		return this.tagboxMinfreq;
	}

	/**
	 * @param tagboxMinfreq the tagboxMinfreq to set
	 */
	public void setTagboxMinfreq(int tagboxMinfreq) {
		this.tagboxMinfreq = tagboxMinfreq;
	}

	/**
	 * @return the tagSort
	 */
	public int getTagSort() {
		return this.tagSort;
	}

	/**
	 * @param tagSort the tagSort to set
	 */
	public void setTagSort(int tagSort) {
		this.tagSort = tagSort;
	}

	/**
	 * @return the tagboxStyle
	 */
	public int getTagboxStyle() {
		return this.tagboxStyle;
	}

	/**
	 * @param tagboxStyle the tagboxStyle to set
	 */
	public void setTagboxStyle(int tagboxStyle) {
		this.tagboxStyle = tagboxStyle;
	}

	/**
	 * @return the confirmDelete
	 */
	public boolean isConfirmDelete() {
		return this.confirmDelete;
	}

	/**
	 * @param confirmDelete the confirmDelete to set
	 */
	public void setConfirmDelete(boolean confirmDelete) {
		this.confirmDelete = confirmDelete;
	}
}
