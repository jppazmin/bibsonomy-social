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

/**
 * Bean for selection result strategy settings
 * @author fei
 * @version $Id: SelectorSettingParam.java,v 1.1 2009-03-26 14:55:06 folke Exp $
 */
public class SelectorSettingParam {
	private Long id;
	private String info;
	private byte[] meta;
	
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfo() {
		return info;
	}
	public void setMeta(byte[] meta) {
		this.meta = meta;
	}
	public byte[] getMeta() {
		return meta;
	}
	
}
