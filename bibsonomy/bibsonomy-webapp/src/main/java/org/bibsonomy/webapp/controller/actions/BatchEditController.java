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

package org.bibsonomy.webapp.controller.actions;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.common.errors.DuplicatePostErrorMessage;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.errors.SystemTagErrorMessage;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.factories.ResourceFactory;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.actions.BatchEditCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.util.spring.security.exceptions.AccessDeniedNoticeException;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;


/**
 * Controller to batch edit (update tags and delete) resources.
 * 
 * The controller handles two cases:
 * <ol>
 * <li>the given posts should be updated (and eventually some posts deleted - if the user flagged them)</li>
 * <li>the given posts should be stored (and eventually some posts ignored - if the user flagged them)</li>
 * </ol>
 * 
 * @author dzo
 * @author ema
 * @version $Id: BatchEditController.java,v 1.35 2011-07-14 13:41:46 nosebrain Exp $
 */
public class BatchEditController implements MinimalisticController<BatchEditCommand>, ErrorAware {
	private static final Log log = LogFactory.getLog(BatchEditController.class);

	private static final int HASH_LENGTH = 32;

	/**
	 * To redirect the user to the page she initially viewed before pressing
	 * the (batch)"edit" button, we need to strip the "bedit*" part of the URL
	 * using this pattern.  
	 */
	private static final Pattern BATCH_EDIT_URL_PATTERN = Pattern.compile("(bedit[a-z,A-Z]+/)");

	/*
	 * TODO: inject using spring?!
	 */
	private static final ResourceFactory RESOURCE_FACTORY = new ResourceFactory();
	
	/**
	 * 
	 * @param resourceClass
	 * @return the old resource name
	 */
	@Deprecated // TODO: remove as soon as bibtex is renamed to puplications in SimpleResourceViewCommand
	public static String getOldResourceName(final Class<? extends Resource> resourceClass) {
		if (BibTex.class.equals(resourceClass)) {
			return "bibtex";
		}
		return ResourceFactory.getResourceName(resourceClass);
	}
	
	
	private RequestLogic requestLogic;
	private LogicInterface logic;

	private Errors errors;

	@Override
	public BatchEditCommand instantiateCommand() {
		final BatchEditCommand command = new BatchEditCommand();
		command.setOldTags(new HashMap<String, String>());
		command.setNewTags(new HashMap<String, String>());
		command.setDelete(new HashMap<String, Boolean>());

		command.getBibtex().setList(new LinkedList<Post<BibTex>>());
		command.getBookmark().setList(new LinkedList<Post<Bookmark>>());
		return command;
	}

