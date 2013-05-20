/**
 *
 *  BibSonomy-Rest-Common - Common things for the REST-client and server.
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

package org.bibsonomy.rest.renderer;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;

/**
 * This interface should be implemented by classes that intend to add additional
 * rendering capabilities to the system.<br/>
 * 
 * Note that it also includes funtionality to read the data, that has been
 * rendered with it.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: Renderer.java,v 1.23 2011-04-29 07:59:08 nosebrain Exp $
 */
public interface Renderer {

	/**
	 * Serializes a {@link List} of {@link Post}s.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param posts
	 *            a {@link List} of {@link Post} objects.
	 * @param viewModel
	 *            the {@link ViewModel} encapsulates additional information,
	 * @throws InternServerException 
	 */
	public void serializePosts(Writer writer, List<? extends Post<? extends Resource>> posts, ViewModel viewModel) throws InternServerException;

	/**
	 * Serializes one {@link Post}.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param post
	 *            one {@link Post} object.
	 * @param model
	 *            the {@link ViewModel} encapsulates additional information,
	 */
	public void serializePost(Writer writer, Post<? extends Resource> post, ViewModel model);

	/**
	 * Serializes a {@link List} of {@link User}s.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param users
	 *            a {@link List} of {@link User} objects.
	 * @param viewModel
	 *            the {@link ViewModel} encapsulates additional information,
	 */
	public void serializeUsers(Writer writer, List<User> users, ViewModel viewModel);

	/**
	 * Serializes one {@link User}.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param user
	 *            one {@link User} object.
	 * @param viewModel
	 *            the {@link ViewModel} encapsulates additional information,
	 */
	public void serializeUser(Writer writer, User user, ViewModel viewModel);

	/**
	 * Serializes a {@link List} of {@link Tag}s.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param tags
	 *            a {@link List} of {@link Tag} objects.
	 * @param viewModel
	 *            the {@link ViewModel} encapsulates additional information,
	 */
	public void serializeTags(Writer writer, List<Tag> tags, ViewModel viewModel);

	/**
	 * Serializes a {@link Tag}'s details, including {@link List} of subtags,
	 * {@link List} of supertags and {@link List} of correlated tags
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param tag
	 *            one {@link Tag} object.
	 * @param viewModel
	 *            the {@link ViewModel} encapsulates additional information,
	 */
	public void serializeTag(Writer writer, Tag tag, ViewModel viewModel);

	/**
	 * Serializes a {@link List} of {@link RecommendedTag}s.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param tags
	 *            a {@link List} of {@link Tag} objects.
	 */
	public void serializeRecommendedTags(Writer writer, Collection<RecommendedTag> tags);
	
	/**
	 * Serializes a {@link RecommendedTag}'s details
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param tag
	 *            one {@link RecommendedTag} object.
	 */
	public void serializeRecommendedTag(Writer writer, RecommendedTag tag);
	
	/**
	 * Serializes a list of {@link Group}s.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param groups
	 *            a {@link List} of {@link Group} objects.
	 * @param viewModel
	 *            the {@link ViewModel} encapsulates additional information,
	 */
	public void serializeGroups(Writer writer, List<Group> groups, ViewModel viewModel);

	/**
	 * Serializes one {@link Group}.
	 * 
	 * @param writer
	 *            a {@link Writer} to use.
	 * @param group
	 *            one {@link Group} object.
	 * @param viewModel
	 *            the {@link ViewModel} encapsulates additional information,
	 */
	public void serializeGroup(Writer writer, Group group, ViewModel viewModel);

	/**
	 * Serializes an errormessage.
	 * 
	 * @param writer
	 *            the {@link Writer} to use.
	 * @param errorMessage
	 *            the error message to send.
	 */
	public void serializeError(Writer writer, String errorMessage);
	
	/**
	 * Serializes a plain OK.
	 * 
	 * @param writer
	 *            the {@link Writer} to use.
	 */
	public void serializeOK(Writer writer);
	
	/**
	 * Serializes a failure status.
	 * 
	 * @param writer
	 *            the {@link Writer} to use.
	 */
	public void serializeFail(Writer writer);		
	
