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

package org.bibsonomy.webapp.controller;

import org.bibsonomy.layout.jabref.JabrefLayoutRenderer;
import org.bibsonomy.webapp.command.ExportPageCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * @author Christian
 * @version $Id: ExportPageController.java,v 1.8 2010-07-14 14:21:45 nosebrain Exp $
 */
public class ExportPageController implements MinimalisticController<ExportPageCommand> {
	
	private JabrefLayoutRenderer layoutRenderer;
	private RequestLogic requestLogic;
	
	/** 
	 * Returns an instance of the command the controller handles.
	 * 
	 * @see org.bibsonomy.webapp.util.MinimalisticController#instantiateCommand()
	 */
	@Override
	public ExportPageCommand instantiateCommand() {		
		return new ExportPageCommand();
	}

	/** Main method which does the registration.
	 * 
	 * @see org.bibsonomy.webapp.util.MinimalisticController#workOn(org.bibsonomy.webapp.command.BaseCommand)
	 */
	@Override
	public View workOn(final ExportPageCommand command) {
		command.setLayoutMap(this.layoutRenderer.getJabrefLayouts());
		command.setLang(this.requestLogic.getLocale().getLanguage());
		
		return Views.EXPORT;
	}
	
	/**
	 * @param layoutRenderer
	 */
	public void setLayoutRenderer(final JabrefLayoutRenderer layoutRenderer) {
		this.layoutRenderer = layoutRenderer;
	}

	/**
	 * 
	 * @param requestLogic
	 */
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

}
