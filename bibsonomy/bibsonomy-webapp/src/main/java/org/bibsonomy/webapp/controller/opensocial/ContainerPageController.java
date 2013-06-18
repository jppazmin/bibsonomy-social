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

import org.apache.shindig.config.ContainerConfig;
import org.bibsonomy.model.User;
import org.bibsonomy.opensocial.security.SecurityTokenUtil;
import org.bibsonomy.util.spring.security.AuthenticationUtils;
import org.bibsonomy.webapp.command.opensocial.OpenSocialCommand;
import org.bibsonomy.webapp.controller.SingleResourceListControllerWithTags;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.Views;

import com.google.inject.Inject;

/**
 * Initial gadget container page for testing the open social interface
 * @author fei
 */
public class ContainerPageController extends SingleResourceListControllerWithTags implements MinimalisticController<OpenSocialCommand> {
	@Inject
	ContainerConfig config;
	
	@SuppressWarnings("deprecation")
	@Override
	public View workOn(OpenSocialCommand command) {
		User loginUser = AuthenticationUtils.getUser();
		//String st;
		try {
			String token = SecurityTokenUtil.getSecurityToken(loginUser, command.getGadgetUrl());
			command.setSecurityToken(token);
		} catch (Exception ex) {
		}
		return Views.GADGETCONTAINER;
	}
	
	@Override
	public OpenSocialCommand instantiateCommand() {
		return new OpenSocialCommand();
	}

}
