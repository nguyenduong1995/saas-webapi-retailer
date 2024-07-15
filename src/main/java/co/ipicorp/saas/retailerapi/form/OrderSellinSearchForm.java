/**
 * OrderSellinSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * OrderSellinSearchForm. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class OrderSellinSearchForm extends LimittedForm {

    private static final long serialVersionUID = -3340674164062694703L;

    private String fromDate;

    private String toDate;
    
    private String orderCode;

    /**
     * get value of <b>fromDate</b>.
     * 
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Set value to <b>fromDate</b>.
     * 
     * @param fromDate
     *            the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * get value of <b>toDate</b>.
     * 
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * Set value to <b>toDate</b>.
     * 
     * @param toDate
     *            the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * get value of <b>orderCode</b>.
     * @return the orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * Set value to <b>orderCode</b>.
     * @param orderCode the orderCode to set
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Override
    public String toString() {
        return "OrderSellinSearchForm [fromDate=" + fromDate + ", toDate=" + toDate + ", orderCode=" + orderCode + "]";
    }

}
