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

package org.bibsonomy.rest.strategy.concepts;

import java.io.ByteArrayOutputStream;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.exceptions.InternServerException;
import org.bibsonomy.model.Tag;
import org.bibsonomy.rest.ViewModel;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.strategy.Context;
import org.bibsonomy.rest.strategy.Strategy;

/**
 * Handles a global concept details request
 * 
 * @author Stefan Stützer
 * @version $Id: GetConceptDetailsStrategy.java,v 1.6 2011-04-06 12:10:05 nosebrain Exp $
 */
public class GetConceptDetailsStrategy extends Strategy {

	private final String conceptName;
	
	/**
	 * @param context
	 * @param conceptName
	 */
	public GetConceptDetailsStrategy(final Context context, final String conceptName) {
		super(context);
		this.conceptName = conceptName;
	}

	@Override
	public void perform(final ByteArrayOutputStream outStream) throws InternServerException, NoSuchResourceException {
		final Tag concept = this.getLogic().getConceptDetails(conceptName, GroupingEntity.ALL, null);
		if (concept == null) {
			throw new NoSuchResourceException("The requested concept '" + conceptName + "' does not exist.");
		}
		this.getRenderer().serializeTag(writer, concept, new ViewModel());
	}
	
	@Override
	protected String getContentType() {
		return "tag";
	}
}