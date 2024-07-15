/**
 * RetailerUpdateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.form;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * RetailerUpdateForm. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class RetailerUpdateForm implements Serializable {
    
    private static final long serialVersionUID = 2068446990008807706L;
    
    @JsonProperty("retailerSign")
    private String retailerSign;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @DateTimeFormat(iso = ISO.DATE)
    @JsonProperty("birthday")
    private LocalDate birthday;

    @JsonProperty("mobile")
    private String mobile;
    
    @JsonProperty("address")
    private String address;

    @JsonProperty("wardId")
    private Integer wardId;

    @JsonProperty("cityId")
    private Integer cityId;

    @JsonProperty("districtId")
    private Integer districtId;
    
    public String getRetailerSign() {
        return retailerSign;
    }

    public void setRetailerSign(String retailerSign) {
        this.retailerSign = retailerSign;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @Override
    public String toString() {
        return "RetailerUpdateForm [retailerSign=" + retailerSign + ", fullName=" + fullName + ", email=" + email + ", birthday=" 
        		+ birthday + ", mobile=" + mobile + ", address=" + address + ", wardId=" + wardId + ", cityId=" + cityId + ", districtId=" + districtId + "]";
    }

}
