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

package org.bibsonomy.model.comparators;

import java.util.Comparator;

import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Tag;


/** Compares two recommended tags.
 * Tags are ordered by their score (confidence not considered).
 * <br/>Two tags are equal based on {@link Tag#equals(Object)} - 
 * independent of their scores!   
 * 
 * @author rja
 * @version $Id: RecommendedTagComparator.java,v 1.7 2011-04-29 06:45:03 bibsonomy Exp $
 */
public class RecommendedTagComparator implements Comparator<RecommendedTag> {

	@Override
	public int compare(RecommendedTag o1, RecommendedTag o2) {
		if (o1 == null) return -1;
		if (o2 == null) return 1;
		/*
		 * tag names equal: regard them as equal
		 * (this basically ensures that a set won't contain tags which equal based
		 *  on their equals() method)
		 */
		if (o1.equals(o2)) return 0;
		/*
		 * the highest score should come first (in the set) - hence, 
		 * do o2 - o1 
		 */
		int signum = new Double(Math.signum(o2.getScore() - o1.getScore())).intValue();
		if (signum != 0) return signum;
		/*
		 * scores equal: consider confidence
		 */
		signum = new Double(Math.signum(o2.getConfidence() - o1.getConfidence())).intValue();
		if (signum != 0) return signum;
		/*
		 * scores and confidence equal (but tag names not): return using compareTo from Tag.
		 */
		return o1.compareTo(o2);
	}
}
