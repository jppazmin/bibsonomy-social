/**
 *
 *  BibSonomy-Layout - Layout engine for the webapp.
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

package org.bibsonomy.layout.csl.model;

import java.util.HashMap;

/**
 * A list of records according to CSL. Basically a HashMap with
 * the entry's ID as key.
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: RecordList.java,v 1.2 2011-07-26 13:44:49 bibsonomy Exp $
 */
public class RecordList extends HashMap<String,Record> {

    public RecordList() {
	super();
    }
    
    public void add(Record rec) {
	this.put(rec.getId(), rec);
    }
    
}
