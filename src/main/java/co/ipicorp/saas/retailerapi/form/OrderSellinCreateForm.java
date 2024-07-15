/**
 * OrderSellinCreateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;
import java.util.List;

/**
 * OrderSellinCreateForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class OrderSellinCreateForm implements Serializable {

	private static final long serialVersionUID = 514003173270078947L;
	
	private Integer retailerId;
	
	private List<SellinItemForm> items;

	public Integer getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	public List<SellinItemForm> getItems() {
		return items;
	}

	public void setItems(List<SellinItemForm> items) {
		this.items = items;
	}

	@Override
    public String toString() {
        return "OrderSellinCreateForm [retailerId=" + retailerId + "items=" + items + "]";
    }
	
}
