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

package org.bibsonomy.webapp.command;

import org.bibsonomy.webapp.util.RequestWrapperContext;

/**
 * Defines a command which has access to the RequestWrapperContext
 * 
 * @author Dominik Benz
 * @version $Id: ContextCommand.java,v 1.1 2008-12-19 10:37:58 dbe Exp $
 */
public interface ContextCommand {

	/**
	 * Get RequestWrapperContext
	 * @return - the request wrapper context
	 */
	public RequestWrapperContext getContext();

	/**
	 * Set RequestWrapperContext
	 * @param context - the request wrapper context
	 */
	public void setContext(RequestWrapperContext context);
}
