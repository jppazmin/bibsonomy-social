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


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.scraper.converter.picatobibtex.rules.AbstractRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.AuthorRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.ISBNRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.ISSNRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.PublisherRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.Rules;
import org.bibsonomy.scraper.converter.picatobibtex.rules.SeriesRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.TagsRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.TitleRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.URNRule;
import org.bibsonomy.scraper.converter.picatobibtex.rules.YearRule;

/**
 * 
 * The bibtexkey is manufactured in accordance to the 013H category of pica+.
 * http://www.allegro-c.de/formate/f/f0528.htm
 * 
 * TODO: need to fix that nearly every category is repeatable
 * TODO: all this should have a better structure
 * TODO: fix special chars
 * 
 * @author C. Kramer
 * @version $Id: PicaParser.java,v 1.7 2011-04-29 07:24:29 bibsonomy Exp $
 */
public class PicaParser{
	private PicaRecord pica = null;
	private String  url = null;
	

	
	/**
	 * @param pica
	 * @param url 
	 */
	public PicaParser(final PicaRecord pica, final String url){
		this.url = url;
		this.pica = pica;
	}
	
	/**
	 * start parsing
	 * 
	 * @return String
	 * @throws Exception 
	 */
	public String getBibRes() throws Exception{		
		return parse();
	}
	
	/**
	 * method to activate the parsing methods and form the complete bibtex string
	 * 
	 * @return String
	 */
	private String parse() throws Exception{
		PicaUtils utils = new PicaUtils(pica);
		StringBuffer bibres = new StringBuffer();
		
		String type = getBibType();
		String author = new AuthorRule(pica, utils).getContent();
		String title = new TitleRule(pica, utils).getContent();
		String year = new YearRule(pica, utils).getContent();
		String isbn = new ISBNRule(pica, utils).getContent();
		String issn = new ISSNRule(pica, utils).getContent();
		String series = new SeriesRule(pica, utils).getContent();
		String abstr = new AbstractRule(pica, utils).getContent();
		String tags = new TagsRule(pica, utils).getContent();
		String publisher = new PublisherRule(pica, utils).getContent();
		
		String opac = "";
		
		Rules urn = new URNRule(pica, utils);
		
		if(urn.isAvailable()){
			url = "http://nbn-resolving.org/urn/resolver.pl?urn=" + urn.getContent();
			opac = this.url;
		} else {
			url = utils.prepareUrl(this.url);
			opac = this.url;
		}
		
		
			
		String bibkey = createBibkey(author,year);
			
			
		bibres.append(type + bibkey + ",\n");
		bibres.append("author = {" + author + "},\n");
		bibres.append("title = {" + title + "},\n");
		bibres.append("year = {" + year + "},\n");
		bibres.append("abstract = {" + abstr + "}, \n");
		bibres.append("keywords = {" + tags + "}, \n");
		bibres.append("url = {" + url + "}, \n");
		bibres.append("opac = {" + opac + "}, \n");
		bibres.append("series = {" + series + "}, \n");
		bibres.append("isbn = {" + isbn + "}, \n");
		bibres.append("issn = {" + issn + "}, \n");
		bibres.append("publisher = {" + publisher + "}, \n");
		bibres.append("}");
		
		return bibres.toString();
	}
	
	/**
	 * This helpmethod should create the bibtexkey be author and year
	 * 
	 * @param author
	 * @param year
	 * @return BibtexKey
	 */
	private String createBibkey(final String author, final String year){
		String key = "";
		
		if ("".equals(author) && "".equals(year)){
			return "imported";
		}
		
		if (author.matches("^(.+?),.*$")){
			Pattern p = Pattern.compile("^(.+?),.*$");
			Matcher m = p.matcher(author);
			if(m.find()){
				key += m.group(1);
			}
		}
		
		key += ":" + year;
		
		return key;
	}
	
	private String getBibType(){
		Row r = null;
		SubField s = null;
		
		/*
		 * tests if the category 013H is existing and test for some values, 
		 * if not check if the title category 021A has a $d subfield and if "proceedings" matches
		 * if thats the case then its a proceeding. If the entry has a ISBN and NO ISSN then its a book,
		 * if it has a ISSN and NO ISBN then its an article otherwise if it has ISBN AND ISSN
		 * its usually a proceeding.
		 * 
		 * If the 013H category is set and the $0 subfield provides the value u then
		 * it will be decided between phdthesis, masterthesis and techreport
		 */
		if ((r = pica.getRow("013H")) != null){
			if ((s = r.getSubField("$0")) != null){
				
				if ("u".equals(s.getContent())){
					Row _tempRow = null;
					SubField _tempSub = null;
					
					if ((_tempRow = pica.getRow("037C")) != null){
						if ((_tempSub = _tempRow.getSubField("$c")) != null){
							final String _tempCont = _tempSub.getContent();
							if (_tempCont.matches("^.*Diss.*$")){
								return "@phdthesis{";
							} else if (_tempCont.matches("^.*Master.*$")){
								return "@mastersthesis{";
							} else {
								return "@techreport{";
							}
						}
					}
				}
			}
		} 
			
		if(pica.getRow("021A").isExisting("$d")){
			if (pica.getRow("021A").getSubField("$d").getContent().trim().matches("^.*proceedings.*$")){
				return "@proceedings{";
			}
		} 
		
		if((pica.isExisting("004A") || pica.isExisting("004D")) && !pica.isExisting("005A") && !pica.isExisting("005D")) {
			return "@book{";
		}
		
		if(((pica.isExisting("005A")) || pica.isExisting("005D")) && !pica.isExisting("004A") && !pica.isExisting("004D")){
			return "@article{";
		}
		
		if(((pica.isExisting("004A")) || pica.isExisting("004D")) && (pica.isExisting("005A") || pica.isExisting("005D"))){
			return "@proceedings{";
		}
		
		return "@misc{";
	}
}
