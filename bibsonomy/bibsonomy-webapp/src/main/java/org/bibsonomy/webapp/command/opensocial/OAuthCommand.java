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

package org.bibsonomy.webapp.command.opensocial;

import net.oauth.OAuthConsumer;

import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret.KeyType;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import org.bibsonomy.opensocial.oauth.database.beans.OAuthConsumerInfo;
import org.bibsonomy.webapp.command.BaseCommand;

/**
 * @author fei
 * @version $Id: OAuthCommand.java,v 1.4 2011-06-10 16:16:04 bsc Exp $
 */
public class OAuthCommand extends BaseCommand {
	public enum AuthorizeAction { Authorize, Deny };

	private String responseString;
	
	private String authorizeAction;
	
	private OAuthConsumer consumer;
	
	/** information about OAuth token and authorization */
	private OAuthEntry entry;
	
	/** consumer meta information */
	private String appTitle;
	/** consumer meta information */
	private String appDescription;
	/** consumer meta information */
	private String appIcon;
	/** consumer meta information */
	private String appThumbnail;
	/** call back URL */
	private String callBackUrl;
	
	private OAuthConsumerInfo consumerInfo;
	
	public KeyType[] getKeyTypes() {
		return KeyType.values();
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setAuthorizeAction(String authorizeAction) {
		this.authorizeAction = authorizeAction;
	}

	public String getAuthorizeAction() {
		return authorizeAction;
	}

	public void setConsumer(OAuthConsumer consumer) {
		this.consumer = consumer;
	}

	public OAuthConsumer getConsumer() {
		return consumer;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppThumbnail(String appThumbnail) {
		this.appThumbnail = appThumbnail;
	}

	public String getAppThumbnail() {
		return appThumbnail;
	}

	public void setEntry(OAuthEntry entry) {
		this.entry = entry;
	}

	public OAuthEntry getEntry() {
		return entry;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setConsumerInfo(OAuthConsumerInfo consumerInfo) {
		this.consumerInfo = consumerInfo;
	}

	public OAuthConsumerInfo getConsumerInfo() {
		return consumerInfo;
	}

}
