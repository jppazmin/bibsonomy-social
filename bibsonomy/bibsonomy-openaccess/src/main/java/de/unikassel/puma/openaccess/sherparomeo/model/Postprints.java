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

package de.unikassel.puma.openaccess.sherparomeo.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "postarchiving",
    "postrestrictions"
})
@XmlRootElement(name = "postprints")
public class Postprints {

    @XmlElement(required = true)
    protected String postarchiving;
    protected List<Postrestrictions> postrestrictions;

    /**
     * Gets the value of the postarchiving property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostarchiving() {
        return postarchiving;
    }

    /**
     * Sets the value of the postarchiving property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostarchiving(String value) {
        this.postarchiving = value;
    }

    /**
     * Gets the value of the postrestrictions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the postrestrictions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPostrestrictions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Postrestrictions }
     * 
     * 
     */
    public List<Postrestrictions> getPostrestrictions() {
        if (postrestrictions == null) {
            postrestrictions = new ArrayList<Postrestrictions>();
        }
        return this.postrestrictions;
    }

}
