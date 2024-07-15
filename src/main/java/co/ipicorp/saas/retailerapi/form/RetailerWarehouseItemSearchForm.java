/**
 * RetailerWarehouseItemSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * RetailerWarehouseItemSearchForm.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class RetailerWarehouseItemSearchForm extends LimittedForm {

    private static final long serialVersionUID = -6874820027245053504L;
    
    private Boolean existed;

    /**
     * get value of <b>existed</b>.
     * @return the existed
     */
    public Boolean getExisted() {
        return existed;
    }

    /**
     * Set value to <b>existed</b>.
     * @param existed the existed to set
     */
    public void setExisted(Boolean existed) {
        this.existed = existed;
    }
    
}
