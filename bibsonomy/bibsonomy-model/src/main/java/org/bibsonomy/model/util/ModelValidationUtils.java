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

package org.bibsonomy.model.util;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.exceptions.InvalidModelException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.renderer.xml.BibtexType;
import org.bibsonomy.rest.renderer.xml.BookmarkType;
import org.bibsonomy.rest.renderer.xml.GroupType;
import org.bibsonomy.rest.renderer.xml.PostType;
import org.bibsonomy.rest.renderer.xml.TagType;
import org.bibsonomy.rest.renderer.xml.UserType;

/**
 * Sanity checks for the model.
 * 
 * 
 * XXX: InvalidModelException was: 
 * 
 * public InvalidXMLException(final String message) {
 *   super("The body part of the received XML document is not valid: " + message);
 * }
 * 
 * Probably we want to have something similar again. Maybe we want a generic
 * ValidationException and an InvalidModelException and InvalidXMLException
 * inheriting from that.
 * 
 * @author Manuel Bork
 * @author Christian Schenk
 * @version $Id: ModelValidationUtils.java,v 1.17 2011-07-11 09:58:10 rja Exp $
 */
public class ModelValidationUtils {

	private static final String XML_IS_INVALID_MSG = "The body part of the received XML document is not valid: ";

	/**
	 * @param tag the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkTag(final Tag tag) throws InvalidModelException {
		if (!present(tag.getName())) {
			throw new InvalidModelException("found a tag without tagname assigned.");
		}
	}

	/**
	 * @param user the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkUser(final User user) throws InvalidModelException {
		if (!present(user.getName())) {
			throw new InvalidModelException("found an user without username assigned.");
		}
	}

	/**
	 * @param group the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkGroup(final Group group) throws InvalidModelException {
		if (!present(group.getName())) {
			throw new InvalidModelException("found a group without groupname assigned.");
		}
	}

	/**
	 * @param bookmark the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkBookmark(final Bookmark bookmark) throws InvalidModelException {
		if (!present(bookmark.getUrl())) {
			throw new InvalidModelException("found a bookmark without URL assigned.");
		}
		if (!present(bookmark.getInterHash()) || !present(bookmark.getIntraHash())) {
			throw new InvalidModelException("found a bookmark without hash assigned.");
		}
	}

	/**
	 * @param publication the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkPublication(final BibTex publication) {
		if (!present(publication.getTitle())) {
			throw new InvalidModelException("found a publication without title assigned.");
		}
		if (!present(publication.getYear())) {
			throw new InvalidModelException("found a publication without year assigned.");
		}
		if (!present(publication.getEntrytype())) {
			throw new InvalidModelException("found a publication without entrytype assigned.");
		}
		if (!present(publication.getBibtexKey())) {
			throw new InvalidModelException("found a publication without BibTeX key assigned.");
		}
		if (!present(publication.getAuthor()) && !present(publication.getEditor())) {
			throw new InvalidModelException("found a publication without author or editor assigned.");
		}
	}
	
	/**
	 * @param xmlTag the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkTag(final TagType xmlTag) throws InvalidModelException {
		final String tagName = xmlTag.getName();
		if (!present(tagName)) {
			throw new InvalidModelException(XML_IS_INVALID_MSG + "tag name is missing in element 'tag'");
		}
		
		if (tagName.contains(" ")) {
			throw new InvalidModelException(XML_IS_INVALID_MSG + "tag name contains space character. To assign several tags to a post, please use one tag element for each tag.");
		}
	}

	/**
	 * @param xmlUser the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkUser(final UserType xmlUser) throws InvalidModelException {
		if (!present(xmlUser.getName())) {
			throw new InvalidModelException(XML_IS_INVALID_MSG + "username is missing in element 'user'");
		}
	}

	/**
	 * @param xmlGroup the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkGroup(final GroupType xmlGroup) throws InvalidModelException {
		if (!present(xmlGroup.getName())) {
			throw new InvalidModelException(XML_IS_INVALID_MSG + "groupname is missing in element 'group'");
		}
	}

	/**
	 * @param xmlPost the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkPost(final PostType xmlPost) throws InvalidModelException {
		if (xmlPost.getTag() == null) throw new InvalidModelException(XML_IS_INVALID_MSG + "list of tags is missing");
		if (xmlPost.getTag().size() == 0) throw new InvalidModelException(XML_IS_INVALID_MSG + "no tags specified");
		
		if (xmlPost.getUser() == null) throw new InvalidModelException(XML_IS_INVALID_MSG + "user is missing");

		final BibtexType xmlPublication = xmlPost.getBibtex();
		final BookmarkType xmlBookmark = xmlPost.getBookmark();
		
		if (xmlPublication == null && xmlBookmark == null) {
			throw new InvalidModelException(XML_IS_INVALID_MSG + "resource is missing inside element 'post'");
		} else if (xmlPublication != null && xmlBookmark != null) {
			throw new InvalidModelException(XML_IS_INVALID_MSG + "only one resource type is allowed inside element 'post'");
		} else {
			// just fine (bibtex xor bookmark):
			// ( xmlBibtex == null && xmlBookmark != null ) || ( xmlBibtex != null || xmlBookmark == null )
		}
	}

	/**
	 * @param xmlPost the xml to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkStandardPost(final PostType xmlPost) throws InvalidModelException {
		// user
		if (xmlPost.getUser() == null) throw new InvalidModelException(XML_IS_INVALID_MSG + "user is missing");
		
		// resource
		if (xmlPost.getGoldStandardPublication() == null) {
			throw new InvalidModelException(XML_IS_INVALID_MSG + "resource is missing inside element 'post'");
		}
	}

	/**
	 * @param xmlBookmark the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkBookmark(final BookmarkType xmlBookmark) throws InvalidModelException {
		if (!present(xmlBookmark.getUrl())) throw new InvalidModelException(XML_IS_INVALID_MSG + "url is missing in element 'bookmark'");
		if (!present(xmlBookmark.getTitle())) throw new InvalidModelException(XML_IS_INVALID_MSG + "title is missing in element 'bookmark'");
	}

	/**
	 * @param xmlPublication the object to run sanity checks on
	 * @throws InvalidModelException if there is a lack of sanity
	 */
	public static void checkPublication(final BibtexType xmlPublication) throws InvalidModelException {
		if (!present(xmlPublication.getTitle())) throw new InvalidModelException(XML_IS_INVALID_MSG + "title is missing in element 'bibtex'");
		if (!present(xmlPublication.getYear())) throw new InvalidModelException(XML_IS_INVALID_MSG + "year is missing in element 'bibtex'");
		if (!present(xmlPublication.getBibtexKey())) throw new InvalidModelException(XML_IS_INVALID_MSG + "bibtex key is missing in element 'bibtex'");
		if (!present(xmlPublication.getEntrytype())) throw new InvalidModelException(XML_IS_INVALID_MSG + "bibtex entry type is missing in element 'bibtex'");
		if (!present(xmlPublication.getAuthor()) && !present(xmlPublication.getEditor())) throw new InvalidModelException(XML_IS_INVALID_MSG + "editor(s) and author(s) are missing (one of the two is required) in element 'bibtex'");
	}
}