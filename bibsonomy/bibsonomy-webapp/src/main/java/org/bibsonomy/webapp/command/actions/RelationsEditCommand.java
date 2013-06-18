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
 * @version $Id: RelationsEditCommand.java,v 1.1 2010-05-28 13:50:45 econ11 Exp $
 */
public class RelationsEditCommand {
	
	private String upper = "";
	
	private String lower = "";
	
	/**
	 * which action is requested
	 * 0 = add relation
	 * 1 = del relation
	 */
	private int forcedAction;

	/**
	 * @param upper the upper to set
	 */
	public void setUpper(String upper) {
		this.upper = upper;
	}

	/**
	 * @return the upper
	 */
	public String getUpper() {
		return upper;
	}

	/**
	 * @param lower the lower to set
	 */
	public void setLower(String lower) {
		this.lower = lower;
	}

	/**
	 * @return the lower
	 */
	public String getLower() {
		return lower;
	}

	/**
	 * @param forcedAction the forcedAction to set
	 */
	public void setForcedAction(int forcedAction) {
		this.forcedAction = forcedAction;
	}

	/**
	 * @return the forcedAction
	 */
	public int getForcedAction() {
		return forcedAction;
	}

}
