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

package org.bibsonomy.layout.csl.model;


/**
 * Models a Person according to CSL input specs. See
 * http://gsl-nagoya-u.net/http/pub/citeproc-doc.html#names
 * 
 * @author Dominik Benz, benz@cs.uni-kassel.de
 * @version $Id: Person.java,v 1.2 2011-07-26 13:44:49 bibsonomy Exp $
 */
public class Person {
   
    // family name
    private String family;
    // given name
    private String given;
    // dropping particle
    private String dropping_particle;
    // non-dropping particle
    private String non_dropping_particle;
    // literal version of name
    private String literal;
    // name suffix
    private String suffix;
    // comma suffix
    private String comma_suffix;
    // whether to use static ordering or not
    private Integer static_ordering;

    //*************************************************
    // getter / setter
    //*************************************************    
    
    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public String getDropping_particle() {
        return dropping_particle;
    }

    public void setDropping_particle(String dropping_particle) {
        this.dropping_particle = dropping_particle;
    }

    public String getNon_dropping_particle() {
        return non_dropping_particle;
    }

    public void setNon_dropping_particle(String non_dropping_particle) {
        this.non_dropping_particle = non_dropping_particle;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getComma_suffix() {
        return comma_suffix;
    }

    public void setComma_suffix(String comma_suffix) {
        this.comma_suffix = comma_suffix;
    }

    public Integer getStatic_ordering() {
        return static_ordering;
    }

    public void setStatic_ordering(Integer static_ordering) {
        this.static_ordering = static_ordering;
    }
        
}
