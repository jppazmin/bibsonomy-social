/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.managers;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.GroupID;
import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.common.errors.MissingTagsErrorMessage;
import org.bibsonomy.common.exceptions.ValidationException;
import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.database.common.params.beans.TagIndex;
import org.bibsonomy.database.managers.chain.tag.TagChain;
import org.bibsonomy.database.params.TagParam;
import org.bibsonomy.database.plugin.DatabasePluginRegistry;
import org.bibsonomy.database.systemstags.SystemTagsExtractor;
import org.bibsonomy.database.util.DatabaseUtils;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.services.searcher.ResourceSearch;

/**
 * Used to retrieve tags from the database.
 * 
 * @author Dominik Benz
 * @author Miranda Grahl
 * @author Jens Illig
 * @author Christian Schenk
 * @version $Id: TagDatabaseManager.java,v 1.100 2011-07-25 12:16:33 folke Exp $
 */
public class TagDatabaseManager extends AbstractDatabaseManager {
	private static final Log log = LogFactory.getLog(TagDatabaseManager.class);

	private static final int MAX_TAG_SIZE = 5;

	private final static TagDatabaseManager singleton = new TagDatabaseManager();
	private static final TagChain chain = new TagChain();

	/** database managers */
	private final GeneralDatabaseManager generalDb;
	private final TagRelationDatabaseManager tagRelDb;
	private final DatabasePluginRegistry plugins;

	/** interface to a resource searcher for building an tag cloud */
	private ResourceSearch<BibTex> publicationSearch;
	private ResourceSearch<Bookmark> bookmarkSearch;

	/**
	 * @return a singleton instance of the TagDatabaseManager
	 */
	public static TagDatabaseManager getInstance() {
		return singleton;
	}

	private TagDatabaseManager() {
		this.generalDb = GeneralDatabaseManager.getInstance();
		this.tagRelDb = TagRelationDatabaseManager.getInstance();
		this.plugins = DatabasePluginRegistry.getInstance();
	}	

	/**
	 * @return the publicationSearch
	 */
	public ResourceSearch<BibTex> getPublicationSearch() {
		return this.publicationSearch;
	}

	/**
	 * @param publicationSearch the publicationSearch to set
	 */
	public void setPublicationSearch(final ResourceSearch<BibTex> publicationSearch) {
		this.publicationSearch = publicationSearch;
	}

	/**
	 * @return the bookmarkSearch
	 */
	public ResourceSearch<Bookmark> getBookmarkSearch() {
		return this.bookmarkSearch;
	}

	/**
	 * @param bookmarkSearch the bookmarkSearch to set
	 */
	public void setBookmarkSearch(final ResourceSearch<Bookmark> bookmarkSearch) {
		this.bookmarkSearch = bookmarkSearch;
	}

	/** 
	 * Return tag for given tagId
	 *  
	 * @param tagId 
	 * @param session 
	 * @return tag for given id
	 */
	public Tag getTagById(final Integer tagId, final DBSession session) {
		return this.queryForObject("getTagById", tagId, Tag.class, session);
	}

	/**
	 * Return all tags for a given tag count
	 * @param param 
	 * @param session 
	 * @return list of tags
	 */
	// FIXME a single tag should be returned instead of a list, shouldn't it?
	public List<Tag> getTagByCount(final TagParam param, final DBSession session) {
		// TODO not tested
		return this.queryForList("getTagByCount", param, Tag.class, session);
	}

	private void updateTagDec(final String tagname, final DBSession session) {
		this.update("updateTagDec", tagname, session);
	}

