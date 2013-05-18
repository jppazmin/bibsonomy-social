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
 * Created on Mar 19, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;

/**
 * A preamble can be used to include pretty much arbitrary latex/tex at the beginning of a
 * generated bibliography. There is usually only one preamble per bibtex file.
 * 
 * @author henkel
 */
public final class BibtexPreamble extends BibtexAbstractEntry {

	private BibtexAbstractValue content;

	BibtexPreamble(BibtexFile file,BibtexAbstractValue content){
		super(file);
		this.content = content;
	}


	/* (non-Javadoc)
	 * @see bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter)
	 */
	public void printBibtex(PrintWriter writer) {
	    
	    assert writer!=null: "writer parameter may not be null.";
	    
		writer.println("@preamble{");
		content.printBibtex(writer);
		writer.println("}");
	}

	/**
	 * @return BibtexAbstractValue
	 */
	public BibtexAbstractValue getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * @param content The content to set
	 */
	public void setContent(BibtexAbstractValue content) {
	    
	    assert content!=null : "content parameter may not be null.";
	    assert !(content instanceof BibtexMultipleValues) : "content parameter may not be an instance of BibtexMultipleValues.";
	    
		this.content = content;
	}

}