	@Override
	public View workOn(final BatchEditCommand command) {
		final RequestWrapperContext context = command.getContext();

		/*
		 * We store the referer in the command, to send the user back to the 
		 * page he's coming from at the end of the posting process. 
		 */
		if (!present(command.getReferer())) {
			command.setReferer(requestLogic.getReferer());
		}

		/*
		 * check if user is logged in
		 */
		if (!context.isUserLoggedIn()) {
			throw new AccessDeniedNoticeException("please log in", "error.general.login");
		}

		/*
		 * check if ckey is valid
		 */
		if (!context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
			return Views.ERROR;
		}

		/*
		 * get user name
		 */
		final String loginUserName = context.getLoginUser().getName();

		log.debug("batch edit for user " + loginUserName + " started");

		/* *******************************************************
		 * FIRST: determine some flags which control the operation
		 * *******************************************************/
		/*
		 * the type of resource we're dealing with 
		 */
		final Set<Class<? extends Resource>> resourceTypes = command.getResourcetype();
		boolean postsArePublications = false;
		Class<? extends Resource> resourceClass = null;
		if (resourceTypes.size() == 1) {
			postsArePublications = resourceTypes.contains(BibTex.class);
			resourceClass = resourceTypes.iterator().next();
		} else {
			// TODO: exception 
			throw new IllegalArgumentException("please provide a resource type");
		}
		
		/*
		 * FIXME: rename/check setting of that flag in the command
		 */
		final boolean flagMeansDelete = command.getDeleteCheckedPosts();  
		/*
		 * When the user can flag posts to be deleted, this means those
		 * posts already exist. Thus, all other posts must be updated.
		 * 
		 * The other setting is, where the posts don't exist in the database
		 * (only in the session) and where they must be stored.
		 */
		final boolean updatePosts = flagMeansDelete;

		log.debug("resourceType: " + resourceTypes + ", delete: " + flagMeansDelete + ", update: " + updatePosts);

		/* *******************************************************
		 * SECOND: get the data we're working on
		 * *******************************************************/
		/*
		 * posts that are flagged are either deleted or ignored 
		 */
		final Map<String, Boolean> postFlags = command.getDelete();
		/*
		 * put the posts from the session into a hash map (for faster access)
		 */
		final Map<String, Post<? extends Resource>> postMap = getPostMap(updatePosts);
		/*
		 * the tags that should be added to all posts
		 */
		final Set<Tag> addTags = getAddTags(command.getTags());
		/*
		 * for each post we have its old tags and its new tags
		 */
		final Map<String, String> newTagsMap = command.getNewTags();
		final Map<String, String> oldTagsMap = command.getOldTags();

		log.debug("#postFlags: " + postFlags.size() + ", #postMap: " + postMap.size() + ", #addTags: " + addTags.size() + ", #newTags: " + newTagsMap.size() + ", #oldTags: " + oldTagsMap.size());

		/* *******************************************************
		 * THIRD: initialize temporary variables (lists)
		 * *******************************************************/
		/*
		 * create lists for the different types of actions 
		 */
		final List<String> postsToDelete = new LinkedList<String>();   // delete
		final List<Post<?>> postsToUpdate = new LinkedList<Post<?>>(); // update/store
		/*
		 * All posts will get the same date.
		 */
		final Date now = new Date();

		/* *******************************************************
		 * FOURTH: prepare the posts
		 * *******************************************************/
		/*
		 * loop through all hashes and check for each post, what to do
		 */
		for (final String intraHash : newTagsMap.keySet()) {
			log.debug("working on post " + intraHash);
			/*
			 * short check if hash is correct
			 */
			if (intraHash.length() != HASH_LENGTH) {
				continue;
			}
			/*
			 * has this post been flagged by the user? 
			 */
			if (postFlags.containsKey(intraHash) && postFlags.get(intraHash)) {
				log.debug("post has been flagged");
				/*
				 * The post has been flagged by the user.
				 * Depending on the meaning of this flag, we add the 
				 * post to the list of posts to be deleted or just
				 * ignore it.
				 */
				if (flagMeansDelete) {
					/*
					 * flagged posts should be deleted, i.e., add them
					 * to the list of posts to be deleted and work on 
					 * the next post.
					 */
					postsToDelete.add(intraHash);
				}
				/*
				 * flagMeansDelete = true:  delete the post
				 * flagMeansDelete = false: ignore the post (neither save nor update it)
				 */
				continue;
			}
			/*
			 * We must store/update the post, thus we parse and check its tags
			 */
			try {
				final Set<Tag> oldTags = TagUtils.parse(oldTagsMap.get(intraHash));
				final Set<Tag> newTags = TagUtils.parse(newTagsMap.get(intraHash));
				/*
				 * we add all global tags to the set of new tags
				 */
				newTags.addAll(getTagsCopy(addTags));
				/*
				 * if we want to update the posts, we only need to update posts
				 * where the tags have changed
				 */
				if (updatePosts && oldTags.equals(newTags)) {
					/*
					 * tags haven't changed, nothing to do
					 */
					continue;
				}
				/*
				 * For the create/update methods we need a post -> 
				 * create/get one.
				 */
				final Post<?> post;
				if (updatePosts) {
					/*
					 * we need only a "mock" posts containing the hash, the date
					 * and the tags, since only the post's tags are updated 
					 */
					final Post<Resource> postR = new Post<Resource>();
					postR.setResource(RESOURCE_FACTORY.createResource(resourceClass));
					postR.getResource().setIntraHash(intraHash);
					post = postR;
				} else {
					/*
					 * we get the complete post from the session, and store
					 * it in the database
					 */
					post = postMap.get(intraHash);
				}
				/*
				 * Finally, add the post to the list of posts that should 
				 * be stored or updated.
				 */
				if (!present(post)) {
					log.warn("post with hash " + intraHash + " not found for user " + loginUserName + " while updating tags");
				} else {
					/*
					 * set the date and the tags for this post 
					 * (everything else should already be set or not be changed)
					 */
					post.setDate(now);
					post.setTags(newTags);
					postsToUpdate.add(post);
				}

			} catch (final RecognitionException ex) {
				log.debug("can't parse tags of resource " + intraHash + " for user " + loginUserName, ex);
			}
		}

		/* *******************************************************
		 * FIFTH: update the database
		 * *******************************************************/
		/*
		 * delete posts
		 */
		if (present(postsToDelete)) {
			log.debug("deleting "  + postsToDelete.size() + " posts for user " + loginUserName);
			try {
				this.logic.deletePosts(loginUserName, postsToDelete);
			} catch (final IllegalStateException e) {
				// ignore - posts were already deleted
			}
		}

		/*
		 * after update/store contains all posts with errors, to show them the user for correction
		 */
		final List<Post<? extends Resource>> postsWithErrors = new LinkedList<Post<? extends Resource>>();
		/*
		 * We need to add the list command already here, otherwise we get an 
		 * org.springframework.beans.InvalidPropertyException
		 */
		addPostListToCommand(command, postsArePublications, postsWithErrors);

		/*
		 * update/store posts
		 */
		if (updatePosts) {
			log.debug("updating " + postsToUpdate.size() + " posts for user " + loginUserName);
			updatePosts(postsToUpdate, resourceClass, postMap, postsWithErrors, PostUpdateOperation.UPDATE_TAGS, loginUserName);
		} else {
			log.debug("storing "  + postsToUpdate.size() + " posts for user " + loginUserName);
			storePosts(postsToUpdate, resourceClass, postMap, postsWithErrors, command.isOverwrite(), loginUserName);
		}

		log.debug("finished batch edit for user " + loginUserName);

		/* *******************************************************
		 * SIXTH: return to view
		 * *******************************************************/
		/*
		 * handle AJAX requests
		 */
		if ("ajax".equals(command.getFormat())) {
			return Views.AJAX_EDITTAGS;
		}

		/*
		 * return to batch edit view on errors
		 */
		if (errors.hasErrors()) {
			if (postsArePublications) {
				return Views.BATCHEDITBIB;
			} 
			return Views.BATCHEDITURL;  
		}

		/*
		 * return to the page the user was initially coming from
		 */
		return this.getFinalRedirect(command.getReferer(), loginUserName);
	}