	/** 
	 * Inserts the TAS into the tas table. If the post is viewable
	 * for more than one group, the first group is inserted into the
	 * tas table.
	 * 
	 * @param param
	 * @param session
	 */
	private void insertTas(final TagParam param, final DBSession session) {
		final Integer firstGroup = param.getGroups().get(0);
		/*
		 * If no group is given, something went wrong ... so we throw an
		 * exception. 
		 */
		if (!present(firstGroup)) {
			throw new ValidationException("No group for TAS given");
		}
		/*
		 * if a post is visible for more than one group, 
		 * only insert an entry for the first group in the tas table.
		 */
		param.setGroupId(firstGroup);

		/*
		 * for each tag, insert a new TAS
		 */
		for (final Tag tag : param.getTags()) {
			param.setTag(tag);
			param.setTasId(generalDb.getNewId(ConstantID.IDS_TAS_ID, session));
			this.insert("insertTas", param, session);
		}
	}

	/**
	 * For each group a post is visible, store an entry in the grouptas table.
	 * @param param
	 * @param session
	 */
	private void insertGroupTas(final TagParam param, final DBSession session) {
		for (final Tag tag : param.getTags()) {
			param.setTag(tag);
			/*
			 * for each group, insert a new row for that tag
			 */
			for (final Integer groupId : param.getGroups()) {
				param.setGroupId(groupId);
				param.setTasId(generalDb.getNewId(ConstantID.IDS_GROUPTAS_ID, session));
				this.insert("insertGroupTas", param, session);
			}
		}
	}

	/**
	 * Deletes the tags from the given post.
	 * 
	 * @param post
	 * @param session
	 */
	public void deleteTags(final Post<?> post, final DBSession session) {
		// add these tags to list and decrease counter in tag table
		for (final Tag tag : post.getTags()) {
			// decrease counter in tag table
			this.updateTagDec(tag.getName(), session);
		}

		// TODO: log all tas related to this post -> this.insertLogTas(...)
		this.plugins.onTagDelete(post.getContentId(), session);
		// delete all tas related to this post
		this.deleteTas(post.getContentId(), session);
		this.deleteGroupTas(post.getContentId(), session);
	}

	private void deleteTas(final Integer contentId, final DBSession session) {
		this.delete("deleteTas", contentId, session);
	}

	private void deleteGroupTas(final Integer contentId, final DBSession session) {
		this.delete("deleteGroupTas", contentId, session);
	}

	
	/**
	 * Checks if the post as at least one tag,
	 * adds MissingTagsErrorMessage else
	 * @param post
	 * @param session
	 */
	private void checkTags(final Post<?> post, final DBSession session) {
		if (!present(post.getTags())) {
			session.addError(post.getResource().getIntraHash(), new MissingTagsErrorMessage());
			log.warn("Added missingTagsErrorMessage for " + post.getResource().getIntraHash());
		}
	}
	
	/**
	 * Inserts the tags from the given post.
	 * 
	 * @param post
	 * @param session
	 */
	public void insertTags(final Post<?> post, final DBSession session) {
		this.checkTags(post, session);
		final TagParam tagParam = new TagParam();
		tagParam.setTags(post.getTags());
		/*
		 * FIXME: The content id is not always new, in particular on updates. 
		 * Thus the naming of this attribute is a bit unfortunate.
		 */
		tagParam.setNewContentId(post.getContentId());
		tagParam.setContentTypeByClass(post.getResource().getClass());
		tagParam.setUserName(post.getUser().getName());
		tagParam.setDate(post.getDate());
	
		
		/*
		 * get changeDate from Post
		 */
		Date changeDate = post.getChangeDate();
		if(!present(changeDate)) {
			changeDate = new Date();
		}
		tagParam.setChangeDate(changeDate);
		
		final List<Integer> groups = new ArrayList<Integer>();
		/*
		 * copy the groups' ids into the param
		 */
		for (final Group group : post.getGroups()) {
			groups.add(group.getGroupId());
		}
		tagParam.setGroups(groups);
		this.insertTags(tagParam, session);
	}

