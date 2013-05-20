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

package org.bibsonomy.rest.renderer.xml;

import static org.bibsonomy.model.util.ModelValidationUtils.checkBookmark;
import static org.bibsonomy.model.util.ModelValidationUtils.checkGroup;
import static org.bibsonomy.model.util.ModelValidationUtils.checkPost;
import static org.bibsonomy.model.util.ModelValidationUtils.checkStandardPost;
import static org.bibsonomy.model.util.ModelValidationUtils.checkTag;
import static org.bibsonomy.model.util.ModelValidationUtils.checkUser;
import static org.bibsonomy.util.ValidationUtils.present;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.bibsonomy.common.exceptions.InvalidModelException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.GoldStandard;
import org.bibsonomy.model.GoldStandardPublication;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.util.ModelValidationUtils;
import org.bibsonomy.rest.validation.ModelValidator;

/**
 * Produces objects from the model based on objects from the XML model generated
 * with JAXB.
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ModelFactory.java,v 1.44 2011-05-23 14:38:43 bibsonomy Exp $
 */
public class ModelFactory {

	private static ModelFactory modelFactory;

	/**
	 * @return the {@link ModelFactory} instance
	 */
	public static ModelFactory getInstance() {
		if (ModelFactory.modelFactory == null) {
			ModelFactory.modelFactory = new ModelFactory();
		}
		return ModelFactory.modelFactory;
	}
	
	private ModelValidator modelValidator = null; 

	private ModelFactory() {
	}

	/**
	 * creates a user based on the xml user
	 * 
	 * @param xmlUser
	 * @return the converted user
	 */
	public User createUser(final UserType xmlUser) {
		checkUser(xmlUser);

		final User user = new User();
		user.setEmail(xmlUser.getEmail());
		try {
			// FIXME move into Factory
			user.setHomepage(new URL(xmlUser.getHomepage()));
		} catch (final MalformedURLException e) {
		}
		user.setName(xmlUser.getName());
		user.setRealname(xmlUser.getRealname());
		user.setPassword(xmlUser.getPassword());
		if (xmlUser.isSpammer() != null)
			user.setSpammer(xmlUser.isSpammer());
		if (xmlUser.getPrediction() != null)
			user.setPrediction(xmlUser.getPrediction().intValue());
		if (xmlUser.getConfidence() != null)
			user.setConfidence(xmlUser.getConfidence());
		user.setAlgorithm(xmlUser.getAlgorithm());
		user.setMode(xmlUser.getClassifierMode());
		if (xmlUser.getToClassify() != null) {
			user.setToClassify(xmlUser.getToClassify().intValue());
		}
		/*
		 * copy groups
		 */
		final GroupsType groups = xmlUser.getGroups();
		if (groups != null) {
			final List<Group> groups2 = user.getGroups();
			for (final GroupType xmlGroup: groups.getGroup()) {
				groups2.add(createGroup(xmlGroup));
			}
		}
		return user;
	}

	/**
	 * creates a {@link Group} based on the xml group
	 * 
	 * @param xmlGroup
	 * @return the converted group
	 */
	public Group createGroup(final GroupType xmlGroup) {
		checkGroup(xmlGroup);

		final Group group = new Group();
		group.setName(xmlGroup.getName());
		group.setDescription(xmlGroup.getDescription());
		group.setRealname(xmlGroup.getRealname());
		try {
			// FIXME move into Factory
			group.setHomepage(new URL(xmlGroup.getHomepage()));
		} catch (final MalformedURLException e) {
		}
		if (xmlGroup.getUser().size() > 0) {
			group.setUsers(new ArrayList<User>());
			for (final UserType xmlUser : xmlGroup.getUser()) {
				group.getUsers().add(createUser(xmlUser));
			}
		}

		return group;
	}

	/**
	 * converts a xml tag to the model representation
	 * 
	 * @param xmlTag
	 * @return the created tag
	 */
	public Tag createTag(final TagType xmlTag) {
		return createTag(xmlTag, 1);
	}

	/**
	 * TODO: improve documentation
	 * 
	 * @param xmlTag
	 * @param depth
	 * @return the created tag
	 */
	public Tag createTag(final TagType xmlTag, final int depth) {
		checkTag(xmlTag);

		final Tag tag = new Tag();
		tag.setName(xmlTag.getName());
		// TODO tag count  häh?
		if (xmlTag.getGlobalcount() != null) tag.setGlobalcount(xmlTag.getGlobalcount().intValue());
		// TODO tag count  häh?
		if (xmlTag.getUsercount() != null) tag.setUsercount(xmlTag.getUsercount().intValue());

		if (depth > 0) {
			if (xmlTag.getSubTags() != null) {
				tag.setSubTags(createTags(xmlTag.getSubTags(), depth - 1));
			}
			if (xmlTag.getSuperTags() != null) {
				tag.setSuperTags(createTags(xmlTag.getSuperTags(), depth - 1));
			}
		}
		return tag;
	}
	
