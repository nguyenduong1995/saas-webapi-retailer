/**
 * PromotionLimitationDetailDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * PromotionLimitationDetailDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class PromotionLimitationDto extends StatusTimestampDto implements Serializable {

    private static final long serialVersionUID = -4734839696476943614L;
    
    @JsonProperty("promotionId")
    private Integer promotionId;
    
    @JsonProperty("rewardFormatId")
    private Integer rewardFormatId;
    
    @JsonProperty("orderNumber")
    private Integer orderNumber;
    
    @JsonProperty("promotionLimitationItems")
    private List<PromotionLimitationItemDto> promotionLimitationItems;

    /**
     * get value of <b>promotionId</b>.
     * @return the promotionId
     */
    public Integer getPromotionId() {
        return promotionId;
    }

    /**
     * Set value to <b>promotionId</b>.
     * @param promotionId the promotionId to set
     */
    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    /**
     * get value of <b>rewardFormatId</b>.
     * @return the rewardFormatId
     */
    public Integer getRewardFormatId() {
        return rewardFormatId;
    }

    /**
     * Set value to <b>rewardFormatId</b>.
     * @param rewardFormatId the rewardFormatId to set
     */
    public void setRewardFormatId(Integer rewardFormatId) {
        this.rewardFormatId = rewardFormatId;
    }

    /**
     * get value of <b>orderNumber</b>.
     * @return the orderNumber
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * Set value to <b>orderNumber</b>.
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * get value of <b>promotionLimitationItems</b>.
     * @return the promotionLimitationItems
     */
    public List<PromotionLimitationItemDto> getPromotionLimitationItems() {
        return promotionLimitationItems;
    }

    /**
     * Set value to <b>promotionLimitationItems</b>.
     * @param promotionLimitationItems the promotionLimitationItems to set
     */
    public void setPromotionLimitationItems(List<PromotionLimitationItemDto> promotionLimitationItems) {
        this.promotionLimitationItems = promotionLimitationItems;
    }

    @Override
    public String toString() {
        return "PromotionLimitationDto [promotionId=" + promotionId + ", rewardFormatId=" + rewardFormatId + ", orderNumber=" + orderNumber
                + ", promotionLimitationItems=" + promotionLimitationItems + "]";
    }
    
}
