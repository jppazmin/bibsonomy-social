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

package org.bibsonomy.recommender.tags.multiplexer;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.util.GroupUtils;

/**
 * @author rja
 * @version $Id: PostPrivacyFilter.java,v 1.3 2009-07-17 01:17:22 folke Exp $
 */
public class PostPrivacyFilter {


	/**
	 * The methods checks if the post can be forwarded to external services. If
	 * not, <code>null</code> is returned. Otherwise, a copy of the post is 
	 * returned where only the public fields are set. Note that this is not 
	 * necessarily a deep copy, i.e., the tags are not copied but just linked.
	 * 
	 * <p>We do white listing here, i.e., we explicitly state, which attribues
	 * to copy.</p>
	 * 
	 * @param post
	 * @return The post containing only public parts or <code>null</code>, if
	 * the post is not public at all.
	 */
	public Post<? extends Resource> filterPost(Post<? extends Resource> post) {
		if (!post.getGroups().contains(GroupUtils.getPublicGroup())) {
			/*
			 * The post does not contain the public group -> no parts of it
			 * are public.
			 */
			// FIXME: THIS IS BROKEN! FOR PUBLIC POSTS, THE CONDITION ABOVE EVALUATES TO TRUE
			return null;
			// return post;
		}
		/*
		 * create a copy of the post which is returned
		 */
		/*
		 * post
		 */
		final Post<Resource> postCopy = new Post<Resource>();
		postCopy.setUser(post.getUser());
		postCopy.setDate(post.getDate());
		postCopy.setContentId(post.getContentId());
		postCopy.setDescription(post.getDescription());
		postCopy.setGroups(post.getGroups());
		postCopy.setTags(post.getTags());
		/*
		 * resource
		 */
		final Resource resource = post.getResource();
		if (resource instanceof BibTex) {
			/*
			 * bibtex
			 */
			final BibTex bibtex = (BibTex) resource;
			final BibTex bibtexCopy = new BibTex();
			postCopy.setResource(bibtexCopy);
			
			bibtexCopy.setAbstract(bibtex.getAbstract());
			bibtexCopy.setAddress(bibtex.getAddress());
			bibtexCopy.setAnnote(bibtex.getAnnote());
			bibtexCopy.setAuthor(bibtex.getAuthor());
			bibtexCopy.setBibtexKey(bibtex.getBibtexKey());
			bibtexCopy.setBooktitle(bibtex.getBooktitle());
			bibtexCopy.setChapter(bibtex.getChapter());
			bibtexCopy.setCrossref(bibtex.getCrossref());
			bibtexCopy.setDay(bibtex.getDay());
			bibtexCopy.setEdition(bibtex.getEdition());
			bibtexCopy.setEditor(bibtex.getEditor());
			bibtexCopy.setEntrytype(bibtex.getEntrytype());
			bibtexCopy.setHowpublished(bibtex.getHowpublished());
			bibtexCopy.setInstitution(bibtex.getInstitution());
			bibtexCopy.setJournal(bibtex.getJournal());
			bibtexCopy.setMisc(bibtex.getMisc());
			bibtexCopy.setMonth(bibtex.getMonth());
			bibtexCopy.setNote(bibtex.getNote());
			bibtexCopy.setNumber(bibtex.getNumber());
			bibtexCopy.setOrganization(bibtex.getOrganization());
			bibtexCopy.setPages(bibtex.getPages());
			bibtexCopy.setPrivnote(bibtex.getPrivnote());
			bibtexCopy.setPublisher(bibtex.getPublisher());
			bibtexCopy.setSchool(bibtex.getSchool());
			bibtexCopy.setSeries(bibtex.getSeries());
			bibtexCopy.setTitle(bibtex.getTitle());
			bibtexCopy.setType(bibtex.getType());
			bibtexCopy.setUrl(bibtex.getUrl());
			bibtexCopy.setVolume(bibtex.getVolume());
			bibtexCopy.setYear(bibtex.getYear());
		} else if (resource instanceof Bookmark) {
			/*
			 * bookmark
			 */
			final Bookmark bookmark = (Bookmark) resource;
			final Bookmark bookmarkCopy = new Bookmark();
			postCopy.setResource(bookmark);
			
			bookmarkCopy.setTitle(bookmark.getTitle());
			bookmarkCopy.setUrl(bookmark.getUrl());
		}
		
		
		/*
		 * new hashes
		 */
		post.getResource().recalculateHashes();
		postCopy.getResource().recalculateHashes();

		return postCopy;
	}

}
