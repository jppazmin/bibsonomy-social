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

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bibsonomy.common.enums.Classifier;
import org.bibsonomy.common.enums.ClassifierSettings;
import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.ConceptUpdateOperation;
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupUpdateOperation;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.InetAddressStatus;
import org.bibsonomy.common.enums.SpamStatus;
import org.bibsonomy.common.enums.TagSimilarity;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.model.Author;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.Wiki;
import org.bibsonomy.model.enums.Order;

/**
 * This interface is an adapter to BibSonomy's core functionality. <br/>
 * 
 * The methods returning information return in general, if there are no matches,
 * an empty set (if a list is requested), or null (if a single entity is
 * requested (e.g. a post)). <br/>
 * 
 * <b>Please try to be as close to the method-conventions as possible.</b> If
 * something is unclear, guess, check occurences and document your result. If
 * you have to change a convention, check all occurences and document it
 * properly! Try to check each possibility with a test-case.<br/>
 * 
 * BE AWARE that this might grow quickly. So distribute methods across classes
 * or at least interfaces (like it has been done with PostLogicInterface) and
 * use these in your code.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @author Jens Illig <illig@innofinity.de>
 * @author Christian Kramer
 * @version $Id: LogicInterface.java,v 1.75 2011-06-20 08:49:18 rja Exp $
 */
public interface LogicInterface extends PostLogicInterface, GoldStandardPostLogicInterface, DiscussionLogicInterface {

	/**
	 * @return the name of the authenticated user
	 */
	public User getAuthenticatedUser();
	
	/**
	 * Generic method to retrieve lists of users
	 * 
	 * @param resourceType
	 * 			- restrict users by a certain resource type
	 * @param grouping
	 * 			- the grouping entity
	 * @param groupingName
	 * 			- the grouping name
	 * @param tags
	 * 			- a list of tags by which to retrieve users (e.g., users related to these tags by folkrank)
	 * @param hash
	 * 			- a resourcehash 
	 * @param order
	 * 			- the order by which to retrieve the users
	 * @param relation
	 * 			- the relation between the users
	 * @param search
	 * 			- a search string
	 * @param start
	 * @param end
	 * 
	 * @return list of user
	 */
	public List<User> getUsers (Class<? extends Resource> resourceType, GroupingEntity grouping, String groupingName, List<String> tags, String hash, Order order, UserRelation relation, String search, int start, int end);	

	/**
	 * Returns details about a specified user
	 * 
	 * @param userName name of the user we want to get details from
	 * @return details about a named user, null else
	 */
	public User getUserDetails(String userName);
	
	

	/**
	 * @param userName
	 * @return WikiVersions
	 */
	public List<Date> getWikiVersions(String userName);
	        
	/**
	 * @param userName
	 * @param date the date of creation from the wikitext, null describe the actual wikitext
	 * @return the requested wikitext from the given user
	 */
	public Wiki getWiki(String userName, Date date); 
	
	/**
	 * @param userName 
	 * @param wiki 
	 */
	public void createWiki(String userName, Wiki wiki); 
	
	/**
	 * @param userName 
	 * @param wiki
	 */
	public void updateWiki(String userName, Wiki wiki); 
	
	/**
	 * @param userName
	 */
	public void deleteWiki(String userName); 
	

	/**
	 * Returns all groups of the system.
	 * 
	 * @param end
	 * @param start
	 * @return a set of groups, an empty set else
	 */
	public List<Group> getGroups(int start, int end);

	/**
	 * Returns details of one group.
	 * 
	 * @param groupName
	 * @return the group's details, null else
	 */
	public Group getGroupDetails(String groupName);

	/**
	 * Returns a list of tags which can be filtered.
	 * @param resourceType
	 * 			  a resourceType (i.e. Bibtex or Bookmark) to get tags
	 *  		  only from a bookmark or a bibtex entry
	 * @param grouping
	 *            grouping tells whom tags are to be shown: the tags of a user,
	 *            of a group or of the viewables.
	 * @param groupingName
	 *            name of the grouping. if grouping is user, then its the
	 *            username. if grouping is set to {@link GroupingEntity#ALL},
	 *            then its an empty string!
	 * @param regex
	 *            a regular expression used to filter the tagnames
	 * @param tags
	 * @param hash
				  a resource hash (bibtex or bookmark)
	 * @param order 
	 * @param start
	 * @param end
	 * @param search - search string
	 * @param relation TODO
	 * @return a set of tags, en empty set else
	 */
	public List<Tag> getTags(Class<? extends Resource> resourceType, GroupingEntity grouping, String groupingName, String regex, List<String> tags, String hash, Order order, int start, int end, String search, TagSimilarity relation);

