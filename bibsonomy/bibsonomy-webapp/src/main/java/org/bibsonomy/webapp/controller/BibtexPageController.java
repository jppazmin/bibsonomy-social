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

package org.bibsonomy.webapp.controller;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.GoldStandardPublication;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.webapp.command.BibtexResourceViewCommand;
import org.bibsonomy.webapp.exceptions.MalformedURLSchemeException;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for bibtex pages:
 *   - /bibtex/HASH
 *   - /bibtex/HASH/USERNAME
 * 
 * @author Michael Wagner
 * @author Dominik Benz, benz@cs.uni-kassel.de 
 * @version $Id: BibtexPageController.java,v 1.31 2011-06-24 12:33:39 sven Exp $
 */
public class BibtexPageController extends SingleResourceListControllerWithTags implements MinimalisticController<BibtexResourceViewCommand> {
	private static final String GOLD_STANDARD_USER_NAME = "";
	private static final int TAG_LIMIT = 1000;

	@SuppressWarnings("unchecked")
	@Override
	public View workOn(final BibtexResourceViewCommand command) {
		final String format = command.getFormat();
		this.startTiming(this.getClass(), format);

		/*
		 * This hash has 33 characters and contains at the first position the
		 * type of the hash (see SimHash class).
		 */
		final String longHash = command.getRequBibtex();
		/*
		 * if no hash given -> error
		 */
		if (!present(longHash)) {
			throw new MalformedURLSchemeException("error.bibtex_no_hash");
		}

		/*
		 * Set hash, username, grouping entity
		 */
		final String requUser = command.getRequestedUser();
		final GroupingEntity groupingEntity = requUser != null ? GroupingEntity.USER : GroupingEntity.ALL;

		/* 
		 * handle case when only tags are requested
		 * retrieve only TAG_LIMIT tags for this resource
		 */
		command.setResourcetype(Collections.<Class<? extends Resource>>singleton(BibTex.class));
		this.handleTagsOnly(command, groupingEntity, requUser, null, null, longHash, TAG_LIMIT, null);

		/*
		 * The hash without the type of hash identifier at the first position.
		 * 32 characters long.
		 */
		final String shortHash = longHash.substring(1);

		/*
		 * To later retrieve the corresponding gold standard post. The intra hash
		 * of gold standard posts equals the inter hash of the corresponding 
		 * regular posts.
		 * 
		 * If an inter hash was queried, this is already the correct hash.
		 * If an intra hash was queried, we later must overwrite it with the 
		 * inter hash.
		 */
		String goldHash = shortHash; 

		/*
		 * retrieve and set the requested publication(s)
		 * 
		 * We always get the publication(s) as list - also when the GroupingEntity 
		 * is "USER" (where we will only show one publication!) - because we don't 
		 * know the type of the requested hash. The getPosts() method of the 
		 * LogicInterface checks for the type and returns the corresponding post(s). 
		 */
		final int entriesPerPage = command.getListCommand(BibTex.class).getEntriesPerPage();		
		this.setList(command, BibTex.class, groupingEntity, requUser, null, longHash, null, null, null, entriesPerPage);

		if (GroupingEntity.ALL.equals(groupingEntity)) {
			/* 
			 * retrieve total count with given hash 
			 * (only for /bibtex/HASH)
			 */
			this.setTotalCount(command, BibTex.class, groupingEntity, requUser, null, longHash, null, null, null, entriesPerPage, null);
		} else if (GroupingEntity.USER.equals(groupingEntity)) {
			/*
			 * Complete the post details for the first post of a given user 
			 * (only for /bibtex/HASH/USER)
			 * 
			 * We will use the intrahash to get all details for the post using
			 * getPostDetails().
			 */
			final String intraHash;
			final List<Post<BibTex>> posts = command.getBibtex().getList();
			if (present(posts)) {
				/*
				 * a post was found -> extract the publication
				 */
				intraHash = posts.get(0).getResource().getIntraHash();
			} else {
				/*
				 * No post was found: we use the requested hash to query for the
				 * post. (Note: if an interhash was requested, we won't get a
				 * post here.) 
				 */
				intraHash = shortHash;
			}
			final Post<BibTex> post = (Post<BibTex>) this.logic.getPostDetails(intraHash, requUser);
			/*
			 * if we did not find a post -> throw an exception
			 */
			if (!present(post)) throw new ResourceNotFoundException(intraHash);
			/*
			 * Why do we set the goldHash here again?
			 * Because at first it might have been an intra hash of a 
			 * user's post. Here we ensure, that it's the post's interhash
			 * because the intrahashes of gold standard posts are the interhashes. 
			 */
			goldHash = post.getResource().getInterHash();
			/*
			 * store the post in the command's list (and replace the original 
			 * list of post)
			 */
			command.getBibtex().setList(Collections.singletonList(post));
		}
		/*
		 * post process and sort list (e.g., insert open URL)
		 */
		this.postProcessAndSortList(command, BibTex.class);

		/*
		 * We always get the gold standard post from the database - even for
		 * user's posts - to show a link to it in the sidebar 
		 */
		Post<GoldStandardPublication> goldStandard = null;
		try {
			/*
			 * get the gold standard
			 */
			goldStandard = (Post<GoldStandardPublication>) this.logic.getPostDetails(goldHash, GOLD_STANDARD_USER_NAME);
			command.setGoldStandardPublication(goldStandard);
		} catch (final ResourceNotFoundException ex) {
			// ignore
		} catch (final ResourceMovedException ex) {
			// ignore
		}
		

		/*
		 * If list is empty, send a 404 error.
		 */
		if (!present(command.getBibtex().getList())) {
			/*
			 * We throw a ResourceNotFoundException such that we don't get empty
			 * resource pages.
			 */
			throw new ResourceNotFoundException(shortHash);
		}
		
		/*
		 * extract first publication
		 */
		final BibTex firstPublication = command.getBibtex().getList().get(0).getResource();			
		command.setDocuments(firstPublication.getDocuments());
		
		/*
		 * Set page title to title of first publication 
		 */
		command.setTitle(firstPublication.getTitle());
		/*
		 * Add additional data for HTML view, e.g., tags, other user's posts, ...
		 */
		this.endTiming();
		
		if ("authoragreement".equals(format)) {

			// get additional metadata fields
			final Map<String, List<String>> additionalMetadataMap = logic.getExtendedFields(BibTex.class, command.getContext().getLoginUser().getName() , shortHash, null);
			/* additionalMetadataMap
			 *  {
			 *  	DDC=[010, 050, 420, 422, 334, 233], 
			 *  	post.resource.openaccess.additionalfields.additionaltitle=[FoB], 
			 *  	post.resource.openaccess.additionalfields.phdreferee2=[Petra Musterfrau], 
			 *  	post.resource.openaccess.additionalfields.phdreferee=[Peter Mustermann], 
			 *  	ACM=[C.2.2], 
			 *  	JEL=[K12], 
			 *  	post.resource.openaccess.additionalfields.sponsor=[DFG, etc..], 
			 *  	post.resource.openaccess.additionalfields.phdoralexam=[17.08.2020], 
			 *  	post.resource.openaccess.additionalfields.institution=[Uni KS tEST ]
			 *  }
			*/

			command.setAdditonalMetadata(additionalMetadataMap);
			
			return Views.AUTHORAGREEMENTPAGE;
		}
		
		
		if ("html".equals(format)) {
			command.setPageTitle("publication :: " + command.getTitle()); // TODO: i18n
			
			if (GroupingEntity.USER.equals(groupingEntity) || present(goldStandard)) {
				/*
				 * fetch posts of all users with the given hash, add users to related
				 * users list				
				 */
				final List<Post<BibTex>> allPosts = this.logic.getPosts(BibTex.class, GroupingEntity.ALL, null, null, firstPublication.getInterHash(), null, null, 0, 1000, null);
				for (final Post<BibTex> post : allPosts) {
					command.getRelatedUserCommand().getRelatedUsers().add(post.getUser());
				}
			}
			
			if (!present(goldStandard)) {
				command.setDiscussionItems(this.logic.getDiscussionSpace(goldHash));
			} else {
				// TODO: call in logic?!
				goldStandard.getResource().parseMiscField();
			}

			if (GroupingEntity.USER.equals(groupingEntity)) {
				// bibtex/HASH/USER
				/* 
				 * set "correct" count .This is the number of ALL users having the publication
				 * with the interHash of firstBibtex in their collection. In allPosts, only public posts
				 * are contained, hence it can be smaller.
				 */
				this.setTotalCount(command, BibTex.class, GroupingEntity.ALL, null, null, firstPublication.getInterHash(), null, null, null, 1000, null);
				firstPublication.setCount(command.getBibtex().getTotalCount());

				/*
				 * show tags by all users for this resource; the ones by the given user
				 * will be highlighted later
				 */
				this.setTags(command, BibTex.class, GroupingEntity.ALL, null, null, null, longHash, TAG_LIMIT, null);
				return Views.BIBTEXDETAILS;
			}
			/*
			 * get only those tags, related to the resource
			 */
			this.setTags(command, BibTex.class, groupingEntity, requUser, null, null, longHash, TAG_LIMIT, null);			
			return Views.BIBTEXPAGE;
		}

		// export - return the appropriate view
		return Views.getViewByFormat(format);

	}

	@Override
	public BibtexResourceViewCommand instantiateCommand() {
		return new BibtexResourceViewCommand();
	}

}
