/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.index.manager;

import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.bibsonomy.lucene.database.LuceneDBLogic;
import org.bibsonomy.lucene.database.LuceneGoldStandardPublicationLogic;
import org.bibsonomy.lucene.param.LucenePost;
import org.bibsonomy.lucene.util.LuceneBase;
import org.bibsonomy.lucene.util.LuceneSpringContextWrapper;
import org.bibsonomy.model.GoldStandardPublication;

/**
 * Updates the gold standard publication posts
 * uses the {@link LuceneBase#FLD_LAST_TAS_ID} for the latest content id
 * (gold standard posts have no tags)
 * 
 * {@link LuceneGoldStandardPublicationLogic} overrides {@link LuceneDBLogic#getLastTasId()}
 * to query for the latest content id
 * TODO: as soon as Lucene supports renaming fields (https://issues.apache.org/jira/browse/LUCENE-2160)
 * the latestTasId property should be renamed
 * 
 * @author dzo
 * @version $Id: LuceneGoldStandardPublicationManager.java,v 1.5 2011-05-28 12:27:35 nosebrain Exp $
 */
public class LuceneGoldStandardPublicationManager extends LuceneResourceManager<GoldStandardPublication> {
	
	private static LuceneGoldStandardPublicationManager INSTANCE = null;

	/**
	 * @return the @{link:LuceneGoldStandardPublicationManager} instance
	 */
	public static LuceneGoldStandardPublicationManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LuceneGoldStandardPublicationManager();
			LuceneSpringContextWrapper.init();
		}
		
		return INSTANCE;
	}

	private LuceneGoldStandardPublicationManager() {
	}
	
	@Override
	protected int updateIndex(final long currentLogDate, int lastId, final long lastLogDate) {
	    /*
	     * get new posts 
	     */
	    final List<LucenePost<GoldStandardPublication>> newPosts = this.dbLogic.getNewPosts(lastId);

	    /*
	     * get posts to delete
	     */
	    final List<Integer> contentIdsToDelete = this.dbLogic.getContentIdsToDelete(new Date(lastLogDate - QUERY_TIME_OFFSET_MS));

	    /*
	     * remove new and deleted posts from the index
	     * and update field 'lastTasId'
	     */
	    for (final LucenePost<GoldStandardPublication> post : newPosts) {
	    	final Integer contentId = post.getContentId();
	    	contentIdsToDelete.add(contentId);
	    	lastId = Math.max(contentId, lastId);
	    }
	    
	    this.resourceIndex.deleteDocumentsInIndex(contentIdsToDelete);

	    final Date currentDate = new Date(currentLogDate);
	    
	    /*
	     * add all new posts to the index 
	     */
	    for (final LucenePost<GoldStandardPublication> post : newPosts) {
	    	post.setLastLogDate(currentDate);
	    	post.setLastTasId(lastId);
	    	final Document postDoc = this.resourceConverter.readPost(post);
	    	this.resourceIndex.insertDocument(postDoc);
	    }
	    
	    return lastId;
	}
}