	/**  
	 * retrieves a filterable list of authors.
	 * 
	 * @param grouping
	 *            grouping tells whom authors are to be shown: the authors of a
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
	 *            all authors belonging to a given resource. if unused, its empty
	 *            but not null.
	 * @param start inclusive start index of the view window
	 * @param end exclusive end index of the view window
	 * @param search free text search
	 * @param order a flag indicating the way of sorting
	 * @param filter filter for the retrieved authors
	 * @return a filtered list of authors. may be empty but not null
	 */
	public List<Author> getAuthors(GroupingEntity grouping, String groupingName, List<String> tags, String hash, Order order, FilterEntity filter, int start, int end, String search);
	
	/**
	 * Returns details about a tag. Those details are:
	 * <ul>
	 * <li>details about the tag itself, like number of occurrences etc</li>
	 * <li>list of subtags</li>
	 * <li>list of supertags</li>
	 * <li>list of correlated tags</li>
	 * </ul>
	 * 
	 * @param tagName name of the tag
	 * @return the tag's details, null else
	 */
	public Tag getTagDetails(String tagName);

	/** Updates the tags of the given user by replacing ALL tags of <code>tagsToReplace</code>
	 * with ALL tags from <code>replacementTags</code>.
	 * <p>That means, in all posts which contain all of the first tags, those tags will be 
	 * replaced by the second tags.</p>
	 * <p>This method does not change relations/concepts!</p>
	 * 
	 * @param user - the user whose tags should be updated.
	 * @param tagsToReplace - the tags which should be replaced. Only when all tags occur
	 * together at the same post, they're replaced!
	 * 
	 * @param replacementTags - the tags which replace the other tags.
	 * @param updateRelations - if true additional to the replace of the tags the corresponding relations are updated,
	 * be aware that this can only be done if tagsToReplace.size == 1 and replacementTags == 1
	 * @return - The number of posts which were updated.
	 */
	public int updateTags(User user, List<Tag> tagsToReplace, List<Tag> replacementTags, boolean updateRelations);
	
	/**
	 * Removes the given user.
	 * 
	 * @param userName the user to delete
	 */
	public void deleteUser(String userName);

	/**
	 * Removes the given group.
	 * 
	 * @param groupName the group to delete
	 */
	public void deleteGroup(String groupName);

	/**
	 * Removes an user from a group.
	 * 
	 * @param groupName the group to change
	 * @param userName the user to remove
	 */
	public void deleteUserFromGroup(String groupName, String userName);

	/**
	 * Adds a user to the database.
	 * 
	 * @param user  the user to add
	 * @return userid the user id of the created user
	 */
	public String createUser(User user);

	/**
	 * Updates a user to the database.
	 * 
	 * @param user  the user to update
	 * @param operation the user operation
	 * @return userid the user id of the updated user
	 */
	public String updateUser(User user, final UserUpdateOperation operation);

	/**
	 * Adds a group to the database.
	 * 
	 * @param group  the group to add
	 * @return groupID the group id of the created group
	 */
	public String createGroup(Group group);

	/**
	 * Updates a group in the database.
	 * 
	 * @param group  the group to update
	 * @param operation the operation which should be performed
	 * @return groupID the group id of the updated group
	 */
	public String updateGroup(Group group, final GroupUpdateOperation operation);

	/**
	 * Adds an existing user to an existing group.
	 * 
	 * @param groupName  name of the existing group
	 * @param userName  user to add
	 */
	public void addUserToGroup(String groupName, String userName);

	/**
	 * Adds a document. If the resourceHash is given, the document is connected
	 * to the corresponding post. Otherwise, the document is independent of any
	 * post (e.g., a layout file.
	 * 
	 * @param document
	 * @param resourceHash
	 * 
	 * @return The hash of the created document.
	 */
	public String createDocument(Document document, String resourceHash);

	/**
	 * Get a (layout) document file for a non post connected document
	 * @param userName
	 * @param fileHash
	 * @return document
	 */
	public Document getDocument(final String userName, final String fileHash);
	
	/**
	 * Get a document from an existing Bibtex entry
	 * @param userName 
	 * @param resourceHash 
	 * @param fileName 
	 * 
	 * @return document
	 */
	public Document getDocument(String userName, String resourceHash, String fileName);

	/**
	 * Deletes an existing document. If the resourceHash is given, the document
	 * is assumed to be connected to the corresponding resource (identified by 
	 * the user name in the document). Otherwise the document is independent of
	 * any post.
     *
	 * @param document - the document which should be deleted.  
	 * @param resourceHash - the hash of a post the document belongs to.
	 */
	public void deleteDocument(Document document, String resourceHash);

