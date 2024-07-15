/**
 * RetailerWarehouseItemHistorySearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * RetailerWarehouseItemHistorySearchForm. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class RetailerWarehouseItemHistorySearchForm extends LimittedForm implements Serializable {
    private static final long serialVersionUID = -5648463266294813108L;

    private Integer productVariationId;

    private String fromDate;

    private String toDate;
  
    /**
     * get value of <b>productVariationId</b>.
     * 
     * @return the productVariationId
     */
    public Integer getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * 
     * @param productVariationId
     *            the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

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

    @Override
    public String toString() {
        return "RetailerWarehouseItemHistorySearchForm [productVariationId=" + productVariationId + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
    }

}
