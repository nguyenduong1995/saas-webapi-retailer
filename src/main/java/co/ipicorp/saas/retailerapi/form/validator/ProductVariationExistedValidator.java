package co.ipicorp.saas.retailerapi.form.validator;

import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.retailerapi.form.RetailerWarehouseImportTicketForm;
import co.ipicorp.saas.retailerapi.form.RetailerWarehouseImportTicketItemForm;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ProductVariationExistedValidator extends AbstractFormValidator {
    
    @Autowired
    private ProductVariationService productVariationService;

    @Override
    public boolean support(Serializable form) {
        return true;
    }

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
        RetailerWarehouseImportTicketForm retailerWarehouseImportTicketForm = (RetailerWarehouseImportTicketForm) form;
        return this.validateByProductVariation(retailerWarehouseImportTicketForm, errors);
    }
    
    private boolean validateByProductVariation(RetailerWarehouseImportTicketForm retailerWarehouseImportTicketForm, Errors errors) {
        if (CollectionUtils.isNotEmpty(retailerWarehouseImportTicketForm.getRetailerWarehouseImportTicketItems())) {
            for (RetailerWarehouseImportTicketItemForm form : retailerWarehouseImportTicketForm.getRetailerWarehouseImportTicketItems()) {
                Integer productVariationId = form.getProductVariationId();
                if ( productVariationId != null) {
                    ProductVariation productVariation = this.productVariationService.get(productVariationId);
                    if ( productVariation == null ) {
                        errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
                                new Object[] { "Product Variation Id", productVariationId },
                                ErrorCode.APP_1401_FIELD_NOT_EXIST);
                    }
                } else {
                    errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                            new Object[] { "Product Variation Id"},
                            ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
                }
            }
        } else {
            errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "Retailer Warehouse Import Ticket Items"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
        }
        return !errors.hasErrors();
    }

}
