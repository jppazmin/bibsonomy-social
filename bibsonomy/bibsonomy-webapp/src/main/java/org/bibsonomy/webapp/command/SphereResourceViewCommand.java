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
import java.util.Map;
import java.util.Set;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.User;

/**
 * Command class for encapsulating sphere related models
 * 
 * TODO: this is a merge of two parameter classes and thus needs some cleanup 
 */
public class SphereResourceViewCommand extends FriendsResourceViewCommand {
	
	private String 			requestedUserRelation 	= "";
	private List<User> 		relatedUsers;
	List<Post<Bookmark>> 	bmPosts;
	List<Post<BibTex>> 		bibPosts;
	
	private Map<String, Set<User>> spheres;
	private Map<String, ListCommand<Post<Bookmark>>> spheresBMPosts;
	private Map<String, ListCommand<Post<BibTex>>> spheresPBPosts;
	private Map<String, TagCloudCommand> spheresTagClouds;

	/**
	 * @return the requestedUserRelation
	 */
	public String getRequestedUserRelation() {
		return this.requestedUserRelation;
	}
	/**
	 * @param requestedUserRelation the requestedUserRelation to set
	 */
	public void setRequestedUserRelation(String requestedUserRelation) {
		this.requestedUserRelation = requestedUserRelation;
	}
	
	/**
	 * @return the relatedUsers
	 */
	public List<User> getRelatedUsers() {
		return this.relatedUsers;
	}
	/**
	 * @param relatedUsers the relatedUsers to set
	 */
	public void setRelatedUsers(List<User> relatedUsers) {
		this.relatedUsers = relatedUsers;
	}
	/**
	 * @return the bmPosts
	 */
	public List<Post<Bookmark>> getBmPosts() {
		return this.bmPosts;
	}
	/**
	 * @param bmPosts the bmPosts to set
	 */
	public void setBmPosts(List<Post<Bookmark>> bmPosts) {
		this.bmPosts = bmPosts;
	}
	/**
	 * @return the bibPosts
	 */
	public List<Post<BibTex>> getBibPosts() {
		return this.bibPosts;
	}
	/**
	 * @param bibPosts the bibPosts to set
	 */
	public void setBibPosts(List<Post<BibTex>> bibPosts) {
		this.bibPosts = bibPosts;
	}	
	
	/**
	 * @param spheres
	 */
	public void setSpheres(Map<String, Set<User>> spheres) {
		this.spheres = spheres;
	}

	/**
	 * @return spheres
	 */
	public Map<String, Set<User>> getSpheres() {
		return spheres;
	}

	/**
	 * @param spheresBMPosts
	 */
	public void setSpheresBMPosts(Map<String, ListCommand<Post<Bookmark>>> spheresBMPosts) {
		this.spheresBMPosts = spheresBMPosts;
	}

	/**
	 * @return spheresBMPosts
	 */
	public Map<String, ListCommand<Post<Bookmark>>> getSpheresBMPosts() {
		return spheresBMPosts;
	}

	/**
	 * @param spheresPBPosts
	 */
	public void setSpheresPBPosts(Map<String, ListCommand<Post<BibTex>>> spheresPBPosts) {
		this.spheresPBPosts = spheresPBPosts;
	}

	/**
	 * @return spheresPBPosts
	 */
	public Map<String, ListCommand<Post<BibTex>>> getSpheresPBPosts() {
		return spheresPBPosts;
	}

	/**
	 * @param spheresTagClouds
	 */
	public void setSpheresTagClouds(Map<String, TagCloudCommand> spheresTagClouds) {
		this.spheresTagClouds = spheresTagClouds;
	}

	/**
	 * @return spheresTagClouds
	 */
	public Map<String, TagCloudCommand> getSpheresTagClouds() {
		return spheresTagClouds;
	}

}
