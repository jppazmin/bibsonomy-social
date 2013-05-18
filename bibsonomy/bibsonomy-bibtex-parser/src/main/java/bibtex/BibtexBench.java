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
 * Created on Dec 14, 2003
 * 
 * @author henkel@cs.colorado.edu
 *  
 */
package bibtex;

import java.io.File;
import java.io.FileFilter;

/**
 * @author henkel
 */
public class BibtexBench {

	public static void main(String[] args) {

		String bibtexExampleDir = "examples";

		File dir = new File(bibtexExampleDir);
		File files[] = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (!pathname.isFile())
					return false;
				if (!pathname.getName().endsWith(".bib"))
					return false;
				return true;
			}
		});
		String parameters[] = new String[] { "-expandCrossReferences", "-expandPersonLists", //"-noOutput",
			"dummy" };
		long start = System.currentTimeMillis();
		for (int i = 0; i < files.length; i++) {
			String path = files[i].getAbsolutePath();
			parameters[parameters.length - 1] = path;
			Main.main(parameters);
		}
		System.err.println("This took "+(System.currentTimeMillis()-start)+".");
	}
}
