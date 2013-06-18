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
import org.bibsonomy.model.UserSettings;
import org.springframework.beans.factory.FactoryBean;

/**
 * accesses the user settings of the logged in user
 *  
 * @see FactoryBean
 * @author Jens Illig
 */
public class UserSettingsFactoryBean implements FactoryBean<UserSettings> {
	private UserSettings instance;
	private User user;
	
	@Override
	public UserSettings getObject() throws Exception {
		if (instance == null) {
			if (this.user.getSettings() == null) {
				instance = new UserSettings();
			} else {
				instance = this.user.getSettings();
			}
		}
		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		return UserSettings.class;
	}

	/**
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean isSingleton() {
		return false;  // TODO: check if singleton is really only singleton in the scope of the factorybean 
	}

}
