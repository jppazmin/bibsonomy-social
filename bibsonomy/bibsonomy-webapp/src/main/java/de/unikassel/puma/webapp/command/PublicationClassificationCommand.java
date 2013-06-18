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

package de.unikassel.puma.webapp.command;

import org.bibsonomy.webapp.command.ajax.AjaxCommand;

/**
 * @author philipp
 * @version $Id: PublicationClassificationCommand.java,v 1.2 2011-02-24 19:21:04 sven Exp $
 */
public class PublicationClassificationCommand extends AjaxCommand {

	private String classificationName = "";
	private String id = "";
	private String hash = "";
	private String key = "";
	private String value = "";
	
	/**
	 * @param name
	 */
	public void setClassificationName(String name) {
		this.classificationName = name;
	}
	
	/**
	 * @return  classification name
	 */
	public String getClassificationName() {
		return this.classificationName;
	}
	
	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param intrahash the intrahash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the intrahash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
