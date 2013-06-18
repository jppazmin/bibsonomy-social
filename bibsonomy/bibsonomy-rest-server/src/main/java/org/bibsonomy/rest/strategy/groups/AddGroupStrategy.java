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

import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.Group;
import org.bibsonomy.rest.strategy.AbstractCreateStrategy;
import org.bibsonomy.rest.strategy.Context;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: AddGroupStrategy.java,v 1.12 2010-11-10 11:59:50 nosebrain Exp $
 */
public class AddGroupStrategy extends AbstractCreateStrategy {
	
	/**
	 * @param context
	 */
	public AddGroupStrategy(final Context context) {
		super(context);
	}

	@Override
	protected String create() throws InternServerException {
		final Group group = this.getRenderer().parseGroup(this.doc);
		return this.getLogic().createGroup(group);
	}

	@Override
	protected void render(Writer writer, String groupID) {
		this.getRenderer().serializeGroupId(writer, groupID);		
	}
}