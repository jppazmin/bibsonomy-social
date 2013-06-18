/**
 *
 *  BibSonomy-Rest-Server - The REST-server.
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

package org.bibsonomy.rest.strategy.users;

import java.io.Writer;
import java.util.List;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.User;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.strategy.AbstractGetListStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * shows all users bibsonomy has
 * 
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetUserListStrategy.java,v 1.13 2011-06-09 12:11:22 rja Exp $
 */
public class GetUserListStrategy extends AbstractGetListStrategy<List<User>> {
	
	/**
	 * @param context
	 */
	public GetUserListStrategy(final Context context) {
		super(context);
	}
	
	@Override
	protected void appendLinkPostFix(StringBuilder sb) {
	}

	@Override
	protected StringBuilder getLinkPrefix() {
		return new StringBuilder(this.getUrlRenderer().getApiUrl() + RestProperties.getInstance().getUsersUrl() );
	}

	@Override
	protected List<User> getList() {
		return this.getLogic().getUsers(null, GroupingEntity.ALL, null, null, null, null, null, null, this.getView().getStartValue(), this.getView().getEndValue());
	}

	@Override
	protected void render(Writer writer, List<User> resultList) {
		this.getRenderer().serializeUsers(writer, resultList, getView());
	}

	@Override
	public String getContentType() {
		return "users";
	}
}