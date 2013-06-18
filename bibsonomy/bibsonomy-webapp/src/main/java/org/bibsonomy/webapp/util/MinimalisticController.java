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

package org.bibsonomy.webapp.util;

import org.bibsonomy.webapp.command.ContextCommand;


/**
 * A minialistic controller that knows nothing about being invoked
 * by a servlet request or a testcase or whatever. It only communicates
 * via the command object (argument of {@link #workOn(ContextCommand)} and result
 * of {@link #instantiateCommand()}).
 * 
 * @param <T> type of the command object
 * 
 * @author Jens Illig
 * @version $Id: MinimalisticController.java,v 1.4 2010-11-17 10:55:36 nosebrain Exp $
 */
public interface MinimalisticController<T extends ContextCommand> {
	/**
	 * @return a command object to be filled by the framework
	 */
	public T instantiateCommand();
	
	/**
	 * @param command a command object initialized by the framework based on
	 *                the parameters of som request-event like a http-request
	 * @return some symbol that describes the next state of the
	 *         application (the view)
	 */
	public View workOn(T command);
}
