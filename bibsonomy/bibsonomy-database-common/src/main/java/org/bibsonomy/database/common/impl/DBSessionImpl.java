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

package org.bibsonomy.database.common.impl;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.errors.ErrorMessage;
import org.bibsonomy.common.exceptions.DatabaseException;
import org.bibsonomy.common.exceptions.QueryTimeoutException;
import org.bibsonomy.database.common.DBSession;
import org.bibsonomy.util.ExceptionUtils;

import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.ibatis.sqlmap.client.SqlMapSession;

/**
 * This class wraps the iBatis SqlMap and manages database sessions. Transactions are virtual,
 * which means a counter is used to emulate nested transactions.
 * 
 * @author Jens Illig
 * @author Christian Schenk
 * @version $Id: DBSessionImpl.java,v 1.11 2011-04-11 13:22:59 nosebrain Exp $
 */
public class DBSessionImpl implements DBSession {
	private static final Log log = LogFactory.getLog(DBSessionImpl.class);

	/** Communication with the database is done with the sqlMap */
	private final SqlMapSession sqlMap;

	/** how many commit-calls have to be made for getting the real transaction to become committed */
	private int transactionDepth;
	private int uncommittedDepth;

	/**
	 * if one virtual transaction is aborted, no other virtual transaction will
	 * become committed until all virtual transactions are ended
	 */
	private boolean aborted;
	private boolean closed;

	private final Map<String, List<ErrorMessage>> errorMessages;

	protected DBSessionImpl(final SqlMapSession sqlMap) {
		this.sqlMap = sqlMap;
		this.transactionDepth = 0;
		this.uncommittedDepth = 0;
		this.aborted = false;
		this.closed = false;

		this.errorMessages = new HashMap<String, List<ErrorMessage>>();
	}

	/**
	 * Starts a virtual transaction (a real one if no real transaction has been
	 * started yet). Either transactionSuccess or transactionFailure MUST be
	 * called hereafter.
	 */
	@Override
	public void beginTransaction() {
		if (this.aborted) {
			// TODO: log message?!?
			ExceptionUtils.logErrorAndThrowRuntimeException(log, null, "real transaction already aborted");
		}
		if (this.transactionDepth == 0) {
			try {
				this.sqlMap.startTransaction();
			} catch (final SQLException ex) {
				// TODO: log message?!?
				ExceptionUtils.logErrorAndThrowRuntimeException(log, ex, "Couldn't start transaction");
			}
			// a new transaction
			this.errorMessages.clear();
		}
		++this.transactionDepth;
		++this.uncommittedDepth;
	}

	/**
	 * Marks the current (virtual) transaction as having been sucessfully
	 * completed. If the transaction isn't virtual a following call to
	 * endTransaction will do a commit on the real transaction.
	 */
	@Override
	public void commitTransaction() {
		if (this.uncommittedDepth > 0) {
			--this.uncommittedDepth;
		} else {
			ExceptionUtils.logErrorAndThrowRuntimeException(log, null, "No transaction open");
		}
	}

	/**
	 * If this is called before the current (virtual) transaction has been
	 * committed, the transaction-stack is marked as failed. This causes the
	 * real transaction (with all N virtual nested transactions) to abort.<br/>
	 * 
	 * This should always be called after each transaction, that has begun with
	 * beginTransaction, sometimes with a preceeding call to commitTransaction,
	 * sometimes (in case of an exception) without.
	 */
	@Override
	public void endTransaction() {
		if (this.transactionDepth > 0) {
			--this.transactionDepth;
			if (this.transactionDepth < this.uncommittedDepth) {
				// endTransaction was called before commitTransaction => abort
				this.aborted = true;
			}
			if (this.transactionDepth == 0) {
				if (this.uncommittedDepth == 0) {
					if (this.aborted == false) {
						if (this.errorMessages.isEmpty()){
							// everything went well during the whole transaction => commit
							try {
								this.sqlMap.commitTransaction();
								log.debug("committed");
							} catch (final SQLException ex) {
								ExceptionUtils.logErrorAndThrowRuntimeException(log, ex, "Couldn't commit transaction");
							}
						}
					}
				}
				this.uncommittedDepth = 0;
				this.aborted = false;
				try {
					this.sqlMap.endTransaction();
					log.debug("ended");
				} catch (final SQLException ex) {
					ExceptionUtils.logErrorAndThrowRuntimeException(log, ex, "Couldn't end transaction");
				}
				if (!this.errorMessages.isEmpty()) {
					// errors occurred, sql connection was closed => throw databaseException
					log.info("Couldn't commit transaction due to errors during the session; error messages: " + this.errorMessages.toString());
					throw new DatabaseException(this.errorMessages);
				}
			}
		} else {
			ExceptionUtils.logErrorAndThrowRuntimeException(log, null, "No transaction open");
		}
	}

	/**
	 * MUST be called to release the db-connection
	 */
	@Override
	public void close() {
		try {
			this.sqlMap.endTransaction();
			log.debug("ended");
		} catch (final SQLException ex) {
			// TODO: log message?!?
			ExceptionUtils.logErrorAndThrowRuntimeException(log, ex, "Couldn't end transaction");
		}
		this.sqlMap.close();
		this.closed = true;
	}

	/**
	 * marks this session to have a failed job
	 */
	private void somethingWentWrong() {
		if (this.transactionDepth > 0) {
			this.aborted = true;
		}
	}

