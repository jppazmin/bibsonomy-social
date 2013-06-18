/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.command;

import java.util.List;
import java.util.Map;

import org.bibsonomy.model.Document;


/**
 * @author mwa
 * @version $Id: BibtexResourceViewCommand.java,v 1.9 2011-06-24 12:33:39 sven Exp $
 */
public class BibtexResourceViewCommand extends TagResourceViewCommand {
	
	/** the intrahash of a publication **/
	private String requBibtex = "";
	
	/** the title of a publication **/
	private String title = "";
	
	/** the group whode resources are requested*/
	private ConceptsCommand concepts = new ConceptsCommand();
	
	private List<Document> documents;

	/**
	 * additional metadata for bibtex resource
	 * 
	 * additionalMetadataMap
	 *  {
	 *  	DDC=[010, 050, 420, 422, 334, 233], 
	 *  	post.resource.openaccess.additionalfields.additionaltitle=[FoB], 
	 *  	post.resource.openaccess.additionalfields.phdreferee2=[Petra Musterfrau], 
	 *  	post.resource.openaccess.additionalfields.phdreferee=[Peter Mustermann], 
	 *  	ACM=[C.2.2], 
	 *  	JEL=[K12], 
	 *  	post.resource.openaccess.additionalfields.sponsor=[DFG, etc..], 
	 *  	post.resource.openaccess.additionalfields.phdoralexam=[17.08.2020], 
	 *  	post.resource.openaccess.additionalfields.institution=[Uni KS tEST ]
	 *  }
	 */
	private Map<String, List<String>> additonalMetadata;

	/**
	 * @return the hash of a bibtex
	 */
	public String getRequBibtex(){
		return this.requBibtex;
	}
	
	/**
	 * @param requBibtex the requBibtex to set
	 */
	public void setRequBibtex(String requBibtex) {
		this.requBibtex = requBibtex;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the concepts
	 */
	public ConceptsCommand getConcepts() {
		return this.concepts;
	}

	/**
	 * @param concepts the concepts to set
	 */
	public void setConcepts(ConceptsCommand concepts) {
		this.concepts = concepts;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	/**
	 * @return the documents
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	/**
	 * @return the map of additonalMetadata
	 */
	public Map<String, List<String>> getAdditonalMetadata() {
		return this.additonalMetadata;
	}

	/**
	 * @param additonalMetadata the map of additonalMetadata to set
	 */
	public void setAdditonalMetadata(Map<String, List<String>> additonalMetadata) {
		this.additonalMetadata = additonalMetadata;
	}
}
