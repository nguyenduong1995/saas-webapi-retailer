/**
 * RetailerImageForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * RetailerImageForm. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class RetailerImageForm implements Serializable {
    
    private static final long serialVersionUID = 2068446990008807706L;
    
    @JsonProperty("image")
    private String image;

    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
    public String toString() {
        return "RetailerImageForm [image=" + image + "]";
    }

}
