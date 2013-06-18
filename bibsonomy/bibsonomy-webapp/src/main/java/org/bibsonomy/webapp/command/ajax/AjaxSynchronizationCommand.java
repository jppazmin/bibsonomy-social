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

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.bibsonomy.model.sync.SyncService;


/**
 * @author wla
 * @version $Id: AjaxSynchronizationCommand.java,v 1.7 2011-08-03 10:05:03 wla Exp $
 */
public class AjaxSynchronizationCommand extends AjaxCommand {

	private URI serviceName;
	private List<SyncService> syncServer;
	private Date syncDate;

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(URI serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceName
	 */
	public URI getServiceName() {
		return serviceName;
	}

	/**
	 * @param syncServer the syncServer to set
	 */
	public void setSyncServer(List<SyncService> syncServer) {
		this.syncServer = syncServer;
	}

	/**
	 * @return the syncServer
	 */
	public List<SyncService> getSyncServer() {
		return syncServer;
	}

	/**
	 * @param syncDate the syncDate to set
	 */
	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}

	/**
	 * @return the syncDate
	 */
	public Date getSyncDate() {
		return syncDate;
	}
}