	/** Updates the posts by replacing all tags as described in {@link LogicInterface#updateTags(User, List, List, boolean)}.
	 * 
	 * TODO: This method hasn't been tested, yet - it has been written
	 * from scratch to migrate the functionality of the /edittags-page.
	 * 
	 * In particular, it very probably uses some old methods to 
	 * insert/update/delete tags and completely ignores grouptas.
	 * 
	 * @param user
	 * @param tagsToReplace
	 * @param replacementTags
	 * @param session 
	 * @return The number of posts which got updated.
	 */
	@SuppressWarnings("unchecked")
	public int updateTags(final User user, final List<Tag> tagsToReplace, final List<Tag> replacementTags, final DBSession session) {
		/*
		 * we might need the empty tag for posts where no tags remain ...
		 */
		final Tag emptyTag = TagUtils.getEmptyTag();
		/*
		 * First: get all posts which need to be updated (i.e., which have all tags from tagsToReplace assigned)
		 * since we're not interested in the resource, we need only data from the TAS table, i.e., we need TAS.
		 */
		final TagParam param = new TagParam();
		for (final Tag tag: tagsToReplace) {
			param.addTagName(tag.getName());
		}
		param.setUserName(user.getName());
		final List<Post<? extends Resource>> posts = this.queryForList("getTASByTagNames", param, session);
		log.debug("################################################################################");
		log.debug(posts);
		log.debug("################################################################################");

		/*
		 * FIXME: shall getting the posts be included in the transaction?
		 */
		session.beginTransaction();
		try {

			/*
			 * all changed posts should get the same change date
			 */
			final Date changeDate = new Date();

			/*
			 * iterate over all posts and exchange their tags
			 */
			for (final Post<? extends Resource> post: posts) {
				log.debug("handling post with content id " + post.getContentId() + " and groups " + post.getGroups());

				final Set<Tag> tags = post.getTags();
				log.debug("  current tags: " + tags);
				/*
				 * removing tags
				 * 
				 * TODO: Case is important here, e.g., "kassel" is not removed, 
				 * if "KASSEL" is contained in tagsToReplace.
				 * 
				 * Probably this is the way it should work - we have to discuss this.
				 * (Although, it might be nice to have a switch to say "ignore case".)
				 */
				tags.removeAll(tagsToReplace);
				/*
				 * adding tags
				 */
				tags.addAll(replacementTags);
				log.debug("  new tags: " + tags);
				/*
				 * Since replacementTags is allowed to be empty (i.e., to remove certain tags),
				 * we must check here, if the post still contains some tags. If not - we add 
				 * the empty tag.
				 */
				if (tags.isEmpty())	tags.add(emptyTag);
				/*
				 * Finally: delete the TAS and insert the new TAS.
				 * FIXME: delete group tas, too.
				 */
				this.deleteTags(post, session);

				final TagParam tagParam = new TagParam();
				tagParam.setTags(post.getTags());
				tagParam.setNewContentId(post.getContentId());
				final Class<? extends Resource> class1 = post.getResource().getClass();
				log.debug("  post has class " + class1);
				tagParam.setContentTypeByClass(class1);
				tagParam.setUserName(post.getUser().getName());
				tagParam.setDate(post.getDate());
				tagParam.setChangeDate(changeDate);
				/*
				 * FIXME: we don't have the groups from the grouptas available ... :-(
				 * How can we get them to insert the new grouptas? Probably we need
				 * a query "getGroupsByContentId" or something similar. First check,
				 * if we have something like that already.
				 */
				final List<Integer> groups = new ArrayList<Integer>();
				final Set<Group> groups2 = post.getGroups();
				for (final Group group : groups2) {
					groups.add(group.getGroupId());
				}
				tagParam.setGroups(groups);

				/*
				 * FIXME: shouldn't we use insertTags() here?
				 * (because otherwise grouptas are not updated 
				 */
				this.insertTags(tagParam, session);
			}

			session.commitTransaction();
		} finally {
			session.endTransaction();
		}

		if (log.isDebugEnabled()) {

			/*
			 * test: check tags
			 */
			final TagParam paramNew = new TagParam();
			for (final Tag tag: replacementTags) {
				paramNew.addTagName(tag.getName());
			}
			paramNew.setUserName(user.getName());
			final List<Post<? extends Resource>> postsNew = this.queryForList("getTASByTagNames", paramNew, session);
			log.debug("################################################################################");
			log.debug(postsNew);
			log.debug("################################################################################");
		}

		/*
		 * return the number of updated posts
		 */
		return posts.size();
	}

