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

package org.bibsonomy.scraper.converter.picatobibtex.rules;


import java.util.LinkedList;

import org.bibsonomy.scraper.converter.picatobibtex.PicaRecord;
import org.bibsonomy.scraper.converter.picatobibtex.PicaUtils;
import org.bibsonomy.scraper.converter.picatobibtex.Row;

/**
 * @author daill
 * @version $Id: TagsRule.java,v 1.5 2011-04-29 07:24:33 bibsonomy Exp $
 */
public class TagsRule implements Rules {
	private PicaRecord pica = null;
	private PicaUtils utils = null;
	
	/**
	 * @param pica
	 * @param utils
	 */
	public TagsRule(PicaRecord pica, PicaUtils utils){
		this.pica = pica;
		this.utils = utils;
	}

	public String getContent() {
		String tags = "";
		
		LinkedList<Row> list = null;
		Row row = null;
		
		if((list = pica.getRows("044K")) != null){
			for(Row r : list){
				if(r.isExisting("$8")){
					tags += r.getSubField("$8").getContent() + " ";
				}
			}
		} else if(pica.isExisting("041A")){
			String cat = "041A";
			tags += utils.getData(cat, "$8") + " ";
			
			int ctr = 1;
			
			row = pica.getRow(cat + "/0" + Integer.toString(ctr));
			
			while(row != null){
				String newCat = cat + "/0" + Integer.toString(ctr);
				
				if(row.isExisting("$8")){
					tags += utils.getData(newCat, "$8") + " ";
				}
				
				ctr++;
	
				if (ctr < 10){
					row = pica.getRow(cat + "/0" + Integer.toString(ctr));
				} else {
					row = pica.getRow(cat + "/" + Integer.toString(ctr));
				}
			}
		}

		return utils.cleanString(tags);
	}

	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

}
