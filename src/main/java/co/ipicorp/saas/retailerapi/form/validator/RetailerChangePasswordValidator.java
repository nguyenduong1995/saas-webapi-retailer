package co.ipicorp.saas.retailerapi.form.validator;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.retailerapi.form.RetailerChangePasswordForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerChangePasswordValidator extends AbstractFormValidator {

    @Override
    public boolean support(Serializable form) {
        return true;
    }

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
    	RetailerChangePasswordForm retailerChangePasswordForm = (RetailerChangePasswordForm) form;
        return this.validate(retailerChangePasswordForm, errors);
    }
    
    private boolean validate(RetailerChangePasswordForm retailerChangePasswordForm, Errors errors) {
    	if (StringUtils.isEmpty(retailerChangePasswordForm.getCurrentPassword())) {
    		errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "Current password"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
    	}
    	
    	if (StringUtils.isEmpty(retailerChangePasswordForm.getNewPassword())) {
    		errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "New password"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
    	}
    	
    	if (StringUtils.isEmpty(retailerChangePasswordForm.getConfirmPassword())) {
    		errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "Confirm password"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
    	}
    	
        if (retailerChangePasswordForm.getCurrentPassword().equals(retailerChangePasswordForm.getNewPassword())) {
        	errors.reject(ErrorCode.APP_2201_NEW_PASSWORD_IS_SAME_CURRENT_PASSWORD,
                    new Object[] { "Password"},
                    ErrorCode.APP_2201_NEW_PASSWORD_IS_SAME_CURRENT_PASSWORD);    
        }
        
        if (!retailerChangePasswordForm.getNewPassword().equals(retailerChangePasswordForm.getConfirmPassword())) {
        	errors.reject(ErrorCode.APP_2202_NEW_PASSWORD_DIFFERENT_CONFIRM_PASSWORD,
                    new Object[] { "Password"},
                    ErrorCode.APP_2202_NEW_PASSWORD_DIFFERENT_CONFIRM_PASSWORD);
        }
        
        return !errors.hasErrors();
    }

}
