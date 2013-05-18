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

package org.bibsonomy.lucene.param;

import java.util.Date;

import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;


/**
 * Lucene Post class, extending the model class with index management fields.
 * 
 * @author fei
 * @version $Id: LucenePost.java,v 1.5 2010-05-28 10:22:50 nosebrain Exp $
 *
 * @param <R>
 */
public class LucenePost<R extends Resource> extends Post<R> {
	private static final long serialVersionUID = 6167951235868739450L;

	/** newest tas_id during last index update */
	private Integer lastTasId;

	/** newest log_date during last index update */
	private Date lastLogDate;

	/**
	 * @return the lastTasId
	 */
	public Integer getLastTasId() {
		return lastTasId;
	}

	/**
	 * @param lastTasId the lastTasId to set
	 */
	public void setLastTasId(Integer lastTasId) {
		this.lastTasId = lastTasId;
	}

	/**
	 * @return the lastLogDate
	 */
	public Date getLastLogDate() {
		return lastLogDate;
	}

	/**
	 * @param lastLogDate the lastLogDate to set
	 */
	public void setLastLogDate(Date lastLogDate) {
		this.lastLogDate = lastLogDate;
	}	
}
