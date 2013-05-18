/**
 *
 *  BibSonomy-OpenAccess - Check Open Access Policies for Publications
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

package de.unikassel.puma.openaccess.sword;


import static org.bibsonomy.util.ValidationUtils.present;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.database.systemstags.SystemTagsUtil;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.exceptions.BadRequestOrResponseException;
import org.bibsonomy.rest.renderer.UrlRenderer;
import org.bibsonomy.rest.renderer.impl.JAXBRenderer;
import org.bibsonomy.rest.renderer.xml.BibtexType;
import org.bibsonomy.rest.renderer.xml.TagType;
import org.bibsonomy.util.XmlUtils;
import org.xml.sax.SAXParseException;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import de.unikassel.puma.openaccess.sword.renderer.xml.DivType;
import de.unikassel.puma.openaccess.sword.renderer.xml.FileType;
import de.unikassel.puma.openaccess.sword.renderer.xml.MdSecType;
import de.unikassel.puma.openaccess.sword.renderer.xml.Mets;
import de.unikassel.puma.openaccess.sword.renderer.xml.ObjectFactory;
import de.unikassel.puma.openaccess.sword.renderer.xml.PumaPost;
import de.unikassel.puma.openaccess.sword.renderer.xml.PumaUserType;
import de.unikassel.puma.openaccess.sword.renderer.xml.StructMapType;
import de.unikassel.puma.openaccess.sword.renderer.xml.DivType.Fptr;
import de.unikassel.puma.openaccess.sword.renderer.xml.FileType.FLocat;
import de.unikassel.puma.openaccess.sword.renderer.xml.MdSecType.MdWrap;
import de.unikassel.puma.openaccess.sword.renderer.xml.MdSecType.MdWrap.XmlData;
import de.unikassel.puma.openaccess.sword.renderer.xml.MetsType.FileSec;
import de.unikassel.puma.openaccess.sword.renderer.xml.MetsType.MetsHdr;
import de.unikassel.puma.openaccess.sword.renderer.xml.MetsType.FileSec.FileGrp;
import de.unikassel.puma.openaccess.sword.renderer.xml.MetsType.MetsHdr.Agent;


/**
 * Generates METS-XML-Files for publication depositing 
 * Supported types: METS/EPDCX
 *
 * METS/MODS could possibly be generated with XSLT 
 * 
 * @author:  sven
 * @version: $Id: MetsBibTexMLGenerator.java,v 1.16 2011-06-09 12:13:01 rja Exp $
 * $Author: rja $
 * 
 */
public class MetsBibTexMLGenerator {
	private static final Log log = LogFactory.getLog(MetsBibTexMLGenerator.class);

	/*
	 * FIXME: Check if this class should be thread-safe. If so, don't use 
	 * object attributes to store data.
	 * 
	 */
	private PumaData<BibTex> _post = null;
	private ArrayList<String> _filenameList = null; 
	private User _user = null; 

	private final PumaRenderer xmlRenderer;

	/**
	 * @return the _user
	 */
	public User getUser() {
		return _user;
	}

	/**
	 * @param user the _user to set
	 */
	public void setUser(User user) {
		_user = user;
	}

	// contains special characters, symbols, etc...
	private static Properties chars = new Properties();




	public MetsBibTexMLGenerator(final UrlRenderer urlRenderer) {
		this.xmlRenderer = new PumaRenderer(urlRenderer);
		this._post = new PumaData<BibTex>();
	}

	public String getFilename(int elementnumber) {
		if (_filenameList.size() > elementnumber) {
			return _filenameList.get(elementnumber);
		} else {
			return null;
		}
	}


	public void setFilenameList(ArrayList<String> filenameList) {
		_filenameList = filenameList;
	}


	/**
	 * Fills url and title of bookmark.
	 * 
	 * @param url
	 * @return
	 */

