package co.ipicorp.saas.retailerapi.form.validator;

import co.ipicorp.saas.retailerapi.form.RetailerWarehouseImportTicketSearchForm;
import co.ipicorp.saas.retailerapi.util.ErrorCode;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerWarehouseImportTicketSearchValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		RetailerWarehouseImportTicketSearchForm formSearch = (RetailerWarehouseImportTicketSearchForm) form;
		return this.limitDate(formSearch, errors);
	}
	
	private boolean limitDate(RetailerWarehouseImportTicketSearchForm formSearch, Errors errors) {
		if ( formSearch.getFromDate() != null && formSearch.getToDate() != null ) {
		    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		    
		    LocalDate fromDate = null;
		    LocalDate toDate = null; 
		            
		    try {
		        fromDate = LocalDate.parse(formSearch.getFromDate(), df);
		        toDate = LocalDate.parse(formSearch.getToDate(), df);
            } catch (Exception ex) {
                // do nothing
            }
		    
			if (fromDate == null || fromDate == null || fromDate.isAfter(toDate)) {
				errors.reject(ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE,
						new Object[] { "fromDate", formSearch.getFromDate(), "toDate", formSearch.getToDate() },
						ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE);
			}
		} else {
		    errors.reject(ErrorCode.APP_1702_FROM_DATE_AND_END_DATE_REQUIRED, "fromDate and toDate is required");
		}
		
		
		return !errors.hasErrors();
	}
}
