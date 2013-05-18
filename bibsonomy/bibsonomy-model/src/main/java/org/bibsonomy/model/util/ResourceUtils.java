/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model.util;

import java.util.HashMap;
import java.util.Map;

import org.bibsonomy.common.exceptions.UnsupportedResourceTypeException;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;
import org.bibsonomy.model.Resource;

/**
 * Static methods to handle Resources.
 * 
 * @author rja
 * @version $Id: ResourceUtils.java,v 1.16 2011-07-28 14:29:07 rja Exp $
 */
public class ResourceUtils {

	
	private static final Map<String, Class<? extends Resource>> byStringMap = new HashMap<String, Class<? extends Resource>>();
	private static final Map<Class<? extends Resource>, String> toStringMap = new HashMap<Class<? extends Resource>, String>();
	static {
		byStringMap.put("BOOKMARK", Bookmark.class);
		byStringMap.put("BIBTEX", BibTex.class);
		byStringMap.put("ALL", Resource.class);
		for (final Map.Entry<String, Class<? extends Resource>> entry : byStringMap.entrySet()) {
			toStringMap.put(entry.getValue(), entry.getKey());
		}
	}

	/**
	 * @param resourceType
	 * @return resource
	 */
	@Deprecated // please use the ResourceFactory#createResource!!!
	public static Class<? extends Resource> getResource(final String resourceType) {
		if (resourceType == null) throw new UnsupportedResourceTypeException("ResourceType is null");
		Class<? extends Resource> rVal = byStringMap.get(resourceType.trim().toUpperCase());
		if (rVal == null) {
			throw new UnsupportedResourceTypeException();
		}
		return rVal;
	}
	
	/**
	 * Returns an instance of a {@link Resource} with the given type.
	 * 
	 * @param resourceType
	 * @return An instance of a resource with the given type.
	 * 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Deprecated // please use the ResourceFactory!!!
	public static Resource getInstance(final String resourceType) throws InstantiationException, IllegalAccessException {
		return getResource(resourceType).newInstance();
	}

	/**
	 * @param clazz
	 * @return string
	 */
	public static String toString(final Class<? extends Resource> clazz) {
		final String rVal = toStringMap.get(clazz);
		if (rVal == null) {
			throw new UnsupportedResourceTypeException();
		}
		return rVal;
	}
	
	/**
	 * 
	 * @param requiredType
	 * @return list with required resource types.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Resource>[] getResourceTypesByClass(Class<? extends Resource> requiredType) {
		if (Resource.class.equals(requiredType)) {
			return new Class[]{Bookmark.class, BibTex.class};
		}
		return new Class[]{requiredType};
	}
}
