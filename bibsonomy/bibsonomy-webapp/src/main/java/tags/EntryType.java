/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package tags;

import static org.bibsonomy.model.util.BibTexUtils.ENTRYTYPES;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Adaption of SWRC scheme types regarding user given entry types. E.g.: Mapping
 * article --> Article
 * 
 * @author mgr
 * @version $Id: EntryType.java,v 1.5 2010-07-16 10:29:35 rja Exp $
 */
@Deprecated
public class EntryType extends TagSupport {
	private static final long serialVersionUID = 234345234589762349l;
	
	private String value;
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int doStartTag() throws JspException {
		try {
			for (int i = 0; i < ENTRYTYPES.length; i++) {
				/* Comparison with current entrytype value */
				if (ENTRYTYPES[i].equals(value)) {
					/* match found -> print and stop loop */
					pageContext.getOut().print(URLEncoder.encode(Functions.swrcEntryTypes[i], "UTF-8"));
					return SKIP_BODY;
				}
			}
			
			/* default value is misc */
			pageContext.getOut().print(URLEncoder.encode(Functions.swrcEntryTypes[11], "UTF-8"));  
			return SKIP_BODY;
		} catch (IOException ioe) {
			throw new JspException("Error: IOException while writing to client" + ioe.getMessage());
		}		
	}
}