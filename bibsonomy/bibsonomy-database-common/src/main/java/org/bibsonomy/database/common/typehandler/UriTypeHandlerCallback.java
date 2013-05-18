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

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

/**
 * An iBATIS type handler callback for {@link URI}s that are mapped to Strings
 * in the database. If a URI cannot be constructed based on the String, then the
 * URI will be set to <code>null</code>.<br/>
 * 
 * Almost copied from <a
 * href="http://opensource.atlassian.com/confluence/oss/display/IBATIS/Type+Handler+Callbacks">Atlassian -
 * Type Handler Callbacks</a>
 * 
 * @author Robert Jäschke
 * @version $Id: UriTypeHandlerCallback.java,v 1.1 2011-06-07 11:53:50 rja Exp $
 */
public class UriTypeHandlerCallback extends AbstractTypeHandlerCallback {
	private static final Log log = LogFactory.getLog(UriTypeHandlerCallback.class);

	@Override
	public void setParameter(final ParameterSetter setter, final Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setNull(Types.VARCHAR);
			setter.setString("");
		} else {
			final URI uri = (URI) parameter;
			setter.setString(uri.toString());
		}
	}

	@Override
	public Object valueOf(final String str) {
		try {
			return new URI(str);
		} catch (final URISyntaxException ex) {
			log.warn("'" + str + "' is not a valid URI");
			return null;
		}
	}
}