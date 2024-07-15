/**
 * ProductVariationRewardDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.dto;

import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.PromotionLimitationItem;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ProductVariationRewardDto. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class ProductVariationRewardDto implements Serializable {

    private static final long serialVersionUID = -3289511360331229289L;

    private int groupId;
    
    private List<Map<String, Object>> itemsInGroup = new LinkedList<>();

    private int productVariationId;

    private Promotion promotion;
    
    private PromotionLimitationItem promotionLimitationItem;

    private int amountInCart;

    private double discount = 0.0;
    
    private boolean discountPercentOnBill;

    // Key: Product Variation Id, value is number
    private List<Map<String, Object>> items = new LinkedList<>();

    /**
     * get value of <b>groupId</b>.
     * 
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Set value to <b>groupId</b>.
     * 
     * @param groupId
     *            the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * get value of <b>productVariationId</b>.
     * 
     * @return the productVariationId
     */
    public int getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * 
     * @param productVariationId
     *            the productVariationId to set
     */
    public void setProductVariationId(int productVariationId) {
        this.productVariationId = productVariationId;
    }

    /**
     * get value of <b>amountInCart</b>.
     * 
     * @return the amountInCart
     */
    public int getAmountInCart() {
        return amountInCart;
    }

    /**
     * Set value to <b>amountInCart</b>.
     * 
     * @param amountInCart
     *            the amountInCart to set
     */
    public void setAmountInCart(int amountInCart) {
        this.amountInCart = amountInCart;
    }

    /**
     * get value of <b>discount</b>.
     * 
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Set value to <b>discount</b>.
     * 
     * @param discount
     *            the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * get value of <b>itemsInGroup</b>.
     * @return the itemsInGroup
     */
    public List<Map<String, Object>> getItemsInGroup() {
        return itemsInGroup;
    }

    /**
     * Set value to <b>itemsInGroup</b>.
     * @param itemsInGroup the itemsInGroup to set
     */
    public void setItemsInGroup(List<Map<String, Object>> itemsInGroup) {
        this.itemsInGroup = itemsInGroup;
    }

    /**
     * get value of <b>promotion</b>.
     * @return the promotion
     */
    public Promotion getPromotion() {
        return promotion;
    }

    /**
     * Set value to <b>promotion</b>.
     * @param promotion the promotion to set
     */
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
    
    /**
     * get value of <b>promotionLimitationItem</b>.
     * @return the promotionLimitationItem
     */
    public PromotionLimitationItem getPromotionLimitationItem() {
        return promotionLimitationItem;
    }

    /**
     * Set value to <b>promotionLimitationItem</b>.
     * @param promotionLimitationItem the promotionLimitationItem to set
     */
    public void setPromotionLimitationItem(PromotionLimitationItem promotionLimitationItem) {
        this.promotionLimitationItem = promotionLimitationItem;
    }

    /**
     * get value of <b>items</b>.
     * 
     * @return the items
     */
    public List<Map<String, Object>> getItems() {
        return items;
    }

    /**
     * Set value to <b>items</b>.
     * 
     * @param items
     *            the items to set
     */
    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    /**
     * get value of <b>discountPercentOnBill</b>.
     * @return the discountPercentOnBill
     */
    public boolean isDiscountPercentOnBill() {
        return discountPercentOnBill;
    }

    /**
     * Set value to <b>discountPercentOnBill</b>.
     * @param discountPercentOnBill the discountPercentOnBill to set
     */
    public void setDiscountPercentOnBill(boolean discountPercentOnBill) {
        this.discountPercentOnBill = discountPercentOnBill;
    }

    /**
     * Get Id of promotion.
     * @return Promotion ID.
     */
    public int getPromotionId() {
        return this.promotion.getId();
    }
    
    @Override
    public String toString() {
        return "ProductVariationRewardDto [groupId=" + groupId + ", itemsInGroup=" + itemsInGroup + ", productVariationId=" + productVariationId
                + ", promotion=" + promotion + ", promotionLimitationItem=" + promotionLimitationItem + ", amountInCart=" + amountInCart + ", discount="
                + discount + ", discountPercentOnBill=" + discountPercentOnBill + ", items=" + items + "]";
    }

}
