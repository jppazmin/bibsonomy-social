/**
 *
 *  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

package org.bibsonomy.recommender.tags.database;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author fei
 * @version $Id: IdleClosingConnectionManager.java,v 1.3 2010-07-14 11:47:58 nosebrain Exp $
 */
public class IdleClosingConnectionManager extends MultiThreadedHttpConnectionManager {
	private static final Log log = LogFactory.getLog(IdleClosingConnectionManager.class);
	
	@Override
	public void releaseConnection(HttpConnection conn) {
		log.debug("Freeing connection to " + conn.getHost());
		super.releaseConnection(conn);
		log.debug("Closing connection to " + conn.getHost());
		conn.close();
		deleteClosedConnections();
	}

}
