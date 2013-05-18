/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.managers;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.List;

import org.bibsonomy.database.common.AbstractDatabaseManager;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.params.DocumentParam;
import org.bibsonomy.model.Document;

/**
 * @author Christian Kramer
 * @version $Id: DocumentDatabaseManager.java,v 1.10 2009-07-15 08:26:19
 *          voigtmannc Exp $
 */
public class DocumentDatabaseManager extends AbstractDatabaseManager {
	/**
	 * Documents not attached to posts get this value as content_id.
	 */
	public static final int DEFAULT_CONTENT_ID = 0;
	
	private static final DocumentDatabaseManager singleton = new DocumentDatabaseManager();
	
	/**
	 * @return DocumentDatabaseManager
	 */
	public static DocumentDatabaseManager getInstance() {
		return singleton;
	}

	private DocumentDatabaseManager() {}

	/**
	 * Checks, if the post has already a document with that name attached.
	 * 
	 * One post might have several documents attached. The documents are
	 * identified by their file name. Only one document per filename/post is
	 * allowed
	 * 
	 * @param userName
	 * @param resourceHash
	 * @param fileName
	 * @param session
	 * @return <code>true</code> if a document is attached to that hash,
	 *         <code>false</code> otherwise
	 */
	public boolean checkForExistingDocuments(final String userName, final String resourceHash, final String fileName, final DBSession session) {
		if (!present(resourceHash)) {
			return false;
		}

		final DocumentParam docParam = new DocumentParam();
		docParam.setUserName(userName);
		docParam.setResourceHash(resourceHash);
		docParam.setFileName(fileName);

		/*
		 * if a post with that filename attached exists, we return true
		 */
		return this.queryForObject("getDocumentForPost", docParam, Document.class, session) != null;
	}

	/**
	 * Inserts a new document to the db
	 * 
	 * @param userName
	 * @param contentId
	 * @param fileHash
	 * @param fileName
	 * @param md5hash
	 * @param session
	 */
	public void addDocument(final String userName, final int contentId, final String fileHash, final String fileName, final String md5hash, final DBSession session) {
		final DocumentParam docParam = new DocumentParam();
		docParam.setUserName(userName);
		docParam.setFileHash(fileHash);
		docParam.setFileName(fileName);
		docParam.setContentId(contentId);
		docParam.setMd5hash(md5hash);
		
		this.insert("insertDoc", docParam, session);
	}

	/**
	 * Updates an existing document with the new hash and filename
	 * 
	 * @param contentId
	 * @param fileHash
	 * @param fileName
	 * @param md5hash
	 * @param session
	 */
	public void updateDocument(final int contentId, final String fileHash, final String fileName, final String md5hash, final DBSession session) {
		final DocumentParam docParam = new DocumentParam();
		docParam.setFileHash(fileHash);
		docParam.setFileName(fileName);
		docParam.setContentId(contentId);
		docParam.setMd5hash(md5hash);
		
		this.update("updateDoc", docParam, session);
	}

	/**
	 * retrieves a (layout) document
	 * 
	 * @param docParam
	 * @param session
	 * @return document
	 */
	private Document getDocumentForLayout(final DocumentParam docParam, final DBSession session) {
		return this.queryForObject("getDocumentForLayout", docParam, Document.class, session);
	}

	/**
	 * retrieves a (layout) document
	 * 
	 * @param userName
	 * @param fileHash
	 * @param session
	 * @return document
	 */
	public Document getDocument(final String userName, final String fileHash, DBSession session) {
		// create the docParam object
		final DocumentParam docParam = new DocumentParam();

		// fill the docParam object
		docParam.setFileHash(fileHash);
		docParam.setUserName(userName);
		docParam.setContentId(0);

		// get the requested document
		return this.getDocumentForLayout(docParam, session);
	}

	/**
	 * This method gets documents object with the given name and hash.
	 * 
	 * @param docParam
	 * @param session
	 * @return document
	 */
	private List<Document> getDocumentsForPost(final DocumentParam docParam, final DBSession session) {
		return this.queryForList("getDocumentsForPost", docParam, Document.class, session);
	}

	/**
	 * Returns the named documents for the given user name and hash
	 * 
	 * @param userName
	 * @param resourceHash
	 * @param session
	 * @return a list of documents
	 */
	public List<Document> getDocumentsForPost(final String userName, final String resourceHash, final DBSession session) {
		// create the docParam object
		final DocumentParam docParam = new DocumentParam();

		// fill the docParam object
		docParam.setResourceHash(resourceHash);
		docParam.setUserName(userName);

		// get the requested document
		final List<Document> doc = getDocumentsForPost(docParam, session);
		
		if (doc == null) {
			throw new IllegalStateException("No documents for this BibTeX entry");
		}
		
		return doc;
	}

	/**
	 * Returns the named document for the given user name and hash.
	 * 
	 * @param userName
	 * @param resourceHash
	 * @param session
	 * @return A document.
	 */
	public List<Document> getDocuments(final String userName, final String resourceHash, final DBSession session) {
		// create the docParam object
		final DocumentParam docParam = new DocumentParam();

		// fill the docParam object
		docParam.setResourceHash(resourceHash);
		docParam.setUserName(userName);

		// get the requested document
		final List<Document> doc = getDocumentsForPost(docParam, session);
		if (doc == null) {
			throw new IllegalStateException("No documents for this BibTeX entry");
		}
		return doc;
	}

	private void deleteDocumentLayout(final DocumentParam docParam, final DBSession session) {
		this.delete("deleteDocWithNoPost", docParam, session);
	}

	/**
	 * deletes a document which is not connected to a post
	 * 
	 * @param contentId
	 * @param userName
	 * @param fileHash
	 * @param session
	 */
	public void deleteDocumentWithNoPost(final int contentId, final String userName, final String fileHash, final DBSession session) {
		// create a DocumentParam object
		final DocumentParam docParam = new DocumentParam();
		docParam.setFileHash(fileHash);
		docParam.setUserName(userName);
		docParam.setContentId(contentId);
		// finally delete the document
		deleteDocumentLayout(docParam, session);
	}

	private void deleteDocumentForPost(final DocumentParam docParam, final DBSession session) {
		this.delete("deleteDoc", docParam, session);
	}

	/**
	 * This method deletes an existing document
	 * 
	 * @param contentId
	 * @param userName
	 * @param fileName
	 * @param session
	 */
	public void deleteDocument(final int contentId, final String userName, final String fileName, final DBSession session) {
		// create a DocumentParam object
		final DocumentParam docParam = new DocumentParam();
		docParam.setFileName(fileName);
		docParam.setUserName(userName);
		docParam.setContentId(contentId);
		// finally delete the document
		deleteDocumentForPost(docParam, session);
	}
}