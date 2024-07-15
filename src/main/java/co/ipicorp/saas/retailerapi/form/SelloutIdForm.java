/**
 * SelloutIdForm.java
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

/**
 * SelloutIdForm.
 * 
 * @author nt.duong
 */
public class SelloutIdForm implements Serializable {

	private static final long serialVersionUID = 7511200413748659075L;

	private Integer selloutId;
    
    /**
     * get value of <b>selloutId</b>.
     * 
     * @return the selloutId
     */
	public Integer getSelloutId() {
		return selloutId;
	}

	/**
     * Set value to <b>selloutId</b>.
     * 
     * @param selloutId
     *            the selloutId to set
     */
	public void setSelloutId(Integer selloutId) {
		this.selloutId = selloutId;
	}

	@Override
	public String toString() {
		return "LoginForm [selloutId=" + selloutId + "]";
	}

}
