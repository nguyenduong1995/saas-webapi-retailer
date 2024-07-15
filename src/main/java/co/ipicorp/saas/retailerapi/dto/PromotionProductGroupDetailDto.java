/**
 * PromotionProductGroupDetailDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * PromotionProductGroupDetailDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class PromotionProductGroupDetailDto extends StatusTimestampDto implements Serializable {
    
    private static final long serialVersionUID = 7641057569663013686L;

    @JsonProperty("promotionId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer promotionId;
    
    @JsonProperty("groupId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer groupId;
    
    @JsonProperty("groupName")
    @JsonInclude(value = Include.NON_NULL)
    private String groupName;

    @JsonProperty("productId")
    @JsonInclude(value = Include.NON_NULL)
    private Integer productId;
    
    @JsonProperty("productVariationId")
    private Integer productVariationId;
    
    @JsonProperty("productVariationName")
    private String productVariationName;
    
    @JsonProperty("sku")
    @JsonInclude(value = Include.NON_NULL)
    private String sku;
    
    @JsonProperty("description")
    @JsonInclude(value = Include.NON_NULL)
    private String description;
    
    @JsonProperty("categoryId")
    private Integer categoryId;

    @JsonProperty("categoryLevel")
    private Integer categoryLevel;
    
    @JsonProperty("categoryIdLv0")
    private Integer categoryIdLv0;
    
    @JsonProperty("categoryIdLv1")
    private Integer categoryIdLv1;
    
    @JsonProperty("categoryIdLv2")
    private Integer categoryIdLv2;
    
    @JsonProperty("categoryIdLv3")
    private Integer categoryIdLv3;
    
    @JsonProperty("categoryIdLv4")
    private Integer categoryIdLv4;
    
    @JsonProperty("categoryIdLv5")
    private Integer categoryIdLv5;
    
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
     * @param promotionId the promotionId to set
     */
    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    /**
     * get value of <b>groupId</b>.
     * 
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Set value to <b>groupId</b>.
     * 
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * get value of <b>groupName</b>.
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Set value to <b>groupName</b>.
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
     * @param productId the productId to set
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
     * @param productVariationId the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
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
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * get value of <b>description</b>.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set value to <b>description</b>.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get value of <b>productVariationName</b>.
     * @return the productVariationName
     */
    public String getProductVariationName() {
        return productVariationName;
    }

    /**
     * Set value to <b>productVariationName</b>.
     * @param productVariationName the productVariationName to set
     */
    public void setProductVariationName(String productVariationName) {
        this.productVariationName = productVariationName;
    }

    /**
     * get value of <b>categoryId</b>.
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Set value to <b>categoryId</b>.
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * get value of <b>categoryLevel</b>.
     * @return the categoryLevel
     */
    public Integer getCategoryLevel() {
        return categoryLevel;
    }

    /**
     * Set value to <b>categoryLevel</b>.
     * @param categoryLevel the categoryLevel to set
     */
    public void setCategoryLevel(Integer categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    /**
     * get value of <b>categoryIdLv0</b>.
     * @return the categoryIdLv0
     */
    public Integer getCategoryIdLv0() {
        return categoryIdLv0;
    }

    /**
     * Set value to <b>categoryIdLv0</b>.
     * @param categoryIdLv0 the categoryIdLv0 to set
     */
    public void setCategoryIdLv0(Integer categoryIdLv0) {
        this.categoryIdLv0 = categoryIdLv0;
    }

    /**
     * get value of <b>categoryIdLv1</b>.
     * @return the categoryIdLv1
     */
    public Integer getCategoryIdLv1() {
        return categoryIdLv1;
    }

    /**
     * Set value to <b>categoryIdLv1</b>.
     * @param categoryIdLv1 the categoryIdLv1 to set
     */
    public void setCategoryIdLv1(Integer categoryIdLv1) {
        this.categoryIdLv1 = categoryIdLv1;
    }

    /**
     * get value of <b>categoryIdLv2</b>.
     * @return the categoryIdLv2
     */
    public Integer getCategoryIdLv2() {
        return categoryIdLv2;
    }

    /**
     * Set value to <b>categoryIdLv2</b>.
     * @param categoryIdLv2 the categoryIdLv2 to set
     */
    public void setCategoryIdLv2(Integer categoryIdLv2) {
        this.categoryIdLv2 = categoryIdLv2;
    }

    /**
     * get value of <b>categoryIdLv3</b>.
     * @return the categoryIdLv3
     */
    public Integer getCategoryIdLv3() {
        return categoryIdLv3;
    }

    /**
     * Set value to <b>categoryIdLv3</b>.
     * @param categoryIdLv3 the categoryIdLv3 to set
     */
    public void setCategoryIdLv3(Integer categoryIdLv3) {
        this.categoryIdLv3 = categoryIdLv3;
    }

    /**
     * get value of <b>categoryIdLv4</b>.
     * @return the categoryIdLv4
     */
    public Integer getCategoryIdLv4() {
        return categoryIdLv4;
    }

    /**
     * Set value to <b>categoryIdLv4</b>.
     * @param categoryIdLv4 the categoryIdLv4 to set
     */
    public void setCategoryIdLv4(Integer categoryIdLv4) {
        this.categoryIdLv4 = categoryIdLv4;
    }

    /**
     * get value of <b>categoryIdLv5</b>.
     * @return the categoryIdLv5
     */
    public Integer getCategoryIdLv5() {
        return categoryIdLv5;
    }

    /**
     * Set value to <b>categoryIdLv5</b>.
     * @param categoryIdLv5 the categoryIdLv5 to set
     */
    public void setCategoryIdLv5(Integer categoryIdLv5) {
        this.categoryIdLv5 = categoryIdLv5;
    }

    @Override
    public String toString() {
        return "PromotionProductGroupDetailDto [promotionId=" + promotionId + ", groupId=" + groupId + ", groupName=" + groupName + ", productId=" + productId
                + ", productVariationId=" + productVariationId + ", productVariationName=" + productVariationName + ", sku=" + sku + ", description="
                + description + ", categoryId=" + categoryId + ", categoryLevel=" + categoryLevel + ", categoryIdLv0=" + categoryIdLv0 + ", categoryIdLv1="
                + categoryIdLv1 + ", categoryIdLv2=" + categoryIdLv2 + ", categoryIdLv3=" + categoryIdLv3 + ", categoryIdLv4=" + categoryIdLv4
                + ", categoryIdLv5=" + categoryIdLv5 + "]";
    }
    
}