	/**
	 * Adds an InetAddress (IP) with the given status to the list of addresses.
	 * Note that an InetAddress has exactly one status - so adding the status 
	 * really means setting it. TODO: this should be cleaned - either by renaming 
	 * the method to "setInetAddressStatus" or by allowing several states for an 
	 * InetAddress (use case?).
	 * 
	 * @param address - the address for which we want to set the status
	 * @param status  - the status of the address (e.g. "blocked") 
	 * @author Robert Jäschke
	 */
	public void createInetAddressStatus (InetAddress address, InetAddressStatus status);

	/** 
	 * Returns the current status of an InetAddress.
	 * 
	 * @param address - the InetAddress which status to get
	 * @return The status of the given address.
	 * @author Robert Jäschke
	 */
	public InetAddressStatus getInetAddressStatus (InetAddress address);

	/** Removes the address from the the list of stati for InetAddresses. Since
	 * currently one address can have only one status, it is not neccessary to
	 * say which status for that address should be removed. TODO: see comment 
	 * for {@link #createInetAddressStatus(InetAddress, InetAddressStatus)}.
	 * 
	 * @param address - the InetAddress which should be removed from the status list.
	 * @author Robert Jäschke
	 */
	public void deleteInetAdressStatus (InetAddress address);

	/**
	 * Retrieve relations
	 * 
	 * @param conceptName - the supertag of the concept
	 * @param grouping - grouping entity
	 * @param groupingName - the grouping name	
	 * @return a concept, i.e. a tag containing its assigned subtags
     * @author sts
	 */
	public Tag getConceptDetails(String conceptName, GroupingEntity grouping, String groupingName);

	/**
	 * Create a new relation/concept
	 * 
	 * @param concept - the new concept
	 * @param grouping - grouping entity
	 * @param groupingName - the grouping name
	 * @return the name of the superconcept-tag, note: if a concept already exists with the given name
	 * it will be replaced
	 * @author sts
	 */
	public String createConcept(Tag concept, GroupingEntity grouping, String groupingName);

	/**
	 * Update an existing relation/concept
	 * 
	 * @param concept - the concept to update
	 * @param grouping - grouping entity
	 * @param groupingName - the grouping name	
	 * @param operation
	 * @return the name of the superconcept-tag  
	 * @author sts
	 */
	public String updateConcept(Tag concept, GroupingEntity grouping, String groupingName, final ConceptUpdateOperation operation);

	/**
	 * Delete an existing concept
	 * 
	 * @param concept - the concept to delete
	 * @param grouping - grouping entity
	 * @param groupingName - the grouping name	 
     * @author sts
	 */
	public void deleteConcept(String concept, GroupingEntity grouping, String groupingName);

	/**
	 * Delete an existing relation
	 * 
	 * @param upper - the concept to delete
	 * @param lower - the subtag of the conceptname
	 * @param grouping - grouping entity
	 * @param groupingName - the grouping name	 
     * @author sts
	 */
	public void deleteRelation(String upper, String lower, GroupingEntity grouping, String groupingName);

	/**
	 * Returns all users that are classified to the specified state by
	 * the given classifier 
	 * 
	 * @param classifier something that classfied the user
	 * @param status the state to which the user was classified
	 * @return list of classified users
	 * @param limit 
	 * @author sts
	 */
	public List<User> getClassifiedUsers(Classifier classifier, SpamStatus status, int limit);

	/**
	 * Returns number of classfied user 
	 * 
	 * @param classifier the classifier
	 * @param status the status classifed
	 * @param interval 
	 * @return count of users
	 */
	public int getClassifiedUserCount(Classifier classifier, SpamStatus status, int interval);
	
	/**
	 * Returns the value of the specified classifier setting
	 * 
	 * @param key The key for which to retrieve the value for
	 * @return The setting value
	 */
	public String getClassifierSettings(ClassifierSettings key);

	/**
	 * Updates the specified classifier setting
	 * 
	 * @param key the setting to update
	 * @param value the new setting value
	 */
	public void updateClassifierSettings(ClassifierSettings key, final String value);	

	/**
	 * Returns the history of classifier predictions 
	 * 
	 * @param userName the user  
	 * @return prediction history
	 */
	public List<User> getClassifierHistory(String userName);

	/**
	 * Retrieves a comparison of classification results
	 * of admins and the automatic classifier
	 * 
	 * @param interval 
	 * @param limit - the number of users to return
	 * @return Userlist with spammer flag of admin and prediction of classifier 
	 */
	public List<User> getClassifierComparison(int interval, int limit);
	
	/**
	 * Returns a username corresponding to a given openid
	 * 
	 * @param openID
	 * @return username
	 */
	public String getOpenIDUser(final String openID);

