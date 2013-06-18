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

package org.bibsonomy.webapp.controller.opensocial;

import static org.bibsonomy.util.ValidationUtils.present;

import java.io.IOException;
import java.net.URISyntaxException;

import net.oauth.OAuth;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import org.bibsonomy.model.User;
import org.bibsonomy.webapp.command.opensocial.OAuthCommand;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

/**
 * This controller implements the OAuth endpoints described in RFC 5849, section 2:
 * 
 *  Token Request ("accessToken")
 *        The endpoint used by the client to request a set of token
 *        credentials using the set of temporary credentials as described
 *        in Section 2.3.
 * 
 * @author fei
 * @version $Id: OAuthAccessTokenController.java,v 1.3 2011-05-11 13:45:01 folke Exp $
 */
public class OAuthAccessTokenController extends OAuthProtocolController {
	private static final Log log = LogFactory.getLog(OAuthAccessTokenController.class);
	
	//------------------------------------------------------------------------
	// OAuthProtocolController interface
	//------------------------------------------------------------------------
	@Override
	protected View doWorkOn(OAuthCommand command, User loginUser) throws IOException, OAuthException, URISyntaxException {
		return createAccessToken(command, loginUser);
	}
	
	//------------------------------------------------------------------------
	// OAuth protocol end point implementation
	//------------------------------------------------------------------------
	/**
	 * Hand out an access token if the consumer key and secret are valid and the user authorized 
	 * the requestToken
	 * 
	 * @param command
	 * @param loginUser 
	 * @return
	 * @throws URISyntaxException 
	 * @throws OAuthException 
	 * @throws IOException 
	 */
	private View createAccessToken(OAuthCommand command, User loginUser) throws IOException, OAuthException, URISyntaxException {
		// extract the OAuth parameters from the request
		OAuthMessage requestMessage = this.requestLogic.getOAuthMessage(null);

		// obtain the corresponding token credential
		OAuthEntry entry = getValidatedEntry(requestMessage);
		if (!present(entry.getUserId())) {
			OAuthProblemException e = new OAuthProblemException(OAuth.Problems.OAUTH_PARAMETERS_ABSENT);
			e.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAUTH_HEADER_USER_ID);
			log.error("No username given for accessing the OAuth token.");
		}
		
		if (!present(entry)) {
			throw new OAuthProblemException(OAuth.Problems.TOKEN_REJECTED);
		}

		if (present(entry.getCallbackToken())) {
			// We're using the fixed protocol
			String clientCallbackToken = requestMessage.getParameter(OAuth.OAUTH_VERIFIER);
			if (!entry.getCallbackToken().equals(clientCallbackToken)) {
				getDataStore().disableToken(entry);
				throw new OAuthProblemException(OAuth.Problems.PARAMETER_REJECTED);
			}
		} else if (!entry.isAuthorized()) {
			// Old protocol.  Catch consumers trying to convert a token to one that's not authorized
			getDataStore().disableToken(entry); 
			throw new OAuthProblemException(OAuth.Problems.TOKEN_REJECTED);
		}
		
	    // turn request token into access token
	    OAuthEntry accessEntry = getDataStore().convertToAccessToken(entry);

		command.setResponseString(OAuth.formEncode(OAuth.newList(
                OAuth.OAUTH_TOKEN, accessEntry.getToken(),
                OAuth.OAUTH_TOKEN_SECRET, accessEntry.getTokenSecret(),
                OAUTH_HEADER_USER_ID, entry.getUserId())));
		return Views.OAUTH_RESPONSE;
	}
	
	@Override
	protected String getRequestAction() {
		return OAuthAction.accessToken.name();
	}

}
