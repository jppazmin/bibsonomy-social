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

package org.bibsonomy.model.sync;

/**
 * @author wla
 * @version $Id: SynchronizationAction.java,v 1.1 2011-07-26 12:15:15 rja Exp $
 */
public enum SynchronizationAction {
	/**
	 * server post must be updated
	 */
	UPDATE_SERVER,
	
	/**
	 * client post must be updated
	 */
    UPDATE_CLIENT,
    
    /**
     * post must be created on server
     */
    CREATE_SERVER,
    
    /**
     * post must be created on client
     */
    CREATE_CLIENT,
    
    /**
     * post must be deleted on server
     */
    DELETE_SERVER,
    
    /**
     * post must be deleted on client
     */
    DELETE_CLIENT,
    
    /**
     * nothing to do
     */
    OK,
    
    /**
     * conflict, ask user
     */
    ASK,
    
    /**
     * something else... possible error, 
     */
    UNDEFINED;
}