	/**
	 * Insert a set of tags for a content (into tas table and what else is
	 * required)
	 */
	private void insertTags(final TagParam param, final DBSession session) {
		// generate a list of tags
		final Collection<Tag> allTags = param.getTags();

		session.beginTransaction();
		try {
			this.insertTas(param, session);

			/* 
			 * if post is visible for a non exclusive group, store for each group
			 * and each tag one entry in the grouptas table 
			 */
			final int firstGroup = param.getGroups().iterator().next();
			if (!GroupUtils.isExclusiveGroup(firstGroup)) {
				/*
				 * first group found is neither public nor private ... so we
				 * have to fill the group tas table!
				 */
				this.insertGroupTas(param, session);
			}

			for (final Tag tag : param.getTags()) {
				this.tagRelDb.insertRelations(tag, param.getUserName(), session);				
			}

			for (final Tag tag : allTags) {
				this.insertTag(tag, session);
			}

			session.commitTransaction();
		} finally {
			session.endTransaction();
		}
	}

	/**
	 * Increases the tag counter in the tag table for the given tag. If this tag
	 * does not exist inside the tag table, inserts it with count 1.
	 * 
	 * @param tag 
	 * @param session 
	 */
	public void insertTag(final Tag tag, final DBSession session) {
		// TODO not tested
		this.insert("insertTag", tag, session);
	}

	/**
	 * @param param
	 * @param session
	 * @return list of sub tags
	 */
	public List<Tag> getSubtagsOfTag(final TagParam param, final DBSession session) {
		return this.queryForList("getSubtagsOfTag", param, Tag.class, session);
	}

	/**
	 * @param param
	 * @param session
	 * @return list of super tags
	 */
	public List<Tag> getSupertagsOfTag(final TagParam param, final DBSession session) {
		return this.queryForList("getSupertagsOfTag", param, Tag.class, session);
	}

	/**
	 * @param param
	 * @param session
	 * @return list of correlated tags
	 */
	public List<Tag> getCorrelatedTagsOfTag(final TagParam param, final DBSession session) {
		return this.queryForList("getCorrelatedTagsOfTag", param, Tag.class, session);
	}

	/**
	 * Returns all tags.
	 * 
	 * @param param
	 * @param session
	 * @return all tags
	 */
	public List<Tag> getAllTags(final TagParam param, final DBSession session) {
		return this.queryForList("getAllTags", param, Tag.class, session);
	}

	/**
	 * Return a tag by its tag name
	 * 
	 * @param param
	 * @param session
	 * @return tag
	 */
	private Tag getTagByName(final TagParam param, final DBSession session) {
		return this.queryForObject("getTagByName", param, Tag.class, session);
	}

