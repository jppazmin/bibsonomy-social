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

package org.bibsonomy.webapp.command.ajax;

/**
 * @author bernd
 * @version $Id: AjaxURLCommand.java,v 1.3 2011-07-14 15:37:39 nosebrain Exp $
 */
public class AjaxURLCommand extends AjaxCommand {
	/**
	 * the hash of the resource
	 */
	private String hash;
	
	/**
	 * the text of the url
	 */
	private String text;
	
	/**
	 * TODO: could this be of type URL?!
	 * the url
	 */
	private String url;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(final String text) {
		this.text = text;
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
	public void setHash(final String hash) {
		this.hash = hash;
	}
}
