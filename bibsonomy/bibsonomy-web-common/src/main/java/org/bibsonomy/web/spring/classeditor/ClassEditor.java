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

package org.bibsonomy.web.spring.classeditor;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.factories.ResourceFactory;

/**
 * extends the functionality of the {@link org.springframework.beans.propertyeditors.ClassEditor}
 * by allowing to use simple string representation (found in the
 * {@link ResourceFactory#RESOURCE_CLASSES_BY_NAME} map) for {@link Resource} classes.
 * 
 * e.g. using the paramater value 'bookmark' sets a {@link Bookmark} class
 * 
 * @author dzo
 * @version $Id: ClassEditor.java,v 1.3 2011-04-11 13:11:02 nosebrain Exp $
 */
@Deprecated // in favour of converters (spring 3.0)
public class ClassEditor extends org.springframework.beans.propertyeditors.ClassEditor {
	
	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (present(text)) {
			final Class<? extends Resource> clazz = ResourceFactory.getResourceClass(text);
			
			if (present(clazz)) {
				this.setValue(clazz);
				return; // got value; nothing to do
			}
		}
		
		super.setAsText(text);
	}
	
	@Override
	public String getAsText() {
		final Class<?> clazz = (Class<?>) getValue();
		if (present(clazz) && clazz.isAssignableFrom(Resource.class)) {
			@SuppressWarnings("unchecked") // checked; have a look at the if statement
			final Class<? extends Resource> resourceClass = (Class<? extends Resource>) clazz;
			return ResourceFactory.getResourceName(resourceClass);
		}
		
		return super.getAsText();
	}
}
