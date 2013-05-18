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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.exceptions.ResourceMovedException;
import org.bibsonomy.common.exceptions.ResourceNotFoundException;
import org.bibsonomy.common.exceptions.SwordException;
import org.bibsonomy.database.DBLogicApiInterfaceFactory;
import org.bibsonomy.database.util.IbatisDBSessionFactory;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Document;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.rest.renderer.UrlRenderer;
import org.bibsonomy.util.HashUtils;
import org.purl.sword.base.DepositResponse;
import org.purl.sword.base.ServiceDocument;
import org.purl.sword.client.Client;
import org.purl.sword.client.PostMessage;
import org.purl.sword.client.SWORDClientException;
import org.springframework.beans.factory.annotation.Required;

/**
 * Sword main
 * 
 * @author:  sven
 * @version: 
 * 
 */
public class SwordService {
	private static final Log log = LogFactory.getLog(SwordService.class);
	
	private static final String SWORDFILETYPE = "application/zip";
	private static final String SWORDFORMAT = "http://purl.org/net/sword-types/METSDSpaceSIP"; 
	
	public static final String AF_ADDITIONALTITLE = "post.resource.openaccess.additionalfields.additionaltitle";
	public static final String AF_SPONSOR = "post.resource.openaccess.additionalfields.sponsor";
	public static final String AF_PHDORALEXAM = "post.resource.openaccess.additionalfields.phdoralexam";
	public static final String AF_PHDREFEREE2 = "post.resource.openaccess.additionalfields.phdreferee2";
	public static final String AF_PHDREFEREE = "post.resource.openaccess.additionalfields.phdreferee";
	public static final String AF_INSTITUTION = "post.resource.openaccess.additionalfields.institution";

	public static final String[] AF_FIELD_NAMES = new String[] {
		AF_INSTITUTION,
		AF_PHDREFEREE,
		AF_PHDREFEREE2,
		AF_PHDORALEXAM,
		AF_SPONSOR,
		AF_ADDITIONALTITLE
	};
	
	private SwordConfig repositoryConfig;
	
	private String projectDocumentPath;
	private UrlRenderer urlRenderer;

	/**
	 * retrieve service document from sword server
	 * @return
	 */
	private ServiceDocument retrieveServicedocument(){
		ServiceDocument serviceDocument = null;
		// get an instance of SWORD-Client
		Client swordClient = new Client();
		swordClient.setServer(repositoryConfig.getHttpServer(), repositoryConfig.getHttpPort());
		swordClient.setUserAgent(repositoryConfig.getHttpUserAgent());
		swordClient.setCredentials(repositoryConfig.getAuthUsername(), repositoryConfig.getAuthPassword());
		try {
			serviceDocument = swordClient.getServiceDocument(repositoryConfig.getHttpServicedocumentUrl());
		} catch (SWORDClientException e) {
			log.info("SWORDClientException! getServiceDocument" + e.getMessage());
		}
		return serviceDocument;
	}
	
