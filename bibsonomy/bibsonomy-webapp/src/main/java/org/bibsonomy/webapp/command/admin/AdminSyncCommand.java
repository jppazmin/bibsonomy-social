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

package org.bibsonomy.webapp.command.admin;

import java.net.URI;
import java.util.List;

import org.bibsonomy.webapp.command.BaseCommand;

/**
 * @author wla
 * @version $Id: AdminSyncCommand.java,v 1.2 2011-07-26 12:45:36 rja Exp $
 */
public class AdminSyncCommand extends BaseCommand {
	
	private List<URI> avlServer;
	private List<URI> avlClients;
	private String action;
	private String service;
	private boolean server;
	
	/**
	 * @param avlServer the avlServer to set
	 */
	public void setAvlServer(List<URI> avlServer) {
		this.avlServer = avlServer;
	}
	/**
	 * @return the avlServer
	 */
	public List<URI> getAvlServer() {
		return avlServer;
	}
	/**
	 * @param avlClients the avlClients to set
	 */
	public void setAvlClients(List<URI> avlClients) {
		this.avlClients = avlClients;
	}
	/**
	 * @return the avlClients
	 */
	public List<URI> getAvlClients() {
		return avlClients;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}
	/**
	 * @param server the server to set
	 */
	public void setServer(boolean server) {
		this.server = server;
	}
	/**
	 * @return the server
	 */
	public boolean isServer() {
		return server;
	}
}
