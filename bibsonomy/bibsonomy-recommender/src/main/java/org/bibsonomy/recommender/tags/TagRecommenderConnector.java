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

package org.bibsonomy.recommender.tags;

import java.util.Properties;

import org.bibsonomy.services.recommender.TagRecommender;


/**
 * @author fei
 * @version $Id: TagRecommenderConnector.java,v 1.4 2010-07-14 11:42:29 nosebrain Exp $
 */
public interface TagRecommenderConnector extends TagRecommender {
	/**
	 * Initialize object.
	 * @param props specific properties
	 * @return true on success, false otherwise
	 * @throws Exception Exception describing problem.
	 */
	public boolean initialize(Properties props) throws Exception;
	
	/**
	 * Establish connection to recommender.
	 * @return true on success, false otherwise
	 * @throws Exception Exception describing problem.
	 */
	public boolean connect() throws Exception;
	
	/**
	 * Terminate connection from recommender.
	 * @return true on success, false otherwise
	 * @throws Exception Exception describing problem.
	 */
	public boolean disconnect() throws Exception;
	
	/**
	 * Arbitrary auxiliary informations.
	 *  
	 * @return the meta informations
	 */
	public byte[] getMeta();

	/**
	 * Description for identifying this recommender.
	 * @return the id
	 */
	public String getId();
}
