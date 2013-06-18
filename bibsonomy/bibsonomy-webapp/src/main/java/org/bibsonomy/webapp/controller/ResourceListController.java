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

package org.bibsonomy.webapp.controller;


import static org.bibsonomy.util.ValidationUtils.present;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.FilterEntity;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.StatisticsConstraint;
import org.bibsonomy.common.enums.TagCloudSort;
import org.bibsonomy.common.enums.TagCloudStyle;
import org.bibsonomy.common.enums.TagsType;
import org.bibsonomy.database.systemstags.SystemTagsExtractor;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.UserSettings;
import org.bibsonomy.model.enums.Order;
import org.bibsonomy.model.factories.ResourceFactory;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.model.util.TagUtils;
import org.bibsonomy.util.SortUtils;
import org.bibsonomy.webapp.command.ListCommand;
import org.bibsonomy.webapp.command.ResourceViewCommand;
import org.bibsonomy.webapp.command.SimpleResourceViewCommand;
import org.bibsonomy.webapp.command.TagCloudCommand;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;

/**
 * controller for retrieving multiple windowed lists with resources. 
 * These are currently the bookmark an the bibtex list
 * 
 * @author Jens Illig
 * @version $Id: ResourceListController.java,v 1.28 2011-05-17 15:20:28 dbe Exp $
 */
public abstract class ResourceListController {
	/** default values for sorting when jabref layouts are to be rendered */
	private static final String DEFAULT_SORTPAGE_JABREF_LAYOUTS = "year|author|title";
	private static final String DEFAULT_SORTPAGEORDER_JABREF_LAYOUTS = "desc|asc|asc";
	
	private static final Log log = LogFactory.getLog(ResourceListController.class);

	protected static <T> Set<T> intersection(final Collection<? extends T> col1, final Collection<? extends T> col2) {
		if (!present(col1) || !present(col2)) {
			return Collections.emptySet();
		}
		
		final Set<T> set = new HashSet<T>(col1);
		set.retainAll(col2);
		return set;
	}
	
	protected LogicInterface logic;
	protected UserSettings userSettings;
	private long startTime;
	
	/**
	 * these resource classes are supported by the controller
	 */
	protected Set<Class<? extends Resource>> supportedResources;
	
	/**
	 * these resource classes the controller should always initialize
	 */
	private Set<Class<? extends Resource>> forcedResources;
	
	/**
	 * if <code>true</code> the controller should not initalize any resource
	 * (e.g. only tags are handled)
	 */
	private boolean initializeNoResources;

	/**
	 * Retrieve a set of tags from the database logic and add them to the command object
	 * 
	 * @param cmd the command
	 * @param resourceType the resource type
	 * @param groupingEntity the grouping entity
	 * @param groupingName the grouping name
	 * @param regex regular expression for tag filtering
	 * @param tags list of tags
	 * @param start start parameter
	 * @param end end parameter
	 */
	protected void setTags(final ResourceViewCommand cmd, final Class<? extends Resource> resourceType, final GroupingEntity groupingEntity, final String groupingName, final String regex, final List<String> tags, final String hash, final int max, final String search) {
		final TagCloudCommand tagCloudCommand = cmd.getTagcloud();
		// retrieve tags
		log.debug("getTags " + " " + groupingEntity + " " + groupingName);
		Order tagOrder = null;
		int tagMax = max;
		/*
		 * check parameters from URL
		 */
		if (tagCloudCommand.getMinFreq() == 0 && tagCloudCommand.getMaxCount() == 0) { // no parameter was set via URL
			/*
			 * check user's settings
			 */
			if (this.userSettings.getIsMaxCount()) {
				tagOrder = Order.FREQUENCY;
				tagMax = Math.min(max, this.userSettings.getTagboxMaxCount());
			} else {
				// overwrite minFreq because it is not explicitly set by URL param
				tagCloudCommand.setMinFreq(this.userSettings.getTagboxMinfreq());
			}
		} else { //parameter set via URL
			if (tagCloudCommand.getMinFreq() == 0) {
				tagOrder = Order.FREQUENCY;
				tagMax = tagCloudCommand.getMaxCount();
			}
		}
		
		/*
		 * allow controllers to overwrite max and order
		 * FIXME: In the hurry I found no nice way to do this. :-( 
		 */
		tagMax = this.getFixedTagMax(tagMax);
		tagOrder = this.getFixedTagOrder(tagOrder);
		
		tagCloudCommand.setTags(this.logic.getTags(resourceType, groupingEntity, groupingName, regex, tags, hash, tagOrder, 0, tagMax, search, null));
		// retrieve tag cloud settings
		tagCloudCommand.setStyle(TagCloudStyle.getStyle(this.userSettings.getTagboxStyle()));
		tagCloudCommand.setSort(TagCloudSort.getSort(this.userSettings.getTagboxSort()));
		tagCloudCommand.setMaxFreq(TagUtils.getMaxUserCount(tagCloudCommand.getTags()));
	}

