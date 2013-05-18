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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author dzo
 * @version $Id: FieldLengthErrorMessage.java,v 1.6 2011-04-29 06:36:58 bibsonomy Exp $
 */
public class FieldLengthErrorMessage extends ErrorMessage {
	
	private final Map<String, Integer> fields;
	
	/**
	 * inits the map
	 */
	public FieldLengthErrorMessage() {
		super("At least one field exceeds text size limit", "database.exception.fieldlength");
		this.fields = new HashMap<String, Integer>();
	}

	/**
	 * adds a field with its maximum length to the error message
	 * 
	 * @param field
	 * @param maxLength
	 */
	public void addToFields(final String field, final int maxLength) {
		this.fields.put(field, maxLength);
	}
	
	/**
	 * @param field
	 * @return the max length of the field
	 */
	public int getMaxLengthForField(final String field) {
		return this.fields.get(field);
	}
	
	/**
	 * @return an iterator of all fields
	 */
	public Iterator<String> iteratorFields() {
		return this.fields.keySet().iterator();
	}
	
	/**
	 * @return <code>true</code> iff one or more field(s) is/are to long
	 */
	public boolean hasErrors() {
		return !this.fields.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.common.errors.ErrorMessage#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder("The following fields are to long (max. length):'");
		
		/*
		 * loop through all fields
		 */
		
		for (final Map.Entry<String, Integer> entry : this.fields.entrySet()) {
			builder.append("\n");
			builder.append(entry.getKey());
			builder.append(" (");
			builder.append(entry.getValue());
			builder.append(")");
		}
		
		return  builder.toString();
	}
}
