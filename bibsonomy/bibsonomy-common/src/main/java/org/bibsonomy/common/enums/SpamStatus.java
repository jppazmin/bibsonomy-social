/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
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

package org.bibsonomy.common.enums;

/**
 * Defines different states of a user
 * 
 * @author Stefan Stützer
 * @version $Id: SpamStatus.java,v 1.11 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum SpamStatus {
	/** no spammer, sure, classified by admin */
	NO_SPAMMER(0),

	/** spammer, sure, classified by admin */
	SPAMMER(1),

	/** no spammer, not sure, classified by classifier */
	NO_SPAMMER_NOT_SURE(2),

	/** spammer, not sure, classified by classifier */
	SPAMMER_NOT_SURE(3),

	/** no information about spammer status */
	UNKNOWN(9);

	private int id;

	private SpamStatus(int id) {
		this.id = id;
	}

	/**
	 * @return the id for the current enum
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return a string saying "yes" if the current enum is SPAMMER, "no" if the
	 *         enum is NO_SPAMMER, otherwise "unknown"
	 */
	public String isSpammer() {
		switch (getStatus(this.id)) {
		case NO_SPAMMER:
			return "no";
		case SPAMMER:
			return "yes";
		default:
			return "unknown";
		}
	}

	/**
	 * @param status
	 * @return true if the given status is SPAMMER or SPAMMER_NOT_SURE,
	 *         otherwise false
	 */
	public static boolean isSpammer(final SpamStatus status) {
		if (status.equals(SPAMMER) || status.equals(SPAMMER_NOT_SURE)) return true;
		return false;
	}

	/**
	 * @param id
	 * @return status
	 */
	public static SpamStatus getStatus(final int id) {
		switch (id) {
		case 0:
			return NO_SPAMMER;
		case 1:
			return SPAMMER;
		case 2:
			return NO_SPAMMER_NOT_SURE;
		case 3:
			return SPAMMER_NOT_SURE;
		default:
			return UNKNOWN;
		}
	}

	/**
	 * Returns dependent on the prediction and the current classifier mode
	 * a spammer state. If prediction is Spammer or No Spammer, the corresponding status 
	 * is returned. The same for non-confident spammers / non spammers, expect for the day 
	 * mode where for non confident spammers, the no spammer status is returned to avoid false 
	 * positive classifications.
	 * 
	 * @param status
	 *            The classifires prediction
	 * @param mode
	 *            The classifiers mode (Day or Night)
	 * @return real state to save in user table
	 */
	public static SpamStatus getRealSpammerState(final SpamStatus status, final ClassifierMode mode) {
		if (status.equals(SPAMMER) || status.equals(NO_SPAMMER)) return status;
		else if (status.equals(SPAMMER_NOT_SURE)) {
			return SPAMMER;
		}
		else if (status.equals(NO_SPAMMER_NOT_SURE)) {
			if (mode.equals(ClassifierMode.DAY)) return NO_SPAMMER;
			return SPAMMER;
		}else{
			return status;
		}
	}

	@Override
	public String toString() {
		switch (this.id) {
		case 0:
			return "no spammer";
		case 1:
			return "spammer";
		case 2:
			return "no spammer, not sure";
		case 3:
			return "spammer, not sure";
		case 9:
			return "unknown";
		default:
			return "";
		}
	}
}