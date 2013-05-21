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

package org.bibsonomy.recommender.tags.multiplexer.strategy;

import java.sql.SQLException;
import java.util.Collection;

import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.recommender.tags.multiplexer.RecommendedTagResultManager;

/**
 * @author fei
 * @version $Id: RecommendationSelector.java,v 1.4 2010-07-14 11:44:27 nosebrain Exp $
 */
public interface RecommendationSelector {
	
	/**
	 * Selects recommendations for given query
	 * 
	 * @param qid
	 * @param resultCache 
	 * @param recommendedTags 
	 * @throws SQLException
	 */
	public void selectResult(Long qid, RecommendedTagResultManager resultCache, Collection<RecommendedTag> recommendedTags) throws SQLException;
	
	/**
	 * selector specific meta informations
	 * @param info 
	 */
	public void setInfo(String info);
	
	/**
	 * @return selector specific meta informations
	 */
	public String getInfo();
	
	/**
	 * short text describing this strategy
	 * @param meta 
	 */
	public void setMeta(byte[] meta);
	
	/**
	 * @return short text describing this strategy
	 */
	public byte[] getMeta();
}
