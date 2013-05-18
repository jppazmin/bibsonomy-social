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

package org.bibsonomy.database.common;


import java.util.List;
import java.util.Map;

import org.bibsonomy.common.errors.ErrorMessage;

/**
 * This interface represents a session for the database. A session normally
 * corresponds to a database connection.
 * 
 * @author Jens Illig
 * @author Christian Schenk
 * @version $Id: DBSession.java,v 1.5 2011-03-14 13:06:17 wla Exp $
 */
public interface DBSession {

	/**
	 * Starts a virtual transaction (a real one if no real transaction has been
	 * started yet). At least endTransaction (probably also commitTransaction
	 * before) must be called hereafter.
	 */
	public void beginTransaction();

	/**
	 * Marks the current transaction as having been sucessfully completed.
	 * However, the real commit may not be called before endTransaction has been
	 * called.
	 */
	public void commitTransaction();

	/**
	 * If this is called before the current transaction has been committed, the
	 * transaction is marked as failed. This causes the transaction to abort.<br/>
	 * 
	 * This should always be called after each transaction, that has begun with
	 * beginTransaction, sometimes with a preceeding call to commitTransaction,
	 * sometimes (in case of an exception) without.
	 */
	public void endTransaction();

	/**
	 * MUST be called to release the db-connection
	 */
	public void close();

	/**
	 * adds a error message to the provided key
	 * 
	 * @param key
	 * @param errorMessage
	 */
	public void addError(String key, ErrorMessage errorMessage);

	/**
	 * 
	 * @param query
	 * @param param
	 * @return the result of the query
	 */
	public Object queryForObject(final String query, final Object param);

	/**
	 * queries for an object in the database, the result will be stored in the
	 * provided store object
	 * 
	 * @param query
	 * @param param
	 * @param store
	 * @return the result of the query
	 */
	public Object queryForObject(final String query, final Object param, final Object store);

	/**
	 * queries for a list in the database
	 *  
	 * @param query
	 * @param param
	 * @return the result list
	 */
	public List<?> queryForList(final String query, final Object param);
	
	/**
	 * 
	 * @param query Statement name
	 * @param param Parameter object
	 * @param key Key property name
	 * @return the result map
	 */
	public Map<?,?> queryForMap(final String query, final Object param, String key );
	
	/**
	 * 
	 * @param query Statement name
	 * @param param Parameter object
	 * @param key Key property name
	 * @param value Value property name
	 * @return The result map
	 */
	public Map<?,?> queryForMap(final String query, final Object param, String key, String value );

	/**
	 * stores the param in the database
	 * 
	 * @param query
	 * @param param
	 * @return the primary key of the new database column
	 */
	public Object insert(final String query, final Object param);

	/**
	 * updates the param
	 * 
	 * @param query
	 * @param param
	 */
	public void update(final String query, final Object param);

	/**
	 * deletes the param from the database
	 * 
	 * @param query
	 * @param param
	 */
	public void delete(final String query, final Object param);
}