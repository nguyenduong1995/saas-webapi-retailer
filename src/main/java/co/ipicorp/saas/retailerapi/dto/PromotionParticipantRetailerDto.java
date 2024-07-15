/**
 * PromotionParticipantRetailerDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import java.io.Serializable;
import java.util.List;

/**
 * PromotionParticipantRetailerDto.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class PromotionParticipantRetailerDto implements Serializable {

    private static final long serialVersionUID = -9127996801173871889L;
    
    private List<Integer> retailerIds;
    private String retailerNames;
    /**
     * get value of <b>retailerIds</b>.
     * @return the retailerIds
     */
    public List<Integer> getRetailerIds() {
        return retailerIds;
    }
    /**
     * Set value to <b>retailerIds</b>.
     * @param retailerIds the retailerIds to set
     */
    public void setRetailerIds(List<Integer> retailerIds) {
        this.retailerIds = retailerIds;
    }
    /**
     * get value of <b>retailerNames</b>.
     * @return the retailerNames
     */
    public String getRetailerNames() {
        return retailerNames;
    }
    /**
     * Set value to <b>retailerNames</b>.
     * @param retailerNames the retailerNames to set
     */
    public void setRetailerNames(String retailerNames) {
        this.retailerNames = retailerNames;
    }
    
}
