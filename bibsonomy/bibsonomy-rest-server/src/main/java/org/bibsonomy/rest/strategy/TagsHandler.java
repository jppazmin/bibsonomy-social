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

package org.bibsonomy.rest.strategy;

import java.util.StringTokenizer;

import org.bibsonomy.rest.enums.HttpMethod;
import org.bibsonomy.rest.exceptions.NoSuchResourceException;
import org.bibsonomy.rest.strategy.tags.GetListOfTagsStrategy;
import org.bibsonomy.rest.strategy.tags.GetTagDetailsStrategy;

/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: TagsHandler.java,v 1.4 2010-07-14 08:31:43 nosebrain Exp $
 */
public class TagsHandler implements ContextHandler {

	@Override
	public Strategy createStrategy(final Context context, final StringTokenizer urlTokens, final HttpMethod httpMethod) {
		final int numTokensLeft = urlTokens.countTokens();

		switch (numTokensLeft) {
		case 0:
			// /tags
			if (HttpMethod.GET == httpMethod) {
				return new GetListOfTagsStrategy(context);
			}
			break;
		case 1:
			// /tags/[tag]
			if (HttpMethod.GET == httpMethod) {
				return new GetTagDetailsStrategy(context, urlTokens.nextToken());
			}
			break;
		}
		throw new NoSuchResourceException("cannot process url (no strategy available) - please check url syntax ");
	}
}