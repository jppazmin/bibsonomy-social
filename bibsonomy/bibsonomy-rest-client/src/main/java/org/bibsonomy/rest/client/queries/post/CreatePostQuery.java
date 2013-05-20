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

package org.bibsonomy.rest.client.queries.post;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.StringWriter;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.rest.client.AbstractQuery;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.util.StringUtils;

/**
 * Use this Class to post a post. ;)
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: CreatePostQuery.java,v 1.18 2011-06-09 12:00:46 rja Exp $
 */
public final class CreatePostQuery extends AbstractQuery<String> {
	private final Post<? extends Resource> post;
	private final String username;

	/**
	 * Creates a new post in bibsonomy.
	 * 
	 * @param username
	 *            the username under which the post is to be created
	 * @param post
	 *            the post to be created
	 * @throws IllegalArgumentException
	 *             if
	 *             <ul>
	 *             <li>the username is null or empty</li>
	 *             <li>no resource is connected with the post</li>
	 *             <li>the resource is a bookmark: if no url is specified</li>
	 *             <li>the resource is a bibtex: if no title is specified</li>
	 *             <li>no tags are specified or the tags have no names</li>
	 *             </ul>
	 */
	public CreatePostQuery(final String username, final Post<? extends Resource> post) throws IllegalArgumentException {
		if (!present(username)) throw new IllegalArgumentException("no username given");
		if (!present(post)) throw new IllegalArgumentException("no post specified");
		if (!present(post.getResource())) throw new IllegalArgumentException("no resource specified");

		if (post.getResource() instanceof Bookmark) {
			final Bookmark bookmark = (Bookmark) post.getResource();
			if (!present(bookmark.getUrl())) throw new IllegalArgumentException("no url specified in bookmark");
		}

		if (post.getResource() instanceof BibTex) {
			final BibTex publication = (BibTex) post.getResource();
			if (!present(publication.getTitle())) throw new IllegalArgumentException("no title specified in bibtex");
		}

		if (!present(post.getTags())) throw new IllegalArgumentException("no tags specified");
		for (final Tag tag : post.getTags()) {
			if (!present(tag.getName())) throw new IllegalArgumentException("missing tagname");
		}

		this.username = username;
		this.post = post;
	}

	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializePost(sw, this.post, null);
		this.downloadedDocument = performRequest(HttpMethod.POST, URL_USERS + "/" + this.username + "/" + URL_POSTS, StringUtils.toDefaultCharset(sw.toString()));
		return null;
	}
	
	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.isSuccess()) {
			return getRendererFactory().getRenderer(getRenderingFormat()).parseResourceHash(this.downloadedDocument);
		}
		return this.getError();
	}	
}