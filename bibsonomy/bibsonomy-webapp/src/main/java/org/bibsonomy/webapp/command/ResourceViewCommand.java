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

import java.util.Set;

import org.bibsonomy.model.Resource;

/**
 * command with fields for the resource lists.
 * 
 * is mainly a container for two list commands (bookmarks & publications), the requested username
 * and a list of tags associated with the bookmarks / publications
 * 
 * @see BaseCommand
 * @author Jens Illig
 * @author Dominik Benz
 */
public class ResourceViewCommand extends BaseCommand {	
	/** default value for sortPage */
	public static final String DEFAULT_SORTPAGE = "none";
	/** default value for sortPageOrder */
	public static final String DEFAULT_SORTPAGEORDER = "asc";
	
	private TagCloudCommand tagcloud = new TagCloudCommand();
	private String requestedUser;
	private Set<Class<? extends Resource>> resourcetype;
	private String tagstype; // for queries for specific kinds of tags
	private String format = "html"; 
	private String layout; // if format="layout", here the requested layout is stored
	private boolean formatEmbedded; // 
	private String sortPage = DEFAULT_SORTPAGE;
	private String sortPageOrder = DEFAULT_SORTPAGEORDER;
	private String duplicates = "yes";
	private boolean notags = false;

	/**
	 * For some pages we need to store the referer to send the user back
	 * to that page.
	 */
	private String referer;
	
	/**
	 * if true, the posts and tags of the requested user will be ranked / highlighted
	 * according to the logged-in user 
	 */
	private Boolean personalized = false;	
	
	/** retrieve only tags without resources */
	private boolean restrictToTags = false;

	/** callback function for JSON outputs */
	private String callback = "";	
	
	/** filter group resources  */
	private String filter = "";

	/**
	 * @return the duplicates
	 */
	public String getDuplicates() {
		return this.duplicates;
	}
	
	/**
	 * @param duplicates the duplicates to set
	 */
	public void setDuplicates(String duplicates) {
		this.duplicates = duplicates;
	}
	
	/**
	 * @return name of the user whose resources are requested
	 */
	public String getRequestedUser() {
		return this.requestedUser;
	}
	/**
	 * @param requestedUser name of the user whose resources are requested
	 */
	public void setRequestedUser(String requestedUser) {
		this.requestedUser = requestedUser;
	}

	/**
	 * @return the tagcloud command
	 */
	public TagCloudCommand getTagcloud() {
		return this.tagcloud;
	}

	/**
	 * @param tagcloud the tagcloud command
	 */
	public void setTagcloud(TagCloudCommand tagcloud) {
		this.tagcloud = tagcloud;
	}

	/**
	 * @return The requested format.
	 * 
	 */
	public String getFormat() {
		if (this.format != null && !this.format.trim().equals("")) return this.format;
		/*
		 * the default is html
		 * */
		return "html";
	}

	/** 
	 * delegated to {@link RequestWrapperContext}
	 */
	/**
	 * @param format
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

	/**
	 * @return the resourcetype
	 */
	public Set<Class<? extends Resource>> getResourcetype() {
		return this.resourcetype;
	}

	/**
	 * @param resourcetype the resourcetype to set
	 */
	public void setResourcetype(Set<Class<? extends Resource>> resourcetype) {
		this.resourcetype = resourcetype;
	}

	/**
	 * @return the sortPage
	 */
	public String getSortPage() {
		return this.sortPage;
	}

	/**
	 * @param sortPage the sortPage to set
	 */
	public void setSortPage(String sortPage) {
		this.sortPage = sortPage;
	}

	/**
	 * @return the sortPageOrder
	 */
	public String getSortPageOrder() {
		return this.sortPageOrder;
	}

	/**
	 * @param sortPageOrder the sortPageOrder to set
	 */
	public void setSortPageOrder(String sortPageOrder) {
		this.sortPageOrder = sortPageOrder;
	}

	/**
	 * @return the restrictToTags
	 */
	public boolean getRestrictToTags() {
		return this.restrictToTags;
	}

	/**
	 * @param restrictToTags the restrictToTags to set
	 */
	public void setRestrictToTags(boolean restrictToTags) {
		this.restrictToTags = restrictToTags;
	}

	/**
	 * @return the callback
	 */
	public String getCallback() {
		return this.callback;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return this.filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return the layout
	 */
	public String getLayout() {
		return this.layout;
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @return the formatEmbedded
	 */
	public boolean getformatEmbedded() {
		return this.formatEmbedded;
	}

	/**
	 * @param formatEmbedded the formatEmbedded to set
	 */
	public void setformatEmbedded(boolean formatEmbedded) {
		this.formatEmbedded = formatEmbedded;
	}
	
	/**
	 * @return the tagstype
	 */
	public String getTagstype() {
		return this.tagstype;
	}

	/**
	 * @param tagstype the tagstype to set
	 */
	public void setTagstype(String tagstype) {
		this.tagstype = tagstype;
	}

	/**
	 * @return the notags
	 */
	public boolean isNotags() {
		return this.notags;
	}

	/**
	 * @param notags the notags to set
	 */
	public void setNotags(boolean notags) {
		this.notags = notags;
	}
	
	/**
	 * @return the personalized
	 */
	public Boolean getPersonalized() {
		return this.personalized;
	}

	/**
	 * @param personalized the personalized to set
	 */
	public void setPersonalized(Boolean personalized) {
		this.personalized = personalized;
	}
	
	/**
	 * @return @see {@link #getPersonalized()}
	 */
	public Boolean isPersonalized() {
		return personalized;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return this.referer;
	}

	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

}