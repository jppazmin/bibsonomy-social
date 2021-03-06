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

package org.bibsonomy.webapp.controller.ajax;

import org.bibsonomy.webapp.command.ajax.LoggingCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * Controller for ajax clicklog requests 
 * 
 * @author Sven Stefani
 * @version $Id: LoggingController.java,v 1.3 2010-10-22 11:45:41 rja Exp $
 */
public class LoggingController extends AjaxController implements MinimalisticController<LoggingCommand> {

	@Override
	public View workOn(final LoggingCommand command) {
		// TODO: implement me
		return Views.AJAX_TEXT;
	}	

	@Override
	public LoggingCommand instantiateCommand() {
		return new LoggingCommand();
	}
}