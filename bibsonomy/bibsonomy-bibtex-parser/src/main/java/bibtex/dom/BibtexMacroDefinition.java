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
 * A string definition is something like
 * 
 * @string{ cacm = "Communications of the ACM }
 * 
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;

/**
 * Bibtex let's you define macros which are essentially just shortcuts for strings.
 * Macros can reference other macros, as long as there's no cycle.
 * 
 * Examples:
 * <ul>
 * <li>&#x0040string(acm = "Association of the Computing Machinery")</li>
 * <li>&#x0040string(acmsigplan = acm # " SIGPLAN")</li>
 * </ul>
 * @author henkel
 */
public final class BibtexMacroDefinition extends BibtexAbstractEntry {

	BibtexMacroDefinition(BibtexFile file,String key, BibtexAbstractValue value){
		super(file);
		this.key = key.toLowerCase();
		this.value = value;
	}

	private String key; private BibtexAbstractValue value;

	/**
	 * @return String
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return BibtexValue
	 */
	public BibtexAbstractValue getValue() {
		return value;
	}

	/**
	 * Sets the key.
	 * @param key The key to set
	 */
	public void setKey(String key) {
		this.key = key.toLowerCase();
	}

	/**
	 * Sets the value.
	 * @param value The value to set
	 */
	public void setValue(BibtexAbstractValue value) {
	    
	    assert value!=null: "value parameter has to be !=null.";
	    
		this.value = value;
	}



	/* (non-Javadoc)
	 * @see bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter)
	 */
	public void printBibtex(PrintWriter writer) {
	    
	    assert writer!=null: "writer parameter has to be !=null.";
	    
		writer.print("@string{");
		writer.print(this.key);
		writer.print("=");
		this.value.printBibtex(writer);
		writer.println("}");

	}

}
