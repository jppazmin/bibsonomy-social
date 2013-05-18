/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model.logic;

import java.util.List;

import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.enums.StatisticsConstraint;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.enums.Order;

/**
 * Access interface from applications (client and server) to the core
 * functionality regarding posts.
 * 
 * @author Jens Illig
 * @version $Id: PostLogicInterface.java,v 1.19 2011-04-29 06:45:02 bibsonomy Exp $
 */
public interface PostLogicInterface {
	/**  
	 * retrieves a filterable list of posts.
	 * 
	 * @param <T> resource type to be shown.
	 * @param resourceType resource type to be shown.
	 * @param grouping
	 *            grouping tells whom posts are to be shown: the posts of a
	 *            user, of a group or of the viewables.
	 * @param groupingName
	 *            name of the grouping. if grouping is user, then its the
	 *            username. if grouping is set to {@link GroupingEntity#ALL},
	 *            then its an empty string!
	 * @param tags
	 *            a set of tags. remember to parse special tags like
	 *            ->[tagname], -->[tagname] and <->[tagname]. see documentation.
	 *            if the parameter is not used, its an empty list
	 * @param hash
	 *            hash value of a resource, if one would like to get a list of
	 *            all posts belonging to a given resource. if unused, its empty
	 *            but not null.
	 * @param start inclusive start index of the view window
	 * @param end exclusive end index of the view window
	 * @param search free text search
	 * @param order a flag indicating the way of sorting
	 * @param filter filter for the retrieved posts
	 * @return a filtered list of posts. may be empty but not null
	 */
	public <T extends Resource> List<Post<T>> getPosts(Class<T> resourceType, GroupingEntity grouping, String groupingName, List<String> tags, String hash, Order order, FilterEntity filter, int start, int end, String search);

	/**
	 * Returns details to a post. A post is uniquely identified by a hash of the
	 * corresponding resource and a username.
	 * 
	 * @param resourceHash hash value of the corresponding resource
	 * @param userName name of the post-owner
	 * @return the post's details, null else
	 * @throws ResourceMovedException  - when no resource 
	 * with that hash exists for that user, but once a resource 
	 * with that hash existed that has been moved. The new hash 
	 * is returned inside the exception. 
	 * @throws ResourceNotFoundException 
	 */
	public Post<? extends Resource> getPostDetails(String resourceHash, String userName) throws ResourceMovedException, ResourceNotFoundException;

	/**
	 * Removes the given posts - identified by the connected resource's hashes -
	 * from the user.
	 * 
	 * @param userName user who's posts are to be removed
	 * @param resourceHashes
	 *            hashes of the resources, which is connected to the posts to delete
	 */
	public void deletePosts(String userName, List<String> resourceHashes);

	/**
	 * Add the posts to the database.
	 * 
	 * @param posts  the posts to add
	 * @return String the resource hashes of the created posts
	 */
	public List<String> createPosts(List<Post<? extends Resource>> posts);

	/**
	 * Updates the posts in the database.
	 * 
	 * @param posts  the posts to update
	 * @param operation  which parts of the posts should be updated
	 * @return resourceHashes the (new) hashes of the updated resources
	 */
	public List<String> updatePosts(List<Post<? extends Resource>> posts, PostUpdateOperation operation);
	
	/**  
	 * retrieves the number of posts matching to the given constraints
	 * 
	 * @param resourceType resource type to be shown.
	 * @param grouping
	 *            grouping tells whom posts are to be shown: the posts of a
	 *            user, of a group or of the viewables.
	 * @param groupingName
	 *            name of the grouping. if grouping is user, then its the
	 *            username. if grouping is set to {@link GroupingEntity#ALL},
	 *            then its an empty string!
	 * @param tags
	 *            a set of tags. remember to parse special tags like
	 *            ->[tagname], -->[tagname] and <->[tagname]. see documentation.
	 *            if the parameter is not used, its an empty list
	 * @param hash
	 *            hash value of a resource, if one would like to get a list of
	 *            all posts belonging to a given resource. if unused, its empty
	 *            but not null.
	 * @param start inclusive start index of the view window
	 * @param end exclusive end index of the view window
	 * @param search free text search
	 * @param order a flag indicating the way of sorting
	 * @param filter filter for the retrieved posts
	 * @param constraint - a possible constraint on the statistics
	 * @return a filtered list of posts. may be empty but not null
	 */
	public int getPostStatistics(Class<? extends Resource> resourceType, GroupingEntity grouping, String groupingName, List<String> tags, String hash, Order order, FilterEntity filter, int start, int end, String search, StatisticsConstraint constraint);
}