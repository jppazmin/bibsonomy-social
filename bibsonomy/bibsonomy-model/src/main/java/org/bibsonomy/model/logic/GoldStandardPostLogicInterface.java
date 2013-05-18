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

package org.bibsonomy.model.logic;

import java.util.Set;


/**
 * @author dzo
 * @version $Id: GoldStandardPostLogicInterface.java,v 1.3 2011-04-29 06:45:02 bibsonomy Exp $
 */
public interface GoldStandardPostLogicInterface extends PostLogicInterface {
	
	/**
	 * adds references to a gold standard resource
	 * 
	 * @param postHash   the hash of the gold standard post
	 * @param references the references to add (interhashes)
	 */
	public void createReferences(final String postHash, final Set<String> references);
	
	/**
	 * deletes references from a gold stanard resource
	 * 
	 * @param postHash	 the hash of the gold standard post
	 * @param references the references to delete (interhashes)
	 */
	public void deleteReferences(final String postHash, final Set<String> references);
}
