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

package org.bibsonomy.rest.renderer.impl;

import static org.bibsonomy.model.util.ModelValidationUtils.checkBookmark;
import static org.bibsonomy.model.util.ModelValidationUtils.checkGroup;
import static org.bibsonomy.model.util.ModelValidationUtils.checkPublication;
import static org.bibsonomy.model.util.ModelValidationUtils.checkTag;
import static org.bibsonomy.model.util.ModelValidationUtils.checkUser;
import static org.bibsonomy.rest.RestProperties.Property.VALIDATE_XML_INPUT;
import static org.bibsonomy.rest.RestProperties.Property.VALIDATE_XML_OUTPUT;
import static org.bibsonomy.util.ValidationUtils.present;

import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.GoldStandardPublication;
import org.bibsonomy.model.Group;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.model.User;
import org.bibsonomy.model.comparators.RecommendedTagComparator;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.renderer.Renderer;
import org.bibsonomy.rest.renderer.UrlRenderer;
import org.bibsonomy.rest.renderer.xml.BibsonomyXML;
import org.bibsonomy.rest.renderer.xml.BibtexType;
import org.bibsonomy.rest.renderer.xml.BookmarkType;
import org.bibsonomy.rest.renderer.xml.DocumentType;
import org.bibsonomy.rest.renderer.xml.DocumentsType;
import org.bibsonomy.rest.renderer.xml.GoldStandardPublicationType;
import org.bibsonomy.rest.renderer.xml.GroupType;
import org.bibsonomy.rest.renderer.xml.GroupsType;
import org.bibsonomy.rest.renderer.xml.ModelFactory;
import org.bibsonomy.rest.renderer.xml.ObjectFactory;
import org.bibsonomy.rest.renderer.xml.PostType;
import org.bibsonomy.rest.renderer.xml.PostsType;
import org.bibsonomy.rest.renderer.xml.ReferenceType;
import org.bibsonomy.rest.renderer.xml.ReferencesType;
import org.bibsonomy.rest.renderer.xml.StatType;
import org.bibsonomy.rest.renderer.xml.TagType;
import org.bibsonomy.rest.renderer.xml.TagsType;
import org.bibsonomy.rest.renderer.xml.UserType;
import org.bibsonomy.rest.renderer.xml.UsersType;
import org.xml.sax.SAXParseException;

/**
 * @author dzo
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * 
 * @version $Id: JAXBRenderer.java,v 1.9 2011-06-09 11:59:14 rja Exp $
 */
public abstract class JAXBRenderer implements Renderer {
	private static final Log log = LogFactory.getLog(JAXBRenderer.class);
	
	protected static final String JAXB_PACKAGE_DECLARATION = "org.bibsonomy.rest.renderer.xml";
	
	protected static Schema schema;
	
	
	protected final DatatypeFactory datatypeFactory;
	protected final UrlRenderer urlRenderer;
	protected final boolean validateXMLInput;
	protected final boolean validateXMLOutput;
	
	protected JAXBRenderer(final UrlRenderer urlRenderer) {
		final RestProperties properties = RestProperties.getInstance();
		this.urlRenderer = urlRenderer;

		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (final DatatypeConfigurationException ex) {
			throw new RuntimeException("Could not instantiate data type factory.", ex);
		}

		this.validateXMLInput = "true".equals( properties.get(VALIDATE_XML_INPUT) );
		this.validateXMLOutput = "true".equals( properties.get(VALIDATE_XML_OUTPUT) );
		ModelFactory.getInstance().setModelValidator(properties.getModelValidator());

		// we only need to load the XML schema if we validate input or output
		if (this.validateXMLInput || this.validateXMLOutput) {
			try {
				schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(this.getClass().getClassLoader().getResource("xschema.xsd"));
			} catch (final Exception e) {
				log.error("Failed to load XML schema", e);
				schema = null;
			}
		} else {
			schema = null;
		}
	}
	
