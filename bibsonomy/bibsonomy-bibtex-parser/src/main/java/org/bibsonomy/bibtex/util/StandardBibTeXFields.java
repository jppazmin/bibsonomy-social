/**
 *
 *  BibSonomy-BibTeX-Parser - BibTeX Parser from
 * 		http://www-plan.cs.colorado.edu/henkel/stuff/javabib/
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

package org.bibsonomy.bibtex.util;

import java.util.HashSet;
import java.util.Set;

/** Holds the standard BibTeX fields. Those are all fields which are 
 * represented by the model and which are not put into the "misc" 
 * column.
 * 
 * @author rja
 * @version $Id: StandardBibTeXFields.java,v 1.9 2011-04-29 06:55:59 bibsonomy Exp $
 */
public class StandardBibTeXFields {

	private final static Set<String> standardBibTeXFields = new HashSet<String>();
	private final static Set<String> standardBibSonomyFields = new HashSet<String>();
	
	static {
		/*
		 * standard BibTeX fields
		 */
		standardBibTeXFields.add("abstract");
		standardBibTeXFields.add("address");
		standardBibTeXFields.add("annote");
		standardBibTeXFields.add("author");
		standardBibTeXFields.add("booktitle");
		standardBibTeXFields.add("chapter");
		standardBibTeXFields.add("crossref");
		standardBibTeXFields.add("day");
		standardBibTeXFields.add("edition");
		standardBibTeXFields.add("editor");
		standardBibTeXFields.add("howpublished");
		standardBibTeXFields.add("institution");
		standardBibTeXFields.add("journal");
		standardBibTeXFields.add("key");
		standardBibTeXFields.add("month");
		standardBibTeXFields.add("note");
		standardBibTeXFields.add("number");
		standardBibTeXFields.add("organization");
		standardBibTeXFields.add("pages");
		standardBibTeXFields.add("publisher");
		standardBibTeXFields.add("school");
		standardBibTeXFields.add("series");
		standardBibTeXFields.add("title");
		standardBibTeXFields.add("type");
		standardBibTeXFields.add("url");
		standardBibTeXFields.add("volume");
		standardBibTeXFields.add("year");
		
		/*
		 * the standard BibSonomy fields
		 */
		standardBibSonomyFields.addAll(standardBibTeXFields);
		
		// added, because otherwise "day" will go to "misc"
		standardBibSonomyFields.add("day");
		// standard fields for bibsonomy
		standardBibSonomyFields.add("description");
		standardBibSonomyFields.add("tags");
		standardBibSonomyFields.add("url");
		standardBibSonomyFields.add("keywords");
		
		standardBibSonomyFields.add("comment");	 // used by CiteULike for private comments, read by BibSonomy (not written)
		standardBibSonomyFields.add("biburl");   // added because this way it is not added to "misc"
	}

	/**
	 * @return the {@link StandardBibTeXFields#standardBibSonomyFields}
	 */
	public static Set<String> getStandardBibSonomyFields() {
		return standardBibSonomyFields;
	}
	
	/**
	 * @return the {@link StandardBibTeXFields#standardBibTeXFields}
	 */
	public static Set<String> getStandardBibTeXFields() {
		return standardBibTeXFields;
	}
	
}