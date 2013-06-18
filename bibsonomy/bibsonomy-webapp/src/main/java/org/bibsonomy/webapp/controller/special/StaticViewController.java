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

package org.bibsonomy.webapp.controller.special;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.webapp.command.BaseCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * This controller only returns the configured view and else does nothing.
 * default view: {@link Views#ERROR}
 * 
 * @author rja
 * @version $Id: StaticViewController.java,v 1.2 2010-04-28 13:33:25 nosebrain Exp $
 */
public class StaticViewController implements MinimalisticController<BaseCommand>{
	private static final Log log = LogFactory.getLog(StaticViewController.class);
	
	private Views view = Views.ERROR;
	
	@Override
	public BaseCommand instantiateCommand() {
		return new BaseCommand();
	}

	@Override
	public View workOn(BaseCommand command) {
		log.debug("returning view " + view);
		return this.view;
	}

	/**
	 * @return the view the controller returns
	 */
	public Views getView() {
		return this.view;
	}

	/** Set the view this controller shall return.
	 * @param view
	 */
	public void setView(Views view) {
		this.view = view;
	}
}
