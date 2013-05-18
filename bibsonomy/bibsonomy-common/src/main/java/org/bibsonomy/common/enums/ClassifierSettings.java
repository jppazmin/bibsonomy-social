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

import org.bibsonomy.util.EnumUtils;

/**
 * Defines different settings to control the behaviour of the classifier
 * 
 * @author Stefan Stützer
 * @author Beate Krause
 * @version $Id: ClassifierSettings.java,v 1.12 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum ClassifierSettings {
	/** the classification algorithm */
	ALGORITHM,

	/** the classifier mode (currently DAY or NIGHT) */
	MODE,

	/** classifier training interval in seconds */
	TRAINING_PERIOD,

	/** classify interval in seconds */
	CLASSIFY_PERIOD,

	/** probability to seperate sure from unsure classifications */
	PROBABILITY_LIMIT,

	/** TODO remove, testing mode will not effect user table */
	TESTING,
	
	/** last classification date to track changes in user profiles */
	LASTCLASSIFICATION,
	
	/** last classification date to track changes in user profiles */
	CLASSIFY_COST,
	
	/** expression to add to whitelist */
	WHITELIST_EXP;

	/**
	 * @param setting
	 *            name of the setting enum to retrieve
	 * @return the corresponding enum object
	 */
	public static ClassifierSettings getClassifierSettings(final String setting) {
		final ClassifierSettings cs = EnumUtils.searchEnumByName(ClassifierSettings.values(), setting);
		if (cs == null) throw new UnsupportedOperationException();
		return cs;
	}

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}