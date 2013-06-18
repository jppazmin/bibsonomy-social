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

package org.bibsonomy.webapp.util.spring.factorybeans;

import org.bibsonomy.model.User;
import org.bibsonomy.webapp.util.RequestLogic;
import org.springframework.beans.factory.FactoryBean;

/**
 * fishes the {@link User} out of the request
 *  
 * @see FactoryBean
 * @author Dominik Benz
 * @version $Id: UserFactoryBean.java,v 1.9 2010-12-08 10:11:57 rja Exp $
 */
public class UserFactoryBean implements FactoryBean<User> {
	private RequestLogic requestLogic;
	private User instance;
	
	/**
	 * The logic to acces the HTTP servlet request.
	 * @param requestLogic
	 */
	public void setRequestLogic(final RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	@Override
	public User getObject() throws Exception {
		if (instance == null) {
			instance = this.requestLogic.getLoginUser();
		}
		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		return User.class;
	}
	
	@Override
	public boolean isSingleton() {
		return false;  // TODO: check if singleton is really only singleton in the scope of the factorybean 
	}

}
