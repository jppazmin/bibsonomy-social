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
 * Creates a JSON list about the available JabRef layouts on the /layoutinfo page.
 * FIXME: complete clone of {@link ExportPageController} 
 * @author mwa, dbe
 * @version $Id: ExportLayoutController.java,v 1.7 2010-11-17 10:55:34 nosebrain Exp $
 */
public class ExportLayoutController implements MinimalisticController<ExportPageCommand> {
	
	/** layout renderer */
	private JabrefLayoutRenderer layoutRenderer;
	/** request logic */
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
	
	/** 
	 * @see org.bibsonomy.webapp.util.MinimalisticController#workOn(org.bibsonomy.webapp.command.BaseCommand)
	 */
	@Override
	public View workOn(ExportPageCommand command) {
		command.setLayoutMap(this.layoutRenderer.getJabrefLayouts());
		command.setLang(this.requestLogic.getLocale().getLanguage());
		
		return Views.EXPORTLAYOUTS;
	}
	
	/**
	 * @param layoutRenderer
	 */
	public void setLayoutRenderer(JabrefLayoutRenderer layoutRenderer) {
		this.layoutRenderer = layoutRenderer;
	}

	/**
	 * 
	 * @param requestLogic
	 */
	public void setRequestLogic(RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}
	
}
