package co.ipicorp.saas.retailerapi.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.RetailerWarehouse;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseService;
import co.ipicorp.saas.retailerapi.form.SelloutIdForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerWarehouseExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
    private OrderSelloutService orderSelloutService;
	
	@Autowired
	private RetailerWarehouseService retailerWarehouseService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		SelloutIdForm selloutIdForm = (SelloutIdForm) form;

		return this.validateById(selloutIdForm, errors);
	}
	
	private boolean validateById(SelloutIdForm selloutIdForm, Errors errors) {
		
		if ( selloutIdForm.getSelloutId() != null ) {
			OrderSellout orderSellout = this.orderSelloutService.getActivated(selloutIdForm.getSelloutId());
			RetailerWarehouse retailerWarehouse = this.retailerWarehouseService.getByRetailerId(orderSellout.getRetailerId());
			if ( retailerWarehouse == null ) {
				errors.reject(ErrorCode.APP_2107_RETAILER_WAREHOUSE_NOT_EXIST,
						new Object[] { "Retailer Warehouse", orderSellout.getRetailerId() },
						ErrorCode.APP_2107_RETAILER_WAREHOUSE_NOT_EXIST);
			}
		}
		
		return !errors.hasErrors();
	}
}
