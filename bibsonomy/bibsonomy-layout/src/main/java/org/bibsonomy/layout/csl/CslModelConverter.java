/**
 *
 *  BibSonomy-Layout - Layout engine for the webapp.
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

package org.bibsonomy.layout.csl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bibsonomy.layout.csl.model.Person;
import org.bibsonomy.layout.csl.model.Record;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.PersonName;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;

/**
 * TENTATIVE implementation of a mapping of our bibtex model
 * to the CSL model, which we transform to JSON later on.
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: CslModelConverter.java,v 1.2 2011-07-26 13:44:49 bibsonomy Exp $
 */
public class CslModelConverter  {
    
    // bibtex -> csl
    private static Map<String, String> typemap;
    
    static {
	// see http://xbiblio-devel.2463403.n2.nabble.com/Citeproc-json-data-input-specs-td5135372.html
	// FIXME: this is incomplete!
	typemap = new HashMap<String, String>();
	typemap.put("article", "article-journal");
	typemap.put("book", "book");
	typemap.put("booklet", "pamphlet");
	typemap.put("inbook", "chapter");
	typemap.put("incollection", "chapter");
	typemap.put("phdthesis", "thesis");
	typemap.put("mastersthesis", "thesis");
	typemap.put("report", "techreport");
	typemap.put("inproceedings", "chapter");
    }
    

    public static List<Post<? extends Resource>> convertEntries(List<Record> entries) {
	// todo
	return null;
    }
    
    public static Post<? extends Resource> convertEntry(Record entry) {
	// todo
	return null;
    }
    
    public static List<Record> convertPosts(List<Post<? extends Resource>> posts) {
	//todo
	return null;
    }
    
    /**
     * Convert a bibtex post into a CSL record.
     * 
     * @param post - the bibtex post
     * @return the corresponding CSL model
     */
    public static Record convertPost(Post<? extends Resource> post) {
	Record rec = new Record();
	BibTex bib = (BibTex) post.getResource();
	//id
	rec.setId(createId(post));
	//type
	rec.setType(mapToCslType(bib.getType()));
	// authors, editors
	for (PersonName author : bib.getAuthorList()) {
	    Person a = new Person();
	    a.setGiven(author.getFirstName());
	    a.setFamily(author.getLastName());
	    rec.getAuthor().add(a);
	}	
	for (PersonName author : bib.getEditorList()) {
	    Person a = new Person();
	    a.setGiven(author.getFirstName());
	    a.setFamily(author.getLastName());
	    rec.getEditor().add(a);
	}
	
	// fields a-z (FIXME: this is a rather tentative mapping!)
	rec.setAbstractt(bib.getAbstract());
	rec.setCitation_label(bib.getBibtexKey());
	rec.setCollection_title(bib.getBooktitle());
	rec.setContainer_title(bib.getBooktitle());
	rec.setDOI(bib.getMiscField("doi"));
	rec.setEvent_place(bib.getAddress());
	rec.setISBN(bib.getMiscField("isbn"));
	rec.setNote(bib.getNote());
	rec.setPage(bib.getPages());
	rec.setPublisher(bib.getPublisher());
	rec.setTitle(bib.getTitle());
	rec.setURL(bib.getUrl());
	rec.setVolume(bib.getVolume());	
	return rec;
    }
    
    /**
     * create ID for a post
     * @param post - the post
     * @return the ID
     */
    private static final String createId(final Post<? extends Resource> post) {
	return ((BibTex) post.getResource()).getIntraHash() + post.getUser().getName();
    }
    
    
    /**
     * get the mapping of bibtex types to CSL types.
     * @param bibtexType - the bibtex entrytype
     * @return the corresponding csl type
     */
    private static final String mapToCslType(final String bibtexType) {
	return typemap.get(bibtexType);
    }
    
}
