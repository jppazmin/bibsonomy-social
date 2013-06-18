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

/*
 * Created on 08.10.2007
 */
package org.bibsonomy.webapp.util;

import org.bibsonomy.webapp.util.spring.controller.MinimalisticControllerSpringWrapper;
import org.springframework.validation.Errors;

/**
 * optional interface MinimalisticControllers can implement to receive info
 * about validation errors.
 * 
 * @author Jens Illig
 */
public interface ErrorAware {
	/**
	 * @return the validationerrors that occured in binding this request
	 *         to the command
	 */
	public Errors getErrors();
	/**
	 * @param errors setter used by the framework (namely {@link MinimalisticControllerSpringWrapper}) to inject possible validationerrors
	 */
	public void setErrors(Errors errors);
}
