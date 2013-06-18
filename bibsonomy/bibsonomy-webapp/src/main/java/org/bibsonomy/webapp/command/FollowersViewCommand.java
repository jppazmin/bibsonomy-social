/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.command;

import java.util.List;

import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;

/**
 * 
 * @author Christian Kramer
 * @version $Id: FollowersViewCommand.java,v 1.4 2010-04-28 13:54:54 nosebrain Exp $
 *
 */
public class FollowersViewCommand extends TagResourceViewCommand {
	private List<User> followersOfUser;
	private List<User> userIsFollowing;

	private RankingCommand ranking = new RankingCommand();
	
	/**
	 * defines the similarity measure by which the related users are computed  
	 * (default is folkrank)
	 */
	private String userSimilarity = UserRelation.FOLKRANK.name();	
	
	
	/**
	 * 
	 * @return all users which are following this user
	 */
	public List<User> getFollowersOfUser() {
		return this.followersOfUser;
	}
	
	/**
	 * 
	 * @param followersOfUser
	 */
	public void setFollowersOfUser(List<User> followersOfUser) {
		this.followersOfUser = followersOfUser;
	}
	
	/**
	 * 
	 * @return list of user which the user is following
	 */
	public List<User> getUserIsFollowing() {
		return this.userIsFollowing;
	}
	
	/**
	 * 
	 * @param userIsFollowing
	 */
	public void setUserIsFollowing(List<User> userIsFollowing) {
		this.userIsFollowing = userIsFollowing;
	}

	/**
	 * @return the ranking
	 */
	public RankingCommand getRanking() {
		return this.ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(RankingCommand ranking) {
		this.ranking = ranking;
	}

	/**
	 * @return the userSimilarity
	 */
	public String getUserSimilarity() {
		return this.userSimilarity;
	}

	/**
	 * @param userSimilarity the userSimilarity to set
	 */
	public void setUserSimilarity(String userSimilarity) {
		this.userSimilarity = userSimilarity;
	}
}
