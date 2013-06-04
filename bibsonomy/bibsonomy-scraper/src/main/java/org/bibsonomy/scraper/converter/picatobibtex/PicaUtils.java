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
 * @author daill
 * @version $Id: PicaUtils.java,v 1.5 2011-04-29 07:24:29 bibsonomy Exp $
 */
public class PicaUtils {
	private PicaRecord pica;
	// String array with all regex pieces to be replaced
	private String[] cleaning = {"@", "&lt;.+?&gt;", "\\{", "\\}"};
	
	/**
	 * @param pica
	 */
	public PicaUtils(final PicaRecord pica){
		this.pica = pica;
	}
	
	/**
	 * use this method to get the data out of a specific row and subfield
	 * 
	 * @param cat
	 * @param sub
	 * @return string
	 */
	public String getData(final String cat, final String sub){
		Row r = null;
		SubField f = null;
		
		if ((r = pica.getRow(cat)) != null){
			if ((f = r.getSubField(sub)) != null){
				return f.getContent();
			}
		}
		
		return "";
	}
	
	/**
	 * Tries to clean the given String from i.e. internal references like @
	 * 
	 * @param toClean
	 * @return String
	 */
	public String cleanString(String toClean){
		String res = toClean;
		
		for (String s : cleaning){
			res = res.replaceAll(s, "");
		}
		
		
		return res;
	}
	
	/**
	 * Replace "XML=1.0/CHARSET=UTF-8/PRS=PP" in the url
	 * 
	 * @param url
	 * @return formatted url
	 */
	public String prepareUrl(String url){
		String new_url = "";
		
		new_url = url.replaceFirst("XML=1.0/CHARSET=UTF-8/PRS=PP/", "");
		
		return new_url;
	}
}