	/**
	 * Returns details about a tag. Those details are:
	 * <ul>
	 * <li>details about the tag itself, like number of occurrences etc</li>
	 * <li>list of subtags</li>
	 * <li>list of supertags</li>
	 * <li>list of correlated tags</li>
	 * </ul>
	 * 
	 * FIXME: is this global or for a given user/group only?
	 * 
	 * FIXME: I think this method needs to be cleaned up an commented ...
	 * 
	 * @param param
	 * @param session
	 * @return the tag's details, null else
	 */
	public Tag getTagDetails(final TagParam param, final DBSession session) {
		param.setCaseSensitiveTagNames(true);

		final Tag tag = this.getTagByName(param, session);

		/*
		 * retrieve all sub-/supertags
		 */
		param.setLimit(10000);
		param.setOffset(0);

		// check for sub-/supertags
		if (param.getNumSimpleConcepts() > 0) {
			final List<Tag> subTags = this.getSubtagsOfTag(param, session);
			tag.setSubTags(setUsercountToGlobalCount(subTags));
		}
		if (param.getNumSimpleConceptsWithParent() > 0) {
			final List<Tag> superTags = this.getSupertagsOfTag(param, session);
			tag.setSuperTags(setUsercountToGlobalCount(superTags));
		}
		if (param.getNumCorrelatedConcepts() > 0) {
			final List<Tag> subTags = this.getSubtagsOfTag(param, session);
			tag.setSubTags(setUsercountToGlobalCount(subTags));
			final List<Tag> superTags = this.getSupertagsOfTag(param, session);
			tag.setSuperTags(setUsercountToGlobalCount(superTags));
		}

		// XXX: this is just a hack as long as we don't supply separate user
		// counts for each tag, DB
		if (present(tag)) {
			tag.setUsercount(tag.getGlobalcount());
		}

		return tag;
	}

	/**
	 * Get all tags of a given user.
	 * 
	 * @param param 
	 * @param session 
	 * @return list of tags
	 */
	public List<Tag> getTagsByUser(final TagParam param, final DBSession session) {
		/* 
		 * another DBLP extra sausage - don't query DB for tags (as only "dblp"
		 * will be returned anyways), but return that directly
		 *
		 * TODO: maybe we put all the DBLP stuff into one class?
		 * Then we could do here:
		 * if (DBLP.isDBLPUser(param.getRequestedUserName()) {
		 *    return DBLP.getDBLPTag();
		 * }
		 */
		if ("dblp".equalsIgnoreCase(param.getRequestedUserName())) {
			final List<Tag> tags = new ArrayList<Tag>();
			final Tag dblp = new Tag();
			dblp.setName("dblp");
			dblp.setGlobalcount(1000000);
			dblp.setUsercount(1000000);
			tags.add(dblp);
			return tags;
		}

		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.queryForList("getTagsByUser", param, Tag.class, session);
	}

	/**
	 * Get all tags of a an author, which assigned to the authors
	 * entries/currently, the cloud is ordered alphabetically.
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getTagsByAuthor(final TagParam param, final DBSession session) {
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);

		final long starttimeQuery = System.currentTimeMillis();
		final List<Tag> retVal = this.queryForList("getTagsByAuthor", param, Tag.class, session);
		log.debug("DB author tag cloud query time: " + (System.currentTimeMillis() - starttimeQuery) + " ms");

		return retVal;
	}

	/**
	 * returns all tags assigned to posts which are matching the given query
	 * 
	 * @param userName
	 * @param requestedUserName
	 * @param requestedGroupName
	 * @param allowedGroups
	 * @param searchTerms
	 * @param titleSearchTerms
	 * @param authorSearchTerms
	 * @param tagIndex
	 * @param year
	 * @param firstYear
	 * @param lastYear
	 * @param limit
	 * @param offset
	 * @return a list of tags
	 */
	public List<Tag> getTagsByResourceSearch(
			final String userName, final String requestedUserName, final String requestedGroupName, 
			final Collection<String> allowedGroups,
			final String searchTerms, final String titleSearchTerms, final String authorSearchTerms, final Collection<String> tagIndex,
			final String year, final String firstYear, final String lastYear, final int limit, final int offset) {
		
		if (present(this.publicationSearch) && present(this.bookmarkSearch) ) {
			final List<Tag> bookmarkTags = 
				bookmarkSearch.getTags(userName, requestedUserName, requestedGroupName, allowedGroups, searchTerms, titleSearchTerms, authorSearchTerms, tagIndex, year, firstYear, lastYear, limit, offset);
			final List<Tag> publicationTags =
				publicationSearch.getTags(userName, requestedUserName, requestedGroupName, allowedGroups, searchTerms, titleSearchTerms, authorSearchTerms, tagIndex, year, firstYear, lastYear, limit, offset);
			final List<Tag> retVal = TagUtils.mergeTagLists(bookmarkTags, publicationTags, Order.POPULAR, Order.POPULAR, limit);
			return retVal;
		}
		
		log.error("no resource searcher is set");		
		return new LinkedList<Tag>();
	}

