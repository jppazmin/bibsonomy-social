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

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.command.AuthorsCommand;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * @author Christian Claus
 * @version $Id: AuthorsPageController.java,v 1.5 2010-11-17 10:55:35 nosebrain Exp $
 */
public class AuthorsPageController implements MinimalisticController<AuthorsCommand> {

	private LogicInterface logic;
	
	@Override
	public View workOn(AuthorsCommand command) {
		command.setPageTitle("Authors"); // TODO: i18n
		command.setAuthorList(this.logic.getAuthors(GroupingEntity.ALL, null, null, null, null, null, 0, Integer.MAX_VALUE, null));
		return Views.AUTHORSPAGE;
	}

	@Override
	public AuthorsCommand instantiateCommand() {
		return new AuthorsCommand();
	}
	
	/**
	 * @param logic the logic to set
	 */
	public void setLogic(LogicInterface logic) {
		this.logic = logic;
	}
}
