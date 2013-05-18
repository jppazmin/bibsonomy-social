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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * An abstract superclass for all bibtex model nodes.
 * 
 * @author henkel
 */
public abstract class BibtexNode {

	/**
	 * Some people have asked why this constructor has a parameter of type BibtexFile (which in turn extends BibtexNode).
	 * The reason is that each BibtexNode has a backpointer to the BibtexFile which it belongs to. Of course, for the
	 * file itself that pointer is null.
	 * 
	 * By the way, it's unlikely that you need to call this constructor or any constructor for BibtexNodes other than
	 * BibtexFile directly - instead, use the BibtexFile as a factory.
	 * 
	 * For example, to create a bibtex file equivalent of this
	 * <pre>
	 * ================
	 * &#64;article{test1,
	 * author="Johannes Henkel",
	 * title="README"
	 * }
	 * ================
	 * you'd do
	 * 
	 * BibtexFile bibtexFile = new BibtexFile();
	 * BibtexEntry onlyEntry = bibtexFile.makeEntry("article","test1");
	 * onlyEntry.setField("author",bibtexFile.makeString("Johannes Henkel"));
	 * onlyEntry.setField("title",bibtexFile.makeString("README"));
	 * </pre>
	 * @param bibtexFile
	 */
	BibtexNode(BibtexFile bibtexFile){
	    
	    assert (this instanceof BibtexFile) || bibtexFile!=null: "bibtexFile parameter may not be null, unless this is a BibtexFile.";
	    
		this.bibtexFile = bibtexFile;
	}

	private final BibtexFile bibtexFile;
	
	public BibtexFile getOwnerFile(){
		return bibtexFile;
	}
	
	abstract public void printBibtex(PrintWriter writer);

	public String toString(){
		StringWriter stringWriter = new StringWriter();
		PrintWriter out = new PrintWriter(stringWriter);
		printBibtex(out);
		out.flush();
		return stringWriter.toString();
	}

}