	/**
	 * Get all tags of a given group
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getTagsByGroup(final TagParam param, final DBSession session) {
		DatabaseUtils.prepareGetPostForGroup(this.generalDb, param, session);
		return this.queryForList("getTagsByGroup", param, Tag.class, session);
	}

	/**
	 * Get all tags of a given regular expression
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getTagsByExpression(final TagParam param, final DBSession session) {
		return this.queryForList("getTagsByExpression", param, Tag.class, session);
	}

	/**
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getTagsViewable(final TagParam param, final DBSession session) {
		if (GroupID.isSpecialGroupId(param.getGroupId()) == true) {
			// show users own tags, which are private, public or for friends
			param.setRequestedUserName(param.getUserName());
			return this.queryForList("getTagsViewableBySpecialGroup", param, Tag.class, session);
		}
		return this.queryForList("getTagsViewable", param, Tag.class, session);
	}

	/**
	 * Get related tags for a given tag or list of tags for a specified group
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getRelatedTagsForGroup(final TagParam param, final DBSession session) {
		// check maximum number of tags
		if (this.exceedsMaxSize(param.getTagIndex())) {
			return new ArrayList<Tag>();
		}		
		return this.queryForList("getRelatedTagsForGroup", param, Tag.class, session);
	}

	/**
	 * Tet related tags from a given user and a given list of tags.
	 * 
	 * @param userName 
	 * @param requestedUserName
	 * @param tagIndex
	 * @param visibleGroupIDs
	 * @param limit 
	 * @param offset 
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getRelatedTagsForUser(final String userName, final String requestedUserName, final List<TagIndex> tagIndex, final List<Integer> visibleGroupIDs, final int limit, final int offset, final DBSession session) {
		// check maximum number of tags
		if (this.exceedsMaxSize(tagIndex)) {
			return new ArrayList<Tag>();
		}		
		final TagParam param = new TagParam();
		param.setUserName(userName);
		param.setRequestedUserName(requestedUserName);
		param.addGroups(visibleGroupIDs);
		param.setTagIndex(tagIndex);
		param.setLimit(limit);
		param.setOffset(offset);
		return this.queryForList("getRelatedTagsRestricted", param, Tag.class, session);
	}

	/**
	 * Get related tags for a given tag.
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getRelatedTagsViewable(final TagParam param, final DBSession session) {
		// check maximum number of tags
		if (this.exceedsMaxSize(param.getTagIndex())) {
			return new ArrayList<Tag>();
		}
		if (GroupID.isSpecialGroupId(param.getGroupId()) == true) {
			// for special groups, check additionally if tag is "owned"
			// by the logged-in user
			param.setRequestedUserName(param.getUserName());
		}
		return this.queryForList("getRelatedTagsViewable", param, Tag.class, session);
	}

	/**
	 * Main function (called from the Logic Interface) to retrieve tags - this
	 * function actually starts the chain of responsibility
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getTags(final TagParam param, final DBSession session) {
		final List<Tag> tags = chain.getFirstElement().perform(param, session);
		SystemTagsExtractor.removeHiddenSystemTags(tags);
		return this.setUsercountToGlobalCount(tags);
	}

	/**
	 * This is just a hack as long as we don't supply separate user counts for
	 * each tag, dbe
	 * 
	 * @param tags
	 *            a list of tags
	 * @return list of tags with usercount set to globalcount for each tag
	 */
	private List<Tag> setUsercountToGlobalCount(final List<Tag> tags) {
		for (final Tag tag : tags) {
			if (tag.getUsercount() == 0) {
				tag.setUsercount(tag.getGlobalcount());
			}
		}
		return tags;
	}

