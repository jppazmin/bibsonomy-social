/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.services;

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.MalformedURLException;
import java.net.URL;

import org.bibsonomy.common.enums.HashID;
import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.util.UrlUtils;

/**
 * Generates the URLs used by the web application.
 * 
 * @author rja
 * @version $Id: URLGenerator.java,v 1.12 2011-04-29 12:40:46 rja Exp $
 */
public class URLGenerator {

	/**
	 * Provides page names.
	 * 
	 * XXX: experimental!
	 *
	 * @author rja
	 *
	 */
	public enum Page {
		/**
		 * all posts users' have sent me using the "send:" system tag
		 */
		INBOX("inbox"),
		/**
		 * all posts I have picked  
		 */
		BASKET("basket");
		
		private final String path; 
		
		private Page(final String path) {
			this.path = path;
		}
		
		/**
		 * @return The string representation of this page
		 */
		public String getPath() {
			return path;
		}
	}
	
	private static final String USER_PREFIX = "user";
	private static final String PUBLICATION_PREFIX = "bibtex";
	private static final String BOOKMARK_PREFIX = "url";
	private static final String PUBLICATION_INTRA_HASH_ID = String.valueOf(HashID.INTRA_HASH.getId());
	private static final String PUBLICATION_INTER_HASH_ID = String.valueOf(HashID.INTER_HASH.getId());

	/**
	 * The default gives relative URLs.
	 */
	private String projectHome = "/";

	/**
	 * Per default, generated URLs are not checked.
	 */
	private boolean checkUrls = false;
	
	/**
	 * Sets up a new URLGenerator with the default projectHome ("/") and no 
	 * checking of URLs.
	 */
	public URLGenerator() {
		// noop
	}

	/**
	 * Sets up a new URLGenerator with the given projectHome.
	 * 
	 * @param projectHome
	 */
	public URLGenerator(final String projectHome) {
		super();
		this.projectHome = projectHome;
	}

	/**
	 * Creates an absolute URL for the given path.
	 * 
	 * @param path - the path part of the URL (TODO: with or without leading "/"?)
	 * @return The absolute URL.
	 */
	public String getAbsoluteUrl(final String path) {
		return this.getUrl(projectHome + path);
	}

	/**
	 * Returns the URL which represents a post. Depending on the type
	 * of the resource, this forwarded to {@link #getBookmarkUrl(Bookmark, User)} and 
	 * {@link #getPublicationUrl(BibTex, User)}.
	 * 
	 * @param post - The post for which the URL should be constructed. User and resources must not be null.
	 * @return The URL representing the given post.
	 */
	public String getPostUrl(final Post<? extends Resource> post) {
		final Resource resource = post.getResource();
		if (resource instanceof Bookmark) {
			return this.getBookmarkUrl(((Bookmark) resource), post.getUser());
		} else if (resource instanceof BibTex) {
			return this.getPublicationUrl(((BibTex) resource), post.getUser());
		} else {
			throw new UnsupportedResourceTypeException();
		}	
	}

	/**
	 * Constructs a URL for the given resource's intrahash. If you have the post as
	 * object, please use {@link #getPostUrl(Post)}.
	 * 
	 * @param resourceType - The type of resource. Currently, only URLs for {@link Bookmark} or {@link BibTex} are supported.
	 * @param intraHash
	 * @param userName
	 * @return The URL pointing to the post of that user for the resource represented by the given intrahash.
	 */
	public String getPostUrl(final Class<?> resourceType, final String intraHash, final String userName) {
		if (resourceType == Bookmark.class) {
			return this.getBookmarkUrl(intraHash, userName);
		} else if (resourceType == BibTex.class) {
			return this.getPublicationUrl(intraHash, userName);
		} else { 
			throw new UnsupportedResourceTypeException();
		}
	}

	/**
	 * Constructs a URL for the given resource and user. If no user 
	 * is given, the URL points to all posts for that resource.
	 * 
	 * @param publication - must have proper inter and intra hashes
	 * (a call to {@link Resource#recalculateHashes()} might be necessary
	 * but is not done by this method)
	 * 
	 * @param user - if null, the URL to all posts for the given publication 
	 * is returned.
	 * @return - The URL which represents the given publication.
	 */
	public String getPublicationUrl(final BibTex publication, final User user) {
		/*
		 * no user given
		 */
		if (!present(user) || !present(user.getName())){
			return this.getUrl(projectHome + PUBLICATION_PREFIX + "/" + PUBLICATION_INTER_HASH_ID + publication.getInterHash());
		}
		return this.getUrl(projectHome + PUBLICATION_PREFIX + "/" + PUBLICATION_INTRA_HASH_ID + publication.getIntraHash() + "/" + UrlUtils.safeURIEncode(user.getName()));
	}

