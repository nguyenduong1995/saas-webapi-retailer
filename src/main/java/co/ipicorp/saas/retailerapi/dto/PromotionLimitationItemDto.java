/**
 * PromotionLimitationDetailDto.java
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

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * PromotionLimitationDetailDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class PromotionLimitationItemDto extends StatusTimestampDto implements Serializable {
    private static final long serialVersionUID = -5534267357130392452L;

    @JsonProperty("promotionId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer promotionId;
    
    @JsonProperty("limitationId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer limitationId;
    
    @JsonProperty("limitationOrderNumber")
    private Integer limitationOrderNumber;
    
    @JsonProperty("conditionFixValue")
    private Double conditionFixValue;
    
    @JsonProperty("conditionRangeFrom")
    private Double conditionRangeFrom;
    
    @JsonProperty("conditionRangeTo")
    private Double conditionRangeTo;
    
    @JsonProperty("rewardFormatId")
    private Integer rewardFormatId;
    
    @JsonProperty("rewardPercent")
    private Double rewardPercent;

    @JsonProperty("rewardValue")
    private Double rewardValue;
    
    @JsonProperty("productGroupId")
    private Integer productGroupId;
    
    @JsonProperty("promotionProductGroupName")
    private String promotionProductGroupName;
    
    @JsonProperty("promotionLimitationItemRewardProducts")
    @JsonInclude(value = Include.NON_NULL)
    private List<PromotionLimitationItemRewardProductDto> promotionLimitationItemRewardProducts;

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
     * get value of <b>limitationId</b>.
     * @return the limitationId
     */
    public Integer getLimitationId() {
        return limitationId;
    }

    /**
     * Set value to <b>limitationId</b>.
     * @param limitationId the limitationId to set
     */
    public void setLimitationId(Integer limitationId) {
        this.limitationId = limitationId;
    }

    /**
     * get value of <b>limitationOrderNumber</b>.
     * @return the limitationOrderNumber
     */
    public Integer getLimitationOrderNumber() {
        return limitationOrderNumber;
    }

    /**
     * Set value to <b>limitationOrderNumber</b>.
     * @param limitationOrderNumber the limitationOrderNumber to set
     */
    public void setLimitationOrderNumber(Integer limitationOrderNumber) {
        this.limitationOrderNumber = limitationOrderNumber;
    }

    /**
     * get value of <b>conditionFixValue</b>.
     * @return the conditionFixValue
     */
    public Double getConditionFixValue() {
        return conditionFixValue;
    }

    /**
     * Set value to <b>conditionFixValue</b>.
     * @param conditionFixValue the conditionFixValue to set
     */
    public void setConditionFixValue(Double conditionFixValue) {
        this.conditionFixValue = conditionFixValue;
    }

    /**
     * get value of <b>conditionRangeFrom</b>.
     * @return the conditionRangeFrom
     */
    public Double getConditionRangeFrom() {
        return conditionRangeFrom;
    }

    /**
     * Set value to <b>conditionRangeFrom</b>.
     * @param conditionRangeFrom the conditionRangeFrom to set
     */
    public void setConditionRangeFrom(Double conditionRangeFrom) {
        this.conditionRangeFrom = conditionRangeFrom;
    }

    /**
     * get value of <b>conditionRangeTo</b>.
     * @return the conditionRangeTo
     */
    public Double getConditionRangeTo() {
        return conditionRangeTo;
    }

    /**
     * Set value to <b>conditionRangeTo</b>.
     * @param conditionRangeTo the conditionRangeTo to set
     */
    public void setConditionRangeTo(Double conditionRangeTo) {
        this.conditionRangeTo = conditionRangeTo;
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
     * get value of <b>productGroupId</b>.
     * @return the productGroupId
     */
    public Integer getProductGroupId() {
        return productGroupId;
    }

    /**
     * Set value to <b>productGroupId</b>.
     * @param productGroupId the productGroupId to set
     */
    public void setProductGroupId(Integer productGroupId) {
        this.productGroupId = productGroupId;
    }

    /**
     * get value of <b>promotionProductGroupName</b>.
     * @return the promotionProductGroupName
     */
    public String getPromotionProductGroupName() {
        return promotionProductGroupName;
    }

    /**
     * Set value to <b>promotionProductGroupName</b>.
     * @param promotionProductGroupName the promotionProductGroupName to set
     */
    public void setPromotionProductGroupName(String promotionProductGroupName) {
        this.promotionProductGroupName = promotionProductGroupName;
    }

    /**
     * get value of <b>promotionLimitationItemRewardProducts</b>.
     * @return the promotionLimitationItemRewardProducts
     */
    public List<PromotionLimitationItemRewardProductDto> getPromotionLimitationItemRewardProducts() {
        return promotionLimitationItemRewardProducts;
    }

    /**
     * Set value to <b>promotionLimitationItemRewardProducts</b>.
     * @param promotionLimitationItemRewardProducts the promotionLimitationItemRewardProducts to set
     */
    public void setPromotionLimitationItemRewardProducts(List<PromotionLimitationItemRewardProductDto> promotionLimitationItemRewardProducts) {
        this.promotionLimitationItemRewardProducts = promotionLimitationItemRewardProducts;
    }

    @Override
    public String toString() {
        return "PromotionLimitationItemDto [promotionId=" + promotionId + ", limitationId=" + limitationId + ", limitationOrderNumber=" + limitationOrderNumber
                + ", conditionFixValue=" + conditionFixValue + ", conditionRangeFrom=" + conditionRangeFrom + ", conditionRangeTo=" + conditionRangeTo
                + ", rewardFormatId=" + rewardFormatId + ", rewardPercent=" + rewardPercent + ", rewardValue=" + rewardValue + ", productGroupId="
                + productGroupId + ", promotionProductGroupName=" + promotionProductGroupName + ", PromotionLimitationItemRewardProducts="
                + promotionLimitationItemRewardProducts + "]";
    }
    
}
