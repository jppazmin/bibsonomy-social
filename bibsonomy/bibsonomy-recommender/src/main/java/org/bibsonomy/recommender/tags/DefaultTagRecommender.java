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

package org.bibsonomy.recommender.tags;

import org.bibsonomy.model.comparators.RecommendedTagComparator;
import org.bibsonomy.recommender.tags.meta.CompositeTagRecommender;
import org.bibsonomy.recommender.tags.simple.SimpleContentBasedTagRecommender;


/**
 * Default tag recommender.
 * 
 * @author rja
 * @version $Id: DefaultTagRecommender.java,v 1.4 2010-07-14 11:42:29 nosebrain Exp $
 */
public class DefaultTagRecommender extends CompositeTagRecommender {

	/**
	 * Currently only the {@link SimpleContentBasedTagRecommender} is included.
	 */
	public DefaultTagRecommender() {
		super(new RecommendedTagComparator());
		addTagRecommender(new SimpleContentBasedTagRecommender());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.recommender.tags.meta.CompositeTagRecommender#getInfo()
	 */
	@Override
	public String getInfo() {
		return "Default tag recommender.";
	}

}
