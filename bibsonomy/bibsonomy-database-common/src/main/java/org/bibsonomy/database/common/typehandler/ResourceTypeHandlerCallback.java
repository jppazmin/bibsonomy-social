/**
 *
 *  BibSonomy-Database-Common - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.database.common.typehandler;

import java.sql.SQLException;

import org.bibsonomy.database.common.enums.ConstantID;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Bookmark;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

/**
 * An iBATIS type handler callback for Resources that are mapped to
 * an int in the database. If the Resource cannot be constructed based
 * on the int, then an exception is thrown.
 * 
 * 
 * @author Robert Jäschke
 * @version $Id: ResourceTypeHandlerCallback.java,v 1.2 2011-06-07 11:40:55 rja Exp $
 */
public class ResourceTypeHandlerCallback extends AbstractTypeHandlerCallback {

	@Override
	public void setParameter(final ParameterSetter setter, final Object parameter) throws SQLException {
		if (parameter == null) {
			throw new IllegalArgumentException("given resource is null");		
		}
		if (parameter instanceof Bookmark) {
			setter.setInt(ConstantID.BOOKMARK_CONTENT_TYPE.getId());
		} else if (parameter instanceof BibTex) {
			setter.setInt(ConstantID.BIBTEX_CONTENT_TYPE.getId());
		} else {
			throw new IllegalArgumentException("unknown content type " + parameter.getClass());
		}
	}

	@Override
	public Object valueOf(final String str) {
		try {
			final int value = Integer.parseInt(str);
			if (value == ConstantID.BOOKMARK_CONTENT_TYPE.getId()) {
				return new Bookmark();
			} else if (value == ConstantID.BIBTEX_CONTENT_TYPE.getId()) {
				return new BibTex();
			}
		} catch (NumberFormatException ex) {
			
		}
		throw new IllegalArgumentException("unknown content type " + str);
	}
}