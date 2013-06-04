/**
 *
 *  BibSonomy-Scraper - Web page scrapers returning BibTeX for BibSonomy.
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

package org.bibsonomy.scraper.converter.picatobibtex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author C. Kramer
 * @version $Id: Row.java,v 1.6 2011-04-29 07:24:29 bibsonomy Exp $
 */
public class Row {
	private String cat = null;
	private Map<String, SubField> subfields = new HashMap<String, SubField>();
	
	
	/**
	 * @param cat
	 */
	public Row(final String cat){
		this.cat = cat;
	}

	/**
	 * Adds a subfield to the row object
	 * 
	 * @param sub
	 */
	public void addSubField(final SubField sub){
		this.subfields.put(sub.getSubTag(), sub);
	}

	/**
	 * Returns the category of the row
	 * 
	 * @return String
	 */
	public String getCat() {
		return this.cat;
	}

	/**
	 * Tests if the given subfield is existing in this row
	 * 
	 * @param sub
	 * @return boolean
	 */
	public boolean isExisting(final String sub){
		return subfields.containsKey(sub);
	}
	
	/**
	 * Returns the requested SubField
	 * 
	 * @param sub
	 * @return SubField
	 */
	public SubField getSubField(final String sub){
		return subfields.get(sub);
	}
	
	/**
	 * Return the complete SubField map of this row
	 * 
	 * @return Map<String, SubField>
	 */
	public Map<String, SubField> getSubFields(){
		return this.subfields;
	}
}
