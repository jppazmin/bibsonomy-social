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

package org.bibsonomy.webapp.command.ajax;

import org.bibsonomy.model.Resource;
import org.bibsonomy.webapp.command.actions.EditPostCommand;



/**
 * Command for recommendation ajax requests.
 * 
 * @author fei
 * @version $Id: AjaxRecommenderCommand.java,v 1.3 2010-07-14 15:38:38 nosebrain Exp $
 * 
 * @param <RESOURCE> the type of resource this command handles 
 */
public class AjaxRecommenderCommand<RESOURCE extends Resource> extends EditPostCommand<RESOURCE> implements AjaxCommandInterface {
	private String responseString;

	/**
	 * @return the responseString
	 */
	@Override
	public String getResponseString() {
		return this.responseString;
	}

	/**
	 * @param responseString the responseString to set
	 */
	@Override
	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}
}