	/**
	 * Check if servicedocument is available and repository contains configured deposit collection   
	 * @param doc Servicedocument
	 * @param url deposit url
	 * @param accept "application/zip"
	 * @param acceptPackaging "http://purl.org/net/sword-types/METSDSpaceSIP"
	 * @return
	 */
	private boolean checkServicedokument(ServiceDocument doc, String url, String accept, String acceptPackaging) {
		// TODO: check service document
		return true;
	}
	
	
	/**
	 * collects all informations to send Documents with metadata to repository 
	 * @throws SwordException 
	 */
	public void submitDocument(PumaData<?> pumaData, User user) throws SwordException {
		log.info("starting sword");
		DepositResponse depositResponse = new DepositResponse(999); 
		File swordZipFile = null;

		Post<?> post = pumaData.getPost(); 
		
		// -------------------------------------------------------------------------------
		/*
		 * retrieve ZIP-FILE
		 */
		if (post.getResource() instanceof BibTex) {
					
			// fileprefix
			String fileID = HashUtils.getMD5Hash(user.getName().getBytes()) + "_"+post.getResource().getIntraHash();
					
			// Destination directory 
			File destinationDirectory = new File(repositoryConfig.getDirTemp()+"/"+fileID);

			// zip-filename
			swordZipFile = new File(destinationDirectory.getAbsoluteFile()+"/"+fileID+".zip");

			byte[] buffer = new byte[18024];
					
			log.info("getIntraHash = " + post.getResource().getIntraHash());

			/*
			 * get documents
			 */
			
			// At the moment, there are no Documents delivered by method parameter post.
			// retrieve list of documents from database - workaround
			
			// get documents for post and insert documents into post 
			((BibTex) post.getResource()).setDocuments(retrieveDocumentsFromDatabase(user, post.getResource().getIntraHash()));
			
			if (((BibTex) post.getResource()).getDocuments().isEmpty()) { 
				// Wenn kein PDF da, dann Fehlermeldung ausgeben!!
				log.info("throw SwordException: noPDFattached");
				throw new SwordException("error.sword.noPDFattached");
			}
					
			try {
				// create directory
				boolean mkdir_success = (new File(destinationDirectory.getAbsolutePath())).mkdir();
				if (mkdir_success) {
					log.info("Directory: " + destinationDirectory.getAbsolutePath() + " created");
				}    
				
						
				// open zip archive to add files to
				log.info("zipFilename: "+swordZipFile);
				ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(swordZipFile));
				
				ArrayList<String> fileList = new ArrayList<String>();
				
				for (final Document document : ((BibTex) post.getResource()).getDocuments()) {

					//getpostdetails
					// get file and store it in hard coded folder "/tmp/"
					//final Document document2 = logic.getDocument(user.getName(), post.getResource().getIntraHash(), document.getFileName());
					
					// move file to user folder with username_resource-hash as folder name
					
					// File (or directory) to be copied 
					//File fileToZip = new File(document.getFileHash());
					
					fileList.add(document.getFileName());
							
			
					
					// Move file to new directory 
					//boolean rename_success = fileToCopy.renameTo(new File(destinationDirectory, fileToMove.getName()));
					/*
					if (!rename_success) { 
						// File was not successfully moved } 
						log.info("File was not successfully moved: "+fileToMove.getName());
					}
					*/
					ZipEntry zipEntry = new ZipEntry(document.getFileName());
		
								
					// Set the compression ratio
					zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
		
					
					String inputFilePath = projectDocumentPath+document.getFileHash().substring(0, 2)+"/"+document.getFileHash();
					FileInputStream in = new FileInputStream(inputFilePath);
		
					// Add ZIP entry to output stream.
					zipOutputStream.putNextEntry(zipEntry);
					
					// Transfer bytes from the current file to the ZIP file
					//out.write(buffer, 0, in.read(buffer));
		
					int len;
					while ((len = in.read(buffer)) > 0)
					{
						zipOutputStream.write(buffer, 0, len);
					}
					
					zipOutputStream.closeEntry();
									
					// Close the current file input stream
					in.close();			
				}

				// write meta data into zip archive
				ZipEntry zipEntry = new ZipEntry("mets.xml");
				zipOutputStream.putNextEntry(zipEntry);				

				// create XML-Document
				// PrintWriter from a Servlet
						
				MetsBibTexMLGenerator metsBibTexMLGenerator = new MetsBibTexMLGenerator(urlRenderer);
				metsBibTexMLGenerator.setUser(user);
				metsBibTexMLGenerator.setFilenameList(fileList);
				//metsGenerator.setMetadata(metadataMap);
				metsBibTexMLGenerator.setMetadata((PumaData<BibTex>) pumaData);

//				PumaPost additionalMetadata = new PumaPost();
//				additionalMetadata.setExaminstitution(null);
//				additionalMetadata.setAdditionaltitle(null);
//				additionalMetadata.setExamreferee(null);
//				additionalMetadata.setPhdoralexam(null);
//				additionalMetadata.setSponsors(null);
//				additionalMetadata.setAdditionaltitle(null);	
				
//				metsBibTexMLGenerator.setMetadata((Post<BibTex>) post);

				//StreamResult streamResult = new StreamResult(zipOutputStream);
						
				zipOutputStream.write(metsBibTexMLGenerator.generateMets().getBytes("UTF-8"));
				
				zipOutputStream.closeEntry();
							
				// close zip archive  
				zipOutputStream.close();
										
				log.debug("saved to "+swordZipFile.getPath());
						
			} catch (MalformedURLException e) {
				// e.printStackTrace();
				log.info("MalformedURLException! " + e.getMessage());
			} catch (IOException e) {
				//e.printStackTrace();
				log.info("IOException! " + e.getMessage());
				
			} catch (ResourceNotFoundException e) {
				// e.printStackTrace();
				log.warn("ResourceNotFoundException! SwordService-retrievePost");
			}
		}
		/*
		 * end of retrieve ZIP-FILE
		 */
		//---------------------------------------------------
		
		/*
		 * do the SWORD stuff
		 */

