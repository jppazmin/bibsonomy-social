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

/**
 * @author C. Kramer
 * @version $Id: SubField.java,v 1.5 2011-04-29 07:24:29 bibsonomy Exp $
 */
public class SubField {
	private String subtag = null;
	private String subcontent = null;
	
	/**
	 * @param subtag
	 * @param subcontent
	 */
	public SubField(final String subtag, final String subcontent){
		this.subtag = subtag;
		this.subcontent = subcontent;
	}
	
	/**
	 * Returns the subtag i.e. $5
	 * 
	 * @return String
	 */
	public String getSubTag(){
		return this.subtag;
	}
	
	/**
	 * Return the content of this subfield
	 * 
	 * @return String
	 */
	public String getContent(){
		return this.subcontent;
	}
}
