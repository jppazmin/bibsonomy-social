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
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

/**
 * Abstract values are the values that can be used as field values in
 * entries or as bodies of macros.
 * 
 * For example, in 
 * <pre>
 * 	&#x0040;string(pldi = acm # " SIGPLAN Conference of Programming
 * 						Language Design and Implementation")
 * </pre>
 * the following is an abstract value.
 * <pre>
 * acm # " SIGPLAN Conference of Programming Language Design and Implementation"
 * </pre>
 * Other examples:
 * <ul>
 * <li>1971</li>
 * <li>"Johannes Henkel"</li>
 * <li>acm</li>
 * <li>dec</li>
 * </ul> 
 *  
 * @author henkel
 */
public abstract class BibtexAbstractValue extends BibtexNode {



	/**
	 * @param bibtexFile
	 */
	protected BibtexAbstractValue(BibtexFile bibtexFile) {
		super(bibtexFile);
	}

}