	/**
	 * Returns a copy of the given tags (i.e., new instances!)
	 * 
	 * @param tags
	 * @return
	 */
	private static Set<Tag> getTagsCopy(final Set<Tag> tags) {
		final Set<Tag> tagsCopy = new TreeSet<Tag>();
		for (final Tag tag: tags) {
			tagsCopy.add(new Tag(tag));
		}
		return tagsCopy;
	}

	/**
	 * Adds the list that will contain the erroneous posts to the command.
	 * We need to do this before rejecting the errors, because otherwise we 
	 * get a {@link org.springframework.beans.InvalidPropertyException}.  
	 *   
	 * @param command
	 * @param postsArePublications
	 * @param postsWithErrors
	 */
	@SuppressWarnings("unchecked")
	private void addPostListToCommand(final BatchEditCommand command, final boolean postsArePublications, final List<Post<? extends Resource>> postsWithErrors) {
		if (postsArePublications) {
			command.setBibtex(new ListCommand<Post<BibTex>>(command, (List) postsWithErrors));
		} else {
			command.setBookmark(new ListCommand<Post<Bookmark>>(command, (List) postsWithErrors));
		}
	}

	/**
	 * Tries to store the posts in the database, updates them if 
	 * necessary (duplicate) and allowed to to so (overwrite = true).
	 *
	 * FIXME: the error handling here is almost identical to that
	 * in {@link PostPublicationController#savePosts}
	 * 
	 * @param posts - the posts that should be stored
	 * @param resourceType - the type of resource the posts contain
	 * @param postMap - to access posts using their hash
	 * @param overwrite
	 * @param loginUserName TODO
	 */
	private void storePosts(final List<Post<? extends Resource>> posts, final Class<? extends Resource> resourceType, final Map<String, Post<?>> postMap, final List<Post<?>> postsWithErrors, final boolean overwrite, final String loginUserName) {
		final List<Post<?>> postsForUpdate  = new LinkedList<Post<?>>();
		try {
			/*
			 * let's try to store the posts ...
			 */
			this.logic.createPosts(posts);
		} catch (final DatabaseException ex) {
			/*
			 * we expect, that something might happen ...
			 */
			final Map<String, List<ErrorMessage>> errorMessages = ex.getErrorMessages();
			/*
			 * check all error messages ...
			 */
			for (final String postHash: errorMessages.keySet()) {
				final Post<?> post = postMap.get(postHash);
				log.debug("checking errors for post " + postHash);
				/*
				 * get all error messages for this post
				 */
				final List<ErrorMessage> postErrorMessages = errorMessages.get(postHash);
				if (present(postErrorMessages)) {
					boolean hasErrors = false;
					boolean hasDuplicate = false;
					/*
					 * Error messages are connected with the erroneous posts
					 * via the post's position in the error list.
					 */
					final int postId = postsWithErrors.size();
					/*
					 * go over all error messages 
					 */
					for (final ErrorMessage errorMessage : postErrorMessages) { 
						log.debug("found error " + errorMessage);
						final String errorItem;
						if (errorMessage instanceof DuplicatePostErrorMessage) {
							hasDuplicate = true;
							if (overwrite) {
								/*
								 * if we shall overwrite posts, duplicates are no errors
								 */
								continue;
							} 
							errorItem = "resource";
						} else if (errorMessage instanceof SystemTagErrorMessage) {
							errorItem = "tags";
						} else {
							errorItem = "resource";
						}
						/*
						 * add post to list of erroneous posts
						 * (only if it has no errors already, to not add it twice) 
						 */
						if (!hasErrors) {
							postsWithErrors.add(post);
						}
						hasErrors = true;
						errors.rejectValue(getOldResourceName(resourceType) + ".list[" + postId + "]." + errorItem, errorMessage.getErrorCode(), errorMessage.getParameters(), errorMessage.getDefaultMessage());
					}
					if (!hasErrors && hasDuplicate) {
						/*
						 * If the post has no errors, but is a duplicate, we add it to
						 * the list of posts which should be updated. 
						 */
						postsForUpdate.add(post);
					}
				}

			}
			if (overwrite) {
				/*
				 * try to update the posts 
				 */
				this.updatePosts(postsForUpdate, resourceType, postMap, postsWithErrors, PostUpdateOperation.UPDATE_ALL, loginUserName);
			}
		}
	}

