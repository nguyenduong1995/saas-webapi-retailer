package co.ipicorp.saas.retailerapi.validator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.OrderSelloutItem;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotionLimitationDetailReward;
import co.ipicorp.saas.nrms.model.RetailerWarehouseTotalItem;
import co.ipicorp.saas.nrms.service.OrderSelloutItemService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionLimitationDetailRewardService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseTotalItemService;
import co.ipicorp.saas.retailerapi.form.SelloutIdForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerWarehouseTotalItemExistValidator extends AbstractFormValidator {

    @Override
    public boolean support(Serializable form) {
        return true;
    }

    @Autowired
    private OrderSelloutService orderSelloutService;

    @Autowired
    private RetailerWarehouseTotalItemService retailerWarehouseTotalItemService;

    @Autowired
    private OrderSelloutItemService orderSelloutItemService;

    @Autowired
    private OrderSelloutPromotionLimitationDetailRewardService osiPromotionLimitationDetailRewardService;

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
        SelloutIdForm selloutIdForm = (SelloutIdForm) form;
        return this.validateById(selloutIdForm, errors);
    }

    private boolean validateById(SelloutIdForm selloutIdForm, Errors errors) {
        if (selloutIdForm.getSelloutId() != null) {
            OrderSellout orderSellout = this.orderSelloutService.getActivated(selloutIdForm.getSelloutId());
            List<OrderSelloutItem> orderSelloutItems = this.orderSelloutItemService.getAllBySelloutId(selloutIdForm.getSelloutId());

            if (orderSelloutItems == null || orderSelloutItems.size() == 0) {
                errors.reject(ErrorCode.APP_2102_ORDER_SELLOUT_ITEM_NOT_EXISTED, new Object[] { "Order Sellout Item", selloutIdForm.getSelloutId() },
                        ErrorCode.APP_2102_ORDER_SELLOUT_ITEM_NOT_EXISTED);

                return !errors.hasErrors();
            }

            Map<Integer, Integer> itemCounter = new HashMap<>();
            Map<Integer, Integer> productMap = new HashMap<>();
            for (OrderSelloutItem item : orderSelloutItems) {
                itemCounter.put(item.getProductVariationId(), item.getTotalAmount());
                productMap.put(item.getProductVariationId(), item.getProductId());
            }

            List<OrderSelloutPromotionLimitationDetailReward> rewards = osiPromotionLimitationDetailRewardService.getByOrderId(selloutIdForm.getSelloutId());
            if (CollectionUtils.isNotEmpty(rewards)) {
                for (OrderSelloutPromotionLimitationDetailReward reward : rewards) {
                    Integer counter = itemCounter.get(reward.getRewardProductVariationId());
                    if (counter == null) {
                        counter = 0;
                    }

                    itemCounter.put(reward.getRewardProductVariationId(), counter + reward.getRewardAmount());
                    productMap.put(reward.getRewardProductVariationId(), reward.getRewardProductId());
                }
            }

            for (Map.Entry<Integer, Integer> entry : productMap.entrySet()) {
                RetailerWarehouseTotalItem retailerWarehouseTotalItem = this.retailerWarehouseTotalItemService
                        .getByRetailerWarehouseTotalItemInfo(orderSellout.getRetailerId(), entry.getValue(), entry.getKey());
 
                validate(errors, orderSellout, entry.getValue(), entry.getKey(), itemCounter.get(entry.getKey()), retailerWarehouseTotalItem);
            }
        }

        return !errors.hasErrors();
    }

    /**
     * @param errors
     * @param orderSellout
     * @param item
     * @param retailerWarehouseTotalItem
     */
    private boolean validate(Errors errors, OrderSellout orderSellout, int productId, int productVariationId, int amountInOrder,
            RetailerWarehouseTotalItem retailerWarehouseTotalItem) {
        if (retailerWarehouseTotalItem == null) {
            errors.reject(ErrorCode.APP_2103_RETAILER_WAREHOUSE_TOTAL_ITEM_NOT_EXIST,
                    new Object[] { "Retailer Warehouse Total Item", orderSellout.getRetailerId(), productId, productVariationId },
                    ErrorCode.APP_2103_RETAILER_WAREHOUSE_TOTAL_ITEM_NOT_EXIST);

            return !errors.hasErrors();
        }

        Integer amount = retailerWarehouseTotalItem.getAmount() - amountInOrder;
        if (amount < 0) {
            errors.reject(ErrorCode.APP_2104_RETAILER_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH,
                    new Object[] { "Product Variation ID", productVariationId, Math.abs(amount) },
                    ErrorCode.APP_2104_RETAILER_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH);

            return !errors.hasErrors();
        }

        return !errors.hasErrors();
    }
}
