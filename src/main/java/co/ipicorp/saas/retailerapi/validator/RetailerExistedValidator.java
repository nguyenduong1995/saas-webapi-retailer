package co.ipicorp.saas.retailerapi.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.retailerapi.form.SelloutIdForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
    private OrderSelloutService orderSelloutService;
	
	@Autowired
	private RetailerService retailerService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		SelloutIdForm selloutIdForm = (SelloutIdForm) form;

		return this.validateById(selloutIdForm, errors);
	}
	
	private boolean validateById(SelloutIdForm selloutIdForm, Errors errors) {
		
		if ( selloutIdForm.getSelloutId() != null ) {
			OrderSellout orderSellout = this.orderSelloutService.getActivated(selloutIdForm.getSelloutId());
			Retailer retailer = this.retailerService.getActivated(orderSellout.getRetailerId());
			if ( retailer == null ) {
				errors.reject(ErrorCode.APP_2106_RETAILER_NOT_EXIST,
						new Object[] { "Retailer Id", orderSellout.getRetailerId() },
						ErrorCode.APP_2106_RETAILER_NOT_EXIST);
			}
		}
		
		return !errors.hasErrors();
	}
}
