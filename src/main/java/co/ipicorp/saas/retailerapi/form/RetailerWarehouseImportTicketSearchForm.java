/**
 * RetailerWarehouseImportTicketSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * RetailerWarehouseImportTicketSearchForm.
 * <<< Detail note.
 * @author nguyeth
 * @access public
 */
public class RetailerWarehouseImportTicketSearchForm extends LimittedForm implements Serializable {
	
    private static final long serialVersionUID = 3544121082854942802L;
    
    private Integer retailerId;

    private String importTicketCode;
    
    private String fromDate;
    
    private String toDate;

    /**
     * get value of <b>retailerId</b>.
     * @return the retailerId
     */
    public Integer getRetailerId() {
        return retailerId;
    }

    /**
     * Set value to <b>retailerId</b>.
     * @param retailerId the retailerId to set
     */
    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    /**
     * get value of <b>importTicketCode</b>.
     * @return the importTicketCode
     */
    public String getImportTicketCode() {
        return importTicketCode;
    }

    /**
     * Set value to <b>importTicketCode</b>.
     * @param importTicketCode the importTicketCode to set
     */
    public void setImportTicketCode(String importTicketCode) {
        this.importTicketCode = importTicketCode;
    }

    /**
     * get value of <b>fromDate</b>.
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Set value to <b>fromDate</b>.
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * get value of <b>toDate</b>.
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * Set value to <b>toDate</b>.
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "RetailerWarehouseImportTicketSearchForm [retailerId=" + retailerId + ", importTicketCode=" + importTicketCode + ", fromDate=" + fromDate
                + ", toDate=" + toDate + "]";
    }

}
