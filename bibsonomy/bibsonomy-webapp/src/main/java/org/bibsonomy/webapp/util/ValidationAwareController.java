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
 * extended MinimalisticControllers interface which can be optionally implemented
 * to tell about whether validation is required or not
 * 
 * @param <T> type of the command object
 * @version $Id: ValidationAwareController.java,v 1.7 2010-11-17 11:05:48 nosebrain Exp $
 * @author Jens Illig
 */
public interface ValidationAwareController<T extends ContextCommand> extends MinimalisticController<T> {
	
	/**
	 * @param command a command object initialized by the framework based on
	 *                the parameters of some request-event like a http-request
	 * @return decision whether validation for this request is required or not
	 */
	public boolean isValidationRequired(T command);
	
	/**
	 * @return the validator to use for validation
	 */
	public Validator<T> getValidator();
}