	/**
	 * Constructs a new publication URL for the given publication and username.
	 * If you have the resource as object, please use {@link #getPublicationUrl(BibTex, User)}. 
	 * @param intraHash
	 * @param userName
	 * @return The URL pointing to the post of that user for the publication represented by the given intrahash. 
	 */
	public String getPublicationUrl(final String intraHash, final String userName) {
		return this.getUrl(projectHome + PUBLICATION_PREFIX + "/" + PUBLICATION_INTRA_HASH_ID + intraHash + "/" + UrlUtils.safeURIEncode(userName));
	}
	
	
	/**
	 * Constructs a URL for the given resource and user. If no user 
	 * is given, the URL points to all posts for that resource.
	 * 
	 * @param bookmark - must have proper inter and intra hashes
	 * (a call to {@link Resource#recalculateHashes()} might be necessary
	 * but is not done by this method)
	 * 
	 * @param user - if null, the URL to all posts for the given bookmark
	 * is returned.
	 * @return - The URL which represents the given bookmark
	 */
	public String getBookmarkUrl(final Bookmark bookmark, final User user) {
		/*
		 * no user given
		 */
		if (!present(user) || !present(user.getName())){
			return this.getUrl(projectHome + BOOKMARK_PREFIX + "/" + bookmark.getInterHash());
		}
		return this.getBookmarkUrl(bookmark.getIntraHash(), user.getName());
	}
	
	/**
	 * Constructs a bookmark URL for the given intraHash and userName. 
	 * If you have the resource as object, please use {@link #getBookmarkUrl(Bookmark, User)}
	 * @param intraHash
	 * @param userName
	 * @return The URL pointing to the post of that user for the bookmark represented by the given intrahash.
	 */
	public String getBookmarkUrl(final String intraHash, final String userName) {
		return this.getUrl(projectHome + BOOKMARK_PREFIX + "/" + intraHash + "/" + UrlUtils.safeURIEncode(userName));
	}


	/**
	 * Constructs the URL for the user's page.
	 * 
	 * @param user
	 * @return The URL for the user's page.
	 */
	public String getUserUrl(final User user) {
		return this.getUrl(projectHome + USER_PREFIX + "/" + UrlUtils.safeURIEncode(user.getName()));
	}
	
	/**
	 * Constructs the URL for the user's page.
	 * 
	 * @param userName
	 * @return The URL for the user's page.
	 */
	public String getUserUrl(final String userName) {
		return this.getUrl(projectHome + USER_PREFIX + "/" + UrlUtils.safeURIEncode(userName));
	}

	/**
	 * If {@link #checkUrls} is <code>true</code>, each given string is converted
	 * into a {@link URL} (if that fails, <code>null</code> is returned).
	 * Otherwise, the given string is returned as is.
	 * 
	 * @param url
	 * @return
	 */
	private String getUrl(final String url) {
		if (checkUrls) {
			try {
				return new URL(url).toString();
			} catch (MalformedURLException ex) {
				// FIXME!
				return null;
			}
		}
		return url;
	}

	/**
	 * @return the projectHome
	 */
	public String getProjectHome() {
		return this.projectHome;
	}

	/**
	 * ProjectHome defaults to <code>/</code>, such that relative URLs are
	 * generated. Note that this does not work with {@link #setCheckUrls(boolean)}
	 * set to <code>true</code>, since {@link URL} does not support relative URLs
	 * (or more correctly: relative URLs are not URLs).
	 * 
	 * @param projectHome
	 */
	public void setProjectHome(String projectHome) {
		this.projectHome = projectHome;
	}

	/**
	 * @see URLGenerator#setCheckUrls(boolean)
	 * @return checkUrls
	 */
	public boolean isCheckUrls() {
		return this.checkUrls;
	}

	/**
	 * If set to <code>true</code>, all generated URLs are put into {@link URL}
	 * objects. If that fails, <code>null</code> is returned. The default is 
	 * <code>false</code> such that no checking occurs. 
	 * 
	 * @param checkUrls
	 */
	public void setCheckUrls(boolean checkUrls) {
		this.checkUrls = checkUrls;
	}
	
	/**
	 * Checks if the given URL points to the given page. Useful for checking the referrer header. 
	 * 
	 * @param url
	 * @param page
	 * @return <code>true</code> if the given URL points to the given page.
	 */
	public boolean matchesPage(final String url, final Page page) {
		final String pageName = page.getPath();
		final String absoluteUrl = getAbsoluteUrl(pageName);
		return url != null && url.contains(absoluteUrl);
	}
	
	/**
	 * Checks if the given URL points to the given resource.
	 * 
	 * @param url - the URL that should be checked.
	 * @param userName - the owner of the resource
	 * @param intraHash - the intra hash of the resource
	 * @return <code>true</code> if the url points to the resource.
	 */
	public boolean matchesResourcePage(final String url, final String userName, final String intraHash) {
		if (!present(url)) return false;
		return url.matches(".*/(" + PUBLICATION_PREFIX + "|" + BOOKMARK_PREFIX + ")/[0-3]?" + intraHash + "/" + userName + ".*");
	}
	
}
