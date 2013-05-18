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
    "parameters",
    "numhits",
    "apicontrol",
    "outcome",
    "message",
    "licence",
    "licenceurl",
    "disclaimer",
    "timestamp"
})
@XmlRootElement(name = "header")
public class Header {

    protected List<Parameters> parameters;
    @XmlElement(required = true)
    protected String numhits;
    @XmlElement(required = true)
    protected String apicontrol;
    @XmlElement(required = true)
    protected String outcome;
    protected List<Message> message;
    @XmlElement(required = true)
    protected String licence;
    @XmlElement(required = true)
    protected String licenceurl;
    @XmlElement(required = true)
    protected String disclaimer;
    @XmlElement(required = true)
    protected String timestamp;

    /**
     * Gets the value of the parameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameters }
     * 
     * 
     */
    public List<Parameters> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Parameters>();
        }
        return this.parameters;
    }

    /**
     * Gets the value of the numhits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumhits() {
        return numhits;
    }

    /**
     * Sets the value of the numhits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumhits(String value) {
        this.numhits = value;
    }

    /**
     * Gets the value of the apicontrol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApicontrol() {
        return apicontrol;
    }

    /**
     * Sets the value of the apicontrol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApicontrol(String value) {
        this.apicontrol = value;
    }

    /**
     * Gets the value of the outcome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * Sets the value of the outcome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutcome(String value) {
        this.outcome = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the message property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Message }
     * 
     * 
     */
    public List<Message> getMessage() {
        if (message == null) {
            message = new ArrayList<Message>();
        }
        return this.message;
    }

    /**
     * Gets the value of the licence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicence() {
        return licence;
    }

    /**
     * Sets the value of the licence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicence(String value) {
        this.licence = value;
    }

    /**
     * Gets the value of the licenceurl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenceurl() {
        return licenceurl;
    }

    /**
     * Sets the value of the licenceurl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenceurl(String value) {
        this.licenceurl = value;
    }

    /**
     * Gets the value of the disclaimer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * Sets the value of the disclaimer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisclaimer(String value) {
        this.disclaimer = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(String value) {
        this.timestamp = value;
    }

}
