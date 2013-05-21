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

package org.bibsonomy.recommender.tags.simple;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.comparators.RecommendedTagComparator;
import org.bibsonomy.recommender.tags.TagRecommenderConnector;
import org.bibsonomy.services.recommender.TagRecommender;

/**
 * Dummy recommender for simulating different latency periods.
 * 
 * @author fei
 * @version $Id: DummyTagRecommender.java,v 1.12 2010-07-14 11:38:29 nosebrain Exp $
 */
public class DummyTagRecommender implements TagRecommender, TagRecommenderConnector {
	private static final Log log = LogFactory.getLog(DummyTagRecommender.class);
	
	
	private boolean init = false;
	private long wait = (long) (Math.random() * 1000); 
	private Integer id = 0;
	
	/**  
	 * Do nothing.
	 * @see org.bibsonomy.services.recommender.TagRecommender#addRecommendedTags(java.util.Collection, org.bibsonomy.model.Post)
	 */
	@Override
	public void addRecommendedTags(Collection<RecommendedTag> recommendedTags, Post<? extends Resource> post) {
		log.info("Dummy recommender: addRecommendedTags.");

		// create informative recommendation:
		for (int i = 0; i < 5; i++) {
			double score = Math.random();
			double confidence = Math.random();
			DecimalFormat df = new DecimalFormat( "0.00" );
			String re = "Dummy("+df.format(score)+","+df.format(confidence)+"["+getWait()+"])";
			recommendedTags.add(new RecommendedTag(re, score, confidence));
		}
		
		try {
			Thread.sleep(getWait());
		} catch (InterruptedException e) {
			// nothing to do.
		}
		
	}

	@Override
	public String getInfo() {
		return "Dummy recommender which does nothing at all.";
	}

	@Override
	public SortedSet<RecommendedTag> getRecommendedTags(Post<? extends Resource> post) {
		final SortedSet<RecommendedTag> recommendedTags = new TreeSet<RecommendedTag>(new RecommendedTagComparator());
		addRecommendedTags(recommendedTags, post);
		return recommendedTags;
	}
	
	@Override
	public boolean connect() throws Exception {
		if( init )
			log.info("connected!");
		else
			log.warn("recommender wasn't initialized prior to connection");
		
		return true;
	}

	@Override
	public boolean disconnect() throws Exception {
		log.info("disconnected!");
		return true;
	}

	@Override
	public boolean initialize(Properties props) throws Exception {
		log.info("initialized!");
		init = true;
		return true;
	}

	@Override
	public byte[] getMeta() {
		return null;
	}

	@Override
	public String getId() {
		return "DummyTagRecommender-"+id;
	}

	@Override
	public void setFeedback(Post<? extends Resource> post) {
		/*
		 * this recommender ignores feedback
		 */
	}

	/**
	 * @return the wait
	 */
	public long getWait() {
		return this.wait;
	}

	/**
	 * @param wait the wait to set
	 */
	public void setWait(long wait) {
		this.wait = wait;
	}	
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}
