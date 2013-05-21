/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

package org.bibsonomy.recommender.tags.simple;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.RecommendedTag;
import org.bibsonomy.model.Resource;
import org.bibsonomy.recommender.tags.AbstractTagRecommender;
import org.bibsonomy.recommender.tags.TagRecommenderConnector;
import org.bibsonomy.util.UrlUtils;
import org.bibsonomy.util.XmlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Tag recommender which parses html file at given url for keywords from the meta-inf section.
 * 
 * @author fei
 * @version $Id: MetaInfoTagRecommender.java,v 1.15 2010-07-14 11:38:29 nosebrain Exp $
 */
public class MetaInfoTagRecommender extends AbstractTagRecommender implements TagRecommenderConnector {
	private static final Log log = LogFactory.getLog(MetaInfoTagRecommender.class);
	
	@Override
	protected void addRecommendedTagsInternal(Collection<RecommendedTag> recommendedTags, Post<? extends Resource> post) {
		if( Bookmark.class.isAssignableFrom(post.getResource().getClass()) ) {
			String url = ((Bookmark)post.getResource()).getUrl();
			if( !UrlUtils.isValid(url) ) {
				log.debug("Invalid url: "+url);
				return;
			}
			log.debug("Scraping " + url + " for keywords.");
			
			String[] keywords = getKeywordsForUrl(url).split(",");
			int ctr = 0;
			if( keywords.length>0 ) {
				for( int i=0; i<keywords.length; i++ ){
					if(keywords[i].length()>0) {
						final String tag = getCleanedTag(keywords[i].toLowerCase().trim().replaceAll("\\s", "_"));
						if (tag != null) {
							ctr++;
							/*
							 * add one to not get 1.0 as score 
							 */
							recommendedTags.add(new RecommendedTag(tag, 1.0 / (ctr + 1.0), 0));
						}
					}
				}
			}
		}
	}

	@Override
	public String getInfo() {
		return "Recommender using html <meta> informations.";
	}

	/**
	 * Parses html file at given url and returns keywords from its meta informations.
	 * @param url file's url
	 * @return keywords as given in html file if present, empty string otherwise.
	 */
	private String getKeywordsForUrl(String url) {
		String keywordsStr = "";
		try {
			final Document document = XmlUtils.getDOM(new URL(url));
			
			final NodeList metaList = document.getElementsByTagName("meta");
			for (int i = 0; i < metaList.getLength(); i++) {
				final Element metaElement = (Element) metaList.item(i);
				
				Attr nameAttr = metaElement.getAttributeNode("name");
				if( (nameAttr!=null) && (nameAttr.getNodeValue().equalsIgnoreCase("keywords")) ) {
					keywordsStr += metaElement.getAttribute("content");
					log.debug("KEYWORDS for URL "+url.toString()+":"+keywordsStr);
				}
			}
		} catch (IOException ex) {
			// ignore exceptions silently
		}
		return keywordsStr;
	}
	

	@Override
	public boolean connect() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getMeta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean initialize(Properties props) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getId() {
		return MetaInfoTagRecommender.class.getName();
	}

	@Override
	protected void setFeedbackInternal(Post<? extends Resource> post) {
		/*
		 * ignored
		 */
	}
}