	private List<Tag> createTags(final List<TagsType> xmlTags, final int depth) {
		final List<Tag> rVal = new ArrayList<Tag>();
		for (final TagsType xmlSubTags : xmlTags) {
			//tags.add(xmlSubTag);
			for (final TagType xmlSubTag : xmlSubTags.getTag()) {
				rVal.add(createTag(xmlSubTag, depth));
			}
		}
		return rVal;
	}
	
	/**
	 * creates a {@link RecommendedTag} based on the xml tag
	 * 
	 * @param xmlTag
	 * @return the created recommended Tag
	 */
	public RecommendedTag createRecommendedTag(final TagType xmlTag) {
		final RecommendedTag tag = new RecommendedTag();
		tag.setName(xmlTag.getName()); assert(tag.getName()!=null);
		if (xmlTag.getGlobalcount() != null) tag.setGlobalcount(xmlTag.getGlobalcount().intValue());
		if (xmlTag.getUsercount() != null) tag.setUsercount(xmlTag.getUsercount().intValue());
		if (xmlTag.getConfidence() != null ) tag.setConfidence(xmlTag.getConfidence().doubleValue());
		if (xmlTag.getScore() != null ) tag.setScore(xmlTag.getScore().doubleValue());
		return tag;
	}
	
	/**
	 * creates a {@link GoldStandard} post based on the xml post
	 * 
	 * @param xmlPost
	 * @return the converted post
	 */
	public Post<Resource> createStandardPost(final PostType xmlPost) {
		checkStandardPost(xmlPost);
		
		final Post<Resource> post = this.createPostWithUserAndDate(xmlPost);
		
		final GoldStandardPublicationType xmlPublication = xmlPost.getGoldStandardPublication();
		if (present(xmlPublication)) {
			ModelValidationUtils.checkPublication(xmlPublication);
			final GoldStandardPublication publication = new GoldStandardPublication();
			this.fillPublicationWithInformations(xmlPublication, publication);
			
			checkPublication(publication);
			
			post.setResource(publication);
		} else {
			// TODO: implement a gold standard for bookmarks?!?
			throw new InvalidModelException("resource is not supported");
		}
		
		return post;
	}

	/**
	 * converts a xml post to the model post
	 * 
	 * @param xmlPost
	 * @return the converted post
	 */
	public Post<Resource> createPost(final PostType xmlPost) {
		checkPost(xmlPost);

		// create post, user and date
		final Post<Resource> post = this.createPostWithUserAndDate(xmlPost);

		// create tags
		for (final TagType xmlTag : xmlPost.getTag()) {
			checkTag(xmlTag);

			final Tag tag = new Tag();
			tag.setName(xmlTag.getName());
			post.getTags().add(tag);
		}

		// create resource
		final BibtexType xmlPublication = xmlPost.getBibtex();
		if (xmlPublication != null) {
			ModelValidationUtils.checkPublication(xmlPublication);
			
			final BibTex publication = new BibTex();
			this.fillPublicationWithInformations(xmlPublication, publication);
			
			/*
			 * check, of the post contains documents
			 */
			final DocumentsType xmlDocuments = xmlPost.getDocuments();
			if (xmlDocuments != null) {
				final List<Document> documents = new LinkedList<Document>();
				for (final DocumentType xmlDocument : xmlDocuments.getDocument()) {
					final Document document = new Document();
					document.setFileName(xmlDocument.getFilename());
					document.setMd5hash(xmlDocument.getMd5Hash());
					documents.add(document);
				}
				publication.setDocuments(documents);
			}

			checkPublication(publication);

			post.setResource(publication);
		}
		
		final BookmarkType xmlBookmark = xmlPost.getBookmark();
		if (xmlBookmark != null) {
			checkBookmark(xmlBookmark);

			final Bookmark bookmark = new Bookmark();
			bookmark.setIntraHash(xmlBookmark.getIntrahash());
			bookmark.setTitle(xmlBookmark.getTitle());
			bookmark.setUrl(xmlBookmark.getUrl());

			post.setResource(bookmark);
		}
		
		if (xmlPost.getGroup() != null) {
			post.setGroups(new HashSet<Group>());
			for (final GroupType xmlGroup : xmlPost.getGroup()) {
				checkGroup(xmlGroup);
				final Group group = new Group();
				group.setDescription(xmlGroup.getDescription());
				group.setName(xmlGroup.getName());
				post.getGroups().add(group);
			}
		}

		return post;
	}

