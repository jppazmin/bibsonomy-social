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

package tags.ajax;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Encodes the given value with URLEncoder in UTF-8 encoding.
 *
 */
public class Autofill extends TagSupport {
	private static final long serialVersionUID = 868854563296030689L;
	private String var;

	public void setVar(String var) {
	    this.var = var;
	}
	
   public int doStartTag() throws JspException {
      try {
         pageContext.getOut().print("<span class=\"autofill\" id=\"" + var + "\"/>");
      } catch (IOException ioe) {
         throw new JspException("Error: IOException while writing to client" + ioe.getMessage());
      }
      return SKIP_BODY;
   }
}