	/**
	 * If a sub-classing controller wants to enforce a fixed order of
	 * the tag cloud, it should overwrite this method and return the
	 * fixed order.
	 * 
	 * @param tagOrder
	 * @return
	 */
	protected Order getFixedTagOrder(final Order tagOrder) {
		return tagOrder;
	}
	
	/**
	 * If a sub-classing controller wants to enforce a fixed number
	 * of tags in the tag cloud, it should overwrite this method and
	 * return the number of tags.
	 * 
	 * @param tagMax
	 * @return
	 */
	protected int getFixedTagMax(final int tagMax) {
		return tagMax;
	}

	/**
	 * Initialize tag list, depending on chosen resourcetype
	 * 
	 * @param cmd
	 * @param listResourceType
	 * @param groupingEntity
	 * @param groupingName
	 * @param regex
	 * @param tags
	 * @param hash
	 * @param order
	 * @param start
	 * @param end
	 * @param search
	 */
	protected void handleTagsOnly(final ResourceViewCommand cmd, final GroupingEntity groupingEntity, String groupingName, String regex, List<String> tags, String hash, int max, String search) {
		final String tagsType = cmd.getTagstype();
		if (tagsType != null) {

			// if tags are requested (not related tags), remove non-systemtags from tags list
			if (tagsType.equalsIgnoreCase(TagsType.DEFAULT.getName()) && tags != null ) {
				SystemTagsExtractor.removeAllNonSystemTags(tags);
			}

			// check if limitation to a single resourcetype is requested			
			Class<? extends Resource> resourcetype = Resource.class;
			if (this.isPublicationOnlyRequested(cmd)) {
				resourcetype = BibTex.class;
			} else if (this.isBookmarkOnlyRequested(cmd)) {
				resourcetype = Bookmark.class;
			}

			// fetch tags, store them in bean
			this.setTags(cmd, resourcetype, groupingEntity, groupingName, regex, tags, hash, max, search);

			// when tags only are requested, we don't need any resources
			this.setInitializeNoResources(true);	
		}
	}


	/**
	 * do some post processing with the retrieved resources
	 * 
	 * @param cmd
	 */
	protected void postProcessAndSortList(final ResourceViewCommand cmd, final List<Post<BibTex>> posts) {
		for (final Post<BibTex> post : posts) {
			// insert openURL into bibtex objects
			post.getResource().setOpenURL(BibTexUtils.getOpenurl(post.getResource()));
		}
		// if a jabref layout is to be rendered and no special order is given, set to default order 
		if (Views.LAYOUT.getName().equalsIgnoreCase(cmd.getFormat()) && ResourceViewCommand.DEFAULT_SORTPAGE.equalsIgnoreCase(cmd.getSortPage())) {
			cmd.setSortPage(DEFAULT_SORTPAGE_JABREF_LAYOUTS);
			cmd.setSortPageOrder(DEFAULT_SORTPAGEORDER_JABREF_LAYOUTS);
		}
		
		if ("no".equals(cmd.getDuplicates())) {
			BibTexUtils.removeDuplicates(posts);
			// re-sort list by date in descending order, if nothing else requested
			if (ResourceViewCommand.DEFAULT_SORTPAGE.equals(cmd.getSortPage())) {
				cmd.setSortPage("date");
				cmd.setSortPageOrder("desc");
			}
		}
		
		if (!ResourceViewCommand.DEFAULT_SORTPAGE.equals(cmd.getSortPage())) {
			BibTexUtils.sortBibTexList(posts, SortUtils.parseSortKeys(cmd.getSortPage()), SortUtils.parseSortOrders(cmd.getSortPageOrder()) );
		}
	}

