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
 * Use this message if something went wrong with a SystemTag
 * 
 * @author sdo
 * @version $Id: SystemTagErrorMessage.java,v 1.7 2011-04-29 06:36:58 bibsonomy Exp $
 */
@Deprecated // TODO: unused remove
public class SystemTagErrorMessage extends ErrorMessage{

	/**
	 * @param errorMessage
	 * @param localizedMessageKey
	 * @param parameters
	 */
	public SystemTagErrorMessage(String errorMessage, String localizedMessageKey, String[] parameters) {
		super(errorMessage, localizedMessageKey, parameters);
	}
}