	/**
	 * @param xmlPost
	 * @return
	 */
	private Post<Resource> createPostWithUserAndDate(final PostType xmlPost) {
		final Post<Resource> post = new Post<Resource>();
		post.setDescription(xmlPost.getDescription());

		// user
		final User user = this.createUser(xmlPost);
		post.setUser(user);
		post.setDate(createDate(xmlPost.getPostingdate()));
		post.setChangeDate(createDate(xmlPost.getChangedate()));
		return post;
	}

	/**
	 * @param xmlPost
	 * @param post
	 */
	private User createUser(final PostType xmlPost) {
		final User user = new User();
		final UserType xmlUser = xmlPost.getUser();
		checkUser(xmlUser);
		user.setName(xmlUser.getName());
		
		return user;
	}

	/**
	 * @param xmlPublication
	 * @param publication
	 */
	private void fillPublicationWithInformations(final BibtexType xmlPublication, final BibTex publication) {
		publication.setAddress(xmlPublication.getAddress());
		publication.setAnnote(xmlPublication.getAnnote());
		publication.setAuthor(xmlPublication.getAuthor());
		publication.setAbstract(xmlPublication.getBibtexAbstract());
		publication.setBibtexKey(xmlPublication.getBibtexKey());
		publication.setKey(xmlPublication.getBKey());
		publication.setBooktitle(xmlPublication.getBooktitle());
		publication.setChapter(xmlPublication.getChapter());
		publication.setCrossref(xmlPublication.getCrossref());
		publication.setDay(xmlPublication.getDay());
		publication.setEdition(xmlPublication.getEdition());
		publication.setEditor(xmlPublication.getEditor());
		publication.setEntrytype(xmlPublication.getEntrytype());
		publication.setHowpublished(xmlPublication.getHowpublished());
		publication.setInstitution(xmlPublication.getInstitution());
		publication.setInterHash(xmlPublication.getInterhash());
		publication.setIntraHash(xmlPublication.getIntrahash());
		publication.setJournal(xmlPublication.getJournal());
		publication.setMisc(xmlPublication.getMisc());
		publication.setMonth(xmlPublication.getMonth());
		publication.setNote(xmlPublication.getNote());
		publication.setNumber(xmlPublication.getNumber());
		publication.setOrganization(xmlPublication.getOrganization());
		publication.setPages(xmlPublication.getPages());
		publication.setPublisher(xmlPublication.getPublisher());
		publication.setSchool(xmlPublication.getSchool());
		publication.setSeries(xmlPublication.getSeries());
		publication.setTitle(xmlPublication.getTitle());
		publication.setType(xmlPublication.getType());
		publication.setUrl(xmlPublication.getUrl());
		publication.setVolume(xmlPublication.getVolume());
		publication.setYear(xmlPublication.getYear());
		publication.setPrivnote(xmlPublication.getPrivnote());
	}

	/**
	 * Checks the bibtex. Here a model validator can be plugged in which does the checking.
	 * 
	 * @param publication
	 */
	protected void checkPublication(final BibTex publication) {
		if (modelValidator != null) {
			modelValidator.checkPublication(publication);
		}
	}

	/**
	 * Helper method to create a date when parsing a post. Two situations may occur:
	 * 
	 * 1/ The post is parsed on client side. Then the date is the one as sent by
	 *    the BibSonomy API.
	 *    
	 * 2/ The post is parsed on server side; this only happens in the two strategies
	 *       {@link org.bibsonomy.rest.strategy.users.PutPostStragegy}} and 
	 *       {@link org.bibsonomy.rest.strategy.users.PostPostStragegy}.
	 *    In both strategies, the date is overwritten in order to prevent malicious users
	 *    from posting posts with faked dates (e.g. from the future)
	 *    
	 * @param date - the date of the XML post
	 * @return a date for this post
	 */
	private Date createDate(final XMLGregorianCalendar date) {
		/*
		 * If there is no date, use the current date. 
		 */
		if (date == null) {
			return new Date();
		}
		/*
		 * this is save because the postingdate is overwritten in the corresponding
		 * strategies when creating or updating a post (see above) 
		 */
		return date.toGregorianCalendar().getTime();
	}

	/**
	 * @return the modelValidator
	 */
	public ModelValidator getModelValidator() {
		return this.modelValidator;
	}

	/**
	 * @param modelValidator the modelValidator to set
	 */
	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}
}