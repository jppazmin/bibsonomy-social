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

package org.bibsonomy.database.systemstags;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bibsonomy.database.common.DBSessionFactory;
import org.bibsonomy.database.systemstags.executable.ExecutableSystemTag;
import org.bibsonomy.database.systemstags.markup.MarkUpSystemTag;
import org.bibsonomy.database.systemstags.search.SearchSystemTag;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Andreas Koch
 * @version $Id: SystemTagFactory.java,v 1.21 2011-05-26 15:32:12 folke Exp $
 */
public class SystemTagFactory {

	// the configuration file for search and executable systemTags
	private static final String SYSTEM_TAG_CONFIG_FILE = "systemtags-context.xml";

	private static final SystemTagFactory singleton = new SystemTagFactory();

	/**
	 * @return the singleton 
	 */
	public static SystemTagFactory getInstance() {
		return singleton;
	}


	/** The map that contains all executable systemTags */
	private final Map<String, ExecutableSystemTag> executableSystemTagMap;

	/** The set that contains all searchSystemTags */
	private final Map<String, SearchSystemTag> searchSystemTagMap;

	/** The set that contains all searchSystemTags */
	private final Map<String, MarkUpSystemTag> markUpSystemTagMap;

	/** The DBSessionFactory (we need it for the forGroup tag) */
	private DBSessionFactory dbSessionFactory;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	private SystemTagFactory() {
		/*
		 * FIXME: shouldn't we configure this from the outside?
		 */
		final ClassPathXmlApplicationContext springBeanFactory = new ClassPathXmlApplicationContext(SYSTEM_TAG_CONFIG_FILE);
		this.executableSystemTagMap = new HashMap<String, ExecutableSystemTag>();
		this.fillExecutableSystemTagMap( (Set<ExecutableSystemTag>)springBeanFactory.getBean("executableSystemTagSet") );
		this.searchSystemTagMap = new HashMap<String, SearchSystemTag>();
		this.fillSearchSystemTagMap( (Set<SearchSystemTag>)springBeanFactory.getBean("searchSystemTagSet") );
		this.markUpSystemTagMap = new HashMap<String, MarkUpSystemTag>();
		this.fillMarkUpSystemTagMap( (Set<MarkUpSystemTag>)springBeanFactory.getBean("markUpSystemTagSet") );
	}


	/**
	 * Fills the ExecutableSystemTagMap with pairs:
	 * SystemTagName -> instance of the corresponding SystemTag
	 * @param executableSystemTags
	 */
	private void fillExecutableSystemTagMap(Set<ExecutableSystemTag> executableSystemTags) {
		for (ExecutableSystemTag sysTag: executableSystemTags) {
			this.executableSystemTagMap.put(sysTag.getName(), sysTag);
		}
	}

	/**
	 * Fills the SearchSystemTagMap with pairs:
	 * SystemTagName -> instance of the corresponding SystemTag
	 * @param searchSystemTags
	 */
	private void fillSearchSystemTagMap (Set<SearchSystemTag> searchSystemTags) {
		for (SearchSystemTag sysTag: searchSystemTags) {
			this.searchSystemTagMap.put(sysTag.getName(), sysTag);
		}
	}

	/**
	 * Fills the MarkUpSystemTagMap with pairs:
	 * SystemTagName -> instance of the corresponding SystemTag
	 * @param searchSystemTags
	 */
	private void fillMarkUpSystemTagMap (Set<MarkUpSystemTag> markUpSystemTags) {
		for (MarkUpSystemTag sysTag: markUpSystemTags) {
			this.markUpSystemTagMap.put(sysTag.getName(), sysTag);
		}
	}

	/**
	 * Returns a new instance of the required systemTag
	 * 
	 * @param tagName = the tag describing the systemTag e. g. "send:xyz" or "sys:for:xyz"
	 * @return an executable system tag or null if 
	 * either the tagType is not in the ExecutableSystemTagMap or
	 * the tagName is not a proper systemTagDescription
	 */
	public ExecutableSystemTag getExecutableSystemTag(final String tagName) {
		final String tagType = SystemTagsUtil.extractType(tagName);
		if (present(tagType)) {
			final ExecutableSystemTag sysTag = this.executableSystemTagMap.get(tagType);
			if (present(sysTag) && sysTag.isInstance(tagName)) {
				// tagName was found and tagName has the correct structure
				return sysTag.newInstance();
			}
		}
		return null;
	}

	/**
	 * Returns a new instance of the required systemTag
	 * 
	 * @param tagType = the tag describing the systemTag e. g. "user" or "days"
	 * @return a search system tag
	 */
	public SearchSystemTag getSearchSystemTag(final String tagName) {
		final String tagType = SystemTagsUtil.extractType(tagName);
		if (present(tagType)) {
			final SearchSystemTag sysTag = this.searchSystemTagMap.get(tagType);
			if (present(sysTag) && sysTag.isInstance(tagName)) {
				// tagName was found and tagName has the correct structure
				return sysTag.newInstance();
			}
		}
		return null;
	}

	public MarkUpSystemTag getMarkUpSystemTag(String tagName) {
		final String tagType = SystemTagsUtil.extractType(tagName);
		if (present(tagType)) {
			final MarkUpSystemTag sysTag = this.markUpSystemTagMap.get(tagType);
			if (present(sysTag) && sysTag.isInstance(tagName)) {
				// tagName was found and tagName has the correct structure
				return sysTag.newInstance();
			}
		}
		return null;
	}
	/**
	 * Determines whether a tag (given by name) is an executable systemTag
	 * 
	 * @param tagName
	 * @return <code>true</code> iff it's an executable system tag
	 */
	public boolean isExecutableSystemTag(final String tagName) {
		final String tagType = SystemTagsUtil.extractType(tagName);
		if (present(tagType)) {
			final ExecutableSystemTag sysTag = this.executableSystemTagMap.get(tagType);
			if (present(sysTag)) {
				/*
				 *  the tagName refers to a systemTag 
				 *  => check if it also has the required structure
				 */
				return sysTag.isInstance(tagName);
			}
		}
		return false;
	}

	/**
	 * Determines whether a tag (given by name) is a systemTag
	 * 
	 * @param tagType
	 * @return <code>true</code> iff it's a search system tag
	 */
	public boolean isSearchSystemTag(final String tagName) {
		final String tagType = SystemTagsUtil.extractType(tagName);
		if (present(tagType)) {
			final SearchSystemTag sysTag = this.searchSystemTagMap.get(tagType);
			if (present(sysTag)) {
				/*
				 *  the tagName refers to a systemTag 
				 *  => check if it also has the required structure
				 */
				return sysTag.isInstance(tagName);
			}
		}
		return false;
	}

	/**
	 * Determines whether a tag (given by name) is a systemTag
	 * 
	 * @param tagType
	 * @return <code>true</code> iff it's a search system tag
	 */
	public boolean isMarkUpSystemTag(final String tagName) {
		final String tagType = SystemTagsUtil.extractType(tagName);
		if (present(tagType)) {
			final SystemTag sysTag = this.markUpSystemTagMap.get(tagType);
			if (present(sysTag)) {
				/*
				 *  the tagName refers to a systemTag 
				 *  => check if it also has the required structure
				 */
				return sysTag.isInstance(tagName);
			}
		}
		return false;
	}

	/**
	 * @return The session factory of this system tag factory.
	 */
	public DBSessionFactory getDbSessionFactory() {
		return this.dbSessionFactory;
	}

	/**  
	 * @param sessionFactory
	 */
	public void setDbSessionFactory(final DBSessionFactory sessionFactory) {
		this.dbSessionFactory = sessionFactory;
	}


}
