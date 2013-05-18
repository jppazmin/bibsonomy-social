/**
 *
 *  BibSonomy-Web-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.web.spring.converter;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.model.Resource;
import org.bibsonomy.model.factories.ResourceFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;

/**
 * @author dzo
 * @version $Id: StringToClassConverter.java,v 1.1 2011-04-11 13:08:57 nosebrain Exp $
 */
public class StringToClassConverter implements Converter<String, Class<?>> {

	private ClassLoader loader = ClassUtils.getDefaultClassLoader();
	
	@Override
	public Class<?> convert(String text) {
		if (present(text)) {
			text = text.trim();
			final Class<? extends Resource> clazz = ResourceFactory.getResourceClass(text);
			
			if (present(clazz)) {
				return clazz;
			}
			
			return ClassUtils.resolveClassName(text, this.loader);
		}
		return null;
	}

	/**
	 * @param loader the loader to set
	 */
	public void setLoader(final ClassLoader loader) {
		this.loader = loader;
	}
}
