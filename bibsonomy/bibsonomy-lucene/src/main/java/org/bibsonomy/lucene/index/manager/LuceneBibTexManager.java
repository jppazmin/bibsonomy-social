/**
 *
 *  BibSonomy-Lucene - A blue social bookmark and publication sharing system.
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

package org.bibsonomy.lucene.index.manager;

import org.bibsonomy.lucene.util.LuceneSpringContextWrapper;
import org.bibsonomy.model.BibTex;

/**
 * TODO: rename to LucenePublicationManager
 * 
 * class for maintaining the lucene index
 * 
 *  - regularly update the index by looking for new posts
 *  - asynchronously handle requests for flagging/unflagging of spam users
 * 
 * @author fei
 * @version $Id: LuceneBibTexManager.java,v 1.4 2010-10-13 11:31:53 nosebrain Exp $
 */
public class LuceneBibTexManager extends LuceneResourceManager<BibTex> {
	
	/** singleton pattern's class instance */
	private static LuceneBibTexManager instance;
	
	/**
	 * singleton pattern's instantiation method
	 * 
	 * @return the {@link LuceneBibTexManager} instance 
	 */
	public static LuceneBibTexManager getInstance() {
		if (instance == null) {
			instance = new LuceneBibTexManager();
			LuceneSpringContextWrapper.init();
		}
		
		return instance;
	}
	
	/**
	 * constructor
	 */
	private LuceneBibTexManager() {
	}
}