	/**
	 * Tries to update the posts in the database.
	 * 
	 * @param posts - the posts that should be updated
	 * @param resourceType - the type of resource the posts contain 
	 * @param postMap - to access posts using their hash
	 * @param postsWithErrors - the list of posts that already had errors. All erroneous posts are added to that list
	 * @param operation - the type of operation that should be performed with the posts in the database. 
	 * @param loginUserName - to complete the post from the database, we need the user's name 
	 */
	private void updatePosts(final List<Post<? extends Resource>> posts, final Class<? extends Resource> resourceType, final Map<String, Post<?>> postMap, final List<Post<?>> postsWithErrors, final PostUpdateOperation operation, final String loginUserName) {
		try {
			this.logic.updatePosts(posts, operation);
		} catch (final DatabaseException ex) {
			final Map<String, List<ErrorMessage>> allErrorMessages = ex.getErrorMessages();
			/*
			 * iterating over all posts ....
			 */
			for (final Post<?> updatedPost : posts) {
				final String postHash = updatedPost.getResource().getIntraHash();
				/*
				 * get errors for this post
				 */
				final List<ErrorMessage> postErrorMessages = allErrorMessages.get(postHash);
				/*
				 * if there are no errors, continue
				 */
				if (!present(postErrorMessages)) {
					continue;
				}
				/*
				 * Error messages are connected with the erroneous posts
				 * via the post's position in the error list.
				 */
				final int postId = postsWithErrors.size();
				boolean hasErrors = false;
				for (final ErrorMessage errorMessage: postErrorMessages) { 
					log.debug("found error " + errorMessage);
					final String errorItem;
					if (errorMessage instanceof SystemTagErrorMessage) {
						errorItem = "tags";
					} else {
						errorItem = "resource";
					}
					/*
					 * add post to list of erroneous posts to show them the user
					 */
					if (!hasErrors) {
						/*
						 * we check for errors, to not add the post twice (if it 
						 * has several errors)
						 * 
						 * NOTE: we need the complete post (not only hash or so) to
						 * show it on the batch edit page.
						 */
						Post<?> post = null;
						if (PostUpdateOperation.UPDATE_ALL.equals(operation)) {
							/*
							 * XXX: we use the type of operation as indicator where to get the posts from
							 * 
							 * Here, the complete post shall be updated, hence, we get it from
							 * the session (user is editing tags after importing posts).
							 */
							post = postMap.get(postHash);
						} else {
							/*
							 * only the tags shall be updated -> we got only the hash from
							 * the page and must get the post from the database
							 */
							try {
								post = this.logic.getPostDetails(postHash, loginUserName);
								/*
								 * we must add the tags from the post we tried to update - 
								 * since those tags probably caused the error 
								 */
								post.setTags(updatedPost.getTags());
							} catch (final ResourceNotFoundException ex1) {
								// ignore
							} catch (final ResourceMovedException ex1) {
								// ignore
							}
						}
						/*
						 * finally add the post
						 */
						postsWithErrors.add(post);
					}
					hasErrors = true;
					errors.rejectValue(getOldResourceName(resourceType) + ".list[" + postId + "]." + errorItem, errorMessage.getErrorCode(), errorMessage.getParameters(), errorMessage.getDefaultMessage());
				}
			}
		}
	}

