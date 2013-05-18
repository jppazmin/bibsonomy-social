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
 * Created on Jul 14, 2004
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.parser;

/**
 * These constants determine how the parser deals with multiple values for the
 * same field. For example, consider the following bibtex entry, which has
 * multiple (2) values for the url field.
 * 
 * <pre>
 * 
 *          &#064;inproceedings{diwan98typebased,
 *            year=1998,
 *            pages={106-117},
 *            title={Type-Based Alias Analysis},
 *            url={citeseer.nj.nec.com/diwan98typebased.html},
 *            booktitle={SIGPLAN Conference on Programming Language Design and Implementation},
 *            author={Amer Diwan and Kathryn S. McKinley and J. Eliot B. Moss},
 *            url={http://www-plan.cs.colorado.edu/diwan/},
 *         }
 *  
 * </pre>
 * 
 * 
 * @author henkel
 */
public class BibtexMultipleFieldValuesPolicy {

    private BibtexMultipleFieldValuesPolicy() {
    }

    /**
     * If a field in a bibtex entry has multiple values, then keep the first
     * value and ignore the other values - this is what bibtex does, so it's the
     * default. For the example above, this means the parser will use
     * 
     * <pre>
     * 
     *  
     *   
     *     {citeseer.nj.nec.com/diwan98typebased.html}
     *    
     *   
     *  
     * </pre>.
     */
    public static final int KEEP_FIRST = 0;
    
    
    /**
     * If a field in a bibtex entry has multiple values, then keep the last
     * value and ignore the other values. For the example above, this means the
     * parser will use
     * 
     * <pre>
     * 
     *  
     *   
     *     {http://www-plan.cs.colorado.edu/diwan/}
     *    
     *   
     *  
     * </pre>.
     */
    public static final int KEEP_LAST = 1;


    /**
     * If a field in a bibtex entry has multiple values, then keep all of them.
     * In this case, we'll use instances of BibtexMultipleValues as field values
     * whenever there is more than one value.
     * 
     * @see bibtex.dom.BibtexMultipleValues
     */
    public static final int KEEP_ALL = 2;

}