	/**
	 * Retrieve related tags.
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getRelatedTags(final TagParam param, final DBSession session) {
		// check maximum number of tags
		if (this.exceedsMaxSize(param.getTagIndex())) {
			return new ArrayList<Tag>();
		}
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.queryForList("getRelatedTags", param, Tag.class, session);
	}

	/**
	 * Retrieve related tags orderey by folkrank.
	 * 
	 * @param param
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getRelatedTagsOrderedByFolkrank(final TagParam param, final DBSession session) {
		// check maximum number of tags
		if (this.exceedsMaxSize(param.getTagIndex())) {
			return new ArrayList<Tag>();
		}
		return this.queryForList("getRelatedTagsOrderedByFolkrank", param, Tag.class, session);
	}

	/**
	 * Retrieve tags attached to a bookmark with a given hash.
	 * 
	 * @param loginUserName
	 * @param hash
	 * @param visibleGroupIDs 
	 * @param limit
	 * @param offset
	 * @param session
	 * @return a list of tags attached to the bookmark with the given hash
	 */
	public List<Tag> getTagsByBookmarkHash(final String loginUserName, final String hash, final List<Integer> visibleGroupIDs, final int limit, final int offset, final DBSession session) {
		final TagParam param = new TagParam();
		param.setHash(hash);
		param.setUserName(loginUserName);
		param.addGroups(visibleGroupIDs);
		param.setLimit(limit);
		param.setOffset(offset);
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.queryForList("getTagsByBookmarkHash", param, Tag.class, session);
	}

	/**
	 * Retrieve tags attached to a bookmark with a given hash for a given user.
	 * 
	 * @param loginUserName
	 * @param requestedUserName
	 * @param hash
	 * @param visibleGroupIDs 
	 * @param limit
	 * @param offset
	 * @param session
	 * @return a list of tags attached to the given user's bookmark with the given hash
	 */
	public List<Tag> getTagsByBookmarkHashForUser(final String loginUserName, final String requestedUserName, final String hash, final List<Integer> visibleGroupIDs, final int limit, final int offset, final DBSession session) {
		final TagParam param = new TagParam();
		param.setHash(hash);
		param.setUserName(loginUserName);
		param.addGroups(visibleGroupIDs);
		param.setRequestedUserName(requestedUserName);
		param.setLimit(limit);
		param.setOffset(offset);		
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.queryForList("getTagsByBookmarkHash", param, Tag.class, session);
	}

	/**
	 * Retrieve tags attached to a bibtex with the given hash.
	 * 
	 * @param loginUserName
	 * @param hash
	 * @param hashId
	 * @param visibleGroupIDs 
	 * @param limit
	 * @param offset
	 * @param session
	 * @return a list of tags attached to a bibtex with the given hash
	 */
	public List<Tag> getTagsByBibtexHash(final String loginUserName, final String hash, final HashID hashId, final List<Integer> visibleGroupIDs, final int limit, final int offset, final DBSession session) {
		final TagParam param = new TagParam();
		param.setHash(hash);
		param.setSimHash(hashId);
		param.setUserName(loginUserName);
		param.addGroups(visibleGroupIDs);
		param.setLimit(limit);
		param.setOffset(offset);		
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.queryForList("getTagsByBibtexHash", param, Tag.class, session);
	}

