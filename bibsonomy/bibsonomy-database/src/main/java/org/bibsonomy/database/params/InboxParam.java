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

package org.bibsonomy.database.params;

/**
 * @author Stephan Doerfel
 * @version $Id: InboxParam.java,v 1.4 2011-06-16 13:55:00 nosebrain Exp $
 */
public class InboxParam extends GenericParam {
	
	private int messageId;
	private int contentId;
	private String intraHash;
	private String receiver;
	private String sender;
	
	/**
	 * @return int
	 */
	public int getContentId() {
		return this.contentId;
	}

	/**
	 * @param contentId
	 */
	public void setContentId(final int contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return int
	 */
	public int getMessageId() {
		return this.messageId;
	}

	/**
	 * @param messageId
	 */
	public void setMessageId(final int messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return intraHash
	 */
	public String getIntraHash() {
		return this.intraHash;
	}

	/**
	 * @param intraHash
	 */
	public void setIntraHash(String intraHash) {
		this.intraHash = intraHash;
	}

	/**
	 * @return String
	 */
	public String getReceiver() {
		return this.receiver;
	}

	/**
	 * @param receiver
	 */
	public void setReceiver(final String receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return String
	 */
	public String getSender() {
		return this.sender;
	}

	/**
	 * @param sender
	 */
	public void setSender(final String sender) {
		this.sender= sender;
	}
}