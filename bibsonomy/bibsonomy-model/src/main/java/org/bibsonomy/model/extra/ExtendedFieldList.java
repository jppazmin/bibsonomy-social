/**
 *
 *  BibSonomy-Model - Java- and JAXB-Model.
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

package org.bibsonomy.model.extra;

import java.util.ArrayList;

/**
 * @author philipp
 * @version $Id: ExtendedFieldList.java,v 1.4 2011-04-29 06:45:07 bibsonomy Exp $
 */
public class ExtendedFieldList {
    
    private String key;
    
    private ArrayList<String> valueList = new ArrayList<String>();

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
	 * @param valueList 
	 */
	public void setValueList(ArrayList<String> valueList) {
		this.valueList = valueList;
	}

	/**
	 * @return the valueList
	 */
	public ArrayList<String> getValueList() {
		return valueList;
	}
    
    
    
}
