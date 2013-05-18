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
    "fundername",
    "funderacronym",
    "julieturl"
})
@XmlRootElement(name = "funder")
public class Funder {

    @XmlElement(required = true)
    protected String fundername;
    protected String funderacronym;
    @XmlElement(required = true)
    protected String julieturl;

    /**
     * Gets the value of the fundername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundername() {
        return fundername;
    }

    /**
     * Sets the value of the fundername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundername(String value) {
        this.fundername = value;
    }

    /**
     * Gets the value of the funderacronym property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunderacronym() {
        return funderacronym;
    }

    /**
     * Sets the value of the funderacronym property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunderacronym(String value) {
        this.funderacronym = value;
    }

    /**
     * Gets the value of the julieturl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJulieturl() {
        return julieturl;
    }

    /**
     * Sets the value of the julieturl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJulieturl(String value) {
        this.julieturl = value;
    }

}
