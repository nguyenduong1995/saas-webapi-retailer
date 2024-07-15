/**
 * PromotionLimitationItemRewardProductDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * PromotionLimitationItemRewardProductDto. <<< Detail note.
 * 
 * @author thuy nguyen
 * @access public
 */
public class PromotionLimitationItemRewardProductDto extends StatusTimestampDto implements Serializable {

    private static final long serialVersionUID = 1327616898993995250L;

    @JsonProperty("promotionId")
    private Integer promotionId;

    @JsonProperty("limitationId")
    private Integer limitationId;

    @JsonProperty("limitationItemId")
    private Integer limitationItemId;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("productId")
    private Integer productId;

    @JsonProperty("productVariationId")
    private Integer productVariationId;

    @JsonProperty("productVariationName")
    private String productVariationName;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("unitAmount")
    private Integer unitAmount;

    @JsonProperty("packingAmount")
    private Integer packingAmount;

    @JsonProperty("unitId")
    private Integer unitId;

    @JsonProperty("packingid")
    private Integer packingId;

    @JsonProperty("packingExchangeRatio")
    private Integer packingExchangeRatio;

    /**
     * get value of <b>promotionId</b>.
     * 
     * @return the promotionId
     */
    public Integer getPromotionId() {
        return promotionId;
    }

    /**
     * Set value to <b>promotionId</b>.
     * 
     * @param promotionId
     *            the promotionId to set
     */
    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    /**
     * get value of <b>limitationId</b>.
     * 
     * @return the limitationId
     */
    public Integer getLimitationId() {
        return limitationId;
    }

    /**
     * Set value to <b>limitationId</b>.
     * 
     * @param limitationId
     *            the limitationId to set
     */
    public void setLimitationId(Integer limitationId) {
        this.limitationId = limitationId;
    }

    /**
     * get value of <b>limitationItemId</b>.
     * 
     * @return the limitationItemId
     */
    public Integer getLimitationItemId() {
        return limitationItemId;
    }

    /**
     * Set value to <b>limitationItemId</b>.
     * 
     * @param limitationItemId
     *            the limitationItemId to set
     */
    public void setLimitationItemId(Integer limitationItemId) {
        this.limitationItemId = limitationItemId;
    }

    /**
     * get value of <b>type</b>.
     * 
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Set value to <b>type</b>.
     * 
     * @param type
     *            the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * get value of <b>productId</b>.
     * 
     * @return the productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * Set value to <b>productId</b>.
     * 
     * @param productId
     *            the productId to set
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * get value of <b>productVariationId</b>.
     * 
     * @return the productVariationId
     */
    public Integer getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * 
     * @param productVariationId
     *            the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

    /**
     * get value of <b>productVariationName</b>.
     * 
     * @return the productVariationName
     */
    public String getProductVariationName() {
        return productVariationName;
    }

    /**
     * Set value to <b>productVariationName</b>.
     * 
     * @param productVariationName
     *            the productVariationName to set
     */
    public void setProductVariationName(String productVariationName) {
        this.productVariationName = productVariationName;
    }

    /**
     * get value of <b>unitAmount</b>.
     * 
     * @return the unitAmount
     */
    public Integer getUnitAmount() {
        return unitAmount;
    }

    /**
     * Set value to <b>unitAmount</b>.
     * 
     * @param unitAmount
     *            the unitAmount to set
     */
    public void setUnitAmount(Integer unitAmount) {
        this.unitAmount = unitAmount;
    }

    /**
     * get value of <b>packingAmount</b>.
     * 
     * @return the packingAmount
     */
    public Integer getPackingAmount() {
        return packingAmount;
    }

    /**
     * Set value to <b>packingAmount</b>.
     * 
     * @param packingAmount
     *            the packingAmount to set
     */
    public void setPackingAmount(Integer packingAmount) {
        this.packingAmount = packingAmount;
    }

    /**
     * get value of <b>sku</b>.
     * 
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * Set value to <b>sku</b>.
     * 
     * @param sku
     *            the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * get value of <b>amount</b>.
     * 
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set value to <b>amount</b>.
     * 
     * @param amount
     *            the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * get value of <b>unitId</b>.
     * 
     * @return the unitId
     */
    public Integer getUnitId() {
        return unitId;
    }

    /**
     * Set value to <b>unitId</b>.
     * 
     * @param unitId
     *            the unitId to set
     */
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    /**
     * get value of <b>packingId</b>.
     * 
     * @return the packingId
     */
    public Integer getPackingId() {
        return packingId;
    }

    /**
     * Set value to <b>packingId</b>.
     * 
     * @param packingId
     *            the packingId to set
     */
    public void setPackingId(Integer packingId) {
        this.packingId = packingId;
    }

    /**
     * get value of <b>packingExchangeRatio</b>.
     * 
     * @return the packingExchangeRatio
     */
    public Integer getPackingExchangeRatio() {
        return packingExchangeRatio;
    }

    /**
     * Set value to <b>packingExchangeRatio</b>.
     * 
     * @param packingExchangeRatio
     *            the packingExchangeRatio to set
     */
    public void setPackingExchangeRatio(Integer packingExchangeRatio) {
        this.packingExchangeRatio = packingExchangeRatio;
    }

    @Override
    public String toString() {
        return "PromotionLimitationItemRewardProductDto [promotionId=" + promotionId + ", limitationId=" + limitationId + ", limitationItemId="
                + limitationItemId + ", type=" + type + ", productId=" + productId + ", productVariationId=" + productVariationId + ", productVariationName="
                + productVariationName + ", sku=" + sku + ", amount=" + amount + ", unitAmount=" + unitAmount + ", packingAmount=" + packingAmount + ", unitId="
                + unitId + ", packingId=" + packingId + ", packingExchangeRatio=" + packingExchangeRatio + "]";
    }

}
