/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
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

package org.bibsonomy.rest.renderer.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.common.exceptions.LayoutRenderingException;
import org.bibsonomy.layout.jabref.JabrefLayout;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.renderer.Renderer;
import org.bibsonomy.services.URLGenerator;

/**
 * @author rja
 * @version $Id: JabrefLayoutRenderer.java,v 1.1 2011-06-09 12:10:17 rja Exp $
 */
public class JabrefLayoutRenderer implements Renderer {

	/*
	 * FIXME: proper initialization!
	 * (e.g., missing UrlGenerator)
	 */
	private final org.bibsonomy.layout.jabref.JabrefLayoutRenderer renderer; 
	
	/**
	 * @param urlGenerator - the class to generate proper URLs
	 */
	public JabrefLayoutRenderer(final URLGenerator urlGenerator) {
		super();
		this.renderer = new org.bibsonomy.layout.jabref.JabrefLayoutRenderer();
		this.renderer.setUrlGenerator(urlGenerator);
	}

	@Override
	public String parseError(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Group parseGroup(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public String parseGroupId(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public List<Group> parseGroupList(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Post<? extends Resource> parsePost(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public List<Post<? extends Resource>> parsePostList(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public RecommendedTag parseRecommendedTag(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public SortedSet<RecommendedTag> parseRecommendedTagList(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Set<String> parseReferences(Reader reader) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public String parseResourceHash(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Post<? extends Resource> parseStandardPost(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public String parseStat(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Tag parseTag(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public List<Tag> parseTagList(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public User parseUser(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public String parseUserId(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public List<User> parseUserList(Reader reader) throws BadRequestOrResponseException {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void serializeError(Writer writer, String errorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serializeFail(Writer writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serializeGroup(Writer writer, Group group, ViewModel viewModel) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void serializeGroupId(Writer writer, String groupId) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void serializeGroups(Writer writer, List<Group> groups, ViewModel viewModel) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void serializeOK(Writer writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serializePost(Writer writer, Post<? extends Resource> post, ViewModel model) {
		serializePosts(writer, Collections.singletonList(post), model);
		
	}

	@Override
	public void serializePosts(Writer writer, List<? extends Post<? extends Resource>> posts, ViewModel viewModel) throws InternServerException {
		/*
		 * FIXME: proper layout selection
		 */
		final boolean embeddedLayout = true;
		try {
			final JabrefLayout layout = renderer.getLayout("simplehtml", null);
			writer.append(renderer.renderLayout(layout, posts, embeddedLayout));
		} catch (LayoutRenderingException ex) {
			throw new InternServerException(ex);
		} catch (IOException ex) {
			throw new InternServerException(ex);
		}
	}

	@Override
	public void serializeRecommendedTag(Writer writer, RecommendedTag tag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void serializeRecommendedTags(Writer writer, Collection<RecommendedTag> tags) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void serializeResourceHash(Writer writer, String hash) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void serializeTag(Writer writer, Tag tag, ViewModel viewModel) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void serializeTags(Writer writer, List<Tag> tags, ViewModel viewModel) {
		throw new UnsupportedOperationException();	
	}

	@Override
	public void serializeURI(Writer writer, String uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void serializeUser(Writer writer, User user, ViewModel viewModel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void serializeUserId(Writer writer, String userId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void serializeUsers(Writer writer, List<User> users, ViewModel viewModel) {
		throw new UnsupportedOperationException();
	}

}
