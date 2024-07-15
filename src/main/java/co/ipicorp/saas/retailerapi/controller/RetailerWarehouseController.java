/**
 * RetailerWarehouseController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.nrms.model.RetailerWarehouseItem;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItemHistory;
import co.ipicorp.saas.nrms.model.dto.RetailerWarehouseItemHistorySearchCondition;
import co.ipicorp.saas.nrms.model.dto.RetailerWarehouseItemSearchCondition;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemHistoryService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemService;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.retailerapi.form.RetailerWarehouseItemHistorySearchForm;
import co.ipicorp.saas.retailerapi.form.RetailerWarehouseItemSearchForm;
import co.ipicorp.saas.retailerapi.security.RetailerSessionInfo;
import co.ipicorp.saas.retailerapi.util.Constants;
import co.ipicorp.saas.retailerapi.util.ControllerAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * RetailerWarehouseController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
@Api(tags = "Retailer Warehouse APIs", description = "all APIs relate to retailer warehouses.")
public class RetailerWarehouseController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private RetailerWarehouseItemService rwiService;
    
    @Autowired
    private RetailerWarehouseItemHistoryService rwiHistoryService;
    
    @Autowired
    private ProductResourceService productResourceService;

    @GetMapping(value = ControllerAction.APP_RETAILER_WAREHOUSE_ITEM)
    @ResponseBody
    public ResponseEntity<?> getAllItems(HttpServletRequest request, HttpServletResponse response, @GetBody RetailerWarehouseItemSearchForm form) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                int retailerId = info.getRetailer().getId();
                
                RetailerWarehouseItemSearchCondition condition = new RetailerWarehouseItemSearchCondition();
                condition.setLimitSearch(true);
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setRetailerId(retailerId);
                condition.setExisted(form.getExisted());
                condition.setStatus(Status.ACTIVE);
                
                long count = rwiService.count(condition);
                getRpcResponse().addAttribute("count", count);
                
                if (count > condition.getSegment()) {
                    return rwiService.search(condition);
                }
                
                return new ArrayList<>();
            }

            @Override
            public String getAttributeName() {
                return "items";
            }

            @SuppressWarnings("unchecked")
            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                DtoFetchingUtils.setProductResourceService(productResourceService);
                return DtoFetchingUtils.fetchRetailerWarehouseItems((List<RetailerWarehouseItem>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @GetMapping(value = ControllerAction.APP_RETAILER_WAREHOUSE_ITEM_HISTORY)
    @ResponseBody
    public ResponseEntity<?> getItemChangeHistories(HttpServletRequest request, HttpServletResponse response,
            @GetBody RetailerWarehouseItemHistorySearchForm form) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                RetailerSessionInfo info = (RetailerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                int retailerId = info.getRetailer().getId();
                RetailerWarehouseItemHistorySearchCondition condition = new RetailerWarehouseItemHistorySearchCondition();
                condition.setLimitSearch(true);
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setRetailerId(retailerId);
                condition.setProductVariationId(form.getProductVariationId());
                
                DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                condition.setFromDate(LocalDate.parse(form.getFromDate(), df));
                condition.setToDate(LocalDate.parse(form.getToDate(), df));
                condition.setEnableDateRange(true);
                
                long count = rwiHistoryService.count(condition);
                getRpcResponse().addAttribute("count", count);
                
                if (count > condition.getSegment()) {
                    return rwiHistoryService.search(condition);
                }
                
                return new ArrayList<>();
            }

            @Override
            public String getAttributeName() {
                return "items";
            }

            @SuppressWarnings("unchecked")
            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                List<RetailerWarehouseItemHistory> histories = (List<RetailerWarehouseItemHistory>) entities;
                List<Map<String, Object>> result = new LinkedList<Map<String,Object>>();
                
                for (RetailerWarehouseItemHistory history : histories) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    this.putHistoryToMap(history, map);
                    result.add(map);
                }
                
                return result;
            }

            private void putHistoryToMap(RetailerWarehouseItemHistory history, Map<String, Object> map) {
                SystemUtils.getInstance().copyPropertiesToMap(history, map, 
                        "id,sku,changeDate,changeType,changeAmount,orderSellinId,orderSellinCode,orderSelloutId,orderSelloutCode,importTicketId,importTicketCode".split(","));
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
}
