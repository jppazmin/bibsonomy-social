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

package org.bibsonomy.layout.feeds;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.Tag;
import org.bibsonomy.services.URLGenerator;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * Creates Atom and RSS feeds.
 * 
 * 
 * @author:  rja
 * @version: $Id: SyndicationFeedWriter.java,v 1.5 2011-04-29 07:34:02 bibsonomy Exp $
 * $Author: bibsonomy $
 * 
 */
public class SyndicationFeedWriter<RESOURCE extends Resource> {

	private URLGenerator urlGenerator;

	private String feedType;
	
	public URLGenerator getUrlGenerator() {
		return urlGenerator;
	}

	/**
	 * The URLGenerator is used to create all project-internal URLs in feeds, 
	 * e.g., URLs to the feed itself and so on.
	 * 
	 * @param urlGenerator
	 */
	public void setUrlGenerator(final URLGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}

	
	public void writeFeed(final SyndFeed feed, final Writer writer) throws IOException, FeedException {
		  final SyndFeedOutput output = new SyndFeedOutput();
		  output.output(feed, writer);
	}

	public void writeFeed (final String title, final String path, final String description, final List<Post<RESOURCE>> posts, final Writer writer) throws IOException, FeedException {
		writeFeed(createFeed(title, path, description, posts), writer);
	}

	
	/**
	 * Creates a new feed for the given list of posts.
	 * 
	 * @param title
	 * @param path
	 * @param description
	 * @param posts
	 * @return
	 */
	public SyndFeed createFeed (final String title, final String path, final String description, final List<Post<RESOURCE>> posts) {
		final SyndFeed feed = createFeed(title, path, description);

		final List<SyndEntry> entries = new LinkedList<SyndEntry>();

		for (final Post<RESOURCE> post: posts) {
			final SyndEntry entry = new SyndEntryImpl();
			final RESOURCE resource = post.getResource();
			entry.setTitle(resource.getTitle());
			/*
			 * For publications, we want to point to BibSonomy, for bookmarks to 
			 * their "real" URL.
			 */
			if (resource instanceof Bookmark) {
				entry.setLink(((Bookmark) resource).getUrl());
			} else {
				entry.setLink(urlGenerator.getPostUrl(post));
			}
			entry.setPublishedDate(post.getDate());
			entry.setAuthor(post.getUser().getName());
			entry.setUri(urlGenerator.getPostUrl(post));
			/*
			 * add the tags as categories
			 */
			final List<SyndCategory> categories = new LinkedList<SyndCategory>();
			for (final Tag tag: post.getTags()) {
				final SyndCategory category = new SyndCategoryImpl();
				category.setName(tag.getName());
				category.setTaxonomyUri(urlGenerator.getUserUrl(post.getUser()) + "/");
				categories.add(category);
			}

			entry.setCategories(categories);

			final SyndContent entryDescription = new SyndContentImpl();

			entryDescription.setType("text/plain");
			entryDescription.setValue(post.getDescription());

			entryDescription.setType("text/html");
			entryDescription.setValue("<p>More Bug fixes, mor API changes, some new features and some Unit testing</p>"+
			"<p>For details check the <a href=\"http://wiki.java.net/bin/view/Javawsxml/RomeChangesLog#RomeV03\">Changes Log</a></p>");

			entry.setDescription(entryDescription);
			entries.add(entry);

		}
	    feed.setEntries(entries);

		return feed;	     
	}
	



	public SyndFeed createFeed(final String title, final String path, final String description) {
		final SyndFeed feed = new SyndFeedImpl();
		feed.setFeedType(feedType);

		feed.setTitle(title);
		feed.setLink(urlGenerator.getAbsoluteUrl(path));
		feed.setDescription(description);
		return feed;
	}

	public String getFeedType() {
		return feedType;
	}

	/**
	 * Configure the type of feed this instance shall create. Supported values 
	 * are
	 * <dl>
	 *   <dt></dt>
	 *   <dd></dd>
	 *   <dt></dt>
	 *   <dd></dd>
	 * </dl>
	 * 
	 * @param feedType
	 */
	public void setFeedType(final String feedType) {
		this.feedType = feedType;
	}

}
