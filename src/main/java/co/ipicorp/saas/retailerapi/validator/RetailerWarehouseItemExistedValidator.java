package co.ipicorp.saas.retailerapi.validator;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.OrderSelloutItem;
import co.ipicorp.saas.nrms.model.RetailerWarehouse;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItem;
import co.ipicorp.saas.nrms.service.OrderSelloutItemService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseService;
import co.ipicorp.saas.retailerapi.form.SelloutIdForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerWarehouseItemExistedValidator extends AbstractFormValidator {

    @Override
    public boolean support(Serializable form) {
        return true;
    }

    @Autowired
    private OrderSelloutService orderSelloutService;

    @Autowired
    private OrderSelloutItemService orderSelloutItemService;

    @Autowired
    private RetailerWarehouseService retailerWarehouseService;

    @Autowired
    private RetailerWarehouseItemService retailerWarehouseItemService;

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
        SelloutIdForm selloutIdForm = (SelloutIdForm) form;

        return this.validateById(selloutIdForm, errors);
    }

    private boolean validateById(SelloutIdForm selloutIdForm, Errors errors) {

        if (selloutIdForm.getSelloutId() != null) {
            OrderSellout orderSellout = this.orderSelloutService.getActivated(selloutIdForm.getSelloutId());
            List<OrderSelloutItem> orderSelloutItems = this.orderSelloutItemService.getAllBySelloutId(selloutIdForm.getSelloutId());
            RetailerWarehouse retailerWarehouse = this.retailerWarehouseService.getByRetailerId(orderSellout.getRetailerId());
            if (orderSelloutItems == null || orderSelloutItems.isEmpty()) {
                errors.reject(ErrorCode.APP_2102_ORDER_SELLOUT_ITEM_NOT_EXISTED, new Object[] { "Order Sellout Item", selloutIdForm.getSelloutId() },
                        ErrorCode.APP_2102_ORDER_SELLOUT_ITEM_NOT_EXISTED);

                return !errors.hasErrors();
            }

            for (OrderSelloutItem item : orderSelloutItems) {

                RetailerWarehouseItem retailerWarehouseItem = this.retailerWarehouseItemService.getByRetailerWarehouseItemInfo(orderSellout.getRetailerId(),
                        retailerWarehouse.getId(), item.getProductId(), item.getProductVariationId());

                if (retailerWarehouseItem == null) {
                    errors.reject(
                            ErrorCode.APP_2108_RETAILER_WAREHOUSE_ITEM_NOT_EXIST, new Object[] { "Retailer Warehouse Item", orderSellout.getRetailerId(),
                                    retailerWarehouse.getId(), item.getProductId(), item.getProductVariationId() },
                            ErrorCode.APP_2108_RETAILER_WAREHOUSE_ITEM_NOT_EXIST);

                    return !errors.hasErrors();
                }

                Integer amount = retailerWarehouseItem.getAmount() - item.getTotalAmount();
                if (amount < 0) {
                    errors.reject(ErrorCode.APP_2109_RETAILER_WAREHOUSE_ITEM_AMOUNT_NOT_ENOUGH,
                            new Object[] { "Retailer Warehouse Item", item.getId(), Math.abs(amount) },
                            ErrorCode.APP_2109_RETAILER_WAREHOUSE_ITEM_AMOUNT_NOT_ENOUGH);

                    return !errors.hasErrors();
                }

            }
        }

        return !errors.hasErrors();
    }
}
