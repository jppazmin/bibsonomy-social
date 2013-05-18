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
 * 
 * A BibtexString is a literal string, such as "Donald E. Knuth"
 * 
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;

/**
 * A string - this class is used for numbers as well - if there's a number wrapped
 * in here, the toString() method will be smart enough to leave out the braces, and thus
 * print {1979} as 1979. 
 * 
 * @author henkel
 */
public class BibtexString extends BibtexAbstractValue {

	//	content does not include the quotes or curly braces around the string!
	private String content;

	/**
	 * content includes the quotes or curly braces around the string!
	 * 
	 * @param content
	 */
	BibtexString(BibtexFile file, String content) {
		super(file);
		this.content = content;
	}

	/**
	 * content includes the quotes or curly braces around the string!
	 * 
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
	    
		// is this really a number?
		try {
			Integer.parseInt(content);
			writer.print(content);
		} catch (NumberFormatException nfe) {
			writer.print('{');
//			for (int begin = 0; begin < content.length();) {
//				int end = content.indexOf('\n', begin);
//				if (end < 0) {
//					if (begin > 0)
//						writer.print(content.substring(begin, content.length()));
//					else
//						writer.print(content);
//
//					break;
//				}
//				writer.println(content.substring(begin, end));
//				writer.print("\t\t");
//				begin = end + 1;
//			}
			writer.print(content);
			writer.print('}');
		}
	}

}
