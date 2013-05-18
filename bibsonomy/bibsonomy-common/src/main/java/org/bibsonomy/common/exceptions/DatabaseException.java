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

import static org.bibsonomy.util.ValidationUtils.present;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bibsonomy.common.errors.ErrorMessage;

/**
 * @author dzo
 * @version $Id: DatabaseException.java,v 1.2 2011-04-29 06:36:47 bibsonomy Exp $
 */
public class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = -5703687462706009432L;
	
	
	private final Map<String, List<ErrorMessage>> errorMessages;
	
	/**
	 * initiate map
	 */
	public DatabaseException() {
		this.errorMessages = new HashMap<String, List<ErrorMessage>>();
	}
	
	/**
	 * @param errorMessages
	 */
	public DatabaseException(final Map<String, List<ErrorMessage>> errorMessages) {
		this.errorMessages = new HashMap<String, List<ErrorMessage>>(errorMessages);
	}
	
	/**
	 * @return the errorMessages
	 */
	public Map<String, List<ErrorMessage>> getErrorMessages() {
		return this.errorMessages;
	}

	/**
	 * @param hash the hash of the post
	 * @return the error Messages for the post (represented by its hash)
	 */
	public List<ErrorMessage> getErrorMessages(final String hash) {
		return this.errorMessages.get(hash);
	}
	
	/**
	 * remove all collected ErrorMessages
	 */
	public void clear() {
		this.errorMessages.clear();
	}
	/**
	 * adds an error Message to the list of error Messages for the specified post (hash)
	 * @param hash the hash of the post
	 * @param errorMessage the error Message to add
	 */
	public void addToErrorMessages(final String hash, final ErrorMessage errorMessage) {
		List<ErrorMessage> list = this.errorMessages.get(hash);
		
		if (!present(list)) {
			list = new LinkedList<ErrorMessage>();
			this.errorMessages.put(hash, list);
		}
		
		list.add(errorMessage);
	}
	
	/**
	 * @param hash	the hash of the post
	 * @return true if post has error Messages
	 */
	public boolean hasErrorMessages(final String hash) {
		return present(this.errorMessages.get(hash));
	}
	
	/**
	 * @return true if any post has error Messages
	 */
	public boolean hasErrorMessages() {
		return (!this.errorMessages.isEmpty());
	}
	
	/**
	 * adds all errorMessages of another DatabaseException to this one
	 * @param de
	 */
	public void addErrors(DatabaseException de) {
		for (String hash: de.getErrorMessages().keySet()) {
			for (ErrorMessage errorMessage: de.getErrorMessages(hash))  {
				this.addToErrorMessages(hash, errorMessage);
			}
		}
	}
	
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder("DatabaseException: listing errorMessages...\n");
		if (errorMessages.isEmpty()) {
			//no errorMessages have been collected
			result.append("No errorMessages have been collected.");
			return result.toString();
		}
		for (final String key: errorMessages.keySet()) {
			result.append(key).append(": ").append(errorMessages.get(key).toString()).append("\n");
		}
		return result.toString();
	}
}