	public void setMetadata(PumaData<BibTex> pumaData) {
		_post.getPost().setDescription(pumaData.getPost().getDescription());
		_post.getPost().setContentId(pumaData.getPost().getContentId());
		_post.getPost().setDate(pumaData.getPost().getDate());
		_post.getPost().setGroups(pumaData.getPost().getGroups());
		_post.getPost().setRanking(pumaData.getPost().getRanking());
		_post.getPost().setResource(pumaData.getPost().getResource());
		_post.getPost().setTags(pumaData.getPost().getTags());
		_post.getPost().setUser(pumaData.getPost().getUser());

		_post.setClassification(pumaData.getClassification());

		_post.setAuthor(pumaData.getAuthor());
		_post.setExaminstitution(pumaData.getExaminstitution());
		_post.setAdditionaltitle(pumaData.getAdditionaltitle());
		_post.setExamreferee(pumaData.getExamreferee());
		_post.setPhdoralexam(pumaData.getPhdoralexam());
		_post.setSponsors(pumaData.getSponsors());
		_post.setAdditionaltitle(pumaData.getAdditionaltitle());	

	}



	private class PumaRenderer extends JAXBRenderer {

		public PumaRenderer(final UrlRenderer urlRenderer) {
			super(urlRenderer);
		}

		protected PumaPost createPumaPost(final PumaData<? extends Resource> pumaData, User userData)	throws InternServerException {

			final PumaPost myPost = new PumaPost();

			fillXmlPost(myPost, pumaData.getPost());


			/*
			 * delete unwanted data from post
			 */

			/*
			 * remove from post
			 */

			/*
			 *  remove all system tags. they should not be sent to repository
			 */

			Iterator<TagType> tagIterator = myPost.getTag().iterator();
			while (tagIterator.hasNext()) {
				TagType tag = tagIterator.next();

				if (SystemTagsUtil.isSystemTag(tag.getName())) {
					tagIterator.remove();
				}
			}


			/*
			 * remove from bibtex 
			 */

			/*
			 * remove url. there is no need for this url to be present in repository
			 */
			final BibtexType bibtex = myPost.getBibtex();
			bibtex.setUrl(null);

			myPost.setBibtex(bibtex);



			/*
			 * add more user informations
			 */
			if (null != userData) {
				if (null == myPost.getUser()) myPost.setUser(new PumaUserType());
				myPost.getUser().setName(userData.getName());
				myPost.getUser().setRealname(userData.getRealname());
				myPost.getUser().setEmail(userData.getEmail());
				myPost.getUser().setId(userData.getLdapId());
			}

			/*
			 * add additional metadata
			 */
			final Resource resource = pumaData.getPost().getResource();
			if (resource instanceof BibTex) {
				final BibTex bibtexResource = (BibTex) resource;
				bibtexResource.parseMiscField();
				if (null != myPost.getBibtex()) {
					if (null != bibtexResource.getMiscField("isbn")) 		myPost.setISBN(bibtexResource.getMiscField("isbn")); 
					if (null != bibtexResource.getMiscField("issn"))		myPost.setISSN(bibtexResource.getMiscField("issn")); 
					if (null != bibtexResource.getMiscField("doi")) 		myPost.setDOI(bibtexResource.getMiscField("doi")); 
					if (null != bibtexResource.getMiscField("location"))	myPost.setLocation(bibtexResource.getMiscField("location")); 
					if (null != bibtexResource.getMiscField("dcc"))			myPost.setDCC(bibtexResource.getMiscField("dcc")); 
				}


				if (null != pumaData.getAuthor()) {
					for (String item : pumaData.getAuthor()) {
						myPost.getAuthor().add(item);
					}
				}

				if (null != pumaData.getExaminstitution()) {
					myPost.setExaminstitution(pumaData.getExaminstitution());
				}

				if (null != pumaData.getExamreferee()) {
					for (String item : pumaData.getExamreferee()) {
						myPost.getExamreferee().add(item);
					}
				}

				if (null != pumaData.getPhdoralexam()) {
					myPost.setPhdoralexam(pumaData.getPhdoralexam());
				}

				if (null != pumaData.getSponsors()) {
					for (String item : pumaData.getSponsors()) {
						myPost.getSponsors().add(item);
					}
				}

				if (null != pumaData.getAdditionaltitle()) {
					for (String item : pumaData.getAdditionaltitle()) {
						myPost.getAdditionaltitle().add(item);
					}
				}

				if (null != pumaData.getClassification()) {					
					for (Entry<String, List<String>> entry : pumaData.getClassification().entrySet()) {
						for (String listValue : entry.getValue() ) {
							PumaPost.Classification pptClassification = new PumaPost.Classification();
							pptClassification.setName(entry.getKey().toLowerCase(Locale.getDefault()).replaceAll("/ /",""));
							pptClassification.setValue(listValue);
							myPost.getClassification().add(pptClassification);
						}
					}
				}

				/*
				 * add publisher info / romeo sherpa 
				 */

				// publisher oder journal
				// get info from romeo/sherpa
				//myPost.setPublisherinfo("");


			}
			return myPost;
		}

