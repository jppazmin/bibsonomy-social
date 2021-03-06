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

import org.bibsonomy.model.sync.ConflictResolutionStrategy;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

public class ConflictResolutionStrategyTypeHandlerCallback extends AbstractTypeHandlerCallback {
	@Override
	public void setParameter(final ParameterSetter setter, final Object parameter) throws SQLException {
		if (parameter == null) {
			throw new IllegalArgumentException("given conflict resolution strategy is null");
		} else {
			if (parameter instanceof ConflictResolutionStrategy) {
				setter.setString(((ConflictResolutionStrategy)parameter).getConflictResolutionStrategy());
			} else {
				throw new IllegalArgumentException("given object isn't a instance of ConflictResolutionStartegy");
			}
		}
	}

	@Override
	public Object valueOf(final String str) {
		return ConflictResolutionStrategy.getConflictResolutionStrategyByString(str);
	}

}
