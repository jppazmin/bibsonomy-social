/**
 *
 *  BibSonomy-Database - Database for BibSonomy.
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

package org.bibsonomy.database.systemstags.search;

import org.bibsonomy.database.systemstags.SystemTagsUtil;




/**
 * System tag for representing relationships in social networking systems 
 * such as facebook, BibSonomy, etc.  
 * 
 * @author fmi
 */
public class NetworkRelationSystemTag extends UserRelationSystemTag {

	public static final String NAME = "network";

	
	// FIXME: SystemTagsUtil and SystemTagFactory have a cyclic dependency which is triggered if included here
	/** the system tag for representing BibSonomy's friendship relation (==trust network) */
	public final static String BibSonomyFriendSystemTag = SystemTagsUtil.buildSystemTagString(NAME, "bibsonomy-friend");// "sys:network:bibsonomy-friend";  // 
	/** the system tag for representing BibSonomy's follower relation  */
	public final static String BibSonomyFollowerSystemTag = "sys:network:bibsonomy-follower";  //SystemTagsUtil.buildSystemTagString(NAME, "bibsonomy-follower");
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public NetworkRelationSystemTag newInstance() {
		return new NetworkRelationSystemTag();
	}
}
