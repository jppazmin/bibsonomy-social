/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model;

import org.bibsonomy.common.enums.Role;

/**
 * This class defines a user. An unknown user has an empty (<code>null</code>) name.
 * 
 * @version $Id: EvaluatorUser.java,v 1.6 2011-04-29 06:45:05 bibsonomy Exp $
 */
public class EvaluatorUser extends User {
	private static final long serialVersionUID = 222500173040042092L;

	/**
	 * The (nick-)name of this user. Is <code>null</code> if the user is not logged in (unknown). 
	 */
	private String evaluator;
	
	private String evalDate;

	/**
	 * Constructor
	 */
	public EvaluatorUser() {
		this(null);
	}
	
	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public EvaluatorUser(final String name) {
		super.setName(name); 
		super.setBasket(new Basket());
		super.setSettings(new UserSettings());
		super.setRole(Role.ADMIN); // TODO: check, if this has any bad implications!
	}

	/**
	 * @return the evaluator
	 */
	public String getEvaluator() {
		return this.evaluator;
	}

	/**
	 * @param evaluator the evaluator to set
	 */
	public void setEvaluator(String evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * @return the evalDate
	 */
	public String getEvalDate() {
		return this.evalDate;
	}

	/**
	 * @param evalDate the evalDate to set
	 */
	public void setEvalDate(String evalDate) {
		this.evalDate = evalDate;
	}
}