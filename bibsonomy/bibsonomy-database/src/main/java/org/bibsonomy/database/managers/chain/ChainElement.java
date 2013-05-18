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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.managers.GeneralDatabaseManager;
import org.bibsonomy.database.managers.GroupDatabaseManager;

/**
 * Represents one element in the chain of responsibility.
 * 
 * @param <L> Type of the fetched result entities
 * @param <P> Type of the param object
 * 
 * @author Miranda Grahl
 * @author Jens Illig
 * @author Christian Schenk
 * @version $Id: ChainElement.java,v 1.31 2011-07-18 14:12:00 nosebrain Exp $
 */
public abstract class ChainElement<L, P> implements ChainPerform<P, L> {
	protected static final Log log = LogFactory.getLog(ChainElement.class);
	
	protected final GeneralDatabaseManager generalDb;
	
	protected final GroupDatabaseManager groupDb;
	
	/** The next element of the chain */
	protected ChainElement<L, P> next;

	/**
	 * Constructor
	 */
	public ChainElement() {
		this.generalDb = GeneralDatabaseManager.getInstance();
		this.groupDb = GroupDatabaseManager.getInstance();
		this.next = null;
	}

	/**
	 * Sets the next element in the chain.
	 * 
	 * @param nextElement
	 *            the next element following this element
	 */
	public final void setNext(final ChainElement<L, P> nextElement) {
		this.next = nextElement;
	}

	@Override
	public final L perform(final P param, final DBSession session) {
		return this.perform(param, session, null);
	}

	/**
	 * @param param
	 * @param session
	 * @param chainStatus
	 * @return list of L's
	 * @see #perform(Object, DBSession)
	 * @see ChainStatus
	 * 
	 * XXX: This method is only interesting for unit testing the chain, i.e. if
	 * you want to know which element executed its <code>handle</code> method.
	 */
	public final L perform(final P param, final DBSession session, final ChainStatus chainStatus) {
		if (this.canHandle(param)) {
			if (chainStatus != null) {
				chainStatus.setChainElement(this);
			}
			log.debug("Handling Chain element: " + this.getClass().getSimpleName());
			return this.handle(param, session);
		}
		if (this.next != null) {
			return this.next.perform(param, session, chainStatus);
		}
		throw new RuntimeException("Can't handle request for param object: " + param.toString());
	}

	/**
	 * Handles the request.
	 */
	protected abstract L handle(P param, DBSession session);

	/**
	 * Returns true if the request can be handled by the current chain element,
	 * otherwise false.
	 */
	protected abstract boolean canHandle(P param);
}