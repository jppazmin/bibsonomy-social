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
 * @version $Id: ConflictResolutionStrategy.java,v 1.3 2011-07-27 15:37:12 wla Exp $
 */
public enum ConflictResolutionStrategy {
    /**
     * client changes will be applied to server account
     */
	CLIENT_WINS("cw"),
    
	/**
	 * server changes will be applied to client account
	 */
	SERVER_WINS("sw"),
	
	/**
	 * latest changes will be applied to another account
	 */
    LAST_WINS("lw"),
    
    /**
     * the first changes will be applied to another account
     */
    FIRST_WINS("fw");
    
    /**
     * user can select, which changes will be applied
     * temporary disabled  
     */
//    ASK_USER("au");
	
	private String strategy;

	private ConflictResolutionStrategy(String strategy) {
		this.strategy = strategy;
	}
	/**
	 * @return the strategy
	 */
	public String getConflictResolutionStrategy() {
		return strategy;
	}
	
	/**
	 * 
	 * @param strategy
	 * @return conflict resolution strategy for given string
	 */
	public static ConflictResolutionStrategy getConflictResolutionStrategyByString(String strategy) {
		if("lw".equals(strategy)) {
			return LAST_WINS;
		} else if("fw".equals(strategy)) {
			return FIRST_WINS;
		} else if("cw".equals(strategy)) {
			return CLIENT_WINS;
		} else if("sw".equals(strategy)) {
			return SERVER_WINS;
//		} else if ("au".equals(strategy)) {
//			return ASK_USER;
		}
		throw new UnsupportedOperationException("ConflictReolutionStrategy: " + strategy + " is unsupported");
	}
	
}
