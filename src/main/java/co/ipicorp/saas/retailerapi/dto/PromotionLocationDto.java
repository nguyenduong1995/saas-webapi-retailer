/**
 * PromotionLocationDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * PromotionLocationDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class PromotionLocationDto implements Serializable {

    private static final long serialVersionUID = 8126767323593560624L;
    
    @JsonProperty("promotionLocationNamesForDisplay")
    private String promotionLocationNamesForDisplay;
    
    @JsonProperty("promotionLocationIds")
    private List<Integer> promotionLocationIds;
    
    @JsonProperty("promotionLocationDetail")
    private List<PromotionLocationDetailDto> promotionLocationDetail;
    
    /**
     * get value of <b>promotionLocationNamesForDisplay</b>.
     * @return the promotionLocationNamesForDisplay
     */
    public String getPromotionLocationNamesForDisplay() {
        return promotionLocationNamesForDisplay;
    }

    /**
     * Set value to <b>promotionLocationNamesForDisplay</b>.
     * @param promotionLocationNamesForDisplay the promotionLocationNamesForDisplay to set
     */
    public void setPromotionLocationNamesForDisplay(String promotionLocationNamesForDisplay) {
        this.promotionLocationNamesForDisplay = promotionLocationNamesForDisplay;
    }

    /**
     * get value of <b>promotionLocationIds</b>.
     * @return the promotionLocationIds
     */
    public List<Integer> getPromotionLocationIds() {
        return promotionLocationIds;
    }

    /**
     * Set value to <b>promotionLocationIds</b>.
     * @param promotionLocationIds the promotionLocationIds to set
     */
    public void setPromotionLocationIds(List<Integer> promotionLocationIds) {
        this.promotionLocationIds = promotionLocationIds;
    }

    /**
     * get value of <b>promotionLocationDetail</b>.
     * @return the promotionLocationDetail
     */
    public List<PromotionLocationDetailDto> getPromotionLocationDetail() {
        return promotionLocationDetail;
    }

    /**
     * Set value to <b>promotionLocationDetail</b>.
     * @param promotionLocationDetail the promotionLocationDetail to set
     */
    public void setPromotionLocationDetail(List<PromotionLocationDetailDto> promotionLocationDetail) {
        this.promotionLocationDetail = promotionLocationDetail;
    }

    @Override
    public String toString() {
        return "PromotionLocationDto [promotionLocationNamesForDisplay=" + promotionLocationNamesForDisplay + ", promotionLocationIds=" + promotionLocationIds
                + ", promotionLocationDetail=" + promotionLocationDetail + "]";
    }
    
}
