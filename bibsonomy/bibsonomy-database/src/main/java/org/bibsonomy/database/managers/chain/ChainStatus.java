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


/**
 * Only interesting for testcases. Usage pattern:
 * <ul>
 * <li>add a new instance of this class as a parameter to the perform method of
 * a chain element</li>
 * <li>once the perform method returns, check {@link #getChainElement()} for
 * the element from the chain that handled the request</li>
 * </ul>
 * 
 * If we don't like this class someday <em>the</em> chain could return the
 * appropriate element instead of the actual result, i.e. not a List of
 * something (e.g. Post<? extends Resource>) but an instance that extends
 * {@link ChainElement}. This way the caller would know the callee (by checking
 * it's instance) and could call the handle method himself. The latter wouldn't
 * be too bad either and this class and the extra method
 * {@link ChainElement#perform(Object, org.bibsonomy.database.common.DBSession, ChainStatus)}
 * would be obsolete.<br/>
 * 
 * Another way would be an aspect (e.g. with AspectJ) with a pointcut for every
 * call to
 * {@link ChainElement#perform(Object, org.bibsonomy.database.common.DBSession)}
 * that memorizes the class which executes its
 * {@link ChainElement#handle(Object, org.bibsonomy.database.common.DBSession)}
 * method. After that one could <em>ask</em> the aspect for the result. This
 * would be very clean because we wouldn't have to change the <em>real</em>
 * code: the aspect for the tests would do it.
 * 
 * @author Christian Schenk
 * @version $Id: ChainStatus.java,v 1.3 2011-07-18 14:12:00 nosebrain Exp $
 */
public class ChainStatus {

	private ChainElement<?, ?> chainElement;

	/**
	 * Constructor
	 */
	public ChainStatus() {
		this.chainElement = null;
	}

	/**
	 * Returns the chain element that <em>handled</em> the request
	 * 
	 * @return chain element
	 */
	public ChainElement<?, ?> getChainElement() {
		return this.chainElement;
	}

	/**
	 * @param chainElement
	 */
	public void setChainElement(ChainElement<?, ?> chainElement) {
		this.chainElement = chainElement;
	}
}