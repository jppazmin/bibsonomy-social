/**
 *
 *  BibSonomy-OpenAccess - Check Open Access Policies for Publications
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

package de.unikassel.puma.openaccess.sword;

/**
 * bean for configuring sword via JNDI
 * 
 * @author
 * @version
 */

public class SwordConfig {
	/** temp directory to build zip-file for sword-deposit */
	private String dirTemp;
	
	/** name or url of sword server of repository */
	private String httpServer;
	
	/** port number of sword server */
	private Integer httpPort = 80;
	
	/** user agent to send to sword server */
	private String httpUserAgent = "PUMA";
	
	/** url to sword service document, e.g.: "/sword/servicedocument" */
	private String httpServicedocumentUrl;
	
	/** url to deposit sword document, e.g. "http://servername:8080/sword/deposit/urn:nbn:de:hebis:12-3456" */
	private String httpDepositUrl;
	
	/** sword authentication username */
	private String authUsername;
	
	/** sword authentication password */
	private String authPassword;

	/**
	 * @return the dirTemp
	 */
	public String getDirTemp() {
		return dirTemp;
	}

	/**
	 * @param dirTemp the dirTemp to set
	 */
	public void setDirTemp(String dirTemp) {
		this.dirTemp = dirTemp;
	}

	/**
	 * @return the httpServer
	 */
	public String getHttpServer() {
		return httpServer;
	}

	/**
	 * @param httpServer the httpServer to set
	 */
	public void setHttpServer(String httpServer) {
		this.httpServer = httpServer;
	}

	/**
	 * @return the httpPort
	 */
	public Integer getHttpPort() {
		return httpPort;
	}

	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(Integer httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * @return the httpUserAgent
	 */
	public String getHttpUserAgent() {
		return httpUserAgent;
	}

	/**
	 * @param httpUserAgent the httpUserAgent to set
	 */
	public void setHttpUserAgent(String httpUserAgent) {
		this.httpUserAgent = httpUserAgent;
	}

	/**
	 * @return the httpServicedocumentUrl
	 */
	public String getHttpServicedocumentUrl() {
		return httpServicedocumentUrl;
	}

	/**
	 * @param httpServicedocumentUrl the httpServicedocumentUrl to set
	 */
	public void setHttpServicedocumentUrl(String httpServicedocumentUrl) {
		this.httpServicedocumentUrl = httpServicedocumentUrl;
	}

	/**
	 * @return the httpDepositUrl
	 */
	public String getHttpDepositUrl() {
		return httpDepositUrl;
	}

	/**
	 * @param httpDepositUrl the httpDepositUrl to set
	 */
	public void setHttpDepositUrl(String httpDepositUrl) {
		this.httpDepositUrl = httpDepositUrl;
	}

	/**
	 * @return the authUsername
	 */
	public String getAuthUsername() {
		return authUsername;
	}

	/**
	 * @param authUsername the authUsername to set
	 */
	public void setAuthUsername(String authUsername) {
		this.authUsername = authUsername;
	}

	/**
	 * @return the authPassword
	 */
	public String getAuthPassword() {
		return authPassword;
	}

	/**
	 * @param authPassword the authPassword to set
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

}
