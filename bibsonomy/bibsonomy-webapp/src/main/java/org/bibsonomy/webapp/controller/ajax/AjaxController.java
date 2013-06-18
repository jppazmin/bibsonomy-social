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

import javax.servlet.http.HttpServletResponse;

import org.bibsonomy.model.UserSettings;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.webapp.util.RequestAware;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.ResponseAware;
import org.bibsonomy.webapp.util.ResponseLogic;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;
import org.springframework.beans.factory.annotation.Required;

/**
 * Controller for ajax requests
 * 
 * @author Stefan Stützer
 * @version $Id: AjaxController.java,v 1.5 2011-06-21 13:54:25 nosebrain Exp $
 */
public abstract class AjaxController implements RequestAware, ResponseAware {

	protected LogicInterface logic;	
	protected UserSettings userSettings;
	
	protected RequestLogic requestLogic;
	protected ResponseLogic responseLogic;
	
	/**
	 * @param logic the logic to set
	 */
	@Required
	public void setLogic(final LogicInterface logic) {
		this.logic = logic;
	}

	/**
	 * @param userSettings the userSettings to set
	 */
	@Required
	public void setUserSettings(final UserSettings userSettings) {
		this.userSettings = userSettings;
	}

	@Override
	@Required
	public void setRequestLogic(RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}
	
	@Override
	@Required
	public void setResponseLogic(ResponseLogic responseLogic) {
		this.responseLogic = responseLogic;
	}

	protected View returnErrorView() {
		this.responseLogic.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
		return Views.AJAX_ERRORS;
	}
}