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
 * @author clemens
 * @version $Id: OpenAccessCommand.java,v 1.2 2011-05-18 15:23:47 sven Exp $
 */
public class OpenAccessCommand extends AjaxCommand {

	/**
	 * publisher to check
	 */
	private String publisher;
	private String jTitle;
	private String qType;
	private String interhash = "";	

	/**
	 * @return publisher
	 */
	public String getPublisher() {
		return this.publisher;
	}

	/**
	 * @param publisher
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return jTitle
	 */
	public String getjTitle() {
		return this.jTitle;
	}

	/**
	 * @param jTitle
	 */
	public void setjTitle(String jTitle) {
		this.jTitle = jTitle;
	}

	/**
	 * @return qType
	 */
	public String getqType() {
		return this.qType;
	}

	/**
	 * @param qType
	 */
	public void setqType(String qType) {
		this.qType = qType;
	} 	
	
	
	/**
	 * @param interhash 
	 */
	public void setInterhash(String interhash) {
		this.interhash = interhash;
	}

	/**
	 * @return the interhash
	 */
	public String getInterhash() {
		return interhash;
	}

	
	
}
