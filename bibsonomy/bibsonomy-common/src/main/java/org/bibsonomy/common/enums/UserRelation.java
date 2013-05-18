/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
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

package org.bibsonomy.common.enums;

/**
 * Defines types of relationships / measures of relatedness between users.
 * 
 * The mapping of user relations to IDs has to be in sync with the content of
 * the table useruser_similarity_measures.
 * 
 * @author Dominik Benz
 * @version $Id: UserRelation.java,v 1.8 2011-04-29 06:36:53 bibsonomy Exp $
 */
public enum UserRelation {
	/** users related based on computation of jaccard similarity */
	JACCARD(0),
	/** users related based on computation of cosine similarity */
	COSINE(1),
	/** users related based on computation of jaccard similarity */
	TFIDF(2),
	/** users related based on folkrank computation */
	FOLKRANK(3),	

	/** source user is friend of target user (i.e. u1 -> u2) i.e. source user is in target users friend list
	 * DO NOT USE THIS IF NOT ABSOLUTELY NECESSARY, try using OF_FRIEND instead
	 * we need this for the getUsers Method in DBLogic*/
	FRIEND_OF(4),
	/** target user is friend of source user (i.e, u1 <- u2) i.e. target user is in source users friend list*/
	OF_FRIEND(5),
	/** source user is curious about the target user */
	CURIOUS_ABOUT(6), 
	/** source user follows target user */
	FOLLOWER_OF(7),
	/** target user follows source user */
	OF_FOLLOWER(8),
	/** relationships can also be established by a custom tag */
	TAGGED(9);
	
	/**
	 * the relation ID. Mainly used in the table useruser_similarity.
	 */
	final int relationId;
	
	/**
	 * Constructor 
	 * 
	 * @param relationId
	 */
	private UserRelation(final int relationId) {
		this.relationId = relationId;
	}
	
	/**
	 * Get the ID of this user relation.
	 * 
	 * @return - the ID (integer) of this relation
	 */
	public int getId() {
		return this.relationId;
	}
	
	/**
	 * get user relation by its integer ID
	 * 
	 * @param id - the id of the relation
	 * @return the corresponding user relation
	 */
	public static UserRelation getUserRelationById(int id) {
		switch (id) {
			case 0: return UserRelation.JACCARD; 
			case 1: return UserRelation.COSINE; 			
			case 2: return UserRelation.TFIDF; 
			case 3: return UserRelation.FOLKRANK; 
			case 4: return UserRelation.FRIEND_OF; 
			case 5: return UserRelation.OF_FRIEND; 
			case 6: return UserRelation.CURIOUS_ABOUT;
			case 7: return UserRelation.FOLLOWER_OF;
			case 8: return UserRelation.OF_FOLLOWER;
			default: return UserRelation.FOLKRANK; 
		}		
	}
	
	/**
	 * check whether the user relation is internal, which means that
	 * both "partners" in the relation must be valid bibsonomy users.
	 * 
	 * this is e.g. not the case when we add friends from other 
	 * social networks, e.g. facebook friends.
	 * 
	 * FIXME: We have to talk about if "tagged" relationships are 
	 * internal or not. This depends especially on how we model external
	 * friends (e.g. from facebook), e.g. by an own UserRelation type
	 * or solely via the tags.
	 * 
	 * @return true if the current user relation is an internal relation, false otherwise.
	 */
	public boolean isInternal() {
		return UserRelation.FOLLOWER_OF.equals(this)   ||
			   UserRelation.OF_FOLLOWER.equals(this)   ||
			   UserRelation.FRIEND_OF.equals(this)	   ||
			   UserRelation.OF_FRIEND.equals(this)     ||
			   UserRelation.JACCARD.equals(this)       ||
			   UserRelation.COSINE.equals(this)        ||
			   UserRelation.TFIDF.equals(this)         ||
			   UserRelation.FOLKRANK.equals(this)      ||
			   UserRelation.CURIOUS_ABOUT.equals(this);
	}
	
	
}