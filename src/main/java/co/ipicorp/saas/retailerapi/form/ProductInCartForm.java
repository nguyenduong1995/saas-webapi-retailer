/**
 * ProductInCartForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * ProductInCartForm.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class ProductInCartForm implements Serializable {

    private static final long serialVersionUID = -4326841098462950033L;
    
    @JsonProperty("productVariationId")
    private Integer productVariationId;
    
    @JsonProperty("amount")
    private Integer amount;

    /**
     * get value of <b>productVariationId</b>.
     * @return the productVariationId
     */
    public Integer getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * @param productVariationId the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

    /**
     * get value of <b>amount</b>.
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set value to <b>amount</b>.
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ProductInCartForm [productVariationId=" + productVariationId + ", amount=" + amount + "]";
    }
}
