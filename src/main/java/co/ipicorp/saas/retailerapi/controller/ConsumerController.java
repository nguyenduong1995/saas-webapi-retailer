/**
 * ConsumerController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.Consumer;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.service.ConsumerService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.model.dto.SearchCondition;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.form.validator.LimittedForm;
import grass.micro.apps.web.util.RequestUtils;

/**
 * ConsumerController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
public class ConsumerController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private ConsumerService consumerService;
    
    @Autowired
    private OrderSelloutService orderSelloutService;

    @RequestMapping(value = "consumer/list", method = RequestMethod.GET)
    @ResponseBody
    @NoRequiredAuth
    public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return consumerService.getAll();
            }

            @Override
            public String getAttributeName() {
                return "consumers";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                return DtoFetchingUtils.fetchConsumers((List<Consumer>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @RequestMapping(value = ControllerAction.APP_CONSUMER_ACTION, method = RequestMethod.GET)
    @ResponseBody
    @Logged
    public ResponseEntity<?> listAllByRetailerId(HttpServletRequest request, HttpServletResponse response,
            @GetBody LimittedForm form) {
        AppControllerSupport support = new AppControllerListingSupport() {
        	
			@Override
			public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response,
					Errors errors, ErrorsKeyConverter errorsProcessor) {
				RetailerSessionInfo sessionInfo = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Retailer retailer = sessionInfo.getRetailer();
				SearchCondition condition = new SearchCondition();
				condition.setSegment(form.getSegment());
				condition.setOffset(form.getOffset());
				condition.setLimitSearch(true);
				List<Consumer> consumers = null;
				List<Integer> consumerIds = ConsumerController.this.orderSelloutService.getAllConsumerId(retailer.getId());
				if (consumerIds != null && consumerIds.size() > 0) {
					consumers = ConsumerController.this.consumerService.getAllByIds(consumerIds, condition);
				}
				return consumers;
			}

			@Override
			public String getAttributeName() {
				return "Consumers";
			}
			
			@Override
			public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
				return DtoFetchingUtils.fetchConsumers((List<Consumer>) entities);
			}
        	
        };
        
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
        
    }
}
