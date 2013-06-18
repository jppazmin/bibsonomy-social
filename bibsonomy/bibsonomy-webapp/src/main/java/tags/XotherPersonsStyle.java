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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author mgr
 * @version $Id: XotherPersonsStyle.java,v 1.2 2010-05-28 13:34:59 nosebrain Exp $
 * 
 * Anzahl der USer, die auch diese Ressource getaggt haben: Einfärbung des Hintergrundes (v)
 */
public class XotherPersonsStyle extends TagSupport {
	private static final long serialVersionUID = -5215482499853870382L;
	
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
			// get integer from string
			int v = Integer.parseInt(value) + 4;
			// set maximum
			if (v > 1024) {
				v = 1024;
			}
			// 1024 posts = 100% (=50% of brightness)
			v = (int) (100.0 - Math.log(v / Math.log(2) * 2.0));
			pageContext.getOut().print("background-color: rgb("+v+"%, "+v+"%, "+v+"%);");
		} catch (IOException ioe) {
			throw new JspException("Error: IOException while writing to client" + ioe.getMessage());
		} catch (NumberFormatException e) {
			throw new JspException("Error: NumberFormatException while writing to client" + e.getMessage());
		}
		return SKIP_BODY;
	}
}