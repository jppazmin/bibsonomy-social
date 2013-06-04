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

package org.bibsonomy.scraper.converter;


import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.scraper.converter.picatobibtex.PicaParser;
import org.bibsonomy.scraper.converter.picatobibtex.PicaRecord;
import org.bibsonomy.scraper.converter.picatobibtex.Row;
import org.bibsonomy.scraper.converter.picatobibtex.SubField;
import org.bibsonomy.scraper.exceptions.ScrapingException;

/**
 * @author C. Kramer
 * @version $Id: PicaToBibtexConverter.java,v 1.7 2011-04-29 07:24:34 bibsonomy Exp $
 */
public class PicaToBibtexConverter {
	private static final Log log = LogFactory.getLog(PicaToBibtexConverter.class);
	
	private PicaRecord pica;
	private String url;
	
	/**
	 * Convert the pica content to the pica object structure
	 * 
	 * @param sc 
	 * @param type
	 * @param url
	 */
	public PicaToBibtexConverter(final String sc, final String type, final String url){
		this.pica = new PicaRecord();
		this.url = url;
		
		parseContent(sc);
	}
	
	/**
	 * Creates an PicaRecord object out of the given content
	 * 
	 * @param sc
	 */
	private void parseContent(String sc){
		try {
			String formattedCont = "";
			StringTokenizer token = null;
			
			// get the content of the xml tag <longtitle></longtitle>
			Pattern p = Pattern.compile("(?s)<LONGTITLE.*?>(.*)</LONGTITLE>");
			Matcher m = p.matcher(sc);
			
			// if there is content save it, if not throw exception
			if (m.find()){
				formattedCont = (m.group(1));
			} else {
				throw new ScrapingException("Problems to get content");
			}
			
			// divide content by newlines
			token = new StringTokenizer(formattedCont, "\n");
			
			// finally create the pica objects
			while(token.hasMoreTokens()){
				String _cont = token.nextToken();
				if(!"<br />".equals(_cont)){
					processRow(_cont);
				}
			}
			
//			// create an xml parser
//			DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = fac.newDocumentBuilder();
//			Document doc = builder.parse(new ByteArrayInputStream(sc.getBytes("UTF-8")));
//			
//			
//			// get the nodelist which holds the "rows"
//			NodeList nodes = doc.getElementsByTagName("LONGTITLE");
//			NodeList childs = nodes.item(0).getChildNodes();
//			
//			// for every node type 3 ( text content ) call processRow
//			for (int i=0; i<childs.getLength(); i++){
//				Node child = childs.item(i);
//				if (child.getNodeType() == 3){
//					processRow(child.getTextContent().trim());
//				}
//			}	
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * This method should extract the necessary content with regex and put the information
	 * to the PicaRecord object
	 * 
	 * @param cont
	 */
	private void processRow(String cont){
		// pattern to extract the pica category
		Pattern p = Pattern.compile("^(\\d{3}[A-Z@]{1}/\\d{2}|\\d{3}[A-Z@]{1}).*$");
		Matcher m = p.matcher(cont);
		
		if(m.matches()){
			Row _tmpRow = new Row(m.group(1));
			String _cont = cont.replaceFirst(m.group(1), "");
			
			// etract the subfield of each category
			Pattern p1 = Pattern.compile("(\\$[0-9a-zA-Z]{1})([^\\$]+)");
			Matcher m1 = p1.matcher(_cont);
			
			// put it to the row object
			while(m1.find()){
				_tmpRow.addSubField(new SubField(m1.group(1),m1.group(2)));
			}
			
			// and finally put the row to the picarecord
			pica.addRow(_tmpRow);
		}
	}
	
	/**
	 * @return PicaRecord
	 */
	public PicaRecord getActualPicaRecord(){
		return pica;
	}
	
	/**
	 * @return Bibtex String
	 * @throws Exception 
	 */
	public String getBibResult() throws Exception{
		PicaParser parser = new PicaParser(pica, url);
		return parser.getBibRes();
	}
}
