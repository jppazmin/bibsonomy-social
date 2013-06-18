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

import javax.servlet.jsp.JspTagException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * The tag checks, if the given (command) path exists and only
 * then executes the content of its body.
 *  
 * @author rja
 * @version $Id: Exists.java,v 1.2 2010-05-28 13:34:59 nosebrain Exp $
 */
public class Exists extends RequestContextAwareTag {
	private static final long serialVersionUID = 8378817318583491829L;
	
	
	private String path;
	
	@Override
	protected int doStartTagInternal() throws Exception {
		final String resolvedPath = ExpressionEvaluationUtils.evaluateString("path", getPath(), pageContext);

		try {
			new BindStatus(getRequestContext(), resolvedPath, false);
		} catch (final IllegalStateException ex) {
			throw new JspTagException(ex.getMessage());
		} catch (final NotReadablePropertyException ex) {
			/*
			 * property not found, skip body of tag
			 */
			return SKIP_BODY;
		}
		return EVAL_BODY_INCLUDE;
	}

	
	/**
	 * Set the path that this tag should apply. Can be a bean (e.g. "person"),
	 * or a bean property (e.g. "person.name"). The tag checks 
	 * 
	 * @param path 
	 * 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Return the path that this tag applies to.
	 * @return The path that this tag applies to.
	 */
	public String getPath() {
		return this.path;
	}
}
