/**
 *
 *  BibSonomy-Layout - Layout engine for the webapp.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
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

package org.bibsonomy.layout.jabref;


import net.sf.jabref.export.layout.LayoutFormatter;

/**
 * Create a valid author-string to get the right format to the DBLP XML
 * 
 */
public class SeparatedAuthorsFormatter implements LayoutFormatter {

	@Override
	public String format(String fieldText) {
		final StringBuffer fin = new StringBuffer();

		//if the string contains an and it will be splitted else theres only one 
		//author!
        final String[] names = fieldText.split(" and ");
        for (int i=0; i<names.length; i++)
        {
          fin.append("<author>" + names[i]+ "</author>");
          if (i < names.length -1)
        	  fin.append("\n");
        }

        return fin.toString();
	}
}