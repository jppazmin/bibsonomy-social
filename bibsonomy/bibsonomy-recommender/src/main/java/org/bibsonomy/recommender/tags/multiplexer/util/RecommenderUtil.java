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

package org.bibsonomy.recommender.tags.multiplexer.util;

import org.bibsonomy.recommender.tags.TagRecommenderConnector;
import org.bibsonomy.services.recommender.TagRecommender;

/**
 * @author bsc
 * @version $Id: RecommenderUtil.java,v 1.1 2010-07-08 14:08:47 bsc Exp $
 */
public class RecommenderUtil {
	/**
	 * Get the Recommenderid of a given TagRecommender.
	 * @param rec
	 * @return Recommenderid
	 */
	public static String getRecommenderId(TagRecommender rec) {
		if (rec instanceof TagRecommenderConnector) {
			return ((TagRecommenderConnector) rec).getId();
		} else {
			return rec.getClass().getCanonicalName();
		}
	}
}
