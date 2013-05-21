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
 * Bean for recommender settings
 * @author fei
 * @version $Id: RecSettingParam.java,v 1.2 2009-04-03 14:02:41 folke Exp $
 */
public class RecSettingParam {
	private Long setting_id;
	private String recId;
	private String recDescr;
	private byte[] recMeta;
	
	public void setRecId(String recId) {
		this.recId = recId;
	}
	public String getRecId() {
		return recId;
	}
	public void setRecMeta(byte[] recMeta) {
		this.recMeta = recMeta;
	}
	public byte[] getRecMeta() {
		return recMeta;
	}
	public void setSetting_id(long setting_id) {
		this.setting_id = setting_id;
	}
	public long getSetting_id() {
		return setting_id;
	}
	public void setRecDescr(String recDescr) {
		this.recDescr = recDescr;
	}
	public String getRecDescr() {
		return recDescr;
	}
}
