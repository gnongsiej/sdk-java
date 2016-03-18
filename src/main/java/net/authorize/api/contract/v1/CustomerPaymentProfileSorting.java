//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.03 at 05:32:06 PM IST 
//


package net.authorize.api.contract.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerPaymentProfileSorting complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerPaymentProfileSorting">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderBy" type="{AnetApi/xml/v1/schema/AnetApiSchema.xsd}CustomerPaymentProfileOrderFieldEnum"/>
 *         &lt;element name="orderDescending" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerPaymentProfileSorting", propOrder = {
    "orderBy",
    "orderDescending"
})
public class CustomerPaymentProfileSorting {

    @XmlElement(required = true)
    protected CustomerPaymentProfileOrderFieldEnum orderBy;
    protected boolean orderDescending;

    /**
     * Gets the value of the orderBy property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerPaymentProfileOrderFieldEnum }
     *     
     */
    public CustomerPaymentProfileOrderFieldEnum getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the value of the orderBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerPaymentProfileOrderFieldEnum }
     *     
     */
    public void setOrderBy(CustomerPaymentProfileOrderFieldEnum value) {
        this.orderBy = value;
    }

    /**
     * Gets the value of the orderDescending property.
     * 
     */
    public boolean isOrderDescending() {
        return orderDescending;
    }

    /**
     * Sets the value of the orderDescending property.
     * 
     */
    public void setOrderDescending(boolean value) {
        this.orderDescending = value;
    }

}