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
import java.util.LinkedList;
import java.util.Map;

/**
 * @author C. Kramer
 * @version $Id: PicaRecord.java,v 1.6 2011-04-29 07:24:29 bibsonomy Exp $
 */
public class PicaRecord {
	private Map<String, LinkedList<Row>> rows = new HashMap<String, LinkedList<Row>>();
	
	/**
	 * Adds a row to this object
	 * 
	 * @param row
	 */
	public void addRow(Row row){
		LinkedList<Row> list = null;
		
		if(rows.containsKey(row.getCat())){
			list = rows.get(row.getCat());
			list.add(row);
		} else {
			list = new LinkedList<Row>();
			list.add(row);
			this.rows.put(row.getCat(), list);
		}
	}

	/**
	 * tests if the given pica category is existing
	 * 
	 * @param cat
	 * @return boolean
	 */
	public boolean isExisting(String cat){
		return rows.containsKey(cat);
	}
	
	/**
	 * get a specific row by category
	 * 
	 * @param cat
	 * @return Row
	 */
	public Row getRow(String cat){
		if (isExisting(cat)){
			LinkedList<Row> list = rows.get(cat);
		
			if(list.size() > 0){
				return list.get(0);
			} else {
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * @param cat
	 * @return The row for the given category.
	 */
	public LinkedList<Row> getRows(String cat){
		return rows.get(cat);
	}
	
}
