/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.validation;

import static org.bibsonomy.util.ValidationUtils.present;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.errors.FieldLengthErrorMessage;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.database.util.DatabaseSchemaInformation;

/**
 * @author dzo
 * @version $Id: DatabaseModelValidator.java,v 1.5 2011-06-16 13:55:02 nosebrain Exp $
 * @param <T> the model to validate
 */
public class DatabaseModelValidator<T> {
	private static final Log log = LogFactory.getLog(DatabaseModelValidator.class);
	
	/**
	 * checks if the string attributes of the model respect the field lengths of
	 * the database
	 * 
	 * @param model 	the model to validate
	 * @param id    	the id of the model (used for the errormessage)
	 * @param session	the session
	 */
	public void validateFieldLength(final T model, final String id, final DBSession session) {
		final Class<? extends Object> clazz = model.getClass();
		final FieldLengthErrorMessage fieldLengthError = new FieldLengthErrorMessage();
		try {
			final BeanInfo bi = Introspector.getBeanInfo(clazz);
			
			/*
			 * loop through all properties
			 * if there are any performance issues, their cause might be here
			 */
			for (final PropertyDescriptor d : bi.getPropertyDescriptors()) {			
				final Method getter = d.getReadMethod();

				if (present(getter)) {					
					final Object value = getter.invoke(model, (Object[])null);

					/*
					 * check max length
					 */
					if (value instanceof String) {
						final String stringValue = (String) value;

						final int length = stringValue.length();
						final String propertyName = d.getName();
						final int maxLength = DatabaseSchemaInformation.getInstance().getMaxColumnLengthForProperty(clazz, propertyName);

						if ((maxLength > 0) && (length > maxLength)) {
							fieldLengthError.addToFields(propertyName, maxLength);
						}
					}
				}
			}
			
			if (fieldLengthError.hasErrors()) {
				session.addError(id, fieldLengthError);
				log.warn("Added fieldlengthError");
			} 
		} catch (final Exception ex) {
			log.error("could not introspect object of class 'user'", ex);
		}
	}
}
