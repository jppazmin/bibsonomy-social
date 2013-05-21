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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.recommender.tags.database.DBLogic;
import org.bibsonomy.recommender.tags.multiplexer.RecommendedTagResultManager;

/**
 * @author fei
 * @version $Id: SelectAll.java,v 1.6 2010-07-14 11:44:27 nosebrain Exp $
 */
public class SelectAll implements RecommendationSelector {
	private static final Log log = LogFactory.getLog(SelectAll.class);
	private String info = "Strategy for selecting all recommended Tags.";
	
	private DBLogic dbLogic;
	
	/**
	 * Selection strategy which simply selects each recommended tag
	 */
	@Override
	public void selectResult(Long qid, RecommendedTagResultManager resultCache, Collection<RecommendedTag> recommendedTags) throws SQLException {
		log.debug("Selecting result.");
		dbLogic.getRecommendations(qid, recommendedTags);
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
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
