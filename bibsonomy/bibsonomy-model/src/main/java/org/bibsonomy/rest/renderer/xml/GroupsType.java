//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.27 at 01:17:20 PM CEST 
//


package org.bibsonomy.rest.renderer.xml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GroupsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroupsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="group" type="{}GroupType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="start" use="required" type="{}positiveIntegerIncludingZero" />
 *       &lt;attribute name="end" use="required" type="{}positiveIntegerIncludingZero" />
 *       &lt;attribute name="next" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupsType", propOrder = {
    "group"
})
public class GroupsType {

    protected List<GroupType> group;
    @XmlAttribute(required = true)
    protected BigInteger start;
    @XmlAttribute(required = true)
    protected BigInteger end;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String next;

    /**
     * Gets the value of the group property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the group property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GroupType }
     * 
     * 
     */
    public List<GroupType> getGroup() {
        if (group == null) {
            group = new ArrayList<GroupType>();
        }
        return this.group;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStart(BigInteger value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEnd(BigInteger value) {
        this.end = value;
    }

    /**
     * Gets the value of the next property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNext() {
        return next;
    }

    /**
     * Sets the value of the next property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNext(String value) {
        this.next = value;
    }

}
