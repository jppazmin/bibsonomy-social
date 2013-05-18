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
 * top-level comments are comments starting with %
 * For simplicity, we allow such comments only outside of all other entries.
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;

/**
 * Toplevel comments wrap everything that is at the toplevel of a bibtex file and not
 * parseable as some other entry. Note that many people think that the latex comment symbol
 * '%' can be used in bibtex. That's a myth - bibtex will just ignore that. If you want to
 * comment out in bibtex, remove the '@' sign at the beginning of an entry. 
 * 
 * @author henkel
 */
public final class BibtexToplevelComment extends BibtexAbstractEntry {

	BibtexToplevelComment(BibtexFile file,String content){
		super(file);
		this.content = content;
	}

	private String content;

	/**
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * @param content The content to set
	 */
	public void setContent(String content) {
	    
	    assert content!=null: "content parameter may not be null.";
	    
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter)
	 */
	public void printBibtex(PrintWriter writer) {	
	    
	    assert writer!=null: "writer parameter may not be null.";
	    
		writer.println(this.content);
	}

}