	/**
	 * retrieve a list of posts from the database logic and add them to the command object
	 * 
	 * @param <T> extends Resource
	 * @param cmd the command object
	 * @param resourceType the resource type
	 * @param groupingEntity the grouping entity
	 * @param groupingName the grouping name
	 * @param itemsPerPage number of items to be displayed on each page
	 */
	protected <T extends Resource> void setList(final SimpleResourceViewCommand cmd, Class<T> resourceType, GroupingEntity groupingEntity, String groupingName, List<String> tags, String hash, Order order, FilterEntity filter, String search, int itemsPerPage) {
		final ListCommand<Post<T>> listCommand = cmd.getListCommand(resourceType);
		// retrieve posts		
		log.debug("getPosts " + resourceType + " " + groupingEntity + " " + groupingName + " " + listCommand.getStart() + " " + itemsPerPage + " " + filter);
		final int start = listCommand.getStart();
		listCommand.setList(this.logic.getPosts(resourceType, groupingEntity, groupingName, tags, hash, order, filter, start, start + itemsPerPage, search));
		// list settings
		listCommand.setEntriesPerPage(itemsPerPage);
	}

	/**
	 * retrieve the number of posts from the database logic and add it to the command object
	 * 
	 * @param <T> extends Resource
	 * @param cmd the command object
	 * @param resourceType the resource type
	 * @param groupingEntity the grouping entity
	 * @param groupingName the grouping name
	 * @param itemsPerPage number of items to be displayed on each page
	 * @param constraint
	 */
	protected <T extends Resource> void setTotalCount(final SimpleResourceViewCommand cmd, Class<T> resourceType, GroupingEntity groupingEntity, String groupingName, List<String> tags, String hash, Order order, FilterEntity filter, String search, int itemsPerPage, StatisticsConstraint constraint) {
		final ListCommand<Post<T>> listCommand = cmd.getListCommand(resourceType);
		log.debug("getPostStatistics " + resourceType + " " + groupingEntity + " " + groupingName + " " + listCommand.getStart() + " " + itemsPerPage + " " + filter);
		final int start = listCommand.getStart();
		final int totalCount = this.logic.getPostStatistics(resourceType, groupingEntity, groupingName, tags, hash, order, filter, start, start + itemsPerPage, search, constraint);
		listCommand.setTotalCount(totalCount);
	}

	protected void startTiming(Class<? extends ResourceListController> controller, String format) {
		log.info("Handling Controller: " + controller.getSimpleName() + ", format: " + format);
		this.startTime = System.currentTimeMillis();
	}

	protected void endTiming() {
		long elapsed = System.currentTimeMillis() - this.startTime;
		log.info("Processing time: " + elapsed + " ms");
	}

	/**
	 * Check if only publications are requested
	 * 
	 * @param cmd - the current command object
	 * @return true if only publications are requested, false otherwise
	 */
	private boolean isPublicationOnlyRequested(final ResourceViewCommand cmd) {
		final Set<Class<? extends Resource>> listsToInitialize = this.getListsToInitialize(cmd.getFormat(), cmd.getResourcetype());
		return listsToInitialize.size() == 1 && listsToInitialize.contains(BibTex.class);
	}

	/**
	 * Check if only bookmarks are requested
	 * 
	 * @param cmd - the current command object
	 * @return true if only bookmarks are requested, false otherwise
	 */
	private boolean isBookmarkOnlyRequested(final ResourceViewCommand cmd) {
		final Set<Class<? extends Resource>> listsToInitialize = this.getListsToInitialize(cmd.getFormat(), cmd.getResourcetype());
		return listsToInitialize.size() == 1 && listsToInitialize.contains(Bookmark.class);
	}

