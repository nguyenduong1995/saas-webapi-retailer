package co.ipicorp.saas.retailerapi.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.retailerapi.form.SelloutIdForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class OrderSelloutExistValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
    
	@Autowired
    private OrderSelloutService orderSelloutService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		SelloutIdForm selloutIdForm = (SelloutIdForm) form;
		return this.validateById(selloutIdForm, errors);
	}
	
	private boolean validateById(SelloutIdForm selloutIdForm, Errors errors) {
		
		if ( selloutIdForm.getSelloutId() != null ) {
			OrderSellout orderSellout = this.orderSelloutService.getActivated(selloutIdForm.getSelloutId());
	    	
	    	if (orderSellout == null) {
	    		errors.reject(ErrorCode.APP_2101_ORDER_SELLOUT_NOT_EXISTED,
						new Object[] { "Order Sellout Id ", selloutIdForm.getSelloutId() },
						ErrorCode.APP_2101_ORDER_SELLOUT_NOT_EXISTED );
				
	    	}
	    	
    	}

		return !errors.hasErrors();
	}
}
