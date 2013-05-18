/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
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

package org.bibsonomy.common.errors;


/**
 * Use this ErrorMessage when a post is updated such that it is identical to another already existing posts
 * 
 * @author sdo
 * @version $Id: IdenticalHashErrorMessage.java,v 1.7 2011-04-29 06:36:57 bibsonomy Exp $
 */
public class IdenticalHashErrorMessage extends ErrorMessage {

	/**
	 * @param resourceClassName
	 * @param intraHash
	 */
	public IdenticalHashErrorMessage(String resourceClassName, String intraHash) {
		super("Could not uptdate " + resourceClassName + ": This " + resourceClassName +
				" already exists in your collection (intrahash: " + intraHash + ")", "database.exception.duplicate");
	}
	
}
