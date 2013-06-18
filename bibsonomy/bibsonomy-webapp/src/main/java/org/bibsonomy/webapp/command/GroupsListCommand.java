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
import org.bibsonomy.model.Group;


/**
 * Bean for list of groups.
 * 
 * @author Folke Eisterlehner
 */
public class GroupsListCommand extends BaseCommand {
	private List<Group> list;
	
	// dirty hack: alphabet for direct access in group list
	private String strAlphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZα"; 
	private char[] alphabet = strAlphabet.toCharArray();

	/**
	 * @return the alphabet
	 */
	public String getStrAlphabet() {
		return this.strAlphabet;
	}

	/**
	 * @return the alphabet
	 */
	public char[] getAlphabet() {
		return this.alphabet;
	}
	
	/**
	 * @return the sublistlist on the current page
	 */
	public List<Group> getList() {
		return this.list;
	}
	/**
	 * @param list the sublistlist on the current page
	 */
	public void setList(List<Group> list) {
		this.list = list;
	}	
}