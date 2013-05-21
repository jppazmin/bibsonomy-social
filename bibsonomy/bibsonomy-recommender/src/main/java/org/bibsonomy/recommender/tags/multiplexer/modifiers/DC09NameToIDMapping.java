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

package org.bibsonomy.recommender.tags.multiplexer.modifiers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.recommender.tags.database.DBLogic;

/**
 * This class substitutes usernames in posts for anonymised user ids as
 * used in the ECMLPKDD09 discovery challenge's datasets.
 * 
 * @author fei
 * @version $Id: DC09NameToIDMapping.java,v 1.4 2010-11-16 09:39:19 nosebrain Exp $
 */
public class DC09NameToIDMapping implements PostModifier {
	private static final Log log = LogFactory.getLog(DC09NameToIDMapping.class);
	private static final Integer UNKNOWNID = Integer.MIN_VALUE;
	
	/** used for mapping user names to ids and vice versa */
	private DBLogic dbLogic;
	
	/** used for caching name->id mappings */
	private Map<String, Integer> nameMap;
	
	//------------------------------------------------------------------------
	// public interface 
	//------------------------------------------------------------------------
	/**
	 * constructor
	 */
	public DC09NameToIDMapping() {
		nameMap = new HashMap<String, Integer>(2000);
	}
	
	/**
	 * replaces given post's user name with the corresponding anonymised user id
	 *  
	 * @param post the post for which tags will be queried
	 */
	@Override
	public void alterPost(Post<? extends Resource> post) {
		String userName = post.getUser().getName();
		Integer userID = nameMap.get(userName);
		if( userID == null )
			userID = this.getDbLogic().getUserIDByName(userName);
		
		if( userID == null )
			userID = UNKNOWNID;
		post.setUser(new User(userID.toString()));
		log.debug("Mapping user "+userName+" to id "+userID);
	}

	/**
	 * @return the dbLogic
	 */
	public DBLogic getDbLogic() {
		return this.dbLogic;
	}

	/**
	 * @param dbLogic the dbLogic to set
	 */
	public void setDbLogic(DBLogic dbLogic) {
		this.dbLogic = dbLogic;
	}
}
