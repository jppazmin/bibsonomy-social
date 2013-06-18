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

package org.bibsonomy.webapp.config;

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * 
 * @author folke
 * @version $Id: AuthConfig.java,v 1.8 2011-03-16 13:26:30 nosebrain Exp $
 */
public class AuthConfig implements InitializingBean {
	
	private List<AuthMethod> authOrder;
	private String[] authOrderString;
	
	/**
	 * check whether given authentication method is enabled
	 * @param methodName name of the authentication method in question
	 * @return true if given authentication method is configured
	 */
	public boolean containsAuthMethod(String methodName) {
		AuthMethod authMethod = AuthMethod.getAuthMethodByName(methodName);
		return this.containsAuthMethod(authMethod);
	}
	
	/**
	 * check whether given authentication method is enabled
	 * @param method the authentication method in question
	 * @return true if given authentication method is configured
	 */
	public boolean containsAuthMethod(AuthMethod method) {
		return this.authOrder.contains(method);
	}	
	
	/**
	 * @return the authOrder
	 */
	public List<AuthMethod> getAuthOrder() {
		return this.authOrder;
	}

	/**
	 * @param authOrderString the authOrderString to set
	 */
	public void setAuthOrderString(String[] authOrderString) {
		this.authOrderString = authOrderString;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!present(this.authOrderString)) throw new IllegalArgumentException();
		
		// parse auth methods
		this.authOrder = new LinkedList<AuthMethod>();
		for (final String authMethodString : this.authOrderString) {
			this.authOrder.add(AuthMethod.getAuthMethodByName(authMethodString));
		}
	}
}
