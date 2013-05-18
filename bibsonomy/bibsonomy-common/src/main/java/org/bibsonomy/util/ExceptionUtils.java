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

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.bibsonomy.common.exceptions.QueryTimeoutException;

/**
 * Convenience methods to throw exceptions.
 *
 * @author Christian Schenk
 * @version $Id: ExceptionUtils.java,v 1.16 2011-04-29 06:36:50 bibsonomy Exp $
 */
public class ExceptionUtils {

	/**
	 * Like the name suggests this method logs an error and throws a
	 * RuntimeException attached with the initial exception.
	 * @param log the logger instance to use
	 * @param ex the exception to log an rethrow wrapped
	 * @param error message of the new RuntimeException
	 * @throws RuntimeException the resulting exception
	 */
	public static void logErrorAndThrowRuntimeException(final Log log, final Exception ex, final String error) throws RuntimeException {
		log.error(error + " - throwing RuntimeException" + ((ex != null) ? ("\n" + ex.toString()) : ""), ex );
		/*
		 * Inserted to get more information (e.g., on "java.sql.SQLException: Unknown error" messages)
		 * FIXME: it's probably not the best place to handle SQL stuff
		 */
		if (ex != null && ex.getCause() != null && ex.getCause().getClass().equals(SQLException.class)) {
			final SQLException sqlException = ((SQLException) ex);
			log.error("SQL error code: " + sqlException.getErrorCode() + ", SQL state: " + sqlException.getSQLState());
		}
		throw new RuntimeException(error, ex);
	}
	
	/**
	 * throw query timeout exception
	 * 
	 * @param log
	 * @param ex
	 * @param query
	 * @throws QueryTimeoutException
	 */
	public static void logErrorAndThrowQueryTimeoutException(final Log log, final Exception ex, final String query) throws QueryTimeoutException {
		log.error("Query timeout for query: " + query);
		throw new QueryTimeoutException(ex, query);
	} 
}