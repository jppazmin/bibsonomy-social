/**
 *  
 *  BibSonomy-BibTeX-Parser - BibTeX Parser from
 * 		http://www-plan.cs.colorado.edu/henkel/stuff/javabib/
 *   
 *  Copyright (C) 2006 - 2010 Knowledge & Data Engineering Group, 
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

/*
 * Created on Mar 27, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A list of BibtexPerson objects that can be used for author or editor fields - use
 * the PersonListExpander to convert all editor/author field values of a particular
 * BibtexFile to BibtexPersonLists.
 * 
 * @author henkel
 */
public final class BibtexPersonList extends BibtexAbstractValue {

	BibtexPersonList(BibtexFile file){
		super(file);
	}

	private LinkedList list = new LinkedList();

	/**
	 * Returns a read-only list which members are instances of BibtexPerson.
	 * 
	 * @return BibtexPerson
	 */
	public List getList() {
		return Collections.unmodifiableList(list);
	}
	
	public void add(BibtexPerson bibtexPerson){
	    
	    assert bibtexPerson!=null: "bibtexPerson parameter may not be null.";
	    
		this.list.add(bibtexPerson);
	}


	/* (non-Javadoc)
	 * @see bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter)
	 */
	public void printBibtex(PrintWriter writer) {
	    
	    assert writer!=null: "writer parameter may not be null.";
	    
		boolean isFirst = true;
		writer.print('{');
		for(Iterator it = list.iterator();it.hasNext();){
			if(isFirst){
				isFirst = false;
			} else writer.print(" and ");
			((BibtexPerson)it.next()).printBibtex(writer);
		}
		writer.print('}');
	}

}
