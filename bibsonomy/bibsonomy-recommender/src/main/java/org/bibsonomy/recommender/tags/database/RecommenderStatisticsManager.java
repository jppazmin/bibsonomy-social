/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

package org.bibsonomy.recommender.tags.database;

import java.util.Random;

/**
 * Implements a service for logging recommendation statistics.
 * 
 * @author fei
 * @version $Id: RecommenderStatisticsManager.java,v 1.4 2009-06-22 12:55:04 rja Exp $
 */
public class RecommenderStatisticsManager {
	/** indicates that post identifier was not given */
	public static int UNKNOWN_POSTID = -1;
	private static final Random rand = new Random();
	
	/**
	 * Get id which indicates that a recommendation query was not associated with a post.
	 * @return UNKNOWN_POSTID
	 */	
	public static int getUnknownPID() {
		return  UNKNOWN_POSTID;
	}
	
	/**
	 * @return A new post ID.
	 */
	public static int getNewPID() {
		return rand.nextInt(Integer.MAX_VALUE);
	}

}
