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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "alias",
    "homeurl",
    "preprints",
    "postprints",
    "conditions",
    "mandates",
    "paidaccess",
    "copyrightlinks",
    "romeocolour",
    "dateadded",
    "dateupdated"
})
@XmlRootElement(name = "publisher")
public class Publisher {

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String id;
    @XmlElement(required = true)
    protected String name;
    protected String alias;
    @XmlElement(required = true)
    protected String homeurl;
    @XmlElement(required = true)
    protected Preprints preprints;
    @XmlElement(required = true)
    protected Postprints postprints;
    @XmlElement(required = true)
    protected Conditions conditions;
    @XmlElement(required = true)
    protected Mandates mandates;
    @XmlElement(required = true)
    protected Paidaccess paidaccess;
    @XmlElement(required = true)
    protected Copyrightlinks copyrightlinks;
    @XmlElement(required = true)
    protected String romeocolour;
    protected String dateadded;
    protected String dateupdated;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the alias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the value of the alias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Gets the value of the homeurl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeurl() {
        return homeurl;
    }

    /**
     * Sets the value of the homeurl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeurl(String value) {
        this.homeurl = value;
    }

    /**
     * Gets the value of the preprints property.
     * 
     * @return
     *     possible object is
     *     {@link Preprints }
     *     
     */
    public Preprints getPreprints() {
        return preprints;
    }

    /**
     * Sets the value of the preprints property.
     * 
     * @param value
     *     allowed object is
     *     {@link Preprints }
     *     
     */
    public void setPreprints(Preprints value) {
        this.preprints = value;
    }

    /**
     * Gets the value of the postprints property.
     * 
     * @return
     *     possible object is
     *     {@link Postprints }
     *     
     */
    public Postprints getPostprints() {
        return postprints;
    }

    /**
     * Sets the value of the postprints property.
     * 
     * @param value
     *     allowed object is
     *     {@link Postprints }
     *     
     */
    public void setPostprints(Postprints value) {
        this.postprints = value;
    }

    /**
     * Gets the value of the conditions property.
     * 
     * @return
     *     possible object is
     *     {@link Conditions }
     *     
     */
    public Conditions getConditions() {
        return conditions;
    }

    /**
     * Sets the value of the conditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Conditions }
     *     
     */
    public void setConditions(Conditions value) {
        this.conditions = value;
    }

    /**
     * Gets the value of the mandates property.
     * 
     * @return
     *     possible object is
     *     {@link Mandates }
     *     
     */
    public Mandates getMandates() {
        return mandates;
    }

    /**
     * Sets the value of the mandates property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mandates }
     *     
     */
    public void setMandates(Mandates value) {
        this.mandates = value;
    }

    /**
     * Gets the value of the paidaccess property.
     * 
     * @return
     *     possible object is
     *     {@link Paidaccess }
     *     
     */
    public Paidaccess getPaidaccess() {
        return paidaccess;
    }

    /**
     * Sets the value of the paidaccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link Paidaccess }
     *     
     */
    public void setPaidaccess(Paidaccess value) {
        this.paidaccess = value;
    }

    /**
     * Gets the value of the copyrightlinks property.
     * 
     * @return
     *     possible object is
     *     {@link Copyrightlinks }
     *     
     */
    public Copyrightlinks getCopyrightlinks() {
        return copyrightlinks;
    }

    /**
     * Sets the value of the copyrightlinks property.
     * 
     * @param value
     *     allowed object is
     *     {@link Copyrightlinks }
     *     
     */
    public void setCopyrightlinks(Copyrightlinks value) {
        this.copyrightlinks = value;
    }

    /**
     * Gets the value of the romeocolour property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRomeocolour() {
        return romeocolour;
    }

    /**
     * Sets the value of the romeocolour property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRomeocolour(String value) {
        this.romeocolour = value;
    }

    /**
     * Gets the value of the dateadded property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateadded() {
        return dateadded;
    }

    /**
     * Sets the value of the dateadded property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateadded(String value) {
        this.dateadded = value;
    }

    /**
     * Gets the value of the dateupdated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateupdated() {
        return dateupdated;
    }

    /**
     * Sets the value of the dateupdated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateupdated(String value) {
        this.dateupdated = value;
    }

}
