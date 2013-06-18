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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;

/**
 * Command class which is used by the MySearchController class.
 * This class stores all information such as relations between serval bibtex informations
 * which are needed by the mySearch.jspx side.
 * 
 * @author Christian Voigtmann
 * @version $Id: MySearchCommand.java,v 1.4 2010-05-11 12:47:58 nosebrain Exp $
 */
public class MySearchCommand extends SimpleResourceViewCommand{

	/**
	 * user object
	 */
	private LinkedList<String> tags;
	private LinkedList<String> authors;
	private LinkedList<String> titles;	
	private SortedSet[] tagTitle;
	private SortedSet[] authorTitle;
	private SortedSet[] tagAuthor;
	private SortedSet[] titleAuthor;
	private	String[]	bibtexHash;
	private String[]	bibtexUrls;
	private int    simHash;
	
	private String requGroup;

	/**
	 * default constructor
	 */
	public MySearchCommand() {
		
		tags = new LinkedList<String>();
		authors = new LinkedList<String>();
		titles = new LinkedList<String>();
	}

	/**
	 * 
	 * @return the simhash 
	 */
	public int getSimHash() {
		return this.simHash;
	}

	/**
	 * set the current simHash
	 * @param simHash
	 */
	public void setSimHash(int simHash) {
		this.simHash = simHash;
	}

	/**
	 * 
	 * @return list of tags
	 */
	public LinkedList<String> getTags() {
		return this.tags;
	}

	/**
	 * sets the tag list
	 * @param tags
	 */
	public void setTags(LinkedList<String> tags) {
		this.tags = tags;
	}

	/**
	 * 
	 * @return list of authors
	 */
	public LinkedList<String> getAuthors() {
		return this.authors;
	}

	/**
	 * sets the author list
	 * @param authors
	 */
	public void setAuthors(LinkedList<String> authors) {
		this.authors = authors;
	}

	/**
	 * 
	 * @return list of titles
	 */
	public LinkedList<String> getTitles() {
		return this.titles;
	}

	/**
	 * set list of titles
	 * @param titles
	 */
	public void setTitles(LinkedList<String> titles) {
		this.titles = titles;
	}

	/**
	 * 
	 * @return relations between tag and title as a string
	 */
	public String getTagTitle() {
		return getArrayToString(this.tagTitle);
	}

	/**
	 * 
	 * @param tagTitle
	 */
	public void setTagTitle(SortedSet[] tagTitle) {
		this.tagTitle = tagTitle;
	}


	/**
	 * 
	 * @return relations between author and title as a string
	 */
	public String getAuthorTitle() {
		return getArrayToString(this.authorTitle);
	}

	/**
	 * 
	 * @param authorTitle
	 */
	public void setAuthorTitle(SortedSet[] authorTitle) {
		this.authorTitle = authorTitle;
	}


	/**
	 * 
	 * @return relations between tag and author as a string
	 */
	public String getTagAuthor() {
		return getArrayToString(this.tagAuthor);
	}

	/**
	 * 
	 * @param tagAuthor
	 */
	public void setTagAuthor(SortedSet[] tagAuthor) {
		this.tagAuthor = tagAuthor;
	}


	/**
	 * 
	 * @return relations between title and author as a string
	 */
	public String getTitleAuthor() {
		return getArrayToString(this.titleAuthor);
	}

	/**
	 * 
	 * @param titleAuthor
	 */
	public void setTitleAuthor(SortedSet[] titleAuthor) {
		this.titleAuthor = titleAuthor;
	}

	/**
	 * 
	 * @return string array of all bitex hashes
	 */
	public String[] getBibtexHash() {
		return this.bibtexHash;
	}

	/**
	 * 
	 * @param bibtexHash
	 */
	public void setBibtexHash(String[] bibtexHash) {
		this.bibtexHash = bibtexHash;
	}

	/**
	 * 
	 * @return string array with all bibtex urls
	 */
	public String[] getBibtexUrls() {
		return this.bibtexUrls;
	}
		
	/**
	 * 
	 * @param bibtexUrls
	 */
	public void setBibtexUrls(String[] bibtexUrls) {
		this.bibtexUrls = bibtexUrls;
	}	
		
	/**
	 * generates a string from given set in javascript array syntax
	 * @param list the set
	 * @return a string of the elements
	 */
	private String getArrayToString(SortedSet[] list) {
		StringBuilder buf = new StringBuilder();
		
		buf.append("[");
		if (list != null) {
			for (int i=0; i<list.length; i++) {
				buf.append("[");

				Iterator iter = list[i].iterator();			
				while(iter.hasNext()) {			
					buf.append(iter.next());				
					if(iter.hasNext())
						buf.append(",");				
				}

				buf.append("]");
				if (i != (list.length -1))
					buf.append(",");
			}
		}
		
		buf.append("]");		
		
		return buf.toString();
	}

	/**
	 * 
	 * @return the requested group
	 */
	public String getRequGroup() {
		return this.requGroup;
	}

	/**
	 * sets the requested group
	 * @param requGroup
	 */
	public void setRequGroup(String requGroup) {
		this.requGroup = requGroup;
	}	
}