	/**
	 * @return whether this session has an aborted transaction 
	 */
	public boolean isAborted() {
		return this.aborted;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.common.DBSession#queryForObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object queryForObject(final String query, final Object param) {
		try {
			return this.sqlMap.queryForObject(query, param);
		} catch (final Exception e) {
			this.handleException(e, query);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.common.DBSession#queryForList(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<?> queryForList(final String query, final Object param) {
		try {
			return this.sqlMap.queryForList(query, param);
		} catch (final Exception e) {
			this.handleException(e, query);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.common.DBSession#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object insert(final String query, final Object param) {
		try {
			return this.sqlMap.insert(query, param);
		} catch (final Exception e) {
			this.handleException(e, query);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.common.DBSession#update(java.lang.String, java.lang.Object)
	 */
	@Override
	public void update(final String query, final Object param) {
		try {
			this.sqlMap.update(query, param);
		} catch (final Exception e) {
			this.handleException(e, query);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.common.DBSession#delete(java.lang.String, java.lang.Object)
	 */
	@Override
	public void delete(final String query, final Object param) {
		try {
			this.sqlMap.delete(query, param);
		} catch (final Exception e) {
			this.handleException(e, query);
		}
	}

	private void handleException(final Exception e, final String query) {
		// XXX: :(
		if (e instanceof NestedSQLException) {
			this.handleException((NestedSQLException)e, query);
		} else {
			log.error("Caught exception " + e.getClass().getSimpleName());
			this.logException(query, e);
		}		
	}

	private void handleException(final NestedSQLException ex, final String query) {
		if (!this.errorMessages.isEmpty()) {
			if ("22001".equals(ex.getSQLState())) {
				/*
				 * 22001 (string too long for the column)
				 * ignore exception because a FieldLengthErrorMessage was added to databaseException
				 * and the "commit" method will throw the databaseException
				 */
				return;
			}
		}

		final Throwable cause = ex.getCause();
		if (cause != null && SQLException.class.equals(cause.getClass())) {
			/*
			 * catch exceptions that happens because of
			 * query interruption due to time limits
			 * 
			 * Error code 1317: "Query execution was interrupted"
			 * Error code 1028: "Sort aborted"
			 * (see http://dev.mysql.com/doc/refman/5.1/en/error-messages-server.html) 
			 * 
			 */
			switch (((SQLException)cause).getErrorCode()) {
			case 1317:
				log.info("Query timeout for query: " + query);
				throw new QueryTimeoutException(ex, query);
			case 1028:
				log.info("Sort aborted for query: " + query);
				throw new QueryTimeoutException(ex, query);
			case 1105:
				/*
				 * Here we catch the wonderful "unknown error" (code 1105) exception of MySQL.
				 * On 2008-04-21 we found that it occurs, when a statement is killed during its
				 * "statistics" phase (mysql version 5.0.45). We filed a bug report 
				 * <http://bugs.mysql.com/bug.php?id=36230> and as workaround added this if- 
				 * block.
				 * 
				 * http://dev.mysql.com/doc/refman/5.1/en/error-messages-server.html
				 */
				log.info("Hit MySQL bug 36230. (with query: " + query + "). See <http://bugs.mysql.com/bug.php?id=36230> for more information.");
				throw new QueryTimeoutException(ex, query);
			default:
				break;
			}
		}

		/*
		 * XXX: this handles a special MySQL exception. We had a check on
		 * the corresponding class here, which brought in a dependency to
		 * MySQL. Therefore, the check is now against the class name - 
		 * which brings problems, if it should change ... 
		 */
		if (cause != null && cause.getClass().getSimpleName().equals("MySQLTimeoutException")) {
			log.info("MySQL Query timeout for query " + query);
			throw new QueryTimeoutException(ex, query);
		}

		this.logException(query, ex);
	}

	/**
	 * @param query
	 * @param ex
	 */
	private void logException(final String query, final Exception ex) {
		final String msg = "Couldn't execute query '" + query + "'";
		this.somethingWentWrong();
		ExceptionUtils.logErrorAndThrowRuntimeException(log, ex, msg);
	}

	@Override
	public void addError(final String key, final ErrorMessage errorMessage) {
		List<ErrorMessage> errorMessages = this.errorMessages.get(key);

		if (errorMessages == null) {
			errorMessages = new LinkedList<ErrorMessage>();
			this.errorMessages.put(key, errorMessages);
		}

		errorMessages.add(errorMessage);
	}

	@Override
	protected void finalize() throws Throwable {
		// Try to take care of other peoples mistakes. It may take a while
		// before this is called, but it's better than nothing.
		if (this.closed == false) {
			log.error(this.getClass().getName() + " not closed");
			this.sqlMap.close();
		}
		super.finalize();
	}

	@Override
	public Object queryForObject(final String query, final Object param, final Object store) {
		try {
			return this.sqlMap.queryForObject(query, param, store);
		} catch (final Exception e) {
			this.handleException(e, query);
		}

		return null;
	}

	/**
	 * @return the sqlMap
	 */
	public SqlMapSession getSqlMapExecutor() {
		return this.sqlMap;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.common.DBSession#queryForMap(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public Map<?, ?> queryForMap(String query, Object param, String key) {
	    try {
		return this.sqlMap.queryForMap(query, param, key);
	    } catch (final Exception e) {
		handleException(e, query);
	    }
	    return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.bibsonomy.database.common.DBSession#queryForMap(java.lang.String, java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<?, ?> queryForMap(String query, Object param, String key, String value) {
	    try {
		return this.sqlMap.queryForMap(query, param, key, value);
	    } catch (final Exception e) {
		handleException(e, query);
	    }
	    return null;
	}
}