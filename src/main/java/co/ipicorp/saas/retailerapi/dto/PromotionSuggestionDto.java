/**
 * PromotionSuggestionDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * PromotionSuggestionDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
@JsonInclude(value = Include.NON_NULL)
public class PromotionSuggestionDto implements Serializable {

    private static final long serialVersionUID = 4758949196242355740L;
    
    @JsonProperty("productVariationId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer productVariationId;

    @JsonProperty("promotions")
    @JsonInclude(value = Include.NON_NULL)
    private List<PromotionDto> promotions;
    
    @JsonProperty("conditionAmount")
    @JsonInclude(value = Include.NON_NULL)
    private Integer conditionAmount;
    
    @JsonProperty("currentAmount")
    @JsonInclude(value = Include.NON_NULL)
    private Integer currentAmount;
    
    @JsonProperty("remainingAmount")
    @JsonInclude(value = Include.NON_NULL)
    private Integer remainingAmount;
    
    @JsonProperty("rewardPercent")
    @JsonInclude(value = Include.NON_NULL)
    private Double rewardPercent;
    
    @JsonProperty("rewardValue")
    @JsonInclude(value = Include.NON_NULL)
    private Double rewardValue;
    
    @JsonProperty("totalBill")
    @JsonInclude(value = Include.NON_NULL)
    private Double totalBill;

    /**
     * get value of <b>promotions</b>.
     * @return the promotions
     */
    public List<PromotionDto> getPromotions() {
        return promotions;
    }

    /**
     * Set value to <b>promotions</b>.
     * @param promotions the promotions to set
     */
    public void setPromotions(List<PromotionDto> promotions) {
        this.promotions = promotions;
    }

    /**
     * get value of <b>conditionAmount</b>.
     * @return the conditionAmount
     */
    public Integer getConditionAmount() {
        return conditionAmount;
    }

    /**
     * Set value to <b>conditionAmount</b>.
     * @param conditionAmount the conditionAmount to set
     */
    public void setConditionAmount(Integer conditionAmount) {
        this.conditionAmount = conditionAmount;
    }

    /**
     * get value of <b>currentAmount</b>.
     * @return the currentAmount
     */
    public Integer getCurrentAmount() {
        return currentAmount;
    }

    /**
     * Set value to <b>currentAmount</b>.
     * @param currentAmount the currentAmount to set
     */
    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }

    /**
     * get value of <b>remainingAmount</b>.
     * @return the remainingAmount
     */
    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    /**
     * Set value to <b>remainingAmount</b>.
     * @param remainingAmount the remainingAmount to set
     */
    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    /**
     * get value of <b>productVariationId</b>.
     * @return the productVariationId
     */
    public Integer getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * @param productVariationId the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }
    
    /**
     * get value of <b>rewardPercent</b>.
     * @return the rewardPercent
     */
    public Double getRewardPercent() {
        return rewardPercent;
    }

    /**
     * Set value to <b>rewardPercent</b>.
     * @param rewardPercent the rewardPercent to set
     */
    public void setRewardPercent(Double rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    /**
     * get value of <b>rewardValue</b>.
     * @return the rewardValue
     */
    public Double getRewardValue() {
        return rewardValue;
    }

    /**
     * Set value to <b>rewardValue</b>.
     * @param rewardValue the rewardValue to set
     */
    public void setRewardValue(Double rewardValue) {
        this.rewardValue = rewardValue;
    }

    /**
     * get value of <b>totalBill</b>.
     * @return the totalBill
     */
    public Double getTotalBill() {
        return totalBill;
    }

    /**
     * Set value to <b>totalBill</b>.
     * @param totalBill the totalBill to set
     */
    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }

    @Override
    public String toString() {
        return "PromotionSuggestionDto [productVariationId=" + productVariationId + ", promotions=" + promotions + ", conditionAmount=" + conditionAmount
                + ", currentAmount=" + currentAmount + ", remainingAmount=" + remainingAmount + ", rewardPercent=" + rewardPercent + ", rewardValue="
                + rewardValue + ", totalBill=" + totalBill + "]";
    }

}