	/**
	 * Restrict result lists by range from startIndex to endIndex.
	 * 
	 * @param cmd - the command object
	 * @param resourceType - the requested resourcetype
	 * @param startIndex - start index
	 * @param endIndex - end index
	 */
	protected void restrictResourceList(SimpleResourceViewCommand cmd, Class<? extends Resource> resourceType, final int startIndex, final int endIndex) {			
		if (BibTex.class.equals(resourceType)) {
			cmd.getBibtex().setList(cmd.getBibtex().getList().subList(startIndex, endIndex));
		}
		if (Bookmark.class.equals(resourceType)) {
			cmd.getBookmark().setList(cmd.getBookmark().getList().subList(startIndex, endIndex));
		}				
	}

	/**
	 * @param supportedResources the supportedResources to set
	 */
	public void setSupportedResources(final Set<Class<? extends Resource>> supportedResources) {
		this.supportedResources = supportedResources;
	}

	/**
	 * @param initializeNoResources the noResourcesToInitialize to set
	 */
	public void setInitializeNoResources(boolean initializeNoResources) {
		this.initializeNoResources = initializeNoResources;
	}

	private Set<Class<? extends Resource>> getUserResourcesFromSettings() {		
		final Set<Class<? extends Resource>> resources = new HashSet<Class<? extends Resource>>();
		
		if (this.userSettings.isShowBookmark()) {
			resources.add(Bookmark.class);
		}
		
		if (this.userSettings.isShowBibtex()) {
			resources.add(BibTex.class);
		}
		
		return resources;
	}

	private Set<? extends Class<? extends Resource>> getResourcesForFormat(final String format) {
		if (Views.isPublicationOnlyFormat(format)) {
			return Collections.singleton(BibTex.class);
		}
		
		if (Views.isBookmarkOnlyFormat(format)) {
			return Collections.singleton(Bookmark.class);
		}
		
		return new HashSet<Class<? extends Resource>>(ResourceFactory.getAllResourceClasses());
	}
	
	/**
	 * @param format e.g. "json"
	 * @param urlParamResources ?resourcetype=bookmark&resourcetype=publication
	 * @return all resources that must be initialized by this controller
	 */
	public Set<Class<? extends Resource>> getListsToInitialize(final String format, final Set<Class<? extends Resource>> urlParamResources) {
		if (this.initializeNoResources) {
			return Collections.emptySet();
		}
		
		if (present(this.forcedResources)) {
			return this.forcedResources;
		}
		
		final Set<Class<? extends Resource>> supportFormat = intersection(this.supportedResources, this.getResourcesForFormat(format));
		final Set<Class<? extends Resource>> supportParam = intersection(this.supportedResources, urlParamResources);
		final Set<Class<? extends Resource>> supportUser = intersection(this.supportedResources, this.getUserResourcesFromSettings());
		
		if (!present(supportFormat) && !present(supportParam)) {
			return Collections.emptySet();
		}
		
		if (present(supportFormat)) {
			final Set<Class<? extends Resource>> supportFormatParam = intersection(supportFormat, supportParam);
			if (present(supportFormatParam)) {
				return supportFormatParam;
			}
			
			final Set<Class<? extends Resource>> supportFormatUser = intersection(supportFormat, supportUser);
			if (present(supportFormatUser)) {
				return supportFormatUser;
			}
			
			return supportFormat;
		}
		
		if (!present(supportFormat) && present(supportParam)) {
			return Collections.emptySet();
		}
		
		return this.supportedResources;
	}
	
	/**
	 * @param userSettings the loginUsers userSettings
	 */
	@Required
	public void setUserSettings(UserSettings userSettings) {
		this.userSettings = userSettings;
	}

	/**
	 * @param logic logic interface
	 */
	@Required
	public void setLogic(LogicInterface logic) {
		this.logic = logic;
	}
	
	/**
	 * @param forcedResources the forcedResources to set
	 */
	public void setForcedResources(Set<Class<? extends Resource>> forcedResources) {
		this.forcedResources = forcedResources;
	}
}