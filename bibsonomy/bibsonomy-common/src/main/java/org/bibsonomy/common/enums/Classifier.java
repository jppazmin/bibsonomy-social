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
 * Defines different possibilities of classifiers of a user
 * 
 * @author Stefan Stützer
 * @version $Id: Classifier.java,v 1.8 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum Classifier {
	/** An automatic classifier algorithm */
	CLASSIFIER(0),

	/** An administrator */
	ADMIN(1),
	
	/** Both */
	BOTH(2);
	
	/** the id */
	private int id;
	
	private Classifier(int id) {
		this.id = id;
	}

	/**
	 * @return the id for an enum
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @param id of the Classifier to retrieve
	 * @return the corresponding Classifier enum
	 */
	public static Classifier getClassifier(final int id) {
		switch(id) {
		case 0: 
			return CLASSIFIER;
		case 1:
			return ADMIN;
		case 2:
			return BOTH;
		default:
			return null;
		}
	}
}