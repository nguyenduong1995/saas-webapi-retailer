/**
 * SellinItemForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

/**
 * SellinItemForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class SellinItemForm implements Serializable {

	private static final long serialVersionUID = 3179165524953993514L;

	private Integer productVariationId;
	
    private Integer amount;
    
	public Integer getProductVariationId() {
		return productVariationId;
	}

	public void setProductVariationId(Integer productVariationId) {
		this.productVariationId = productVariationId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
    public String toString() {
        return "SellinItemForm [productVariationId=" + productVariationId + "amount=" + amount + "]";
    }
	
}