	/**
	 * Unmarshalls the document from the reader to the generated java
	 * model.
	 * 
	 * @return A BibsonomyXML object that contains the unmarshalled content
	 * @throws InternServerException
	 *             if the content can't be unmarshalled
	 */
	private BibsonomyXML parse(final Reader reader) throws InternServerException {
		// first: check the reader 
		this.checkReader(reader);
		try {
			// initialize JAXB context. We provide the classloader here because we experienced that under
			// certain circumstances (e.g. when used within JabRef as a JPF-Plugin), the wrong classloader is
			// used which has the following exception as consequence:
			//
			//   javax.xml.bind.JAXBException: "org.bibsonomy.rest.renderer.xml" doesnt contain ObjectFactory.class or jaxb.index
			//
			// (see also http://ws.apache.org/jaxme/apidocs/javax/xml/bind/JAXBContext.html)
			final JAXBContext jc = this.getJAXBContext();

			// create an Unmarshaller
			final Unmarshaller u = jc.createUnmarshaller();

			// set schema to validate input documents
			if (this.validateXMLInput) {
				u.setSchema(schema);
			}

			/*
			 * unmarshal a xml instance document into a tree of Java content
			 * objects composed of classes from the restapi package.
			 */
			final JAXBElement<BibsonomyXML> xmlDoc = unmarshal(u, reader);
			return xmlDoc.getValue();
		} catch (final JAXBException e) {
			if (e.getLinkedException() != null && e.getLinkedException().getClass() == SAXParseException.class) {
				final SAXParseException ex = (SAXParseException) e.getLinkedException();
				throw new BadRequestOrResponseException(
						"Error while parsing XML (Line " 
						+ ex.getLineNumber() + ", Column "
						+ ex.getColumnNumber() + ": "
						+ ex.getMessage()
				);				
			}			
			throw new InternServerException(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	protected JAXBElement<BibsonomyXML> unmarshal(final Unmarshaller u, final Reader reader) throws JAXBException {
		return (JAXBElement<BibsonomyXML>) u.unmarshal(reader);
	}
	
	private void checkReader(final Reader reader) throws BadRequestOrResponseException {
		if (reader == null) throw new BadRequestOrResponseException("The body part of the received document is missing");
	}
	
	/**
	 * Initializes java xml bindings, builds the document and then marshalls
	 * it to the writer.
	 * 
	 * @throws InternServerException
	 *             if the document can't be marshalled
	 */
	private void serialize(final Writer writer, final BibsonomyXML xmlDoc) throws InternServerException {
		try {
			// initialize context for java xml bindings
			final JAXBContext jc = this.getJAXBContext();

			// buildup document model
			final JAXBElement<BibsonomyXML> webserviceElement = new ObjectFactory().createBibsonomy(xmlDoc);

			// create a marshaller
			final Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			if (this.validateXMLOutput) {
				// validate the XML produced by the marshaller
				marshaller.setSchema(schema);
			}

			// marshal to the writer
			this.marshal(marshaller, webserviceElement, writer);
			// TODO log
			// log.debug("");
		} catch (final JAXBException e) {
			final Throwable linkedException = e.getLinkedException();
			if (present(linkedException) && linkedException.getClass() == SAXParseException.class) {
				final SAXParseException ex = (SAXParseException) linkedException;
				throw new BadRequestOrResponseException(
						"Error while parsing XML (Line " 
						+ ex.getLineNumber() + ", Column "
						+ ex.getColumnNumber() + ": "
						+ ex.getMessage()
				);				
			}						
			throw new InternServerException(e.toString());
		}
	}

	protected void marshal(Marshaller marshaller, JAXBElement<BibsonomyXML> webserviceElement, Writer writer) throws JAXBException {
		marshaller.marshal(webserviceElement, writer);
	}

	protected abstract JAXBContext getJAXBContext() throws JAXBException;

	@Override
	public void serializePosts(final Writer writer, final List<? extends Post<? extends Resource>> posts, final ViewModel viewModel) throws InternServerException {
		final PostsType xmlPosts = new PostsType();
		if (viewModel != null) {
			xmlPosts.setEnd(BigInteger.valueOf(viewModel.getEndValue()));
			if (viewModel.getUrlToNextResources() != null) xmlPosts.setNext(viewModel.getUrlToNextResources());
			xmlPosts.setStart(BigInteger.valueOf(viewModel.getStartValue()));
		} else if( posts!=null ) {
			xmlPosts.setStart(BigInteger.valueOf(0));
			xmlPosts.setEnd(BigInteger.valueOf(posts.size()));
		} else {
			xmlPosts.setStart(BigInteger.valueOf(0));
			xmlPosts.setEnd(BigInteger.valueOf(0));
		}
		
		if (present(posts)) {
			for (final Post<? extends Resource> post : posts) {
				final PostType xmlPost = createXmlPost(post);
				xmlPosts.getPost().add(xmlPost);
			}
		}
		
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setPosts(xmlPosts);
		serialize(writer, xmlDoc);
	}

	@Override
	public void serializePost(final Writer writer, final Post<? extends Resource> post, final ViewModel xxx) throws InternServerException {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setPost(createXmlPost(post));
		serialize(writer, xmlDoc);
	}

	protected PostType createXmlPost(final Post<? extends Resource> post) throws InternServerException {
		final PostType xmlPost = new PostType();
		fillXmlPost(xmlPost, post);
		return xmlPost;
	}
	
	protected void fillXmlPost(final PostType xmlPost, final Post<? extends Resource> post) {
		checkPost(post);

		// set user
		checkUser(post.getUser());
		final UserType xmlUser = new UserType();
		xmlUser.setName(post.getUser().getName());
		xmlUser.setHref(urlRenderer.createHrefForUser(post.getUser().getName()));
		xmlPost.setUser(xmlUser);
		if (post.getDate() != null)
			xmlPost.setPostingdate(createXmlCalendar(post.getDate()));
		if (post.getChangeDate() != null)
			xmlPost.setChangedate(createXmlCalendar(post.getChangeDate()));
		
		// add tags
		if (post.getTags() != null) {
			for (final Tag t : post.getTags()) {
				checkTag(t);
				final TagType xmlTag = new TagType();
				xmlTag.setName(t.getName());
				xmlTag.setHref(urlRenderer.createHrefForTag(t.getName()));
				xmlPost.getTag().add(xmlTag);
			}
		}

		// add groups
		for (final Group group : post.getGroups()) {
			checkGroup(group);
			final GroupType xmlGroup = new GroupType();
			xmlGroup.setName(group.getName());
			xmlGroup.setHref(urlRenderer.createHrefForGroup(group.getName()));
			xmlPost.getGroup().add(xmlGroup);
		}

		xmlPost.setDescription(post.getDescription());
		
		// check if the resource is a publication
		final Resource resource = post.getResource();
		if (resource instanceof BibTex && !(resource instanceof GoldStandardPublication)) {
			final BibTex publication = (BibTex) post.getResource();
			checkPublication(publication);
			final String userName = post.getUser().getName();
			final BibtexType xmlBibtex = new BibtexType();

			xmlBibtex.setHref(urlRenderer.createHrefForResource(userName, publication.getIntraHash()));
			
			fillXmlPublicationDetails(publication, xmlBibtex);
			
			xmlPost.setBibtex(xmlBibtex);
			
			// if the publication has documents …
			final List<Document> documents = publication.getDocuments();
			if (documents != null) {
			
				checkPublication(publication);
				// … put them into the xml output
				final DocumentsType xmlDocuments = new DocumentsType();
				for (final Document document : documents){
					final DocumentType xmlDocument = new DocumentType();
					xmlDocument.setFilename(document.getFileName());
					xmlDocument.setMd5Hash(document.getMd5hash());
					xmlDocument.setHref(urlRenderer.createHrefForResourceDocument(userName, publication.getIntraHash(), document.getFileName()));
					xmlDocuments.getDocument().add(xmlDocument);
				}
				xmlPost.setDocuments(xmlDocuments);
			}
		}
		// if resource is a bookmark create a xml representation
		if (resource instanceof Bookmark) {
			final Bookmark bookmark = (Bookmark) post.getResource();
			checkBookmark(bookmark);
			final BookmarkType xmlBookmark = new BookmarkType();
			xmlBookmark.setHref(urlRenderer.createHrefForResource(post.getUser().getName(), bookmark.getIntraHash()));
			xmlBookmark.setInterhash(bookmark.getInterHash());
			xmlBookmark.setIntrahash(bookmark.getIntraHash());
			xmlBookmark.setTitle(bookmark.getTitle());
			xmlBookmark.setUrl(bookmark.getUrl());
			xmlPost.setBookmark(xmlBookmark);
		}
		
		if (resource instanceof GoldStandardPublication) {
			/*
			 * first clear tags; gold standard publications have (currently) no tags
			 */
			xmlPost.getTag().clear();
			
			final GoldStandardPublication publication = (GoldStandardPublication) post.getResource();
			
			final GoldStandardPublicationType xmlPublication = new GoldStandardPublicationType();
			this.fillXmlPublicationDetails(publication, xmlPublication);
			
			/*
			 * add references
			 */
			final ReferencesType xmlReferences = new ReferencesType();
			xmlPublication.setReferences(xmlReferences);

			final List<ReferenceType> referenceList = xmlReferences.getReference();
			
			for (final BibTex reference : publication.getReferences()) {
				final ReferenceType xmlReference = new ReferenceType();
				xmlReference.setInterhash(reference.getInterHash());
				
				referenceList.add(xmlReference);
			}
			
			xmlPost.setGoldStandardPublication(xmlPublication);
		}
		

	}

	protected void fillXmlPublicationDetails(final BibTex publication, final BibtexType xmlPublication) {
		xmlPublication.setAddress(publication.getAddress());
		xmlPublication.setAnnote(publication.getAnnote());
		xmlPublication.setAuthor(publication.getAuthor());
		xmlPublication.setBibtexAbstract(publication.getAbstract());
		xmlPublication.setBibtexKey(publication.getBibtexKey());
		xmlPublication.setBKey(publication.getKey());
		xmlPublication.setBooktitle(publication.getBooktitle());
		xmlPublication.setChapter(publication.getChapter());
		xmlPublication.setCrossref(publication.getCrossref());
		xmlPublication.setDay(publication.getDay());
		xmlPublication.setEdition(publication.getEdition());
		xmlPublication.setEditor(publication.getEditor());
		xmlPublication.setEntrytype(publication.getEntrytype());
		xmlPublication.setHowpublished(publication.getHowpublished());
		xmlPublication.setInstitution(publication.getInstitution());
		xmlPublication.setInterhash(publication.getInterHash());
		xmlPublication.setIntrahash(publication.getIntraHash());
		xmlPublication.setJournal(publication.getJournal());
		xmlPublication.setMisc(publication.getMisc());
		xmlPublication.setMonth(publication.getMonth());
		xmlPublication.setNote(publication.getNote());
		xmlPublication.setNumber(publication.getNumber());
		xmlPublication.setOrganization(publication.getOrganization());
		xmlPublication.setPages(publication.getPages());
		xmlPublication.setPublisher(publication.getPublisher());
		xmlPublication.setSchool(publication.getSchool());
		xmlPublication.setSeries(publication.getSeries());
		xmlPublication.setTitle(publication.getTitle());
		xmlPublication.setType(publication.getType());
		xmlPublication.setUrl(publication.getUrl());
		xmlPublication.setVolume(publication.getVolume());
		xmlPublication.setYear(publication.getYear());
		xmlPublication.setPrivnote(publication.getPrivnote());
	}

	private XMLGregorianCalendar createXmlCalendar(final Date date) {
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return datatypeFactory.newXMLGregorianCalendar(cal);
	}

	private void checkPost(final Post<? extends Resource> post) throws InternServerException {
		if (post.getUser() == null) throw new InternServerException("error no user assigned!");
		// there may be posts whithout tags
		// 2009/01/07, fei: as stated above - there are situations where posts don't have tags
		//                  for example, when serializing a post for submission to a remote 
		//                  recommender -> commenting out
		// 2010/03/19, dzo: gold standard posts have also no tags
		// if( post.getTags() == null || post.getTags().size() == 0 ) throw new InternServerException( "error no tags assigned!" );
		if (post.getResource() == null) throw new InternServerException("error no ressource assigned!");
	}

	@Override
	public void serializeUsers(final Writer writer, final List<User> users, final ViewModel viewModel) throws InternServerException {
		final UsersType xmlUsers = new UsersType();
		if (viewModel != null) {
			xmlUsers.setEnd(BigInteger.valueOf(viewModel.getEndValue()));
			if (viewModel.getUrlToNextResources() != null) xmlUsers.setNext(viewModel.getUrlToNextResources());
			xmlUsers.setStart(BigInteger.valueOf(viewModel.getStartValue()));
		} else if( users!=null ) {
			xmlUsers.setStart(BigInteger.valueOf(0));
			xmlUsers.setEnd(BigInteger.valueOf(users.size()));
		} else {
			xmlUsers.setStart(BigInteger.valueOf(0));
			xmlUsers.setEnd(BigInteger.valueOf(0));
		}
		
		if (present(users)) {
			for (final User user : users) {
				xmlUsers.getUser().add(createXmlUser(user));
			}
		}
		
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setUsers(xmlUsers);
		serialize(writer, xmlDoc);
	}

	@Override
	public void serializeUser(final Writer writer, final User user, final ViewModel viewModel) throws InternServerException {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setUser(createXmlUser(user));
		serialize(writer, xmlDoc);
	}

	private UserType createXmlUser(final User user) throws InternServerException {
		checkUser(user);
		final UserType xmlUser = new UserType();
		xmlUser.setEmail(user.getEmail());
		if (user.getHomepage() != null) {
			xmlUser.setHomepage(user.getHomepage().toString());
		}
		xmlUser.setName(user.getName());
		xmlUser.setRealname(user.getRealname());
		xmlUser.setHref(urlRenderer.createHrefForUser(user.getName()));
		if (user.getSpammer() != null)
			xmlUser.setSpammer(user.getSpammer());
		if (user.getPrediction() != null)
			xmlUser.setPrediction(BigInteger.valueOf(user.getPrediction()));
		if (user.getConfidence() != null)
			xmlUser.setConfidence(Double.valueOf(user.getConfidence()));
		xmlUser.setAlgorithm(user.getAlgorithm());
		xmlUser.setClassifierMode(user.getMode());
		if (user.getToClassify() != null)
			xmlUser.setToClassify(BigInteger.valueOf(user.getToClassify()));

		/*
		 * copy groups
		 */
		final List<Group> groups = user.getGroups();
		xmlUser.setGroups(new GroupsType());
		if (groups != null) {
			final List<GroupType> group2 = xmlUser.getGroups().getGroup();
			for (final Group group: groups) {
				group2.add(createXmlGroup(group));
			}
			xmlUser.getGroups().setStart(BigInteger.valueOf(0));
			xmlUser.getGroups().setEnd(BigInteger.valueOf(groups.size()));
		}
		return xmlUser;
	}

	@Override
	public void serializeTags(final Writer writer, final List<Tag> tags, final ViewModel viewModel) throws InternServerException {
		final TagsType xmlTags = new TagsType();
		if (viewModel != null) {
			xmlTags.setEnd(BigInteger.valueOf(viewModel.getEndValue()));
			if (viewModel.getUrlToNextResources() != null) xmlTags.setNext(viewModel.getUrlToNextResources());
			xmlTags.setStart(BigInteger.valueOf(viewModel.getStartValue()));
		} else if( tags!=null ) {
			xmlTags.setStart(BigInteger.valueOf(0));
			xmlTags.setEnd(BigInteger.valueOf(tags.size()));
		} else {
			xmlTags.setStart(BigInteger.valueOf(0));
			xmlTags.setEnd(BigInteger.valueOf(0));
		}
		
		if (present(tags)) {
			for (final Tag tag : tags) {
				xmlTags.getTag().add(createXmlTag(tag));
			}
		}
		
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setTags(xmlTags);
		serialize(writer, xmlDoc);
	}

	@Override
	public void serializeTag(final Writer writer, final Tag tag, final ViewModel model) throws InternServerException {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setTag(createXmlTag(tag));
		serialize(writer, xmlDoc);
	}

	private TagType createXmlTag(final Tag tag) throws InternServerException {
		final TagType xmlTag = new TagType();
		checkTag(tag);
		xmlTag.setName(tag.getName());
		xmlTag.setHref(urlRenderer.createHrefForTag(tag.getName()));
		// if (tag.getGlobalcount() > 0) {
		xmlTag.setGlobalcount(BigInteger.valueOf(tag.getGlobalcount()));
		// }
		// if (tag.getUsercount() > 0) {
		xmlTag.setUsercount(BigInteger.valueOf(tag.getUsercount()));
		// }

		// add sub-/supertags - dbe, 20070718
		if (tag.getSubTags() != null && tag.getSubTags().size() > 0) {			
			xmlTag.getSubTags().add(createXmlTags(tag.getSubTags()));		
		}
		if (tag.getSuperTags() != null && tag.getSuperTags().size() > 0) {
			xmlTag.getSuperTags().add(createXmlTags(tag.getSuperTags()));
		}
		return xmlTag;
	}

	private TagsType createXmlTags(final List<Tag> tags) {
		final TagsType xmlTags = new TagsType();
		for (final Tag tag : tags) {
			xmlTags.getTag().add(createXmlTag(tag));				
		}
		xmlTags.setStart(BigInteger.valueOf(0));
		xmlTags.setEnd(BigInteger.valueOf(tags.size()));		
		return xmlTags;
	}

	/**
	 * Creates an xml-representation for a given {@link RecommendedTag}.
	 * @param tag recommended tag to encode
	 * @return an {@link TagType} for given {@link RecommendedTag}
	 * @throws InternServerException
	 */
	private TagType createXmlRecommendedTag(final RecommendedTag tag) throws InternServerException {
		final TagType xmlTag = new TagType();
		checkTag(tag);
		xmlTag.setName(tag.getName());
		xmlTag.setHref(urlRenderer.createHrefForTag(tag.getName()));
		// if (tag.getGlobalcount() > 0) {
		xmlTag.setGlobalcount(BigInteger.valueOf(tag.getGlobalcount()));
		// }
		// if (tag.getUsercount() > 0) {
		xmlTag.setUsercount(BigInteger.valueOf(tag.getUsercount()));
		// }
		xmlTag.setConfidence(tag.getConfidence());
		xmlTag.setScore(tag.getScore());

		return xmlTag;
	}

	@Override
	public void serializeGroups(final Writer writer, final List<Group> groups, final ViewModel viewModel) throws InternServerException {
		final GroupsType xmlGroups = new GroupsType();
		if (viewModel != null) {
			xmlGroups.setEnd(BigInteger.valueOf(viewModel.getEndValue()));
			if (viewModel.getUrlToNextResources() != null) xmlGroups.setNext(viewModel.getUrlToNextResources());
			xmlGroups.setStart(BigInteger.valueOf(viewModel.getStartValue()));
		} else if (groups!=null) {
			xmlGroups.setStart(BigInteger.valueOf(0));
			xmlGroups.setEnd(BigInteger.valueOf(groups.size()));
		} else {
			xmlGroups.setStart(BigInteger.valueOf(0));
			xmlGroups.setEnd(BigInteger.valueOf(0));
		}
		
		if (present(groups)) {
			for (final Group group : groups) {
				xmlGroups.getGroup().add(createXmlGroup(group));
			}
		}
		
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setGroups(xmlGroups);
		serialize(writer, xmlDoc);
	}

	@Override
	public void serializeGroup(final Writer writer, final Group group, final ViewModel model) throws InternServerException {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setGroup(createXmlGroup(group));
		serialize(writer, xmlDoc);
	}

	private GroupType createXmlGroup(final Group group) {
		checkGroup(group);
		final GroupType xmlGroup = new GroupType();
		xmlGroup.setName(group.getName());
		xmlGroup.setDescription(group.getDescription());
		xmlGroup.setRealname(group.getRealname());
		if (group.getHomepage() != null) {
			xmlGroup.setHomepage(group.getHomepage().toString());
		}
		xmlGroup.setHref(urlRenderer.createHrefForGroup(group.getName()));
		xmlGroup.setDescription(group.getDescription());
		if (group.getUsers() != null) {
			for (final User user : group.getUsers()) {
				xmlGroup.getUser().add(createXmlUser(user));
			}
		}
		return xmlGroup;
	}

	@Override
	public void serializeOK(final Writer writer) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		serialize(writer, xmlDoc);
	}

	@Override
	public void serializeFail(final Writer writer) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.FAIL);
		serialize(writer, xmlDoc);
	}	

