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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.recommender.tags.database.DBLogic;
import org.bibsonomy.recommender.tags.database.params.Pair;
import org.bibsonomy.recommender.tags.multiplexer.RecommendedTagResultManager;

/**
 * This selection strategy selects exactly one recommender.
 *  
 * @author fei
 * @version $Id: SelectOneWithoutReplacement.java,v 1.6 2010-07-14 11:44:27 nosebrain Exp $
 */
public class SelectOneWithoutReplacement implements RecommendationSelector {
	private static final Log log = LogFactory.getLog(SelectOneWithoutReplacement.class);
	private String info = "Strategy for selecting one recommender.";
	private DBLogic dbLogic;

	/**
	 * Selection strategy which selects recommender (uniform) randomly.
	 * If selected recommender didn't deliver recommendations - a fallback
	 * recommender is chosen. 
	 */
	@Override
	public void selectResult(Long qid, RecommendedTagResultManager resultCache, Collection<RecommendedTag> recommendedTags) throws SQLException {
		log.debug("("+qid+")Selecting result.");
		
		// get list of recommenders which delivered tags in given query
		// TODO: use the set interface
		final List<Long> listActive = new ArrayList<Long>(resultCache.getActiveRecommender(qid));
		
		// final List<Long> listActive = dbLogic.getActiveRecommenderIDs(qid);
		// log.debug("Result cache check for query "+qid+" : "+ listActive.size() +" / " + resultCache.getActiveRecommender(qid).size());

		log.debug("("+qid+")Selecting result #1");
		// get list of all recommenders for this post process with corresponding number of 
		// queries where they were selected
		final List<Pair<Long,Long>> selectionCount = dbLogic.getRecommenderSelectionCount(qid);
		
		//--------------------------------------------------------------------
		// create list of all recommenders from which the next one shall be drawn
		//--------------------------------------------------------------------
		final Vector<Long> listAll = new Vector<Long>();
		// id of last recommender
		long last = -1;
		if( !selectionCount.isEmpty() )
			last = selectionCount.get(0).getSecond();
		// collect those recommenders which were selected least 
		log.debug("("+qid+")Selecting result #2");
		while( !selectionCount.isEmpty() && (selectionCount.get(0).getSecond()==last) ) {
			listAll.add(selectionCount.get(0).getFirst());
			selectionCount.remove(0);
		}
		log.debug("("+qid+")Selecting result #3");

		// if no recommendation available, append nothing
		if( listAll.size() == 0 || listActive.size() == 0 ) {
			log.debug("("+qid+")No results available!");
			return;
		}
		// select recommender
		Long sid = listAll.get((int) Math.floor((Math.random() * listAll.size())));
		// store selection in database
		dbLogic.addSelectedRecommender(qid, sid);
		log.debug("("+qid+")Selected setting " + sid + " out of "+listActive.size()+"/"+listAll.size());
		
		// check if selected recommender delivered tags
		boolean isActive = false;
		for(Iterator<Long> i = listActive.iterator(); i.hasNext(); ) {
			Long next = i.next();
			if( next.equals(sid) ) 
				isActive = true;
		}
		// if not, select a fall back recommender
		if( !isActive ) {
			sid = listActive.get((int) Math.floor((Math.random()*listActive.size())));
			log.debug("("+qid+")Selected setting not active, fall back is " + sid);
		}
		
		// finally get recommended tags
		SortedSet<RecommendedTag> cachedResult = resultCache.getResults(qid,sid);
		if( cachedResult != null ) {
			recommendedTags.addAll(cachedResult);
		} else {
			// this shouldn't happen!
			log.error("("+qid+")Selected result not cached -> fetching it from database");
			dbLogic.getRecommendations(qid, sid, recommendedTags);
		}
			
	}	

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String info) {
		this.info = info;
	}


	@Override
	public byte[] getMeta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMeta(byte[] meta) {
		// TODO Auto-generated method stub
		
	}

	public DBLogic getDbLogic() {
		return this.dbLogic;
	}

	public void setDbLogic(DBLogic dbLogic) {
		this.dbLogic = dbLogic;
	}


}
