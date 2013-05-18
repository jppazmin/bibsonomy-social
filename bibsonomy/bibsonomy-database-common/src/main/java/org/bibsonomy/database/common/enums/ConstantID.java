/**
 *
 *  BibSonomy-Database-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.database.common.enums;

import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;

/**
 * Constants that are used in SQL statements
 * 
 * @author Christian Schenk
 * @author Christian Kramer
 * @version $Id: ConstantID.java,v 1.7 2011-07-25 08:35:49 rja Exp $
 */
public enum ConstantID {
	/*
	 * SQL constants
	 */
	/** Contenttype for Bookmark */
	BOOKMARK_CONTENT_TYPE(1),
	/** Contenttype for BibTeX */
	BIBTEX_CONTENT_TYPE(2),
	/** Contenttype for ALL contents */
	ALL_CONTENT_TYPE(0),

	/* names for ids table */
	/** id of the contentId in ids table */
	IDS_CONTENT_ID(0),
	/** id of the tasId in ids table */
	IDS_TAS_ID(1),
	/** id of the tagRelId in ids table */
	IDS_TAGREL_ID(2),
	/** id of the quastionId in ids table */
	IDS_QUESTION_ID(3),
	/** id of the cycleId in ids table */
	IDS_CYCLE_ID(4),
	/** id of the exgtendedFieldsId in ids table */
	IDS_EXTENDED_FIELDS(5),
	/** id of the scraperMetadataId in ids table */
	IDS_SCRAPER_METADATA(7),
	/**
     * FolkRank rankings
     */
    IDS_RANKINGS(10),
    /**
     * FolkRank ranking jobs
     */
    IDS_RANKING_JOBS(11),
    /** id of the groupTasId in the ids table */
	IDS_GROUPTAS_ID(12),
	/** id of messageId in the ids table*/
	IDS_INBOX_MESSAGE_ID(14),
	/** id of discussion item */
	IDS_DISCUSSION_ITEM_ID(15),
	/** id of a syncronization service */
	IDS_SYNC_SERVICE(16),
	
	/* other ids (not related to SQL tables! */
	/** marks that no special content type has yet been assigned */
	IDS_UNDEFINED_CONTENT_ID(-1);

	private final int id;

	private ConstantID(final int id) {
		this.id = id;
	}

	/**
	 * @return the id constant behind the symbol
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @param resourceType
	 * @return the content type ID for the given resource type. This ID is used 
	 * inside the database only!
	 */
	public static ConstantID getContentTypeByClass(final Class<? extends Resource> resourceType) {
		if (BibTex.class.isAssignableFrom(resourceType)) {
			return ConstantID.BIBTEX_CONTENT_TYPE;
		} else if (Bookmark.class.isAssignableFrom(resourceType)) {
			return ConstantID.BOOKMARK_CONTENT_TYPE;
		} else if (Resource.class.isAssignableFrom(resourceType)) {
			return ConstantID.ALL_CONTENT_TYPE;
		} else {
			throw new UnsupportedResourceTypeException();
		}
	}
	
	/**
	 * The opposite of {@link #getContentTypeByClass(Class)}
	 * 
	 * @param contentType
	 * @return
	 */
	public static Class<? extends Resource> getClassByContentType(final ConstantID contentType) {
		return getClassByContentType(contentType.id);
	}

	/**
	 * The opposite of {@link #getContentTypeByClass(Class)}
	 * 
	 * @param contentType
	 * @return
	 */
	public static Class<? extends Resource> getClassByContentType(final int contentType) {
		switch (contentType) {
		case 2:
			return BibTex.class;
		case 1:
			return Bookmark.class;
		case 0:
			return Resource.class;
		default:
			throw new UnsupportedResourceTypeException();
		}
	}
	
}