	@Override
	public void serializeError(final Writer writer, final String errorMessage) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.FAIL);
		xmlDoc.setError(errorMessage);
		serialize(writer, xmlDoc);
	}

	@Override
	public void serializeGroupId(final Writer writer, final String groupId) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setGroupid(groupId);
		serialize(writer, xmlDoc);		
	}

	@Override
	public void serializeResourceHash(final Writer writer, final String hash) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setResourcehash(hash);
		serialize(writer, xmlDoc);		
	}

	@Override
	public void serializeUserId(final Writer writer, final String userId) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setUserid(userId);
		serialize(writer, xmlDoc);		
	}

	@Override
	public void serializeURI(final Writer writer, final String uri) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setUri(uri);
		serialize(writer, xmlDoc);		
	}	

	@Override
	public String parseError(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getError() != null) {
			return xmlDoc.getError();
		}
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no error defined.");
	}

	@Override
	public User parseUser(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);

		if (xmlDoc.getUser() != null) {
			return ModelFactory.getInstance().createUser(xmlDoc.getUser());
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no user defined.");
	}

	@Override
	public Post<? extends Resource> parsePost(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		
		if (xmlDoc.getPost() != null) {
			return ModelFactory.getInstance().createPost(xmlDoc.getPost());
		}
		
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no post defined.");
	}

	@Override
	public Post<? extends Resource> parseStandardPost(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		
		if (xmlDoc.getPost() != null) {
			return ModelFactory.getInstance().createStandardPost(xmlDoc.getPost());
		}
		
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no post defined.");
	}

	@Override
	public Group parseGroup(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);

		if (xmlDoc.getGroup() != null) {
			return ModelFactory.getInstance().createGroup(xmlDoc.getGroup());
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no group defined.");
	}

	@Override
	public List<Group> parseGroupList(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getGroups() != null) {
			final List<Group> groups = new LinkedList<Group>();
			for (final GroupType gt : xmlDoc.getGroups().getGroup()) {
				final Group g = ModelFactory.getInstance().createGroup(gt);
				groups.add(g);
			}
			return groups;
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no list of groups defined.");
	}

	@Override
	public List<Post<? extends Resource>> parsePostList(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getPosts() != null) {
			final List<Post<? extends Resource>> posts = new LinkedList<Post<? extends Resource>>();
			for (final PostType pt : xmlDoc.getPosts().getPost()) {
				final Post<? extends Resource> p = ModelFactory.getInstance().createPost(pt);
				posts.add(p);
			}
			return posts;
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no list of posts defined.");
	}

	@Override
	public List<Tag> parseTagList(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getTags() != null) {
			final List<Tag> tags = new LinkedList<Tag>();
			for (final TagType tt : xmlDoc.getTags().getTag()) {
				final Tag t = ModelFactory.getInstance().createTag(tt);
				tags.add(t);
			}
			return tags;
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no list of tags defined.");
	}

	@Override
	public List<User> parseUserList(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getUsers() != null) {
			final List<User> users = new LinkedList<User>();
			for (final UserType ut : xmlDoc.getUsers().getUser()) {
				final User u = ModelFactory.getInstance().createUser(ut);
				users.add(u);
			}
			return users;
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no list of users defined.");
	}

	@Override
	public Set<String> parseReferences(final Reader reader) {
		final BibsonomyXML xmlDoc = this.parse(reader);
		final ReferencesType referencesType = xmlDoc.getReferences();
		
		if (present(referencesType)) {
			final Set<String> references = new HashSet<String>();
			final List<ReferenceType> referenceList = referencesType.getReference();
			
			if (present(referenceList)) {
				for (final ReferenceType referenceType : referenceList) {
					references.add(referenceType.getInterhash());
				}
			}
			
			return references;
		}		
		
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no list of references defined.");
	}
	
	@Override
	public Tag parseTag(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getTag() != null) {
			return ModelFactory.getInstance().createTag(xmlDoc.getTag());
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no tag defined.");
	}

	@Override
	public String parseStat(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getStat() != null) {
			return xmlDoc.getStat().value();
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no status defined.");
	}

	@Override
	public String parseGroupId(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getGroupid() != null) {
			return xmlDoc.getGroupid();
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no group id.");
	}

	@Override
	public String parseResourceHash(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getResourcehash() != null) {
			return xmlDoc.getResourcehash();
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no resource hash defined.");
	}

	@Override
	public String parseUserId(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getUserid() != null) {
			return xmlDoc.getUserid();
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no user id defined.");
	}

	@Override
	public RecommendedTag parseRecommendedTag(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getTag() != null) {
			return ModelFactory.getInstance().createRecommendedTag(xmlDoc.getTag());
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no tag defined.");
	}

	@Override
	public SortedSet<RecommendedTag> parseRecommendedTagList(final Reader reader) throws BadRequestOrResponseException {
		final BibsonomyXML xmlDoc = this.parse(reader);
		if (xmlDoc.getTags() != null) {
			final SortedSet<RecommendedTag> tags = new TreeSet<RecommendedTag>(new RecommendedTagComparator());
			for (final TagType tt : xmlDoc.getTags().getTag()) {
				final RecommendedTag t = ModelFactory.getInstance().createRecommendedTag(tt);
				tags.add(t);
			}
			return tags;
		}
		if (xmlDoc.getError() != null) throw new BadRequestOrResponseException(xmlDoc.getError());
		throw new BadRequestOrResponseException("The body part of the received document is erroneous - no list of tags defined.");
	}

	@Override
	public void serializeRecommendedTag(final Writer writer, final RecommendedTag tag) {
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setTag(this.createXmlRecommendedTag(tag));
		serialize(writer, xmlDoc);		
	}
	
	@Override
	public void serializeRecommendedTags(final Writer writer, final Collection<RecommendedTag> tags) {		
		final TagsType xmlTags = new TagsType();
		xmlTags.setStart(BigInteger.valueOf(0));
		xmlTags.setEnd(BigInteger.valueOf(tags != null ? tags.size() : 0));
		if (tags != null) {
			for (final RecommendedTag tag : tags) {
				xmlTags.getTag().add(createXmlRecommendedTag(tag));
			}
		}
		final BibsonomyXML xmlDoc = new BibsonomyXML();
		xmlDoc.setStat(StatType.OK);
		xmlDoc.setTags(xmlTags);
		serialize(writer, xmlDoc);	
	}
}
