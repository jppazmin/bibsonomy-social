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

package org.bibsonomy.rest.strategy.groups;

import java.io.Writer;
import java.util.List;

import org.bibsonomy.model.Group;
import org.bibsonomy.rest.RestProperties;
import org.bibsonomy.rest.strategy.AbstractGetListStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: GetListOfGroupsStrategy.java,v 1.12 2011-06-09 12:11:22 rja Exp $
 */
public class GetListOfGroupsStrategy extends AbstractGetListStrategy<List<Group>> {
	
	/**
	 * @param context
	 */
	public GetListOfGroupsStrategy(final Context context) {
		super(context);
	}

	@Override
	public String getContentType() {
		return "groups";
	}

	@Override
	protected void appendLinkPostFix(StringBuilder sb) {
	}

	@Override
	protected StringBuilder getLinkPrefix() {
		return new StringBuilder(this.getUrlRenderer().getApiUrl() + RestProperties.getInstance().getGroupsUrl());
	}

	@Override
	protected List<Group> getList() {
		return this.getLogic().getGroups(getView().getStartValue(), getView().getEndValue());
	}

	@Override
	protected void render(Writer writer, List<Group> resultList) {
		this.getRenderer().serializeGroups(writer, resultList, getView());
	}
}