	/**
	 * Retrieve tags attached to a bibtex of a given user with the given hash.
	 * 
	 * @param loginUserName
	 * @param requestedUserName
	 * @param hash
	 * @param hashId
	 * @param visibleGroupIDs 
	 * @param limit
	 * @param offset
	 * @param session
	 * @return a list of tags attached to a given user's bibtex with the given hash
	 */
	public List<Tag> getTagsByBibtexHashForUser(final String loginUserName, final String requestedUserName, final String hash, final HashID hashId, final List<Integer> visibleGroupIDs, final int limit, final int offset, final DBSession session) {
		final TagParam param = new TagParam();
		param.setHash(hash);
		param.setSimHash(hashId);
		param.addGroups(visibleGroupIDs);		
		param.setUserName(loginUserName);
		param.setRequestedUserName(requestedUserName);
		param.setLimit(limit);
		param.setOffset(offset);		
		DatabaseUtils.prepareGetPostForUser(this.generalDb, param, session);
		return this.queryForList("getTagsByBibtexHash", param, Tag.class, session);
	}	

	/**
	 * Helper function to check maximum number of tags for which related tags
	 * are to be computed.
	 * 
	 * @param index
	 * @return true if maximum number is exeeded, false otherwise
	 */
	private boolean exceedsMaxSize(final List<TagIndex> index) {
		return index != null && index.size() > MAX_TAG_SIZE;
	}

	/**
	 * Returns a list of similar tags.
	 * 
	 * @param tagIndex
	 * @param visibleGroupIDs
	 * @param limit
	 * @param offset
	 * @param session
	 * @return list of tags
	 */
	public List<Tag> getSimilarTags(final List<TagIndex> tagIndex, final List<Integer> visibleGroupIDs, final int limit, final int offset, final DBSession session) {
		final TagParam param = new TagParam();
		param.setTagName(tagIndex.get(0).getTagName()); // index 0 is always present, because otherwise the calling chain element won't answer
		param.setGroups(visibleGroupIDs);
		param.setLimit(limit);
		param.setOffset(offset);
		return this.queryForList("getSimilarTags", param, Tag.class, session);
	} 

	/**
	 * See getAllTags
	 * 
	 * @param param
	 * @param session
	 * @return all pupular tags
	 */
	public List<Tag> getTagsPopular(final TagParam param, final DBSession session){
		return this.queryForList("getTagsPopular", param, Tag.class, session);
	}

	/**
	 * Gets list of global popular tags (no restriction in days)
	 * 
	 * @param param
	 * @param session
	 * @return list of popular tags
	 */
	public List<Tag> getPopularTags(final TagParam param, final DBSession session){
		return this.queryForList("getPopularTags", param, Tag.class, session);
	}

	/**
	 * @param param
	 * @param session
	 * @return list of tags from a given friend of a given user
	 */
	public List<Tag> getTagsByFriendOfUser(final TagParam param, final DBSession session) {
		return this.queryForList("getTagsByFriendOfUser", param, Tag.class, session);
	}
	
	/**
	 * @param param
	 * @param session
	 * @return list of tags from a given friend of a given user
	 */
	public List<Tag> getTagsByTaggedUserRelation(final TagParam param, final DBSession session) {
		return this.queryForList("getTagsByTaggedUserRelation", param, Tag.class, session);
	}

	/**
	 * Retrieve tags for a given bibtexkey
	 * 
	 * @param bibtexKey
	 * 			- the requested key
	 * @param visibleGroupIDs
	 * 			- the groups the logged-in user is allowed to see
	 * @param requestedUserName
	 * 			- retrieve only tags of this user  
	 * @param loginUserName 
	 * @param session
	 * 			- the DB session
	 * @param limit 
	 * @param offset
	 * @return a list of tags, used to annotate the bibtex(s) with the given bibtex key (eventually by the requested user)
	 */
	public List<Tag> getTagsByBibtexkey(final String bibtexKey, final List<Integer> visibleGroupIDs, final String requestedUserName, final String loginUserName, final int limit, final int offset, final DBSession session) {
		final TagParam param = new TagParam();
		param.setBibtexKey(bibtexKey);
		param.setGroups(visibleGroupIDs);
		param.setUserName(loginUserName);		
		param.setRequestedUserName(requestedUserName);
		param.setLimit(limit);
		param.setOffset(offset);
		return this.queryForList("getTagsByBibtexkey", param, Tag.class, session);
	}
}