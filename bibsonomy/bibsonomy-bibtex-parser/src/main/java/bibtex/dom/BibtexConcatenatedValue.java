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
 * E. g. "asdf " # 19
 * 
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;

/**
 * 
 * Two abstract values concatenated by the hash-operator (#).
 * 
 * Examples:
 * <ul>
 * 	<li>acm # " SIGPLAN"</li>
 * 	<li>"10th " # pldi</li>
 * </ul>
 * 
 * 
 * @author henkel
 */
public final class BibtexConcatenatedValue extends BibtexAbstractValue {

	BibtexConcatenatedValue(BibtexFile file,BibtexAbstractValue left, BibtexAbstractValue right){
		super(file);
		this.left=left;
		this.right=right;
	}

	private BibtexAbstractValue left, right;

	/**
	 * @return BibtexValue
	 */
	public BibtexAbstractValue getLeft() {
		return left;
	}

	/**
	 * @return BibtexValue
	 */
	public BibtexAbstractValue getRight() {
		return right;
	}

	/**
	 * Sets the left.
	 * @param left The left to set
	 */
	public void setLeft(BibtexAbstractValue left) {
	    
	    assert !(left instanceof BibtexMultipleValues): "left parameter may not be an instance of BibtexMultipleValues."; 
	    
		this.left = left;
	}

	/**
	 * Sets the right.
	 * @param right The right to set
	 */
	public void setRight(BibtexAbstractValue right) {
	    
	    assert !(right instanceof BibtexMultipleValues): "right parameter may not be an instance of BibtexMultipleValues.";
	    
		this.right = right;
	}

	/* (non-Javadoc)
	 * @see bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter)
	 */
	public void printBibtex(PrintWriter writer) {
	    
	    assert writer!=null: "writer paramter may not be null.";
	    
		this.left.printBibtex(writer);
		writer.print('#');
		this.right.printBibtex(writer);
	}

}