		@Override
		protected JAXBContext getJAXBContext() throws JAXBException {
			return JAXBContext.newInstance("org.bibsonomy.rest.renderer.xml:de.unikassel.puma.openaccess.sword.renderer.xml", this.getClass().getClassLoader());
		}

		/**
		 * Initializes java xml bindings, builds the document and then marshalls
		 * it to the writer.
		 * 
		 * @throws InternServerException
		 *             if the document can't be marshalled
		 */
		private void serializeMets(final Writer writer, final Mets mets) throws InternServerException {
			try {
				// initialize context for java xml bindings
				final JAXBContext jc = this.getJAXBContext();

				// buildup document model
				final JAXBElement<Mets> webserviceElement = new JAXBElement<Mets>(new QName("http://www.loc.gov/METS/", "mets"), Mets.class, null, mets);

				// create a marshaller
				final Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd");


				//xsi:schemaLocation="http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd";>

				/*
				 * configure namespace
				 */
				final NamespacePrefixMapper npmapper = new NamespacePrefixMapper() {

					private final String[] namespace_decls = new String[] {
							"mets", "http://www.loc.gov/METS/",
							"bib", "http://www.bibsonomy.org/2010/11/BibSonomy",
							"puma", "http://puma.uni-kassel.de/2010/11/PUMA-SWORD",
							"xsi", "http://www.w3.org/2001/XMLSchema-instance",
							"xlink", "http://www.w3.org/1999/xlink"
					};


					@Override
					public String getPreferredPrefix(String arg0, String arg1, boolean arg2) {

						//return "mets";


						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public String[] getContextualNamespaceDecls() {
						return namespace_decls;
					}

					@Override
					public String[] getPreDeclaredNamespaceUris2() {

						//return new String[] { "mets", "http://www.loc.gov/METS/" };
						return namespace_decls;

						//return null;
					}

				};
				marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", npmapper);

				if (this.validateXMLOutput) {
					// validate the XML produced by the marshaller
					marshaller.setSchema(schema);
				}

				// marshal to the writer
				marshaller.marshal(webserviceElement, writer);
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

	}

	public String generateMets() {
		/*
		 * Helfer
		 */

		final StringWriter sw = new StringWriter();
		final ObjectFactory objectFactory = new ObjectFactory();


		/*
		 * METS
		 */
		final Mets mets = objectFactory.createMets();
		mets.setID("sort-mets_mets");
		mets.setOBJID("sword-mets");
		mets.setLABEL("DSpace SWORD Item");
		mets.setPROFILE("DSpace METS SIP Profile 1.0");


		//mets xmlns=\"http://www.loc.gov/METS/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd\">\n";

		/*
		 * METS Hdr
		 */

		final MetsHdr metsHdr = objectFactory.createMetsTypeMetsHdr();


		GregorianCalendar c = new GregorianCalendar();
		XMLGregorianCalendar currentDate;
		try {
			currentDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			metsHdr.setCREATEDATE(currentDate);
		} catch (DatatypeConfigurationException e) {
			log.warn("DatatypeConfigurationException");
		}

		mets.setMetsHdr(metsHdr);

		final List<Agent> metsHdrAgentList = metsHdr.getAgent();
		final Agent metsHdrAgent = new Agent();
		metsHdrAgent.setROLE("CUSTODIAN");
		metsHdrAgent.setTYPE("ORGANIZATION");
		metsHdrAgent.setName("PUMA");

		metsHdrAgentList.add(metsHdrAgent);

		final List<MdSecType> dmdSec = mets.getDmdSec();

		final MdSecType mdSec = objectFactory.createMdSecType();
		mdSec.setID("sword-mets-dmd-1");
		mdSec.setGROUPID("sword-mets-dmd-1_group-1");
		dmdSec.add(mdSec);

		final MdWrap mdWrap = objectFactory.createMdSecTypeMdWrap();
		mdWrap.setMIMETYPE("text/xml");
		mdWrap.setMDTYPE("OTHER");
		mdWrap.setOTHERMDTYPE("BIBTEXML");
		mdSec.setMdWrap(mdWrap);

		final XmlData xmlData = objectFactory.createMdSecTypeMdWrapXmlData();
		mdWrap.setXmlData(xmlData);


		/*
		 * METS FileSec
		 */

		final FileSec metsFileSec = objectFactory.createMetsTypeFileSec();
		mets.setFileSec(metsFileSec);

		final FileGrp metsFileSecFileGrp = objectFactory.createMetsTypeFileSecFileGrp();
		List<FileType> fileItemList = new ArrayList<FileType>(); 

		metsFileSecFileGrp.setID("sword-mets-fgrp-1");
		metsFileSecFileGrp.setUSE("CONTENT");
		Integer filenumber = 0;
		for(Document doc : _post.getPost().getResource().getDocuments()) {
			FileType fileItem = new FileType();
			//			fileItem.setGROUPID("sword-mets-fgid-0");
			fileItem.setID("sword-mets-file-".concat(filenumber.toString()));
			// TODO: if file is not pdf, set MIMEtype to something like binary data
			fileItem.setMIMETYPE("application/pdf");

			FLocat fileLocat = new FLocat();
			fileLocat.setLOCTYPE("URL");
			fileLocat.setHref(doc.getFileName());
			fileItem.getFLocat().add(fileLocat);

			// add fileitem to filepointerlist for struct section
			fileItemList.add(fileItem);

			metsFileSecFileGrp.getFile().add(fileItem);
			filenumber++;
		}		


		metsFileSec.getFileGrp().add(metsFileSecFileGrp);

		/*
		 * METS structMap
		 */

		final StructMapType structMap = new StructMapType();

		structMap.setID("sword-mets-struct-1");
		structMap.setLABEL("structure");
		structMap.setTYPE("LOGICAL");

		final DivType div1 = new DivType();
		div1.setID("sword-mets-div-1");
		div1.getDMDID().add(mdSec);   // TODO check if msSec is correct, or this must be a string?
		div1.setTYPE("SWORD Object");

		final DivType div2 = new DivType();
		div2.setID("sword-mets-div-2");
		div2.setTYPE("File");

		for(FileType fItem : fileItemList) {
			Fptr fptr = new Fptr();
			fptr.setFILEID(fItem);

			div2.getFptr().add(fptr);
		}		
		//xmlDocument += "<div ID=\"sword-mets-div-1\" DMDID=\"sword-mets-dmd-1\" TYPE=\"SWORD Object\">\n";
		// fptr
		//xmlDocument += "<fptr FILEID=\"sword-mets-file-1\"/>\n";

		div1.getDiv().add(div2);

		structMap.setDiv(div1);

		mets.getStructMap().add(structMap);


		/*
		 * unser Post
		 */
		final PumaPost pumaPost = xmlRenderer.createPumaPost(_post, _user);


		//derPost.set
		//"puma", "http://puma.uni-kassel.de/2010/11/PUMA-SWORD"

		xmlData.getAny().add(pumaPost);

		xmlRenderer.serializeMets(sw, mets);

		// DEBUG
		log.debug(sw.toString());

		return XmlUtils.removeXmlControlCharacters(sw.toString());

	}	

}
