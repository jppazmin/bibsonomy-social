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

import static org.bibsonomy.util.ValidationUtils.present;

/**
 * @author sdo
 * @version $Id: ErrorMessage.java,v 1.12 2011-04-29 06:36:58 bibsonomy Exp $
 */
public class ErrorMessage {

	private String defaultMessage;
	private String errorCode;
	private String[] parameters;
	
	/**
	 * @param defaultMessage is like the exception message
	 * @param errorCode 	is a key to the corresponding localized String in the message_properties files
	 */
	public ErrorMessage(final String defaultMessage, final String errorCode) {
		this(defaultMessage, errorCode, null);
	}
	
	/**
	 * @param defaultMessage is like the exception message
	 * @param errorCode 	is a key to the corresponding localized String in the message_properties files
	 * @param parameters are some Strings for the localized message
	 */
	public ErrorMessage(final String defaultMessage, final String errorCode, final String[] parameters) {
		this.defaultMessage = defaultMessage;
		this.errorCode = errorCode;
		if (present(parameters)) {
			this.parameters = parameters;
		} else {
			this.parameters = new String[]{};
		}
	}

	/**
	 * @return the defaultMessage
	 */
	public String getDefaultMessage() {
		return this.defaultMessage;
	}

	/**
	 * @param defaultMessage the errorMessage to set
	 */
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
	
	/**
	 * @return the localizedMessageKey
	 */
	public String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * @param errorCode the localizedMessageKey to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the parameters
	 */
	public String[] getParameters() {
		return this.parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	
	
	@Override
	public String toString() {
		return defaultMessage;
	}

}
