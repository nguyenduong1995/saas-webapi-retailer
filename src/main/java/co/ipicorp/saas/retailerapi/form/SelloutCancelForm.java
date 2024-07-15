/**
 * SelloutCancelForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

/**
 * SelloutCancelForm.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class SelloutCancelForm extends SelloutIdForm {

    private static final long serialVersionUID = -7644885759218071141L;
    
    private String reason = "Không có lý do.";

    /**
     * get value of <b>reason</b>.
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set value to <b>reason</b>.
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "SelloutCancelForm [reason=" + reason + ", getSelloutId()=" + getSelloutId() + "]";
    }
    
}