	/**
	 * Serializes a resourcehash.
	 * 
	 * @param writer
	 *            the {@link Writer} to use.
	 * @param hash
	 *            the hash to send.
	 */
	public void serializeResourceHash(Writer writer, String hash);
	
	/**
	 * Serializes a userid.
	 * 
	 * @param writer
	 *            the {@link Writer} to use.
	 * @param userId
	 *            the userId to send.
	 */
	public void serializeUserId(Writer writer, String userId);
	
	/**
	 * Serializes an uri of an resource
	 * 
	 * @param writer
	 * @param uri
	 */
	public void serializeURI(Writer writer, String uri);
	
	/**
	 * Serializes a groupid.
	 * 
	 * @param writer
	 *            the {@link Writer} to use.
	 * @param groupId
	 *            the groupId to send.
	 */
	public void serializeGroupId(Writer writer, String groupId);	

	/**
	 * Reads an errormessage from a {@link Reader}
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return an error string
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public String parseError(Reader reader) throws BadRequestOrResponseException;
	
	/**
	 * Reads an resource hash from a {@link Reader}
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return an resource hash
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public String parseResourceHash(Reader reader) throws BadRequestOrResponseException;
	
	/**
	 * Reads an user id from a {@link Reader}
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return an resource hash
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public String parseUserId(Reader reader) throws BadRequestOrResponseException;
	
	/**
	 * Reads a group id from a {@link Reader}
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return an resource hash
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public String parseGroupId(Reader reader) throws BadRequestOrResponseException;	
	
	/**
	 * Reads the status from a {@link Reader}
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return the status ("ok" or "fail")
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public String parseStat(Reader reader) throws BadRequestOrResponseException;	
	
	/**
	 * Reads a List of {@link User}s from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return a {@link List} of {@link User} objects.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public List<User> parseUserList(Reader reader) throws BadRequestOrResponseException;

	/**
	 * Reads one {@link User} from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return one {@link User} object.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public User parseUser(Reader reader) throws BadRequestOrResponseException;

	/**
	 * Reads a List of {@link Post}s from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return a {@link List} of {@link Post} objects.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public List<Post<? extends Resource>> parsePostList(Reader reader) throws BadRequestOrResponseException;

	/**
	 * Reads one {@link Post} from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return one {@link Post} object.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public Post<? extends Resource> parsePost(Reader reader) throws BadRequestOrResponseException;
	
	/**
	 * Reads one standard {@link Post} from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return one {@link Post} object.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public Post<? extends Resource> parseStandardPost(Reader reader) throws BadRequestOrResponseException;

	/**
	 * Reads a List of {@link Group}s from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return a {@link List} of {@link Group} objects.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public List<Group> parseGroupList(Reader reader) throws BadRequestOrResponseException;

	/**
	 * Reads one {@link Group} from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return one {@link Group} object.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public Group parseGroup(Reader reader) throws BadRequestOrResponseException;

	/**
	 * Reads a List of {@link Tag}s from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return a {@link List} of {@link Tag} objects.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public List<Tag> parseTagList(Reader reader) throws BadRequestOrResponseException;
	
	/**
	 * Reads one {@link Tag} from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return one {@link Post} object.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public Tag parseTag(Reader reader) throws BadRequestOrResponseException;

	/**
	 * Reads a List of {@link RecommendedTag}s from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return a {@link List} of {@link Tag} objects.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public SortedSet<RecommendedTag> parseRecommendedTagList(Reader reader) throws BadRequestOrResponseException;
	
	/**
	 * Reads one {@link RecommendedTag} from a {@link Reader}.
	 * 
	 * @param reader
	 *            the {@link Reader} to use.
	 * @return one {@link Post} object.
	 * @throws BadRequestOrResponseException
	 *             if the document within the reader is errorenous.
	 */
	public RecommendedTag parseRecommendedTag(Reader reader) throws BadRequestOrResponseException;
	
	/**
	 * @param reader the reader to use
	 * @return  a list of references (their interhashes) from the reader
	 */
	public Set<String> parseReferences(Reader reader);
}