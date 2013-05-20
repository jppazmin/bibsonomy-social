/**
 *
 *  BibSonomy-Rest-Client - The REST-client.
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

package org.bibsonomy.rest.client;

import java.net.InetAddress;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.Classifier;
import org.bibsonomy.common.enums.ClassifierSettings;
import org.bibsonomy.common.enums.ConceptStatus;
import org.bibsonomy.common.enums.ConceptUpdateOperation;
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupUpdateOperation;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.InetAddressStatus;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.enums.SpamStatus;
import org.bibsonomy.common.enums.StatisticsConstraint;
import org.bibsonomy.common.enums.TagSimilarity;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.common.enums.UserUpdateOperation;
import org.bibsonomy.model.Author;
import org.bibsonomy.model.DiscussionItem;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.Wiki;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.rest.client.queries.delete.DeleteGroupQuery;
import org.bibsonomy.rest.client.queries.delete.DeletePostQuery;
import org.bibsonomy.rest.client.queries.delete.DeleteUserQuery;
import org.bibsonomy.rest.client.queries.delete.RemoveUserFromGroupQuery;
import org.bibsonomy.rest.client.queries.get.GetFriendsQuery;
import org.bibsonomy.rest.client.queries.get.GetGroupDetailsQuery;
import org.bibsonomy.rest.client.queries.get.GetGroupListQuery;
import org.bibsonomy.rest.client.queries.get.GetPostDetailsQuery;
import org.bibsonomy.rest.client.queries.get.GetPostDocumentQuery;
import org.bibsonomy.rest.client.queries.get.GetPostsQuery;
import org.bibsonomy.rest.client.queries.get.GetTagDetailsQuery;
import org.bibsonomy.rest.client.queries.get.GetTagsQuery;
import org.bibsonomy.rest.client.queries.get.GetUserDetailsQuery;
import org.bibsonomy.rest.client.queries.get.GetUserListOfGroupQuery;
import org.bibsonomy.rest.client.queries.get.GetUserListQuery;
import org.bibsonomy.rest.client.queries.post.AddUserToGroupQuery;
import org.bibsonomy.rest.client.queries.post.CreateGroupQuery;
import org.bibsonomy.rest.client.queries.post.CreatePostQuery;
import org.bibsonomy.rest.client.queries.post.CreateUserQuery;
import org.bibsonomy.rest.client.queries.post.CreateUserRelationshipQuery;
import org.bibsonomy.rest.client.queries.put.ChangeGroupQuery;
import org.bibsonomy.rest.client.queries.put.ChangePostQuery;
import org.bibsonomy.rest.client.queries.put.ChangeUserQuery;
import org.bibsonomy.util.ExceptionUtils;

public class RestLogic implements LogicInterface {
	private static final Log log = LogFactory.getLog(RestLogic.class);
	private final Bibsonomy bibsonomy;
	private final User authUser;

	public RestLogic(final String username, final String apiKey, final String apiURL) {
		this(username, apiKey);
		this.bibsonomy.setApiURL(apiURL);
	}

	public RestLogic(final String username, final String apiKey) {
		this.bibsonomy = new Bibsonomy(username, apiKey);
		this.authUser = new User(username);
	}

	private <T> T execute(final AbstractQuery<T> query) {
		try {
			bibsonomy.executeQuery(query);
		} catch (final Exception ex) {
			ExceptionUtils.logErrorAndThrowRuntimeException(log, ex, "unable to execute " + query.toString());
		}
		return query.getResult();
	}

	@Override
	public void addUserToGroup(final String groupName, final String userName) {
		// TODO: only the username is used, but a whole user object is
		// transmitted, so a dummy with only username is used here.
		// -> This could lead to future problems
		final User dummyUserObject = new User();
		dummyUserObject.setName(userName);
		execute(new AddUserToGroupQuery(groupName, dummyUserObject));
	}

	@Override
	public void deleteGroup(final String groupName) {
		execute(new DeleteGroupQuery(groupName));
	}

	@Override
	public void deletePosts(final String userName, final List<String> resourceHashes) {
		/*
		 * FIXME: this iteration should be done on the server, i.e.,
		 * DeletePostQuery should support several posts ... although it's
		 * probably not so simple.
		 */
		for (final String resourceHash : resourceHashes) {
			execute(new DeletePostQuery(userName, resourceHash));
		}
	}

	@Override
	public void deleteUser(final String userName) {
		execute(new DeleteUserQuery(userName));
	}

	@Override
	public User getAuthenticatedUser() {
		return this.authUser;
	}

	@Override
	public Group getGroupDetails(final String groupName) {
		return execute(new GetGroupDetailsQuery(groupName));
	}

	@Override
	public List<Group> getGroups(final int start, final int end) {
		return execute(new GetGroupListQuery(start, end));
	}

	@Override
	public Post<? extends Resource> getPostDetails(final String resourceHash, final String userName) {
		return execute(new GetPostDetailsQuery(userName, resourceHash));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Resource> List<Post<T>> getPosts(final Class<T> resourceType, final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final FilterEntity filter, final int start, final int end, final String search) {
		// TODO: clientside chain of responsibility
		final GetPostsQuery query = new GetPostsQuery(start, end);
		query.setGrouping(grouping, groupingName);
		query.setResourceHash(hash);
		query.setResourceType(resourceType);
		query.setTags(tags);
		return (List) execute(query);
	}

	@Override
	public Tag getTagDetails(final String tagName) {
		return execute(new GetTagDetailsQuery(tagName));
	}

	@Override
	public List<Tag> getTags(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final String regex, final List<String> tags, final String hash, final Order order, final int start, final int end, final String search, final TagSimilarity relation) {
		final GetTagsQuery query = new GetTagsQuery(start, end);
		query.setGrouping(grouping, groupingName);
		query.setFilter(regex);
		return execute(query);
	}

	@Override
	public User getUserDetails(final String userName) {
		return execute(new GetUserDetailsQuery(userName));
	}

	@Override
	public void deleteUserFromGroup(final String groupName, final String userName) {
		execute(new RemoveUserFromGroupQuery(userName, groupName));
	}

	@Override
	public String createGroup(final Group group) {
		return execute(new CreateGroupQuery(group));
	}

	@Override
	public List<String> createPosts(final List<Post<?>> posts) {
		/*
		 * FIXME: this iteration should be done on the server, i.e.,
		 * CreatePostQuery should support several posts ... although it's
		 * probably not so simple.
		 */
		final List<String> resourceHashes = new LinkedList<String>();
		for (final Post<?> post : posts) {
			resourceHashes.add(execute(new CreatePostQuery(this.authUser.getName(), post)));
		}
		return resourceHashes;
	}

	@Override
	public String createUser(final User user) {
		return execute(new CreateUserQuery(user));
	}

	@Override
	public String updateGroup(final Group group, final GroupUpdateOperation operation) {
		// groups cannot be renamed
		return execute(new ChangeGroupQuery(group.getName(), group));
	}

	@Override
	public List<String> updatePosts(final List<Post<?>> posts, final PostUpdateOperation operation) {
		/*
		 * FIXME: this iteration should be done on the server, i.e.,
		 * CreatePostQuery should support several posts ... although it's
		 * probably not so simple.
		 */
		final List<String> resourceHashes = new LinkedList<String>();
		for (final Post<?> post : posts) {
			// hashes are recalculated by the server
			resourceHashes.add(execute(new ChangePostQuery(this.authUser.getName(), post.getResource().getIntraHash(), post)));
		}
		return resourceHashes;
	}

	@Override
	public String updateUser(final User user, final UserUpdateOperation operation) {
		// accounts cannot be renamed
		return execute(new ChangeUserQuery(user.getName(), user));
	}

	@Override
	public String createDocument(final Document doc, final String resourceHash) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getDocument(final String userName, final String fileHash) {
		return null;
	}

	@Override
	public Document getDocument(final String userName, final String resourceHash, final String fileName) {
		/*
		 * FIXME: files are stored in /tmp and thus publicly readable! Make
		 * directory configurable!
		 */
		return execute(new GetPostDocumentQuery(userName, resourceHash, fileName, "/tmp/"));
	}

	@Override
	public void deleteDocument(final Document document, final String resourceHash) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createInetAddressStatus(final InetAddress address, final InetAddressStatus status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteInetAdressStatus(final InetAddress address) {
		// TODO Auto-generated method stub

	}

	@Override
	public InetAddressStatus getInetAddressStatus(final InetAddress address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tag> getConcepts(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final String regex, final List<String> tags, final ConceptStatus status, final int start, final int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createConcept(final Tag concept, final GroupingEntity grouping, final String groupingName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateConcept(final Tag concept, final GroupingEntity grouping, final String groupingName, final ConceptUpdateOperation operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteConcept(final String concept, final GroupingEntity grouping, final String groupingName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRelation(final String upper, final String lower, final GroupingEntity grouping, final String groupingName) {
		// TODO Auto-generated method stub

	}

	@Override
	public Tag getConceptDetails(final String conceptName, final GroupingEntity grouping, final String groupingName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsers(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final UserRelation relation, final String search, final int start, final int end) {
		// here we just simulate two possible answers of the user chain
		if (GroupingEntity.ALL.equals(grouping)) {
			return execute(new GetUserListQuery(start, end));
		}
		if (GroupingEntity.GROUP.equals(grouping)) {
			return execute(new GetUserListOfGroupQuery(groupingName, start, end));
		}
		log.error("grouping entity " + grouping.name() + " not yet supported in RestLogic implementation.");
		return null;
	}

	@Override
	public String getClassifierSettings(final ClassifierSettings key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateClassifierSettings(final ClassifierSettings key, final String value) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getClassifiedUserCount(final Classifier classifier, final SpamStatus status, final int interval) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> getClassifiedUsers(final Classifier classifier, final SpamStatus status, final int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getClassifierHistory(final String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getClassifierComparison(final int interval, final int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPostStatistics(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final FilterEntity filter, final int start, final int end, final String search, final StatisticsConstraint constraint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getOpenIDUser(final String openID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateTags(final User user, final List<Tag> tagsToReplace, final List<Tag> replacementTags, final boolean updateRelations) {
		return 0;
	}

	@Override
	public int getTagStatistics(final Class<? extends Resource> resourceType, final GroupingEntity grouping, final String groupingName, final String regex, final List<String> tags, final ConceptStatus status, final int start, final int end) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Author> getAuthors(final GroupingEntity grouping, final String groupingName, final List<String> tags, final String hash, final Order order, final FilterEntity filter, final int start, final int end, final String search) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserRelationship(final String sourceUser, final String targetUser, final UserRelation relation, final String tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createUserRelationship(final String sourceUser, final String targetUser, final UserRelation relation, final String tag) {
		/*
		 * Transform UserRelation into String.
		 * FIXME: shouldn't we do this in a nicer way?
		 */
		final String relationType;
		switch (relation) {
		case OF_FRIEND:
			relationType = CreateUserRelationshipQuery.FRIEND_RELATIONSHIP;
			break;
		case FOLLOWER_OF:
			relationType = CreateUserRelationshipQuery.FOLLOWER_RELATIONSHIP;
			break;
		default:
			throw new IllegalArgumentException("Only OF_FRIEND (for friend relations) and FOLLOWER_OF (for followers) are allowed values for the relation param." );
		}
		execute(new CreateUserRelationshipQuery(sourceUser, targetUser, relationType, tag));
	}

	@Override
	public List<User> getUserRelationship(final String sourceUser, final UserRelation relation, final String tag) {
		switch (relation) {
		case FRIEND_OF:
			return execute(new GetFriendsQuery(0, 100, sourceUser, GetFriendsQuery.INCOMING_ATTRIBUTE_VALUE_RELATION));
		case OF_FRIEND:
			return execute(new GetFriendsQuery(0, 100, sourceUser, GetFriendsQuery.OUTGOING_ATTRIBUTE_VALUE_RELATION));
		default:
			throw new UnsupportedOperationException("The user relation " + relation + " is currently not supported.");
		}
	}

	@Override
	public int createBasketItems(final List<Post<? extends Resource>> posts) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteBasketItems(final List<Post<? extends Resource>> posts, final boolean clearAll) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteInboxMessages(final List<Post<? extends Resource>> posts, final boolean clearInbox) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getUsernameByLdapUserId(final String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createReferences(final String postHash, final Set<String> references) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteReferences(final String postHash, final Set<String> references) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Date> getWikiVersions(final String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Wiki getWiki(final String userName, final Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createWiki(final String userName, final Wiki wiki) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateWiki(final String userName, final Wiki wiki) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteWiki(final String userName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void createExtendedField(final Class<? extends Resource> resourceType, final String userName, final String intraHash, final String key, final String value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteExtendedField(final Class<? extends Resource> resourceType, final String userName, final String intraHash, final String key, final String value) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, List<String>> getExtendedFields(final Class<? extends Resource> resourceType, final String userName, final String intraHash, final String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createDiscussionItem(final String interHash, final String username, final DiscussionItem comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDiscussionItem(final String username, final String interHash, final DiscussionItem discussionItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDiscussionItem(final String username, final String interHash, final String commentHash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DiscussionItem> getDiscussionSpace(final String interHash) {
		// TODO Auto-generated method stub
		return null;
	}
}