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

import java.util.List;

import org.bibsonomy.model.Tag;

/**
 * Bean for tag relations
 * 
 * @author Dominik Benz
 * @version $Id: ConceptsCommand.java,v 1.5 2010-04-28 15:33:23 nosebrain Exp $
 */
public class ConceptsCommand extends ListCommand<Tag> {
	/**
	 * a list of concepts
	 */
	private int numConcepts = 0;

	/**
	 * constructor used by the UserRelationCommand
	 * @param parentCommand
	 */
	public ConceptsCommand(final ContextCommand parentCommand) {
		super(parentCommand);
	}
	
	/**
	 * default constructor
	 */
	public ConceptsCommand() {
		super(new BaseCommand());
	}

	/**
	 * @return the numConcepts
	 */
	public int getNumConcepts() {
		return this.numConcepts;
	}

	/**
	 * @param numConcepts the numConcepts to set
	 */
	public void setNumConcepts(int numConcepts) {
		this.numConcepts = numConcepts;
	}

	/**
	 * @return the list of concepts
	 */
	public List<Tag> getConceptList() {
		return this.getList();
	}

	/**
	 * @param concepts the list of concepts to set
	 */
	public void setConceptList(final List<Tag> concepts) {
		this.setList(concepts);
	}
	
}
