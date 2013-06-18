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

package org.bibsonomy.webapp.util.spring.security.exceptions;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author dzo
 * @version $Id: AccessDeniedNoticeException.java,v 1.1 2011-06-15 14:24:42 nosebrain Exp $
 */
public class AccessDeniedNoticeException extends AccessDeniedException {
	private static final long serialVersionUID = 8538409864663313358L;
	
	private final String notice;
	
	/**
	 * @param msg the message to set
	 * @param notice the notice to set
	 */
	public AccessDeniedNoticeException(final String msg, final String notice) {
		super(msg);
		this.notice = notice;
	}

	/**
	 * @return the notice
	 */
	public String getNotice() {
		return notice;
	}
}
