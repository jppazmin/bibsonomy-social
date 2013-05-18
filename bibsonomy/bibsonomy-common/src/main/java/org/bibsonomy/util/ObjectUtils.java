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

package org.bibsonomy.util;

import static org.bibsonomy.util.ValidationUtils.present;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dzo
 * @version $Id: ObjectUtils.java,v 1.3 2011-04-29 06:36:50 bibsonomy Exp $
 */
public class ObjectUtils {
	private static final Log log = LogFactory.getLog(ObjectUtils.class);

	/**
	 * copies all property values (all properties that have a getter and setter) from the source to the target
	 * 
	 * @param <T> 
	 * @param source the source object
	 * @param target the target object
	 */
	public static <T> void copyPropertyValues(final T source, final T target) {
		try {
			final BeanInfo biSource = Introspector.getBeanInfo(source.getClass());
			final BeanInfo biTarget = Introspector.getBeanInfo(target.getClass());
			
			/* 
			 * source can be a subtype of target
			 * to ensure that a setter of the subtype isn't called choose the array
			 * that contains less properties
			 */
			final PropertyDescriptor[] sourceProperties = biSource.getPropertyDescriptors();
			final PropertyDescriptor[] targetProperties = biTarget.getPropertyDescriptors();
			final PropertyDescriptor[] copyProperties = sourceProperties.length > targetProperties.length ? targetProperties : sourceProperties;
			
			/*
			 * loop through all properties
			 */
			for (final PropertyDescriptor d : copyProperties) {			
				final Method getter = d.getReadMethod();
				final Method setter = d.getWriteMethod();

				if (present(getter) && present(setter)) {					
					// get the value from the source
					final Object value = getter.invoke(source, (Object[])null);
					// and set in in the target
					setter.invoke(target, value);
				}
			}
		} catch (Exception ex) {
			log.error("error while copying property values from an object " + source.getClass() + " to an object " + target.getClass(), ex);
		}
	}

}