	/**
	 * Create an extended field for a publication
	 * 
	 * @param resourceType - the type of resource for which the extended field shall be created 
	 * @param userName
	 * @param intraHash
	 * @param key
	 * @param value
	 */
	public void createExtendedField(Class<? extends Resource> resourceType, String userName, String intraHash, String key,String value);

	/**
	 * Delete an extended field for a publication
	 * 
	 * @param resourceType - the type of resource for which the extended field shall be created
	 * @param userName
	 * @param intraHash
	 * @param key
	 * @param value
	 */
	public void deleteExtendedField(Class<? extends Resource> resourceType, String userName, String intraHash, String key, String value);
	
	/**
	 * Get all or just specific extended fields for a given publication
	 * 
	 * @param resourceType - the type of resource for which the extended field shall be created
	 * @param userName
	 * @param intraHash
	 * @param key
	 * @return Map with the extended fields
	 */
	public Map<String, List<String>> getExtendedFields(Class<? extends Resource> resourceType, String userName, String intraHash, String key);

	
	/**
	 * Retrieve relations
	 * 
	 * @param resourceType - the reqtested resourcetype
	 * @param grouping - grouping entity
	 * @param groupingName - the grouping name
	 * @param regex - a regex to possibly filter the relatons retrieved
	 * @param tags - a list of tags which shall be part of the relations
	 * @param status - the conceptstatus, i.e. all, picked or unpicked
	 * @param start - start index
	 * @param end - end index
	 * @return a list of concepts, i.e. tags containing their assigned subtags
     * @author dbe
	 */
	public List<Tag> getConcepts(Class<? extends Resource> resourceType, GroupingEntity grouping, String groupingName, String regex, List<String> tags, ConceptStatus status, int start, int end);
	
	/**
	 * Retrieve the number of relations from a user
	 * 
	 * @param resourceType
	 * @param grouping
	 * @param groupingName
	 * @param regex
	 * @param tags
	 * @param status
	 * @param start
	 * @param end
	 * @return the number of relations from a user
	 */
	public int getTagStatistics(Class<? extends Resource> resourceType, GroupingEntity grouping, String groupingName, String regex, List<String> tags, ConceptStatus status, int start, int end);

	/** 
	 * We return all Users that are in (the) relation with the sourceUser
	 * as targets.
	 * @param sourceUser - leftHandSide of the relation
	 * @param relation - the User relation
	 * @param tag - relationships can also be tagged (e.g. for customized friendship lists or external friends from facebook etc)
	 * @return all rightHandsides, that is all Users u with
	 * (sourceUser, u)\in relation
	 */
	public List<User> getUserRelationship(String sourceUser, UserRelation relation, String tag);
	
	/**
	 * We delete a UserRelation of the form (sourceUser, targetUser)\in relation
	 * sourceUser should be logged in to have access to this
	 * 
	 * @param sourceUser - leftHandSide of the relation
	 * @param targetUser - rightHandSie of the relation 
	 * @param relation - the type of the relation
	 * @param tag - relations can also be tagged
	 * 
	 */
	public void deleteUserRelationship(String sourceUser, String targetUser, UserRelation relation, String tag);
	
	/**
	 * We create a UserRelation of the form (sourceUser, targetUser)\in relation
	 * sourceUser should be logged in for this
	 * 
	 * @param sourceUser - leftHandSide of the relation
	 * @param targetUser - rightHandSie of the relation 
	 * @param relation - the type of the relation
	 * @param tag - relations can also be tagged
	 */
	public void createUserRelationship(String sourceUser, String targetUser, UserRelation relation, String tag);
	
	/**
	 * Create basket items
	 * 
	 * @param posts - list of posts which should be added to the basket
	 * @return size of basket
	 */
	public int createBasketItems(List<Post<? extends Resource>> posts);
	
	/**
	 * Delete basket items 
	 * 
	 * @param posts - list of posts which should be deleted from the basket
	 * @param clearBasket - this should be true if the whole basket should be dropped, in all other cases false. It's necessary because 
	 * 		you have to differ if you want to delete some posts or all. This parameter is true if you call the "Remove all from Basket"-link
	 * 		on the new basket page.
	 * @return size of basket
	 */
	public int deleteBasketItems(List<Post<? extends Resource>> posts, boolean clearBasket);
	
	/**
	 * Delete Messages from the inbox by resourceHash sender and receiver
	 * @param posts 
	 * @param clearInbox 
	 * @return the new size of the inbox
	 */
	public int deleteInboxMessages(final List<Post<? extends Resource>> posts, final boolean clearInbox);	
	
	/**
	 * Retrieves bibsonomy username for given ldap user id
	 * @param userId User ID 
	 * @return username
	 */
	public String getUsernameByLdapUserId(String userId);	
}