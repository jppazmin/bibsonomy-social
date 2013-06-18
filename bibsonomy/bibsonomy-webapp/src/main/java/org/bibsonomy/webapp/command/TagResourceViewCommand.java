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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bibsonomy.model.Tag;

/**
 * Bean for Tag Sites
 * 
 * @author Michael Wagner
 * @version $Id: TagResourceViewCommand.java,v 1.8 2010-06-22 17:21:39 akn Exp $
 */
public class TagResourceViewCommand extends SimpleResourceViewCommand{
	
	/** tags to search for */
	private String requestedTags = "";
	
	/** tags to search for, as list */
	private List<String> requestedTagsList = null;
	
	/** the specified order */
	private String order = "added";
		
	/** bean for related tags */
	private RelatedTagCommand relatedTagCommand = new RelatedTagCommand();
	
	/** re-using relatedTagCommand to store similar tags */
	private RelatedTagCommand similarTags = new RelatedTagCommand();
	
	/** related users - needed for FolkRank */
	private RelatedUserCommand relatedUserCommand = new RelatedUserCommand();

	private int postCountForTagsForLoginUser = 0;
	private int postCountForTagsForRequestedUser = 0;
	private int postCountForTagsForGroup = 0;
	private int postCountForTagsForAll = 0;
	private List<Tag> conceptsOfLoginUser = new ArrayList<Tag>();
	private List<Tag> conceptsOfRequestedUser = new ArrayList<Tag>();
	private List<Tag> conceptsOfGroup = new ArrayList<Tag>();
	private List<Tag> conceptsOfAll = new ArrayList<Tag>();
	
	/**
	 * @return the requested tagstring as a list
	 */
	public List<String> getRequestedTagsList() {		
		// tagstring has not yet been tokenized 
		if (this.requestedTagsList == null) {
			this.requestedTagsList = new ArrayList<String>();			
			final StringTokenizer st = new StringTokenizer(requestedTags);
			while (st.hasMoreTokens()) {			
				final String tagname = st.nextToken();			
				this.requestedTagsList.add(tagname);			
			}			
		}		
		return this.requestedTagsList;
	}
	
	/**
	 * @return requested tags as string
	 */
	public String getRequestedTags() {
		return this.requestedTags;
	}	
	
	/**
	 * sets the requested tags
	 * @param requestedTags 
	 */
	public void setRequestedTags(final String requestedTags) {
		relatedTagCommand.setRequestedTags(requestedTags);
		this.requestedTags = requestedTags;
	}

	/**
	 * @return command with related tags
	 */
	public RelatedTagCommand getRelatedTagCommand() {
		return this.relatedTagCommand;
	}
	
	/**
	 * @return the relatedUserCommand
	 */
	public RelatedUserCommand getRelatedUserCommand() {
		return this.relatedUserCommand;
	}
	
	/**
	 * @param relatedUserCommand
	 */
	public void setRelatedUserCommand(final RelatedUserCommand relatedUserCommand) {
		this.relatedUserCommand = relatedUserCommand;
	}

	/**
	 * @param relatedTagCommand command with related tags
	 */
	public void setRelatedTagCommand(final RelatedTagCommand relatedTagCommand) {
		this.relatedTagCommand = relatedTagCommand;
	}

	/**
	 * @return order
	 */
	public String getOrder() {
		return this.order;
	}

	/**
	 * @param order
	 */
	public void setOrder(final String order) {
		this.order = order;
	}

	/**
	 * @return the similarTags
	 */
	public RelatedTagCommand getSimilarTags() {
		return this.similarTags;
	}

	/**
	 * @param similarTags the similarTags to set
	 */
	public void setSimilarTags(final RelatedTagCommand similarTags) {
		this.similarTags = similarTags;
	}
	
	
	/**
	 * @param postCount
	 */
	public void setPostCountForTagsForLoginUser(int postCount) {
		this.postCountForTagsForLoginUser = postCount;
	}

	/**
	 * @return  number of loginUser's posts for the requestedTags
	 */
	public int getPostCountForTagsForLoginUser() {
		return postCountForTagsForLoginUser;
	}
	
	/**
	 * @param postCount
	 */
	public void setPostCountForTagsForRequestedUser(int postCount) {
		this.postCountForTagsForRequestedUser = postCount;
	}

	/**
	 * @return number of requestedUser's posts for the requestedTags
	 */
	public int getPostCountForTagsForRequestedUser() {
		return postCountForTagsForRequestedUser;
	}
	
	/**
	 * @param postCount
	 */
	public void setPostCountForTagsForGroup(int postCount) {
		this.postCountForTagsForGroup = postCount;
	}

	/**
	 * @return number of requestedGroup's posts for the requestedTags
	 */
	public int getPostCountForTagsForGroup() {
		return postCountForTagsForGroup;
	}
	
	/**
	 * @param postCount
	 */
	public void setPostCountForTagsForAll(int postCount) {
		this.postCountForTagsForAll = postCount;
	}

	/**
	 * @return number of all posts for the requestedTags
	 */
	public int getPostCountForTagsForAll() {
		return postCountForTagsForAll;
	}
	
	/**
	 * @param conceptsOfLoginUser
	 */
	public void setConceptsOfLoginUser(List<Tag> conceptsOfLoginUser) {
		this.conceptsOfLoginUser = conceptsOfLoginUser;
	}

	/**
	 * @return conceptsOfLoginUser (a list of tags)
	 */
	public List<Tag> getConceptsOfLoginUser() {
		return conceptsOfLoginUser;
	}
	
	/**
	 * @param conceptsOfRequestedUser
	 */
	public void setConceptsOfRequestedUser(List<Tag> conceptsOfRequestedUser) {
		this.conceptsOfRequestedUser = conceptsOfRequestedUser;
	}

	/**
	 * @return conceptsOfRequestedUser (a list of tags)
	 */
	public List<Tag> getConceptsOfRequestedUser() {
		return conceptsOfRequestedUser;
	}
	
	/**
	 * @param conceptsOfGroup
	 */
	public void setConceptsOfGroup(List<Tag> conceptsOfGroup) {
		this.conceptsOfGroup = conceptsOfGroup;
	}

	/**
	 * @return conceptsOfGroup (a list of tags)
	 */
	public List<Tag> getConceptsOfGroup() {
		return conceptsOfGroup;
	}
	
	/**
	 * @param conceptsOfAll
	 */
	public void setConceptsOfAll(List<Tag> conceptsOfAll) {
		this.conceptsOfAll = conceptsOfAll;
	}

	/**
	 * @return conceptsOfAll (a list of tags)
	 */
	public List<Tag> getConceptsOfAll() {
		return conceptsOfAll;
	}
}
