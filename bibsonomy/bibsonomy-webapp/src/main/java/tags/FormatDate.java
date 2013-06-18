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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class FormatDate extends TagSupport {

	/*
	 * pattern="yyyy-MM-dd'T'HH:mm:ssZ" 
	 */
	
	private static final long serialVersionUID = 4202115254832670512L;
	private Date value;
	private String type;

	public void setType(String type) {
		this.type = type;
	}
	public void setValue(Date value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {
		try {
			String date = null;
			if ("rss".equals(type)) {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(value);
			} else {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(value);
			}
			pageContext.getOut().print(date.substring(0, date.length()-2) + ":" + date.substring(date.length()-2, date.length()));
		} catch (IOException ioe) {
			throw new JspException("Error: IOException while writing to client" + ioe.getMessage());
		}
		return SKIP_BODY;
	}

}
