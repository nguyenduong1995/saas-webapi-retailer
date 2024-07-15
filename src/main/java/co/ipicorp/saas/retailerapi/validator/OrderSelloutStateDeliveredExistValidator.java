package co.ipicorp.saas.retailerapi.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.OrderSelloutStatus;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.retailerapi.form.SelloutIdForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class OrderSelloutStateDeliveredExistValidator extends AbstractFormValidator {

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
	    	
	    	if ( !orderSellout.getOrderStatus().equals(OrderSelloutStatus.DELIVERED.toString())){
	    		errors.reject(ErrorCode.APP_2105_ORDER_SELLOUT_STATE_NOT_EXACTLY,
						new Object[] { "Order Sellout Id", selloutIdForm.getSelloutId(), OrderSelloutStatus.DELIVERED.toString() },
						ErrorCode.APP_2105_ORDER_SELLOUT_STATE_NOT_EXACTLY );
	    	}
    	}

		return !errors.hasErrors();
	}
}
