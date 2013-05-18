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
 * Created on Mar 29, 2003
 * 
 * @author henkel@cs.colorado.edu
 *  
 */
package bibtex.expansions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bibtex.dom.BibtexAbstractEntry;
import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexString;

/**
 * This expander expands the crossreferences defined by the crossref fields -
 * you should run the MacroReferenceExpander first.
 * 
 * @author henkel
 */
public final class CrossReferenceExpander extends AbstractExpander implements Expander {

	/** Equivalent to CrossReferenceExpander(true) */
	public CrossReferenceExpander() {
		this(true);
	}
	
	/**
	 * @param throwAllExpansionExceptions
	 *            Setting this to true means that all exceptions will be thrown
	 *            immediately. Otherwise, the expander will skip over things it
	 *            can't expand and you can use getExceptions to retrieve the
	 *            exceptions later
	 */
	public CrossReferenceExpander(boolean throwAllExpansionExceptions) {
		super(throwAllExpansionExceptions);
	}

	/**
	 * Note: If you don't use the MacroReferenceExpander first, this function
	 * may lead to inconsistent macro references.
	 * 
	 * If you use the flag throwAllExpansionExceptions set to false, you can
	 * retrieve all the exceptions using getExceptions()
	 * 
	 * @param bibtexFile
	 */
	public void expand(BibtexFile bibtexFile) throws ExpansionException {
		HashMap entryKey2Entry = new HashMap();
		ArrayList entriesWithCrossReference = new ArrayList();
		for (Iterator entryIt = bibtexFile.getEntries().iterator(); entryIt.hasNext();) {
			BibtexAbstractEntry abstractEntry = (BibtexAbstractEntry) entryIt.next();
			if (!(abstractEntry instanceof BibtexEntry))
				continue;
			BibtexEntry entry = (BibtexEntry) abstractEntry;
			entryKey2Entry.put(entry.getEntryKey().toLowerCase(), abstractEntry);
			if (entry.getFields().containsKey("crossref")) {
				entriesWithCrossReference.add(entry);
			}
		}
		for (Iterator entryIt = entriesWithCrossReference.iterator(); entryIt.hasNext();) {
			BibtexEntry entry = (BibtexEntry) entryIt.next();
			String crossrefKey = ((BibtexString) entry.getFields().get("crossref")).getContent().toLowerCase();
			//entry.undefineField("crossref"); habe diese Zeile rausgenommen, damit wir an das Feld noch drankommen, falls es 
			BibtexEntry crossrefEntry = (BibtexEntry) entryKey2Entry.get(crossrefKey);
			if (crossrefEntry == null)
				throwExpansionException("Crossref key not found: \"" + crossrefKey + "\"");
			if (crossrefEntry.getFields().containsKey("crossref"))
				throwExpansionException(
					"Nested crossref: \""
						+ crossrefKey
						+ "\" is crossreferenced but crossreferences itself \""
						+ ((BibtexString) crossrefEntry.getFields().get("crossref")).getContent()
						+ "\"");
			Map entryFields = entry.getFields();
			Map crossrefFields = crossrefEntry.getFields();
			for (Iterator fieldIt = crossrefFields.keySet().iterator(); fieldIt.hasNext();) {
				String key = (String) fieldIt.next();
				if (!entryFields.containsKey(key)) {
					entry.setField(key, (BibtexAbstractValue) crossrefFields.get(key));
				}
			}
		}
		finishExpansion();
	}
}
