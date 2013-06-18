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

package org.bibsonomy.webapp.command.ajax;

import java.util.HashSet;
import java.util.Set;

/**
 * @author dzo
 * @version $Id: EditGoldstandardReferencesCommand.java,v 1.1 2010-05-10 13:39:57 nosebrain Exp $
 */
public class EditGoldstandardReferencesCommand extends AjaxCommand {
	private String hash;
	private Set<String> references;
	
	/**
	 * inits the references set
	 */
	public EditGoldstandardReferencesCommand() {
		this.references = new HashSet<String>();
	}
	
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(Set<String> references) {
		this.references = references;
	}

	/**
	 * @return the references
	 */
	public Set<String> getReferences() {
		return references;
	}
}
