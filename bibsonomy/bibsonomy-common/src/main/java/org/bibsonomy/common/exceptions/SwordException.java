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

package org.bibsonomy.common.exceptions;


/**
 * @author Manuel Bork <manuel.bork@uni-kassel.de>
 * @version $Id: ResourceNotFoundException.java,v 1.2 2007-10-30 17:37:35 jillig
 *          Exp $
 */
public class SwordException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String message;
	private final String localizedMessage;

	/**
	 * Constructs a new sword exception with a specified message
	 * 
	 * @param messageId
	 *            the message to be shown in gui
	 */
	public SwordException(final String messageId) {
		super(messageId);
		message = messageId;
		localizedMessage = messageId;
	}
	
	@Override
	public String getMessage() {
		return message;
		
	}
	
	@Override
	public String getLocalizedMessage() {
		return localizedMessage;
	}
	
	
	
}