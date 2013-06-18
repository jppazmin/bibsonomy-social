/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.webapp.command.actions;

import org.bibsonomy.webapp.command.BaseCommand;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author cvo
 * @version $Id: JabRefImportCommand.java,v 1.2 2010-05-11 12:24:53 nosebrain Exp $
 */
public class JabRefImportCommand extends BaseCommand{
	
	/** the file to import **/
	private CommonsMultipartFile fileBegin;
	
	private CommonsMultipartFile fileItem;
	
	private CommonsMultipartFile fileEnd;
	
	/**
	 * contains the string for the action. The action could be create or delete
	 */
	private String action;

	/**
	 * name of the begin layout file
	 */
	private String beginName;
	
	/**
	 * hash of the begin layout file
	 */
	private String beginHash;
	
	/**
	 * name of the item layout file
	 */
	private String itemName;
	
	/**
	 * hash of the begin layout file
	 */
	private String itemHash;
	
	/**
	 * name of the end layout file
	 */
	private String endName;
	
	/**
	 * hash of the end layout file
	 */
	private String endHash;
	
	/**
	 * hash of the layout definition
	 */
	private String hash;

	/**
	 * @return the current chosen action, this could be create or delete 
	 */
	public String getAction() {
		return this.action;
	}

	/**
	 * 
	 * @param action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the fileBegin
	 */
	public CommonsMultipartFile getFileBegin() {
		return this.fileBegin;
	}

	/**
	 * @param fileBegin the fileBegin to set
	 */
	public void setFileBegin(CommonsMultipartFile fileBegin) {
		this.fileBegin = fileBegin;
	}

	/**
	 * @return the fileItem
	 */
	public CommonsMultipartFile getFileItem() {
		return this.fileItem;
	}

	/**
	 * @param fileItem the fileItem to set
	 */
	public void setFileItem(CommonsMultipartFile fileItem) {
		this.fileItem = fileItem;
	}

	/**
	 * @return the fileEnd
	 */
	public CommonsMultipartFile getFileEnd() {
		return this.fileEnd;
	}

	/**
	 * @param fileEnd the fileEnd to set
	 */
	public void setFileEnd(CommonsMultipartFile fileEnd) {
		this.fileEnd = fileEnd;
	}

	/**
	 * @return the beginName
	 */
	public String getBeginName() {
		return this.beginName;
	}

	/**
	 * @param beginName the beginName to set
	 */
	public void setBeginName(String beginName) {
		this.beginName = beginName;
	}

	/**
	 * @return the beginHash
	 */
	public String getBeginHash() {
		return this.beginHash;
	}

	/**
	 * @param beginHash the beginHash to set
	 */
	public void setBeginHash(String beginHash) {
		this.beginHash = beginHash;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemHash
	 */
	public String getItemHash() {
		return this.itemHash;
	}

	/**
	 * @param itemHash the itemHash to set
	 */
	public void setItemHash(String itemHash) {
		this.itemHash = itemHash;
	}

	/**
	 * @return the endName
	 */
	public String getEndName() {
		return this.endName;
	}

	/**
	 * @param endName the endName to set
	 */
	public void setEndName(String endName) {
		this.endName = endName;
	}

	/**
	 * @return the endHash
	 */
	public String getEndHash() {
		return this.endHash;
	}

	/**
	 * @param endHash the endHash to set
	 */
	public void setEndHash(String endHash) {
		this.endHash = endHash;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return this.hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
}
