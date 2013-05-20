/**
 *
 *  BibSonomy-Rest-Client - The REST-client.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.rest.client;

import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.logic.LogicInterfaceFactory;

/**
 * 
 * @version $Id: RestLogicFactory.java,v 1.8 2011-05-24 09:38:22 bibsonomy Exp $
 */
public class RestLogicFactory implements LogicInterfaceFactory {

	private final String apiUrl;

	public RestLogicFactory() {
		this.apiUrl = null;
	}

	/**
	 * @param apiUrl the api base url of the REST service
	 */
	public RestLogicFactory(final String apiUrl) {
		this.apiUrl = apiUrl;
	}

	@Override
	public LogicInterface getLogicAccess(final String loginName, final String apiKey) {
		if (this.apiUrl != null) return new RestLogic(loginName, apiKey, this.apiUrl);
		return new RestLogic(loginName, apiKey);
	}
}