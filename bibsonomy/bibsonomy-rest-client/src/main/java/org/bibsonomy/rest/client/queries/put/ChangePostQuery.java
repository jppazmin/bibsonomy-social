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

package org.bibsonomy.rest.client.queries.put;

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

/**
 * Use this Class to change details of an existing post - change tags, for
 * example.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ChangePostQuery.java,v 1.16 2011-06-09 12:00:49 rja Exp $
 */
public final class ChangePostQuery extends AbstractQuery<String> {
	private final Post<? extends Resource> post;
	private final String username;
	private final String resourceHash;

	/**
	 * Changes details of an existing post.
	 * 
	 * @param username
	 *            the username under which the post is to be created
	 * @param resourceHash
	 *            hash of the resource to change
	 * @param post
	 *            the new value for the post
	 * @throws IllegalArgumentException
	 *             if
	 *             <ul>
	 *             <li>username or resourcehash are not specified</li>
	 *             <li>no resource is connected with the post</li>
	 *             <li>the resource is a bookmark: if no url is specified</li>
	 *             <li>the resource is a bibtex: if no title is specified</li>
	 *             <li>no tags are specified or the tags have no names</li>
	 *             </ul>
	 */
	public ChangePostQuery(final String username, final String resourceHash, final Post<? extends Resource> post) throws IllegalArgumentException {
		if (!present(username)) throw new IllegalArgumentException("no username given");
		if (!present(resourceHash)) throw new IllegalArgumentException("no resourceHash given");
		if (!present(post)) throw new IllegalArgumentException("no post specified");
		if (!present(post.getResource())) throw new IllegalArgumentException("no resource specified");

		if (post.getResource() instanceof Bookmark) {
			final Bookmark bookmark = (Bookmark) post.getResource();
			if (!present(bookmark.getUrl())) throw new IllegalArgumentException("no url specified in bookmark");
		}

		if (post.getResource() instanceof BibTex) {
			final BibTex publication = (BibTex) post.getResource();
			if (!present(publication.getIntraHash())) {
				throw new IllegalArgumentException("found an publication without intrahash assigned.");
			}
		}

		if (!present(post.getTags())) throw new IllegalArgumentException("no tags specified");
		for (final Tag tag : post.getTags()) {
			if (!present(tag.getName())) throw new IllegalArgumentException("missing tagname");
		}

		this.username = username;
		this.resourceHash = resourceHash;
		this.post = post;
	}

	@Override
	protected String doExecute() throws ErrorPerformingRequestException {
		final StringWriter sw = new StringWriter(100);
		getRendererFactory().getRenderer(getRenderingFormat()).serializePost(sw, post, null);
		this.downloadedDocument = performRequest(HttpMethod.PUT, URL_USERS + "/" + this.username + "/" + URL_POSTS + "/" + resourceHash, sw.toString());
		return null;
	}
	
	@Override
	public String getResult() throws BadRequestOrResponseException, IllegalStateException {
		if (this.isSuccess())
			return getRendererFactory().getRenderer(getRenderingFormat()).parseResourceHash(this.downloadedDocument); 
		return this.getError();
	}
}