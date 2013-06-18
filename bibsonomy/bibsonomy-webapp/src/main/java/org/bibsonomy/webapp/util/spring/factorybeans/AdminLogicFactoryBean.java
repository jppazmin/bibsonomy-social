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

import org.bibsonomy.common.enums.Role;
import org.bibsonomy.database.DBLogicUserInterfaceFactory;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.springframework.beans.factory.FactoryBean;

/**
 * Lets {@link DBLogicUserInterfaceFactory} appear as a FactoryBean, which itself
 * is customizable in the spring context.
 * 
 * This bean returns an implementation of the LogicInterface which has admin
 * access enabled. This is done by giving it a user which has the Role "admin".
 *  
 * @see FactoryBean
 * @see Role
 * @author rja
 * @version $Id: AdminLogicFactoryBean.java,v 1.5 2010-12-08 10:11:57 rja Exp $
 */
public class AdminLogicFactoryBean extends DBLogicUserInterfaceFactory implements FactoryBean<LogicInterface> {

	private final User user;
	private LogicInterface instance = null;
	
	/**
	 * Creates a new instance of the AdminLogicFactoryBean.
	 * 
	 */
	public AdminLogicFactoryBean() {
		this.user = new User();
		user.setRole(Role.ADMIN);
	}
	
	@Override
	public LogicInterface getObject() throws Exception {
		if (instance == null) {
			instance = this.getLogicAccess(user.getName(), "");
		}
		return instance;
	}
	
	@Override
	protected User getLoggedInUser(String loginName, String password) {
		// return the admin user
		return user;
	}

	@Override
	public Class<?> getObjectType() {
		return LogicInterface.class;
	}

	@Override
	public boolean isSingleton() {
		return false;  // TODO: check if singleton is really only singleton in the scope of the factorybean 
	}
	
	/** Set the name the admin user will have.
	 * 
	 * @param adminUserName
	 */
	public void setAdminUserName(final String adminUserName) {
		this.user.setName(adminUserName);
	}
	
	/** Get the name of the admin user.
	 * 
	 * @return The name of the admin user.
	 */
	public String getAdminUserName() {
		return this.user.getName();
	}
	
	
}
