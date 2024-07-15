/**
 * RetailerSessionInfo.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.security;

import co.ipicorp.saas.nrms.model.Retailer;

import grass.micro.apps.web.security.SessionInfo;

/**
 * RetailerSessionInfo. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class RetailerSessionInfo extends SessionInfo {
    private static final long serialVersionUID = -5357680670533819229L;
    public static final String APP_RETAILER_SESSION_ID_KEY = "__a_r_s_id";
    private Retailer retailer;

    /**
     * get value of <b>retailer</b>.
     * 
     * @return the retailer
     */
    public Retailer getRetailer() {
        return retailer;
    }

    /**
     * Set value to <b>retailer</b>.
     * 
     * @param retailer
     *            the retailer to set
     */
    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

}
