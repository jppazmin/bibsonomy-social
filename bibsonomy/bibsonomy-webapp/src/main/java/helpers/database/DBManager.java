/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package helpers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 * Abstract class to provide basic functionality for getting datasources and closing statements.
 * 
 * This class can be used to build new DBManagers: 
 * - just extend DBManager
 * - copy the method do_db_stuff from this class into the new class and use it as a template
 *   for your DB methods
 *
 */
@Deprecated
public abstract class DBManager {
	
	/**
	 * This is the static datasource variable which is initialized only once. It 
	 * is used to get connections from the pool. This has to be done in a synchronized
	 * manner, since several instances of DBManagers subclasses might access it 
	 * concurrently.
	 */
	private static DataSource dataSource = null;
	
	/**
	 * the same for the database slave
	 */
	private static DataSource slaveDataSource = null;
	
	
	/**
	 * Checks, if the datasource class variable has been initalized, if not, initializes it. 
	 * @throws NamingException
	 */
	private static synchronized void checkDataSource() throws NamingException {
		if (dataSource == null) {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/bibsonomy");
		}
	}

	/**
	 * Checks, if the slave datasource class variable has been initalized, if not, initializes it. 
	 * @throws NamingException
	 */
	private static synchronized void checkSlaveDataSource() throws NamingException {
		if (slaveDataSource == null) {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			slaveDataSource = (DataSource) envContext.lookup("jdbc/bibsonomy_slave");			
		}
	}
	
	// class to save some typing work for parameters
	
	/**
	 * This class holds together a connection, a prepared statement and a result set together with
	 * an initalization and close method. 
	 * 
	 * As a static member class, this class is NOT an inner class, but has access to private
	 * members and attributes of the enclosing class.
	 * 
	 * see http://java.sun.com/docs/books/jls/third_edition/html/classes.html#8.5
	 * 
	 * This means, this class can have instances! These instances have all their own connection,
	 * statement and results set and access only the static datasource of the enclosing class 
	 * in a static way (therefore we have the synchronize block and checkDataSource() sis
	 * synchronized as well).
	 * 
	 * 
	 * TODO: re-check the above, if it is correct! 
	 * 
	 *
	 */
	@Deprecated
	protected static class DBContext {
		public Connection         conn = null;
		public ResultSet           rst = null;
		public PreparedStatement  stmt = null;	

		/**
		 * Checks, if the datasource is available and gets a connection from it.
		 * 
		 * @return <code>true</code> if a connection is available.
		 * @throws SQLException
		 */
		public boolean init () throws SQLException {
			try {
				checkDataSource();
			} catch (NamingException e) {
				throw new SQLException("Could not get datasource: " + e);
			}
			synchronized(dataSource) {
				if(dataSource != null){
					conn = dataSource.getConnection();
					return true;
				}
				throw new SQLException("Could not get datasources");
			}			
		}
		
		/**
		 * Checks, if the slave datasource is available and gets a connection from it.
		 * 
		 * @return <code>true</code> if a connection is available.
		 * @throws SQLException
		 */
		public boolean initSlave () throws SQLException {
			try {
				checkSlaveDataSource();
			} catch (NamingException e) {
				throw new SQLException("Could not get datasource: " + e);
			}
			synchronized(slaveDataSource) {
				if(slaveDataSource != null){
					conn = slaveDataSource.getConnection();
					return true;
				}
				throw new SQLException("Could not get slave datasources");
			}			
		}
		
		// closes all connections, statements and result sets
		public void close () {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (rst  != null) {try {rst.close(); } catch (SQLException e) {} rst  = null;}						
			if (stmt != null) {try {stmt.close();} catch (SQLException e) {} stmt = null;}
			if (conn != null) {try {conn.close();} catch (SQLException e) {} conn = null;}
		}

	}
	
	/**
	 * This is a template of a method which subclasses of DBManager should
	 * use to implement the database methods. 
	 * Just copy it into the subclass, change the name and put your logic
	 * between c.init() and the catch statement. 
	 * 
	 * @return <code>true</code> if the method succeeds
	 */
	/*
	public static boolean do_db_stuff () {
		DBContext c = new DBContext();
		try {
			if (c.init()) { // initialize database
			
			// do database stuff here
				
			}
		} catch (SQLException e) {
			System.out.println("DBSM: " + e);
			return false;
		} finally {
			c.close(); // close database connection
		}
		return false;
	}
	*/
}
