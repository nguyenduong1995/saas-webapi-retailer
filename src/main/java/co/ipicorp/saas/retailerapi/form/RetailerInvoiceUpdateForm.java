/**
 * RetailerInvoiceUpdateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;

/**
 * 
 * RetailerInvoiceUpdateForm. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class RetailerInvoiceUpdateForm implements Serializable {

	private static final long serialVersionUID = 4047736096617990780L;

	private String tel;

    private String bankName;
    
    private String bankBranch;
    
    private String bankAccountNo;
    
    private String bankAccountName;
    
    public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	@Override
    public String toString() {
        return "RetailerInvoiceUpdateForm [tel=" + tel + ", bankName=" + bankName + ", bankBranch=" + bankBranch + ", bankAccountNo=" 
        		+ bankAccountNo + ", bankAccountName=" + bankAccountName + "]";
    }

}
