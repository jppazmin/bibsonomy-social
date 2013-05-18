/**
 *
 *  BibSonomy-OpenAccess - Check Open Access Policies for Publications
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.unikassel.puma.openaccess.sherparomeo;

public interface SherpaRomeoInterface {
    
    
    /**
     * Performs a search for the publisher. 
     * 
     * @param publisher
     * @param qtype     possible values:
     *                  - all   All the strings in publisher must appear in the publisher’s name, 
     *                          but they may be in any order or location. This is the default qtype
     *                  - any   Publishers’ names must include one or more of the publisher strings
     *                  - exact The publisher string must appear as an intact ‘phrase’ somewhere in the
     *                          pblisher’s name
     * @return
     */
    public String getPolicyForPublisher(String publisher, String qtype);
    
    
    /**
     * Performs a search for the journal title. Qtype can be exact or contains
     * 
     * @param jtitle
     * @param qtype     possible values:
     *                  - starts    The pub string must appear as an intact 'phrase' somewhere in the
     *                              publisher’s name
     *                  - contains  The jtitle string may appear anywhere in the journal title
     *                  - exact     Full title (excluding any added place of publication) must equal jtitle
     * @return
     */
    public String getPolicyForJournal(String jtitle, String qtype);
}