		if (null != swordZipFile) {

			// get an instance of SWORD-Client
			Client swordClient = new Client();

			PostMessage swordMessage = new PostMessage();

			// create sword post message

			// message file
			// create directory in temp-folder
			// store post documents there
			// store meta data there in format http://purl.org/net/sword-types/METSDSpaceSIP
			// delete post document files and meta data file

			// add files to zip archive
			// -- send zip archive
			// -- delete zip archive

			
			swordClient.setServer(repositoryConfig.getHttpServer(), repositoryConfig.getHttpPort());
			swordClient.setUserAgent(repositoryConfig.getHttpUserAgent());
			swordClient.setCredentials(repositoryConfig.getAuthUsername(), repositoryConfig.getAuthPassword());

			// message meta
			swordMessage.setNoOp(false);
			swordMessage.setUserAgent(repositoryConfig.getHttpUserAgent());
			swordMessage.setFilepath(swordZipFile.getAbsolutePath());
			swordMessage.setFiletype("application/zip");
			swordMessage.setFormatNamespace("http://purl.org/net/sword-types/METSDSpaceSIP"); // sets packaging!
			swordMessage.setVerbose(false);


			try {
				// check depositurl against service document
				if (checkServicedokument(retrieveServicedocument(), repositoryConfig.getHttpServicedocumentUrl(), SWORDFILETYPE, SWORDFORMAT)) {
					// transmit sword message (zip file with document metadata and document files
					swordMessage.setDestination(repositoryConfig.getHttpDepositUrl());
	
					depositResponse = swordClient.postFile(swordMessage);
					
					/*
					 * 200 OK Used in response to successful GET operations and
					 * to Media Resource Creation operations where X-No-Op is
					 * set to true and the server supports this header.
					 * 
					 * 201 Created
					 * 
					 * 202 Accepted - One of these MUST be used to indicate that
					 * a deposit was successful. 202 Accepted is used when
					 * processing of the data is not yet complete.
					 * 
					 * 
					 * 400 Bad Request - used to indicate that there is some
					 * problem with the request where there is no more
					 * appropriate 4xx code.
					 * 
					 * 401 Unauthorized - In addition to the usage described in
					 * HTTP, servers that support mediated deposit SHOULD use
					 * this status code when the server does not understand the
					 * value given in the X-Behalf-Of header. In this case a
					 * human-readable explanation MUST be provided.
					 * 
					 * 403 Forbidden - indicates that there was a problem making
					 * the deposit, it may be that the depositor is not
					 * authorised to deposit on behalf of the target owner, or
					 * the target owner does not have permission to deposit into
					 * the specified collection.
					 * 
					 * 412 Precondition failed - MUST be returned by server
					 * implementations if a calculated checksum does not match a
					 * value provided by the client in the Content-MD5 header.
					 * 
					 * 415 Unsupported Media Type - MUST be used to indicate
					 * that the format supplied in either a Content-Type header
					 * or in an X-Packaging header or the combination of the two
					 * is not accepted by the server.
					 */
					
					log.info("throw SwordException: errcode"+depositResponse.getHttpResponse());
					throw new SwordException("error.sword.errcode"+depositResponse.getHttpResponse());

				}

			} catch (SWORDClientException e) {
				log.warn("SWORDClientException: " + e.getMessage() + "\n" + e.getCause() + " / " + swordMessage.getDestination());
				throw new SwordException("error.sword.urlnotaccessable");
			}

		}

	}

	
	/*
	 * Workaround method to retrieve 
	 */
	private List<Document> retrieveDocumentsFromDatabase(User user, String resourceHash) {

		/*
		 * getting DB access
		 */
		final String username = user.getName();

		log.info("getting database access for user " + username);
		final LogicInterface logic;

		final DBLogicApiInterfaceFactory factory = new DBLogicApiInterfaceFactory();
		factory.setDbSessionFactory(new IbatisDBSessionFactory());

		logic = factory.getLogicAccess(username, user.getApiKey());
		
		// get meta data for post
		try {
			final Post<? extends Resource> post = logic.getPostDetails(resourceHash, username); 
			if (post.getResource() instanceof BibTex) {
				
				// get documents for post
				return ((BibTex) post.getResource()).getDocuments();
			}			
		} catch (ResourceNotFoundException e) {
			// e.printStackTrace();
			log.warn("ResourceNotFoundException! SwordService-retrieveDocumentsFromDatabase");
		} catch (ResourceMovedException e) {
			log.warn("ResourceMovedException! SwordService-retrieveDocumentsFromDatabase");
		}
		return null;
	}


	/**
	 * Configuration of Sword-Server (Repository) 
	 * 
	 * @param repositoryConfig the repositoryConfig to set
	 */
	public void setRepositoryConfig(SwordConfig repositoryConfig) {
		this.repositoryConfig = repositoryConfig;
	}


	
	/**
	 * The path to the documents.
	 * 
	 * @param projectDocumentPath
	 */
	@Required
	public void setProjectDocumentPath(String projectDocumentPath) {
		this.projectDocumentPath = projectDocumentPath;
	}

	/**
	 * The URL renderer to create the proper API XML.
	 * @param urlRenderer
	 */
	@Required
	public void setUrlRenderer(UrlRenderer urlRenderer) {
		this.urlRenderer = urlRenderer;
	}

	
}



