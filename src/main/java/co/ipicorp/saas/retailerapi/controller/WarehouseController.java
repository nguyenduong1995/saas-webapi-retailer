/**
 * WarehouseController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.controller;

import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.service.WarehouseTotalItemService;
import co.ipicorp.saas.nrms.web.util.ControllerAction;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * WarehouseController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
public class WarehouseController {
    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private WarehouseTotalItemService whtiService;

    @RequestMapping(value = ControllerAction.APP_WAREHOUSE_GET_TOTAL_ITEMS_ACTION, method = RequestMethod.GET)
    @ResponseBody
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> getProductVariationDetail(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("productVariationIds") List<Integer> ids) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                
                Map<Integer, WarehouseTotalItem> items = null;
                if (!CollectionUtils.isEmpty(ids)) {
                    items = whtiService.getByProductVarationId(ids);
                } else {
                    items = new LinkedHashMap<>();
                }
                
                for (Integer pvId : ids) {
                    items.putIfAbsent(pvId, new WarehouseTotalItem());
                }
                
                getRpcResponse().addAttribute("items", items);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

}
