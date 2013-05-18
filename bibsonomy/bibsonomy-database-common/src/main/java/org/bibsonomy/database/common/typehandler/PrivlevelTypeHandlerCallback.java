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

import org.bibsonomy.common.enums.Privlevel;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

/**
 * An iBATIS type handler callback for {@link Privlevel}es that are mapped to
 * Strings in the database. If the {@link Privlevel} cannot be constructed based
 * on the String, then the Privlevel will be set to <code>MEMBERS</code>.<br/>
 * 
 * Almost copied from <a
 * href="http://opensource.atlassian.com/confluence/oss/display/IBATIS/Type+Handler+Callbacks">Atlassian -
 * Type Handler Callbacks</a>
 * 
 * @author Ken Weiner
 * @author Christian Schenk
 * @author Robert Jaeschke
 * @version $Id: PrivlevelTypeHandlerCallback.java,v 1.1 2010-05-28 13:49:23 nosebrain Exp $
 */
public class PrivlevelTypeHandlerCallback extends AbstractTypeHandlerCallback {

	@Override
	public void setParameter(final ParameterSetter setter, final Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setInt(Privlevel.MEMBERS.getPrivlevel());
		} else {
			final Privlevel privlevel = (Privlevel) parameter;
			setter.setInt(privlevel.getPrivlevel());
		}
	}

	@Override
	public Object valueOf(final String str) {
		try {
			return Privlevel.getPrivlevel(Integer.parseInt(str));
		} catch (NumberFormatException ex) {
			return Privlevel.MEMBERS;
		}
	}
}