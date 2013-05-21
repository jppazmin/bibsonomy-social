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

package org.bibsonomy.recommender.tags.database.params;

import java.sql.Timestamp;

/**
 * @author fei
 * @version $Id: PostParam.java,v 1.1 2009-03-26 14:55:06 folke Exp $
 */
public class PostParam extends ListParam {
	private Timestamp timestamp;
	private String userName;
	private Integer contentID;
	private String intraHash;
	
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setUserName(String user_name) {
		this.userName = user_name;
	}
	public String getUserName() {
		return userName;
	}
	public void setContentID(Integer contentID) {
		this.contentID = contentID;
	}
	public Integer getContentID() {
		return contentID;
	}
	public String getIntraHash() {
		return this.intraHash;
	}
	public void setIntraHash(String intraHash) {
		this.intraHash = intraHash;
	}


}
