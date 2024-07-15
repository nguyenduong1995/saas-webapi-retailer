/**
 * RetailerChangePasswordForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

/**
 * 
 * RetailerChangePasswordForm. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class RetailerChangePasswordForm implements Serializable {
	
	private static final long serialVersionUID = -916194713424610246L;

	private String currentPassword;

    private String newPassword;
    
    private String confirmPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
    public String toString() {
        return "RetailerChangePasswordForm [currentPassword=" + currentPassword + ", newPassword=" + newPassword + ", confirmPassword=" + confirmPassword + "]";
    }

}