	/**
	 * If updatePosts is false, we have to store the posts from 
	 * the session in the database. Therefore, this method gets 
	 * those posts from the session and puts them into a hashmap
	 * for faster access. 
	 * 
	 * @param updatePosts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Post<? extends Resource>> getPostMap(final boolean updatePosts) {
		final Map<String, Post<? extends Resource>> postMap = new HashMap<String, Post<? extends Resource>>();
		final List<Post<? extends Resource>> postsFromSession = (List<Post<? extends Resource>>) this.requestLogic.getSessionAttribute(PostPublicationController.TEMPORARILY_IMPORTED_PUBLICATIONS);
		if (!updatePosts && present(postsFromSession)) {
			/*
			 * Put the posts into a map, so we don't have to loop 
			 * through the list for every stored post.
			 */
			for (final Post<? extends Resource> post : postsFromSession) {
				postMap.put(post.getResource().getIntraHash(), post);
			}
		}
		return postMap;
	} 


	/**
	 * Parses the tags that should be added to each post. 
	 * 
	 * @param addTagString
	 * @return
	 */
	private Set<Tag> getAddTags(final String addTagString) {
		try {
			/*
			 * ensure, that we don't try to parse a null string
			 */
			return TagUtils.parse(present(addTagString) ? addTagString : "");
		} catch (final RecognitionException ex) {
			log.warn("can't parse tags that should be added to all posts", ex);
		}
		return Collections.emptySet();
	}

	/**
	 * If the referer points to /bedit{bib,url}/abc, we redirect to /abc, otherwise
	 * to /user/loginUserName
	 * 
	 * @param referer
	 * @param loginUserName
	 * @return
	 */
	private View getFinalRedirect(final String referer, final String loginUserName) {
		String redirectUrl = referer;
		if (present(referer)) {
			/*
			 * if we come from bedit{bib, burl}/{group, user}/{groupname, username},
			 * we remove this prefix to get back to the simple resource view in the group or user section
			 */
			final Matcher prefixMatcher = BATCH_EDIT_URL_PATTERN.matcher(referer);
			if (prefixMatcher.find()) {
				redirectUrl = prefixMatcher.replaceFirst("");
			}
		}
		/*
		 * if no URL is given, we redirect to the user's page
		 */
		if (!present(redirectUrl)) {
			redirectUrl = UrlUtils.safeURIEncode("/user" + loginUserName); // TODO: should be done by the URLGenerator
		}
		return new ExtendedRedirectView(redirectUrl);
	}

	@Override
	public Errors getErrors() {
		return this.errors;
	}


	@Override
	public void setErrors(final Errors errors) {
		this.errors = errors;
	}

	/**
	 * sets the logic
	 * @param logic the logic
	 */
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * sets the requestLogic
	 * @param requestLogic the RequestLogic
	 */
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

}
