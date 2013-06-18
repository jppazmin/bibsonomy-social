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

package org.bibsonomy.webapp.command;

/**
 * Bean for Concept Sites
 * 
 * @author Michael Wagner
 * @version $Id: ConceptResourceViewCommand.java,v 1.3 2010-04-28 15:33:23 nosebrain Exp $
 */
public class ConceptResourceViewCommand extends TagResourceViewCommand {
	
	/** the user whose resources are requested */
	// TODO: duplicate field @see ResourceViewCommand
	private String requestedUser = "";
	
	/** the group which resources are requested */
	private String requestedGroup = "";

	/** bean for concepts */
	private ConceptsCommand concepts = new ConceptsCommand();
	
	/**
	 * @return requestedUser the name of the user whose resources are requested
	 */
	@Override
	public String getRequestedUser() {
		return this.requestedUser;
	}

	/**
	 * @param requestedUser the name of the user whose resources are requested
	 */
	@Override
	public void setRequestedUser(String requestedUser) {
		this.requestedUser = requestedUser;
	}
	
	/**	
	 * @return the name of the group that resources are requested
	 */
	public String getRequestedGroup() {
		return this.requestedGroup;
	}

	/**
	 * @param requestedGroup the group
	 */
	public void setRequestedGroup(String requestedGroup) {
		this.requestedGroup = requestedGroup;
	}

	/**
	 * @return the concepts
	 */
	public ConceptsCommand getConcepts() {
		return this.concepts;
	}

	/**
	 * @param concepts
	 */
	public void setConcepts(ConceptsCommand concepts) {
		this.concepts = concepts;
	}	
}