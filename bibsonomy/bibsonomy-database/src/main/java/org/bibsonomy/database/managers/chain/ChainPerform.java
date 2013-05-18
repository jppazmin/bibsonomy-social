/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.database.managers.chain;

import org.bibsonomy.database.common.DBSession;

/**
 * This interface encapsulates the getter for a L object
 *
 * @param <L> Type of the fetched result entities
 * @param <P> Type of the param object
 * 
 * @author Christian Schenk
 * @version $Id: ChainPerform.java,v 1.14 2011-07-18 14:12:00 nosebrain Exp $
 */
public interface ChainPerform<P, L> {

	/**
	 * Walks through the chain until a ChainElement is found that can handle the
	 * request.
	 * 
	 * @param param describes the requirements of the request
	 * @param session a database session
	 * @return the list of entities, which is returned by the fitting chainelement 
	 */
	public L perform(P param, DBSession session);
}